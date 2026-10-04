package dev.aihub.recycling;

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
    private Map<String,Object> receipt(String no, double weight, double amount, String status) {
        return new LinkedHashMap<>(Map.of("materialId", 1, "receiptNo", no, "source", "演示来源", "weight", weight,
            "amount", amount, "eventDate", "2026-10-04", "status", status, "notes", "虚构数据"));
    }
    @Test void weighingAndSettlementAreValidatedAndPersisted() {
        var app = controller();
        rejected(HttpStatus.BAD_REQUEST, () -> app.create("receipts", receipt("R-2", 0, 10, "待核对")));
        rejected(HttpStatus.BAD_REQUEST, () -> app.create("receipts", receipt("R-2", 10, 0, "待核对")));
        rejected(HttpStatus.BAD_REQUEST, () -> app.create("receipts", receipt("R-3", 1, 1, "已结算")));
        var created = app.create("receipts", receipt("R-2", 10.5, 12.6, "待核对"));
        long id = ((Number)created.get("id")).longValue();
        rejected(HttpStatus.CONFLICT, () -> app.create("receipts", receipt("r-2", 1, 1, "待核对")));
        app.update("receipts", id, receipt("R-2", 10.5, 12.6, "已结算"));
        rejected(HttpStatus.CONFLICT, () -> app.update("receipts", id, receipt("R-2", 10.5, 12.6, "待核对")));
        assertEquals("已结算", controller().list("receipts").stream().filter(x -> ((Number)x.get("id")).longValue() == id).findFirst().orElseThrow().get("status"));
    }
    @Test void cannotRemoveReferencedMaterial() {
        var app = controller();
        rejected(HttpStatus.CONFLICT, () -> app.delete("materials", 1));
    }
}
