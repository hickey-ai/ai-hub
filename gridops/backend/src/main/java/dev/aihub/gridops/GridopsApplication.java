package dev.aihub.gridops;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

@SpringBootApplication
public class GridopsApplication {
    public static void main(String[] args) { SpringApplication.run(GridopsApplication.class, args); }
}

@RestController
@RequestMapping("/api")
class GridController {
    private final ObjectMapper mapper;
    private final Path file;
    private final List<Map<String,Object>> cases = new ArrayList<>();
    private long nextId = 1;

    GridController(ObjectMapper mapper, @Value("${aihub.data-file}") String path) {
        this.mapper = mapper;
        this.file = Path.of(path).toAbsolutePath();
        if (Files.exists(file)) {
            try {
                Map<String,Object> saved = mapper.readValue(file.toFile(), new TypeReference<>() {});
                if (!(saved.get("nextId") instanceof Number n) || n.longValue() < 1 || !(saved.get("cases") instanceof List<?> rows))
                    throw new IOException("invalid snapshot");
                nextId = n.longValue();
                for (Object row : rows) {
                    if (!(row instanceof Map<?,?> record) || !(record.get("id") instanceof Number)
                        || !(record.get("history") instanceof List<?>) || !(record.get("status") instanceof String))
                        throw new IOException("invalid case");
                    @SuppressWarnings("unchecked") Map<String,Object> item = (Map<String,Object>) record;
                    cases.add(new LinkedHashMap<>(item));
                }
            } catch (IOException | ClassCastException ex) {
                throw new IllegalStateException("数据文件损坏，请备份并修复：" + file, ex);
            }
        } else {
            var demo = new LinkedHashMap<String,Object>();
            demo.put("id", nextId++); demo.put("number", "GRID-DEMO-001"); demo.put("grid", "演示 A 网格");
            demo.put("category", "设施维护"); demo.put("title", "虚构路灯报修演示"); demo.put("description", "用于体验完整处理流程的虚构事件");
            demo.put("assignee", "演示处理人"); demo.put("status", "待受理");
            demo.put("history", List.of(Map.of("to", "待受理", "at", "2026-10-03T00:00:00Z", "note", "演示数据")));
            cases.add(demo);
        }
    }

    @GetMapping("/cases")
    synchronized List<Map<String,Object>> list() { return copy(); }

    @GetMapping("/cases/{id}")
    synchronized Map<String,Object> get(@PathVariable long id) { return new LinkedHashMap<>(cases.get(find(id))); }

    @PostMapping("/cases")
    @ResponseStatus(HttpStatus.CREATED)
    synchronized Map<String,Object> create(@RequestBody Map<String,Object> input) {
        String number = text(input, "number", 40), grid = text(input, "grid", 60), title = text(input, "title", 120);
        unique(number, null);
        var item = new LinkedHashMap<String,Object>();
        item.put("id", nextId++); item.put("number", number); item.put("grid", grid);
        item.put("category", text(input, "category", 40)); item.put("title", title);
        item.put("description", text(input, "description", 500)); item.put("assignee", text(input, "assignee", 60));
        item.put("status", "待受理");
        item.put("history", List.of(Map.of("to", "待受理", "at", Instant.now().toString(), "note", "事件登记")));
        cases.add(item);
        try { persist(); } catch (RuntimeException ex) { cases.remove(item); nextId--; throw ex; }
        return new LinkedHashMap<>(item);
    }

    @PutMapping("/cases/{id}/transition")
    synchronized Map<String,Object> transition(@PathVariable long id, @RequestBody Map<String,Object> input) {
        int index = find(id);
        Map<String,Object> previous = cases.get(index);
        String from = (String) previous.get("status");
        String to = text(input, "to", 20);
        boolean valid = switch (from) {
            case "待受理" -> to.equals("处理中");
            case "处理中" -> to.equals("待核验");
            case "待核验" -> to.equals("已办结") || to.equals("处理中");
            default -> false;
        };
        if (!valid) throw new ResponseStatusException(HttpStatus.CONFLICT, "状态流转不允许");
        String note = text(input, "note", 300);
        var updated = new LinkedHashMap<>(previous);
        var history = new ArrayList<Object>((List<?>) previous.get("history"));
        history.add(Map.of("from", from, "to", to, "at", Instant.now().toString(), "note", note));
        updated.put("status", to); updated.put("history", history);
        cases.set(index, updated);
        try { persist(); } catch (RuntimeException ex) { cases.set(index, previous); throw ex; }
        return new LinkedHashMap<>(updated);
    }

    @DeleteMapping("/cases/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    synchronized void delete(@PathVariable long id) {
        int index = find(id);
        if (!"待受理".equals(cases.get(index).get("status")))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "已流转事件不可删除");
        var removed = cases.remove(index);
        try { persist(); } catch (RuntimeException ex) { cases.add(index, removed); throw ex; }
    }

    private int find(long id) {
        for (int i=0; i<cases.size(); i++) if (((Number)cases.get(i).get("id")).longValue() == id) return i;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "事件不存在");
    }
    private void unique(String number, Long exclude) {
        if (cases.stream().anyMatch(row -> (exclude == null || ((Number)row.get("id")).longValue() != exclude)
            && number.equalsIgnoreCase((String)row.get("number"))))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "事件编号已存在");
    }
    private static String text(Map<String,Object> input, String key, int max) {
        Object raw = input.get(key);
        if (!(raw instanceof String value) || value.isBlank() || value.trim().length() > max)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, key + " 必填且长度不超过 " + max);
        return value.trim();
    }
    private List<Map<String,Object>> copy() {
        return cases.stream().map(row -> mapper.convertValue(row, new TypeReference<Map<String,Object>>() {})).toList();
    }
    private void persist() {
        try {
            Path parent = file.getParent(); Files.createDirectories(parent);
            Path temp = Files.createTempFile(parent, "gridops-", ".tmp");
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), Map.of("nextId", nextId, "cases", cases));
                try { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
                catch (AtomicMoveNotSupportedException ex) { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING); }
            } finally { Files.deleteIfExists(temp); }
        } catch (IOException ex) { throw new IllegalStateException("写入数据失败：" + file, ex); }
    }
}
