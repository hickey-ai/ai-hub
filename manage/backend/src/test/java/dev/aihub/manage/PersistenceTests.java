package dev.aihub.manage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
class PersistenceTests {
    @TempDir Path dir;
    @Test void editsSurviveRestart() {
        String file = dir.resolve("manage.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        ManageController first = new ManageController(mapper, file);
        first.create(new UserInput("新人", "new@example.com", "编辑", "产品", "正常"));
        first.delete(1);
        ManageController restarted = new ManageController(mapper, file);
        assertEquals(8, restarted.users().size());
        assertTrue(restarted.users().stream().anyMatch(u -> u.email().equals("new@example.com")));
        assertTrue(restarted.logs().stream().anyMatch(l -> l.action().equals("新增用户：新人")));
        assertEquals(10, restarted.create(new UserInput("下一位", "next@example.com", "编辑", "产品", "正常")).id());
    }
    @Test void failedWriteDoesNotChangeMemory() throws Exception {
        String file = dir.resolve("blocked.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        ManageController app = new ManageController(mapper, file);
        java.nio.file.Files.createDirectory(Path.of(file));
        assertThrows(IllegalStateException.class, () -> app.create(new UserInput("新人", "new@example.com", "编辑", "产品", "正常")));
        assertEquals(8, app.users().size());
        assertTrue(app.logs().stream().noneMatch(l -> l.action().equals("新增用户：新人")));
        java.nio.file.Files.delete(Path.of(file));
        assertEquals(9, app.create(new UserInput("新人", "new@example.com", "编辑", "产品", "正常")).id());
    }
    @Test void corruptFileRefusesStartup() throws Exception {
        Path file = dir.resolve("corrupt.json");
        java.nio.file.Files.writeString(file, "not-json");
        assertThrows(IllegalStateException.class, () -> new ManageController(new ObjectMapper().findAndRegisterModules(), file.toString()));
    }
}
