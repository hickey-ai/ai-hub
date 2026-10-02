package dev.aihub.forestry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
@SpringBootTest @AutoConfigureMockMvc
class ForestryApplicationTests {
    static final Path FILE;
    static { try { FILE=Files.createTempDirectory("aihub-forestry-").resolve("data.json"); } catch(Exception e) { throw new ExceptionInInitializerError(e); } }
    @DynamicPropertySource static void props(DynamicPropertyRegistry r) { r.add("aihub.data-file",()->FILE.toString()); }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Test void crudValidationAndPersistence() throws Exception {
        String resource="patrols";
        mvc.perform(get("/api/parcels")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/unknown")).andExpect(status().isNotFound());
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
        String payload=mapper.writeValueAsString(mapper.readValue("{\"parcelId\": 1, \"date\": \"2026-10-02\", \"observer\": \"演示护林员\", \"finding\": \"无异常\", \"notes\": \"虚构巡护记录\"}",java.util.Map.class));
        var invalidRelation = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree(payload);
        invalidRelation.put("parcelId", 999999);
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(invalidRelation))).andExpect(status().isBadRequest());
        var invalidChoice = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree(payload);
        invalidChoice.put("finding", "invalid-choice");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(invalidChoice))).andExpect(status().isBadRequest());
        var invalidDate = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree(payload);
        invalidDate.put("date", "2026-99-99");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(invalidDate))).andExpect(status().isBadRequest());
        mvc.perform(get("/api/"+resource)).andExpect(jsonPath("$.length()").value(1));
        String created=mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id=mapper.readTree(created).get("id").asLong();
        mvc.perform(delete("/api/parcels/1")).andExpect(status().isConflict());
        mvc.perform(put("/api/"+resource+"/"+id).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isOk());
        assertTrue(Files.exists(FILE));
        assertEquals(2,new RecordsController(mapper,FILE.toString()).list(resource).size());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNotFound());
    }
}
