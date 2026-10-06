package dev.aihub.manufacturing;

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
public class ManufacturingApplication {
    public static void main(String[] args) { SpringApplication.run(ManufacturingApplication.class, args); }
}

@RestController
@RequestMapping("/api")
class RecordsController {
    private final ObjectMapper mapper;
    private final Path file;
    private final Map<String, List<Map<String,Object>>> data = new LinkedHashMap<>();
    private long nextId = 1;
    private final List<Map<String,Object>> productionEvents = new ArrayList<>();

    RecordsController(ObjectMapper mapper, @Value("${aihub.data-file}") String fileName) {
        this.mapper = mapper;
        this.file = Path.of(fileName).toAbsolutePath();
        for (String name : List.of("materials", "workorders")) data.put(name, new ArrayList<>());
        if (Files.exists(file)) {
            try {
                Map<String,Object> saved = mapper.readValue(file.toFile(), new TypeReference<>() {});
                if (!(saved.get("nextId") instanceof Number)) throw new IOException("missing nextId");
                nextId = ((Number)saved.get("nextId")).longValue();
                Object storedEvents = saved.getOrDefault("productionEvents", List.of());
                if (!(storedEvents instanceof List<?> eventList)) throw new IOException("invalid productionEvents");
                for (Object value : eventList) {
                    if (!(value instanceof Map<?,?> row) || !(row.get("eventId") instanceof String)
                        || !(row.get("workorderId") instanceof Number) || !(row.get("kind") instanceof String)
                        || !(row.get("quantity") instanceof Number) || !(row.get("time") instanceof String))
                        throw new IOException("invalid production event");
                    @SuppressWarnings("unchecked") Map<String,Object> item = (Map<String,Object>) row;
                    productionEvents.add(new LinkedHashMap<>(item));
                }
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
            seed("materials", Map.of("code", "MAT-101", "name", "铝合金壳体", "unit", "件", "available", 320, "supplier", "演示供应商"));
            seed("workorders", Map.of("materialId", 1, "orderNo", "WO-2026-01", "product", "智能控制盒", "quantity", 80, "dueDate", "2026-10-15", "status", "生产中", "notes", "演示工单"));
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
        item.put("id", id);
        var previous = list.get(index);
        if (resource.equals("workorders") && productionEvents.stream().anyMatch(e -> ((Number)e.get("workorderId")).longValue() == id)) {
            for (String key : List.of("materialId", "orderNo", "product", "quantity", "dueDate", "status"))
                if (!String.valueOf(previous.get(key)).equals(String.valueOf(item.get(key))))
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "已有模拟事件的工单只能修改备注");
        }
        list.set(index, item);
        try { persist(); }
        catch (RuntimeException ex) { list.set(index, previous); throw ex; }
        return item;
    }

    @DeleteMapping("/{resource}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    synchronized void delete(@PathVariable String resource, @PathVariable long id) {
        var list = records(resource);
        int index = indexOf(list, id);
        if (resource.equals("workorders") && productionEvents.stream()
            .anyMatch(e -> ((Number)e.get("workorderId")).longValue() == id))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "有追溯事件的工单不可删除");
        if (resource.equals("materials") && records("workorders").stream()
            .anyMatch(row -> ((Number) row.get("materialId")).longValue() == id))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "请先删除关联记录");
        var removed = list.remove(index);
        try { persist(); }
        catch (RuntimeException ex) { list.add(index, removed); throw ex; }
    }

    // This is a deterministic offline device simulator, not a PLC/OT interface.
    @GetMapping("/workorders/{id}/trace")
    synchronized Map<String,Object> trace(@PathVariable long id) {
        var order = records("workorders").get(indexOf(records("workorders"), id));
        long produced = productionEvents.stream()
            .filter(e -> ((Number)e.get("workorderId")).longValue() == id && e.get("kind").equals("OUTPUT"))
            .mapToLong(e -> ((Number)e.get("quantity")).longValue()).sum();
        var result = new LinkedHashMap<String,Object>();
        result.put("order", new LinkedHashMap<>(order));
        result.put("produced", produced);
        result.put("events", productionEvents.stream().filter(e -> ((Number)e.get("workorderId")).longValue() == id)
            .map(LinkedHashMap::new).toList());
        return result;
    }

    @PostMapping("/workorders/{id}/simulate")
    @ResponseStatus(HttpStatus.CREATED)
    synchronized Map<String,Object> simulate(@PathVariable long id, @RequestBody Map<String,Object> input) {
        var orders = records("workorders");
        int index = indexOf(orders, id);
        if (input == null) bad("事件不能为空");
        Object rawId = input.get("eventId"), rawKind = input.get("kind");
        if (!(rawId instanceof String) || !((String)rawId).matches("[A-Za-z0-9_-]{1,80}")
            || !(rawKind instanceof String) || !List.of("START", "OUTPUT", "FAULT", "RESUME", "QC", "FINISH").contains(rawKind))
            bad("eventId 或 kind 不合法");
        String eventId = (String) rawId, kind = (String) rawKind;
        long quantity;
        try { quantity = new BigDecimal(String.valueOf(input.get("quantity"))).longValueExact(); }
        catch (NumberFormatException | ArithmeticException ex) { bad("quantity 必须为整数"); return Map.of(); }
        if (kind.equals("OUTPUT") ? quantity <= 0 : quantity != 0) bad("该事件的 quantity 不合法");
        for (var existing : productionEvents) if (existing.get("eventId").equals(eventId)) {
            if (((Number)existing.get("workorderId")).longValue() == id
                && existing.get("kind").equals(kind) && ((Number)existing.get("quantity")).longValue() == quantity)
                return new LinkedHashMap<>(existing); // durable idempotency, including after restart
            throw new ResponseStatusException(HttpStatus.CONFLICT, "eventId 已用于其他事件");
        }
        var order = orders.get(index);
        String status = (String)order.get("status");
        String required = switch (kind) {
            case "START" -> "待排产";
            case "OUTPUT", "FAULT", "QC" -> "生产中";
            case "RESUME" -> "异常停机";
            default -> "质检中";
        };
        if (!status.equals(required)) throw new ResponseStatusException(HttpStatus.CONFLICT, "工单状态不允许该事件");
        long produced = (long)trace(id).get("produced");
        long planned = ((Number)order.get("quantity")).longValue();
        if (kind.equals("OUTPUT") && quantity > planned - produced)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "产出超过计划数量");
        if (kind.equals("QC") && (planned == 0 || produced != planned))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "必须完成计划产量后再提交质检");
        String nextStatus = switch (kind) {
            case "START", "RESUME" -> "生产中";
            case "FAULT" -> "异常停机";
            case "QC" -> "质检中";
            case "FINISH" -> "已完成";
            default -> status;
        };
        var updated = new LinkedHashMap<>(order);
        updated.put("status", nextStatus);
        var event = new LinkedHashMap<String,Object>();
        event.put("eventId", eventId);
        event.put("workorderId", id);
        event.put("deviceId", "SIM-01");
        event.put("kind", kind);
        event.put("quantity", quantity);
        event.put("time", Instant.now().toString());
        orders.set(index, updated);
        productionEvents.add(event);
        try { persist(); }
        catch (RuntimeException ex) { productionEvents.remove(productionEvents.size()-1); orders.set(index, order); throw ex; }
        return new LinkedHashMap<>(event);
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
        switch(resource) { case "materials" -> {
                text(input, out, "code", true);
                text(input, out, "name", true);
                text(input, out, "unit", true);
                number(input, out, "available");
                text(input, out, "supplier", true);
            } case "workorders" -> {
                relation(input, out, "materialId", "materials");
                text(input, out, "orderNo", true);
                text(input, out, "product", true);
                number(input, out, "quantity");
                date(input, out, "dueDate");
                choice(input, out, "status", "待排产", "生产中", "异常停机", "质检中", "已完成");
                text(input, out, "notes", false);
            } default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND); }
        return out;
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
                snapshot.put("productionEvents", productionEvents);
                mapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(), snapshot);
                try { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
                catch (AtomicMoveNotSupportedException ex) { Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING); }
            } finally { Files.deleteIfExists(temp); }
        } catch (IOException ex) { throw new IllegalStateException("写入数据失败：" + file, ex); }
    }
}
