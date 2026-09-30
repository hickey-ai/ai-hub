package dev.aihub.oa;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
class PersistenceTests {
    @TempDir Path dir;
    @Test void decisionSurvivesRestart() {
        String file = dir.resolve("oa.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        OaController first = new OaController(mapper, file);
        Request request = first.create(new RequestInput("报销", "差旅", "张三", "客户拜访", 100));
        first.submit(request.id());
        first.review(request.id(), new DecisionInput("已驳回", "票据缺失"));
        OaController restarted = new OaController(mapper, file);
        assertEquals("已驳回", restarted.requests().getLast().status());
        assertEquals("票据缺失", restarted.requests().getLast().reviewNote());
        assertEquals(6, restarted.create(new RequestInput("请假", "休假", "张三", "年假", 0)).id());
    }
    @Test void failedWriteDoesNotChangeMemory() throws Exception {
        String file = dir.resolve("blocked.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        OaController app = new OaController(mapper, file);
        java.nio.file.Files.createDirectory(Path.of(file));
        assertThrows(IllegalStateException.class, () -> app.create(new RequestInput("报销", "差旅", "张三", "客户拜访", 100)));
        assertEquals(4, app.requests().size());
    }
    @Test void corruptFileRefusesStartup() throws Exception {
        Path file = dir.resolve("corrupt.json");
        java.nio.file.Files.writeString(file, "not-json");
        assertThrows(IllegalStateException.class, () -> new OaController(new ObjectMapper().findAndRegisterModules(), file.toString()));
    }
}
