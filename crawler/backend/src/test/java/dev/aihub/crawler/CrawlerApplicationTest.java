package dev.aihub.crawler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrawlerApplicationTest {
  @LocalServerPort int port;
  @Autowired TestRestTemplate rest;

  @Test void overviewIsLocalFirst() {
    ResponseEntity<String> response = rest.getForEntity("http://127.0.0.1:" + port + "/api/overview", String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("1.5 MB");
  }

  @Test void privateAddressIsRejectedBeforeNetworkRequest() {
    var request = new HttpEntity<>("{\"url\":\"http://127.0.0.1:8080\"}", headers());
    ResponseEntity<String> response = rest.postForEntity("http://127.0.0.1:" + port + "/api/crawl", request, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private HttpHeaders headers() { var headers = new HttpHeaders(); headers.setContentType(MediaType.APPLICATION_JSON); return headers; }
}
