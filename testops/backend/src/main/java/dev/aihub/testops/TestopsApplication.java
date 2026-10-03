package dev.aihub.testops;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.*;
import java.util.*;

@SpringBootApplication
public class TestopsApplication {
    public static void main(String[] args) { SpringApplication.run(TestopsApplication.class, args); }
}

@RestController
@RequestMapping("/api")
class RecordsController {
    private final ObjectMapper mapper;
    private final Path file;
    private final Map<String, List<Map<String,Object>>> data = new LinkedHashMap<>();
    private long nextId = 1;

    RecordsController(ObjectMapper mapper, @Value("${aihub.data-file}") String fileName) {
        this.mapper = mapper;
        this.file = Path.of(fileName).toAbsolutePath();
        for (String name : List.of("testcases", "executions")) data.put(name, new ArrayList<>());
        if (Files.exists(file)) {
            try {
                Map<String,Object> saved = mapper.readValue(file.toFile(), new TypeReference<>() {});
                if (!(saved.get("nextId") instanceof Number)) throw new IOException("missing nextId");
                nextId = ((Number)saved.get("nextId")).longValue();
                for (String name : data.keySet()) {
                    if (!(saved.get(name) instanceof List<?> list)) throw new IOException("missing " + name);
                    for (Object value : list) {
                        if (!(value instanceof Map<?,?> row) || !(row.get("id") instanceof Number)) throw new IOException("invalid " + name);
                        @SuppressWarnings("unchecked") Map<String,Object> item = (Map<String,Object>) row;
                        data.get(name).add(new LinkedHashMap<>(item));
                    }
                }
            } catch (IOException | ClassCastException ex) {
                throw new IllegalStateException("数据文件损坏，请先备份并修复：" + file, ex);
            }
        } else {
            seed("testcases", Map.of("caseNo", "TC-DEMO-01", "title", "购物车添加商品", "module", "商城", "priority", "P1", "steps", "打开商品详情，点击加入购物车", "expected", "购物车数量增加 1"));
            seed("executions", Map.of("caseId", 1, "version", "v0.1.0", "eventDate", "2026-10-02", "result", "通过", "status", "待复核", "tester", "演示测试员", "actual", "购物车数量增加 1"));
        }
    }

    private Map<String,Object> seed(String resource, Map<String,Object> body) {
        var item = new LinkedHashMap<String,Object>(body);
        item.put("id", nextId++);
        data.get(resource).add(item);
        return item;
    }

    @GetMapping("/{resource}")
    synchronized List<Map<String,Object>> list(@PathVariable String resource) {
        return records(resource).stream().map(LinkedHashMap::new).map(x -> (Map<String,Object>)x).toList();
    }

    @PostMapping("/{resource}")
    @ResponseStatus(HttpStatus.CREATED)
    synchronized Map<String,Object> create(@PathVariable String resource, @RequestBody Map<String,Object> input) {
        var item = validate(resource, input);
        ensureUnique(resource, item, null);
        item.put("id", nextId++);
        data.get(resource).add(item);
        try { persist(); }
        catch (RuntimeException ex) { data.get(resource).remove(item); nextId--; throw ex; }
        return item;
    }

    @PutMapping("/{resource}/{id}")
    synchronized Map<String,Object> update(@PathVariable String resource, @PathVariable long id, @RequestBody Map<String,Object> input) {
        var list = records(resource);
        int index = indexOf(list, id);
        var item = validate(resource, input);
        ensureUnique(resource, item, id);
        item.put("id", id);
        var previous = list.set(index, item);
        try { persist(); }
        catch (RuntimeException ex) { list.set(index, previous); throw ex; }
        return item;
    }

