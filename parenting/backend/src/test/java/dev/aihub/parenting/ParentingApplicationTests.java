package dev.aihub.parenting;

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
class ParentingApplicationTests {
    private static final Path FILE;
    static {
        try { FILE = Files.createTempDirectory("aihub-parenting-test-").resolve("records.json"); }
        catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    @DynamicPropertySource static void properties(DynamicPropertyRegistry registry) {
        registry.add("aihub.data-file", () -> FILE.toString());
    }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void crudValidationAndRestartPersistence() throws Exception {
        mvc.perform(get("/api/children")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/noSuchResource")).andExpect(status().isNotFound());
        mvc.perform(put("/api/children/99999").contentType(APPLICATION_JSON).content("{\"name\":\"测试档案\",\"birthDate\":\"2022-01-01\",\"notes\":\"虚构\"}")).andExpect(status().isNotFound());
        mvc.perform(post("/api/children").contentType(APPLICATION_JSON).content("{\"name\":\"\",\"birthDate\":\"2022-01-01\"}")).andExpect(status().isBadRequest());

        mvc.perform(delete("/api/children/1")).andExpect(status().isConflict());
        mvc.perform(post("/api/moments").contentType(APPLICATION_JSON).content("{\"childId\":99999,\"date\":\"2026-10-01\",\"category\":\"其他\",\"title\":\"测试\"}" )).andExpect(status().isBadRequest());
        mvc.perform(post("/api/moments").contentType(APPLICATION_JSON).content("{\"childId\":1,\"date\":\"2026-10-01\",\"category\":\"学习兴趣\",\"title\":\"阅读\",\"notes\":\"演示\"}" )).andExpect(status().isCreated());
        mvc.perform(post("/api/moments").contentType(APPLICATION_JSON).content("{\"childId\":1,\"date\":\"2026-10-01\",\"category\":\"不存在的类别\",\"title\":\"无效\"}"))
            .andExpect(status().isBadRequest());
        String payload = "{\"name\":\"测试档案\",\"birthDate\":\"2022-01-01\",\"notes\":\"虚构\"}";
        String created = mvc.perform(post("/api/children").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(created).get("id").asLong();
        mvc.perform(put("/api/children/"+id).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
        assertTrue(Files.exists(FILE));
        var restarted = new RecordsController(mapper, FILE.toString());
        assertTrue(restarted.list("children").stream().anyMatch(row -> ((Number)row.get("id")).longValue() == id));
        mvc.perform(delete("/api/children/"+id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/children/"+id)).andExpect(status().isNotFound());
    }

    @Test void failedWriteRollsBackMemory() throws Exception {
        Path blocked = Files.createTempFile("aihub-parenting-blocked-", ".file");
        var controller = new RecordsController(mapper, blocked.resolve("data.json").toString());
        var input = mapper.readValue("{\"name\":\"虚构档案\",\"birthDate\":\"2022-01-01\"}", java.util.Map.class);
        int before = controller.list("children").size();
        assertThrows(IllegalStateException.class, () -> controller.create("children", input));
        assertEquals(before, controller.list("children").size());
        var original = controller.list("children").get(0);
        assertThrows(IllegalStateException.class, () -> controller.update("children", 1L, input));
        assertEquals(original, controller.list("children").get(0));
        int childCount = controller.list("moments").size();
        assertThrows(IllegalStateException.class, () -> controller.delete("moments", 2L));
        assertEquals(childCount, controller.list("moments").size());
    }
}
