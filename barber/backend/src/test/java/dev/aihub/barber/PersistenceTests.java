package dev.aihub.barber;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceTests {
    @TempDir Path dir;

    @Test void bookingSurvivesRestart() {
        String file = dir.resolve("barber.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        String date = LocalDate.now().plusDays(1).toString();
        BarberController first = new BarberController(mapper, file);
        BarberController.Booking created = first.book(new BarberController.BookingInput(1, 1, date, "10:00", "顾客", "13800138000"));
        BarberController restarted = new BarberController(mapper, file);
        assertEquals(created.id(), restarted.booking(created.id(), "13800138000").id());
        assertFalse(((java.util.List<?>) restarted.slots(1, date).get("available")).contains("10:00"));
    }

    @Test void failedWriteDoesNotChangeMemory() throws Exception {
        Path blockedParent = dir.resolve("blocked-parent");
        Files.writeString(blockedParent, "not-a-directory");
        Path file = blockedParent.resolve("barber.json");
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        BarberController app = new BarberController(mapper, file.toString());
        String date = LocalDate.now().plusDays(1).toString();
        assertThrows(IllegalStateException.class, () -> app.book(new BarberController.BookingInput(1, 1, date, "10:00", "顾客", "13800138000")));
        assertTrue(app.bookings("13800138000").isEmpty());
    }
}
