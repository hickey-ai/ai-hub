package dev.aihub.childcare;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BusinessRulesTests {
    @TempDir Path dir;
    private final ObjectMapper mapper = new ObjectMapper();
    private RecordsController controller() { return new RecordsController(mapper, dir.resolve("records.json").toString()); }
    private static void rejected(HttpStatus status, org.junit.jupiter.api.function.Executable work) {
        assertEquals(status, assertThrows(ResponseStatusException.class, work).getStatusCode());
    }
    private Map<String,Object> handoff(String alias, String type, String status, String notes) {
        return new LinkedHashMap<>(Map.of("groupId", 1, "childAlias", alias, "guardianAlias", "演示接送人",
            "eventDate", "2026-10-04", "type", type, "status", status, "notes", notes));
    }
    @Test void handoffRequiresNotesOnExceptionAndPreventsCompletedRollback() {
        var app = controller();
        rejected(HttpStatus.BAD_REQUEST, () -> app.create("handoffs", handoff("演示儿童二", "入园", "异常待核", "")));
        rejected(HttpStatus.BAD_REQUEST, () -> app.create("handoffs", handoff("演示儿童二", "入园", "已交接", "")));
        var created = app.create("handoffs", handoff("演示儿童二", "入园", "待交接", "午睡计划待确认"));
        long id = ((Number)created.get("id")).longValue();
        app.update("handoffs", id, handoff("演示儿童二", "入园", "已交接", "家长已确认"));
        rejected(HttpStatus.CONFLICT, () -> app.update("handoffs", id, handoff("演示儿童二", "入园", "待交接", "")));
        assertEquals("已交接", controller().list("handoffs").stream().filter(x -> ((Number)x.get("id")).longValue() == id).findFirst().orElseThrow().get("status"));
    }
    @Test void exceptionCanBeReopenedBeforeHandoff() {
        var app = controller();
        var created = app.create("handoffs", handoff("演示儿童三", "离园", "待交接", ""));
        long id = ((Number)created.get("id")).longValue();
        app.update("handoffs", id, handoff("演示儿童三", "离园", "异常待核", "接送人身份待核"));
        app.update("handoffs", id, handoff("演示儿童三", "离园", "待交接", "已联系监护人"));
        assertEquals("待交接", controller().list("handoffs").stream().filter(x -> ((Number)x.get("id")).longValue() == id).findFirst().orElseThrow().get("status"));
    }
}
