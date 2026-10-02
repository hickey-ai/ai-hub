package dev.aihub.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class CrawlerApplication {
  public static void main(String[] args) { SpringApplication.run(CrawlerApplication.class, args); }
}

@RestController
@RequestMapping("/api")
class CrawlerController {
  private static final int MAX_BODY_BYTES = 1_500_000;
  private static final Pattern TITLE = Pattern.compile("(?is)<title[^>]*>(.*?)</title>");
  private static final Pattern META_DESCRIPTION = Pattern.compile("(?is)<meta\\s+[^>]*name=[\\\"']description[\\\"'][^>]*content=[\\\"'](.*?)[\\\"'][^>]*>");
  private static final Pattern TAG = Pattern.compile("(?is)<(h1|h2|h3)\\b[^>]*>(.*?)</\\1>");
  private static final Pattern LINK = Pattern.compile("(?is)<a\\s+[^>]*href=[\\\"'](.*?)[\\\"'][^>]*>(.*?)</a>");
  private static final Pattern SCRIPT_STYLE = Pattern.compile("(?is)<(script|style|noscript).*?>.*?</\\1>");
  private static final Pattern TAGS = Pattern.compile("(?is)<[^>]+>");
  private final CopyOnWriteArrayList<Map<String, Object>> jobs = new CopyOnWriteArrayList<>();
  private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(6)).followRedirects(HttpClient.Redirect.NEVER).build();

  @GetMapping("/overview")
  Map<String, Object> overview() {
    return map("jobs", jobs.size(), "maxBody", "1.5 MB", "redirects", "手动确认", "robots", "请遵守目标站点规则", "local", true);
  }

  @GetMapping("/jobs")
  List<Map<String, Object>> jobs() { return List.copyOf(jobs); }

  @PostMapping("/crawl")
  Map<String, Object> crawl(@RequestBody CrawlRequest request) {
    URI uri = validateUrl(request.url());
    long started = System.nanoTime();
    try {
      HttpRequest httpRequest = HttpRequest.newBuilder(uri)
          .timeout(Duration.ofSeconds(10))
          .header("User-Agent", "ai-hub-crawler/0.1 (local research tool)")
          .header("Accept", "text/html,application/xhtml+xml;q=0.9")
          .GET().build();
      HttpResponse<byte[]> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
      if (response.statusCode() >= 300 && response.statusCode() < 400) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "目标站点要求跳转，请提交跳转后的最终地址");
      }
      if (response.statusCode() >= 400) {
        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "目标站点返回 HTTP " + response.statusCode());
      }
      byte[] bytes = response.body();
      if (bytes.length > MAX_BODY_BYTES) throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "页面超过 1.5 MB 限制");
      String contentType = response.headers().firstValue("content-type").orElse("");
      if (!contentType.isBlank() && !contentType.toLowerCase(Locale.ROOT).contains("text/html") && !contentType.toLowerCase(Locale.ROOT).contains("application/xhtml+xml")) {
        throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "当前版本只解析 HTML 页面");
      }
      String html = new String(bytes, StandardCharsets.UTF_8);
      Map<String, Object> result = parse(uri, response.statusCode(), contentType, html, started);
      jobs.add(0, result);
      while (jobs.size() > 20) jobs.remove(jobs.size() - 1);
      return result;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "抓取被中断");
    } catch (ResponseStatusException e) { throw e; }
    catch (Exception e) { throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "抓取失败：" + safeMessage(e)); }
  }

  private Map<String, Object> parse(URI uri, int status, String contentType, String html, long started) {
    String plain = normalize(SCRIPT_STYLE.matcher(html).replaceAll(" ").replaceAll("&nbsp;", " ").replaceAll("&amp;", "&"));
    String title = first(TITLE, html).map(this::normalize).orElse(uri.getHost());
    String description = first(META_DESCRIPTION, html).map(this::normalize).orElse(plain.length() > 180 ? plain.substring(0, 180) + "…" : plain);
    List<String> headings = new ArrayList<>();
    Matcher headingMatcher = TAG.matcher(html);
    while (headingMatcher.find() && headings.size() < 8) headings.add(normalize(headingMatcher.group(2)));
    List<Map<String, String>> links = new ArrayList<>();
    Matcher linkMatcher = LINK.matcher(html);
    while (linkMatcher.find() && links.size() < 20) {
      try {
        URI link = uri.resolve(linkMatcher.group(1).trim()).normalize();
        if (!List.of("http", "https").contains(link.getScheme())) continue;
        String label = normalize(linkMatcher.group(2));
        links.add(map("label", label.isBlank() ? link.toString() : label, "url", link.toString()));
      } catch (Exception ignored) { }
    }
    return map("id", UUID.randomUUID().toString(), "url", uri.toString(), "host", uri.getHost(), "title", title.isBlank() ? "未命名页面" : title,
        "description", description, "status", status, "contentType", contentType, "bytes", html.getBytes(StandardCharsets.UTF_8).length,
        "words", plain.isBlank() ? 0 : plain.split("\\s+").length, "headings", headings, "links", links,
        "excerpt", plain.length() > 420 ? plain.substring(0, 420) + "…" : plain, "durationMs", (System.nanoTime() - started) / 1_000_000,
        "createdAt", OffsetDateTime.now().toString());
  }

  private URI validateUrl(String raw) {
    if (raw == null || raw.isBlank() || raw.length() > 2048) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请输入 2048 字符以内的 URL");
    try {
      URI uri = URI.create(raw.trim());
      if (!List.of("http", "https").contains(uri.getScheme()) || uri.getHost() == null || uri.getUserInfo() != null) throw new IllegalArgumentException();
      String host = uri.getHost().toLowerCase(Locale.ROOT);
      if (Set.of("localhost", "localhost.localdomain", "metadata.google.internal", "metadata", "0.0.0.0").contains(host)) throw new IllegalArgumentException();
      for (InetAddress address : InetAddress.getAllByName(host)) {
        if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress() || address.isSiteLocalAddress() || address.isMulticastAddress()) throw new IllegalArgumentException();
      }
      return uri;
    } catch (Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "仅允许访问公开的 http/https 页面，已拒绝本机或内网地址"); }
  }

  private Optional<String> first(Pattern pattern, String input) { Matcher matcher = pattern.matcher(input); return matcher.find() ? Optional.ofNullable(matcher.group(1)) : Optional.empty(); }
  private String normalize(String value) { return TAGS.matcher(value == null ? "" : value).replaceAll(" ").replaceAll("\\s+", " ").trim(); }
  private String safeMessage(Exception e) { return e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage(); }
  private static <T> LinkedHashMap<String, T> map(Object... values) { var result = new LinkedHashMap<String, T>(); for (int i = 0; i < values.length; i += 2) result.put((String) values[i], (T) values[i + 1]); return result; }
  record CrawlRequest(String url) { }
}