    @DeleteMapping("/{resource}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    synchronized void delete(@PathVariable String resource, @PathVariable long id) {
        var list = records(resource);
        int index = indexOf(list, id);
        if (resource.equals("testcases") && records("executions").stream()
            .anyMatch(row -> ((Number) row.get("caseId")).longValue() == id))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "请先删除关联记录");
        var removed = list.remove(index);
        try { persist(); }
        catch (RuntimeException ex) { list.add(index, removed); throw ex; }
    }

    private List<Map<String,Object>> records(String resource) {
        var found = data.get(resource);
        if (found == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "未知资源");
        return found;
    }
    private int indexOf(List<Map<String,Object>> list, long id) {
        for (int i=0; i<list.size(); i++) if (((Number)list.get(i).get("id")).longValue() == id) return i;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "记录不存在");
    }
    private static void bad(String message) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message); }
    private static String text(Map<String,Object> input, Map<String,Object> out, String key, boolean required) {
        Object raw = input.get(key);
        if (raw != null && !(raw instanceof String)) bad(key + " 必须是文本");
        String value = raw == null ? "" : ((String)raw).trim();
        if (required && value.isBlank()) bad(key + " 不能为空");
        if (value.length() > 500) bad(key + " 最多 500 字");
        out.put(key, value);
        return value;
    }
    private static void choice(Map<String,Object> input, Map<String,Object> out, String key, String... options) {
        String value = text(input, out, key, true);
        if (!List.of(options).contains(value)) bad(key + " 不是可选类别");
    }
    private static LocalDate date(Map<String,Object> input, Map<String,Object> out, String key) {
        String value = text(input, out, key, true);
        try { var date = LocalDate.parse(value); out.put(key, date.toString()); return date; }
        catch (Exception ex) { bad(key + " 日期格式不正确"); return null; }
    }
    private static LocalDateTime dateTime(Map<String,Object> input, Map<String,Object> out, String key) {
        String value = text(input, out, key, true);
        try { var date = LocalDateTime.parse(value); out.put(key, date.toString()); return date; }
        catch (Exception ex) { bad(key + " 时间格式不正确"); return null; }
    }
    private static void number(Map<String,Object> input, Map<String,Object> out, String key) {
        try {
            var number = new BigDecimal(String.valueOf(input.get(key))).longValueExact();
            if (number < 0 || number > 100_000_000) bad(key + " 必须为合理的非负整数");
            out.put(key, number);
        } catch (NumberFormatException | ArithmeticException ex) { bad(key + " 必须为非负整数"); }
    }
    private static void money(Map<String,Object> input, Map<String,Object> out, String key) {
        try {
            var value = new BigDecimal(String.valueOf(input.get(key)));
            if (value.signum() < 0 || value.compareTo(new BigDecimal("100000000")) > 0 || value.scale() > 2) bad("费用最多两位小数，且不能为负");
            out.put(key, value);
        } catch (NumberFormatException ex) { bad("费用格式不正确"); }
    }
    private void relation(Map<String,Object> input, Map<String,Object> out, String key, String parent) {
        try {
            long id = Long.parseLong(String.valueOf(input.get(key)));
            indexOf(records(parent), id);
            out.put(key, id);
        } catch (NumberFormatException | ResponseStatusException ex) { bad("请先选择有效的关联档案"); }
    }
    private Map<String,Object> validate(String resource, Map<String,Object> input) {
        records(resource);
        var out = new LinkedHashMap<String,Object>();
        switch(resource) { case "testcases" -> {
                text(input, out, "caseNo", true);
                text(input, out, "title", true);
                text(input, out, "module", true);
                choice(input, out, "priority", "P0", "P1", "P2", "P3");
                text(input, out, "steps", true);
                text(input, out, "expected", true);
            } case "executions" -> {
                relation(input, out, "caseId", "testcases");
                text(input, out, "version", true);
                date(input, out, "eventDate");
                choice(input, out, "result", "未执行", "通过", "失败", "阻塞");
                choice(input, out, "status", "待复核", "已复核");
                text(input, out, "tester", true);
                text(input, out, "actual", false);
            } default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND); }
        return out;
    }
    private void ensureUnique(String resource, Map<String,Object> item, Long excludeId) {
        if (!resource.equals("testcases")) return;
        String number = (String) item.get("caseNo");
        boolean exists = records(resource).stream().anyMatch(row ->
            (excludeId == null || ((Number) row.get("id")).longValue() != excludeId)
            && number.equalsIgnoreCase((String) row.get("caseNo")));
        if (exists) throw new ResponseStatusException(HttpStatus.CONFLICT, "编号已存在");
    }

    private void persist() {
        try {
            Path parent = file.getParent();
            Files.createDirectories(parent);
            Path temp = Files.createTempFile(parent, "records-", ".tmp");
            try {
                var snapshot = new LinkedHashMap<String,Object>();
                snapshot.put("nextId", nextId);
                snapshot.putAll(data);
                mapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), snapshot);
                try { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
                catch (AtomicMoveNotSupportedException ex) { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING); }
            } finally { Files.deleteIfExists(temp); }
        } catch (IOException ex) { throw new IllegalStateException("写入数据失败：" + file, ex); }
    }
}
