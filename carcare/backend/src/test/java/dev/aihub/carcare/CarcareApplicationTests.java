package dev.aihub.carcare;

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
class CarcareApplicationTests {
    private static final Path FILE;
    static {
        try { FILE = Files.createTempDirectory("aihub-carcare-test-").resolve("records.json"); }
        catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    @DynamicPropertySource static void properties(DynamicPropertyRegistry registry) {
        registry.add("aihub.data-file", () -> FILE.toString());
    }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void crudValidationAndRestartPersistence() throws Exception {
        mvc.perform(get("/api/vehicles")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/noSuchResource")).andExpect(status().isNotFound());
        mvc.perform(put("/api/vehicles/99999").contentType(APPLICATION_JSON).content("{\"plate\":\"新车A\",\"model\":\"电动车\",\"mileage\":100,\"notes\":\"测试\"}")).andExpect(status().isNotFound());
        mvc.perform(post("/api/vehicles").contentType(APPLICATION_JSON).content("{\"plate\":\"\",\"model\":\"电动车\",\"mileage\":100}")).andExpect(status().isBadRequest());

        mvc.perform(delete("/api/vehicles/1")).andExpect(status().isConflict());
        mvc.perform(post("/api/services").contentType(APPLICATION_JSON).content("{\"vehicleId\":99999,\"date\":\"2026-10-01\",\"type\":\"维修\",\"mileage\":100,\"cost\":2}" )).andExpect(status().isBadRequest());
        mvc.perform(post("/api/services").contentType(APPLICATION_JSON).content("{\"vehicleId\":1,\"date\":\"2026-10-01\",\"type\":\"维修\",\"mileage\":28500,\"cost\":180.50,\"notes\":\"检查\"}" )).andExpect(status().isCreated());
        mvc.perform(post("/api/services").contentType(APPLICATION_JSON).content("{\"vehicleId\":1,\"date\":\"2026-10-01\",\"type\":\"维修\",\"mileage\":28500,\"cost\":-1}"))
            .andExpect(status().isBadRequest());
        String payload = "{\"plate\":\"新车A\",\"model\":\"电动车\",\"mileage\":100,\"notes\":\"测试\"}";
        String created = mvc.perform(post("/api/vehicles").contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(created).get("id").asLong();
        mvc.perform(put("/api/vehicles/"+id).contentType(APPLICATION_JSON).content(payload))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
        assertTrue(Files.exists(FILE));
        var restarted = new RecordsController(mapper, FILE.toString());
        assertTrue(restarted.list("vehicles").stream().anyMatch(row -> ((Number)row.get("id")).longValue() == id));
        mvc.perform(delete("/api/vehicles/"+id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/vehicles/"+id)).andExpect(status().isNotFound());
    }

    @Test void failedWriteRollsBackMemory() throws Exception {
        Path blocked = Files.createTempFile("aihub-carcare-blocked-", ".file");
        var controller = new RecordsController(mapper, blocked.resolve("data.json").toString());
        var input = mapper.readValue("{\"plate\":\"测试车\",\"model\":\"SUV\",\"mileage\":100}", java.util.Map.class);
        int before = controller.list("vehicles").size();
        assertThrows(IllegalStateException.class, () -> controller.create("vehicles", input));
        assertEquals(before, controller.list("vehicles").size());
        var original = controller.list("vehicles").get(0);
        assertThrows(IllegalStateException.class, () -> controller.update("vehicles", 1L, input));
        assertEquals(original, controller.list("vehicles").get(0));
        int childCount = controller.list("services").size();
        assertThrows(IllegalStateException.class, () -> controller.delete("services", 2L));
        assertEquals(childCount, controller.list("services").size());
    }
}
