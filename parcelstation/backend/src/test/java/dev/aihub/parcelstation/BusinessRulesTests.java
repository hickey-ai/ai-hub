package dev.aihub.parcelstation;

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
    private Map<String,Object> parcel(String tracking, String code, String status, String notes) {
        return new LinkedHashMap<>(Map.of("shelfId", 1, "trackingNo", tracking, "recipient", "演示用户",
            "pickupCode", code, "eventDate", "2026-10-04", "status", status, "notes", notes));
    }
    @Test void pickupFlowAndConflictsSurviveRestart() {
        var app = controller();
        rejected(HttpStatus.BAD_REQUEST, () -> app.create("parcels", parcel("P-101", "ZZ-1", "已签收", "")));
        var first = app.create("parcels", parcel("P-102", "ABC-12", "待取件", ""));
        long id = ((Number)first.get("id")).longValue();
        rejected(HttpStatus.CONFLICT, () -> app.create("parcels", parcel("p-102", "OTHER", "待取件", "")));
        rejected(HttpStatus.CONFLICT, () -> app.create("parcels", parcel("P-103", "abc-12", "待取件", "")));
        rejected(HttpStatus.BAD_REQUEST, () -> app.update("parcels", id, parcel("P-102", "ABC-12", "异常件", "")));
        app.update("parcels", id, parcel("P-102", "ABC-12", "已签收", "签收演示"));
        rejected(HttpStatus.CONFLICT, () -> app.update("parcels", id, parcel("P-102", "ABC-12", "待取件", "")));
        assertEquals("已签收", controller().list("parcels").stream().filter(x -> ((Number)x.get("id")).longValue() == id).findFirst().orElseThrow().get("status"));
        assertEquals(2, controller().list("parcels").size());
    }
    @Test void failedWriteKeepsOriginalState() throws Exception {
        var app = controller();
        Files.createDirectory(dir.resolve("records.json"));
        assertThrows(IllegalStateException.class, () -> app.create("parcels", parcel("P-104", "Q-3", "待取件", "")));
        assertEquals(1, app.list("parcels").size());
    }
}
