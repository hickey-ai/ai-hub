package dev.aihub.bugtrack;
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
class BugtrackApplicationTests {
    static final Path FILE;
    static { try { FILE=Files.createTempDirectory("aihub-bugtrack-").resolve("data.json"); } catch(Exception e) { throw new ExceptionInInitializerError(e); } }
    @DynamicPropertySource static void props(DynamicPropertyRegistry r) { r.add("aihub.data-file",()->FILE.toString()); }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Test void crudValidationAndPersistence() throws Exception {
        String resource="defects";
        mvc.perform(get("/api/projects")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/unknown")).andExpect(status().isNotFound());
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
        String payload=mapper.writeValueAsString(mapper.readValue("{\"projectId\": 1, \"issueNo\": \"BUG-002\", \"title\": \"演示按钮状态异常\", \"severity\": \"中\", \"status\": \"待验证\", \"reporter\": \"演示测试员\", \"eventDate\": \"2026-10-02\", \"steps\": \"打开详情页后切换状态\"}",java.util.Map.class));
        var badRelation=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badRelation).put("projectId", 99999);
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badRelation))).andExpect(status().isBadRequest());
        var badStatus=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badStatus).put("status", "INVALID_STATUS");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badStatus))).andExpect(status().isBadRequest());
        var badDate=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badDate).put("eventDate", "bad-date");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badDate))).andExpect(status().isBadRequest());
        String created=mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id=mapper.readTree(created).get("id").asLong();
        mvc.perform(delete("/api/projects/1")).andExpect(status().isConflict());
        mvc.perform(put("/api/"+resource+"/"+id).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isOk());
        assertTrue(Files.exists(FILE));
        assertEquals(2,new RecordsController(mapper,FILE.toString()).list(resource).size());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNotFound());
    }

    @Test void rejectsDuplicateNumbersWithoutChangingStoredRecords() throws Exception {
        String path = "/api/defects";
        var original = mapper.readTree(mvc.perform(get(path)).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString()).get(0);
        var duplicate = original.deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode) duplicate).put("issueNo", "  bug-001  ");
        mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(duplicate)))
            .andExpect(status().isConflict());
        var different = original.deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode) different).put("issueNo", "UNIQUE-QUALITY-LOOP-01");
        String created = mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(different)))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(created).get("id").asLong();
        mvc.perform(put(path + "/" + id).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(different)))
            .andExpect(status().isOk());
        mvc.perform(put(path + "/" + id).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(duplicate)))
            .andExpect(status().isConflict());
        assertEquals("UNIQUE-QUALITY-LOOP-01", new RecordsController(mapper, FILE.toString()).list("defects").stream()
            .filter(row -> ((Number) row.get("id")).longValue() == id).findFirst().orElseThrow().get("issueNo"));
        var project = mapper.readTree(mvc.perform(get("/api/projects")).andReturn().getResponse().getContentAsString()).get(0).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode) project).put("code", "QUALITY-LOOP-PROJECT");
        String createdProject = mvc.perform(post("/api/projects").contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(project))).andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        long projectId = mapper.readTree(createdProject).get("id").asLong();
        ((com.fasterxml.jackson.databind.node.ObjectNode) duplicate).put("projectId", projectId);
        String crossProject = mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(duplicate))).andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        mvc.perform(delete(path + "/" + mapper.readTree(crossProject).get("id").asLong())).andExpect(status().isNoContent());
        mvc.perform(delete("/api/projects/" + projectId)).andExpect(status().isNoContent());
        mvc.perform(delete(path + "/" + id)).andExpect(status().isNoContent());
    }
}
