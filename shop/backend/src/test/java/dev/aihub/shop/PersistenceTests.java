package dev.aihub.shop;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class PersistenceTests {
    @TempDir Path dir;
    @Test void checkoutSurvivesRestart() {
        String file = dir.resolve("shop.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        ShopController first = new ShopController(mapper, file);
        first.checkout(new CheckoutRequest("顾客", "buyer@example.com", List.of(new CartLine(1, 2))));
        ShopController restarted = new ShopController(mapper, file);
        assertEquals(16, restarted.products().getFirst().stock());
        assertEquals(1002, restarted.checkout(new CheckoutRequest("顾客", "buyer@example.com", List.of(new CartLine(1, 1)))).id());
    }
    @Test void overflowingCartDoesNotCreateAStateFile() {
        String file = dir.resolve("overflow.json").toString();
        ShopController app = new ShopController(new ObjectMapper().findAndRegisterModules(), file);
        org.springframework.web.server.ResponseStatusException error = assertThrows(
            org.springframework.web.server.ResponseStatusException.class,
            () -> app.checkout(new CheckoutRequest("顾客", "buyer@example.com",
                List.of(new CartLine(1, Integer.MAX_VALUE), new CartLine(1, Integer.MAX_VALUE)))));
        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, error.getStatusCode());
        assertEquals(18, app.products().getFirst().stock());
        assertTrue(app.orders("buyer@example.com").isEmpty());
        assertFalse(java.nio.file.Files.exists(Path.of(file)));
    }
    @Test void failedWriteDoesNotChangeMemory() throws Exception {
        String file = dir.resolve("blocked.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        ShopController app = new ShopController(mapper, file);
        java.nio.file.Files.createDirectory(Path.of(file));
        assertThrows(IllegalStateException.class, () -> app.checkout(new CheckoutRequest("顾客", "buyer@example.com", List.of(new CartLine(1, 1)))));
        assertEquals(18, app.products().getFirst().stock());
    }
    @Test void corruptFileRefusesStartup() throws Exception {
        Path file = dir.resolve("corrupt.json");
        java.nio.file.Files.writeString(file, "not-json");
        assertThrows(IllegalStateException.class, () -> new ShopController(new ObjectMapper().findAndRegisterModules(), file.toString()));
    }
}
