package dev.aihub.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ScheduleApplicationTests {
    private static final Path FILE;
    static {
        try { FILE = Files.createTempDirectory("aihub-schedule-test-").resolve("records.json"); }
        catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    @DynamicPropertySource static void properties(DynamicPropertyRegistry registry) {
        registry.add("aihub.data-file", () -> FILE.toString());
    }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void crudValidationAndRestartPersistence() throws Exception {
        mvc.perform(get("/api/events")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/noSuchResource")).andExpect(status().isNotFound());
        mvc.perform(put("/api/events/99999").contentType(APPLICATION_JSON).content("{\"title\":\"体检预约\",\"startAt\":\"2026-10-02T09:00\",\"endAt\":\"2026-10-02T10:00\",\"category\":\"生活\",\"notes\":\"演示\",\"done\":false}")).andExpect(status().isNotFound());
        mvc.perform(post("/api/events").contentType(APPLICATION_JSON).content("{\"title\":\"\",\"startAt\":\"2026-10-02T09:00\",\"endAt\":\"2026-10-02T10:00\",\"category\":\"生活\"}")).andExpect(status().isBadRequest());
        mvc.perform(post("/api/events").contentType(APPLICATION_JSON).content("{\"title\":\"无效时间\",\"startAt\":\"2026-10-02T10:00\",\"endAt\":\"2026-10-02T09:00\",\"category\":\"生活\"}"))
            .andExpect(status().isBadRequest());
        String payload = "{\"title\":\"体检预约\",\"startAt\":\"2026-10-02T09:00\",\"endAt\":\"2026-10-02T10:00\",\"category\":\"生活\",\"notes\":\"演示\",\"done\":false}";
        String created = mvc.perform(post("/api/events").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(created).get("id").asLong();
        mvc.perform(put("/api/events/"+id).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
        assertTrue(Files.exists(FILE));
        var restarted = new RecordsController(mapper, FILE.toString());
        assertTrue(restarted.list("events").stream().anyMatch(row -> ((Number)row.get("id")).longValue() == id));
        mvc.perform(delete("/api/events/"+id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/events/"+id)).andExpect(status().isNotFound());
    }

    @Test void failedWriteRollsBackMemory() throws Exception {
        Path blocked = Files.createTempFile("aihub-schedule-blocked-", ".file");
        var controller = new RecordsController(mapper, blocked.resolve("data.json").toString());
        var input = mapper.readValue("{\"title\":\"测试\",\"startAt\":\"2026-10-02T09:00\",\"endAt\":\"2026-10-02T10:00\",\"category\":\"生活\"}", java.util.Map.class);
        int before = controller.list("events").size();
        assertThrows(IllegalStateException.class, () -> controller.create("events", input));
        assertEquals(before, controller.list("events").size());
        var original = controller.list("events").get(0);
        assertThrows(IllegalStateException.class, () -> controller.update("events", 1L, input));
        assertEquals(original, controller.list("events").get(0));
        assertThrows(IllegalStateException.class, () -> controller.delete("events", 1L));
        assertEquals(before, controller.list("events").size());
    }
}
