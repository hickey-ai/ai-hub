package dev.aihub.gridops;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.file.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class GridopsApplicationTests {
    static final Path FILE;
    static { try { FILE=Files.createTempDirectory("gridops-test-").resolve("data.json"); } catch (Exception ex) { throw new ExceptionInInitializerError(ex); } }
    @DynamicPropertySource static void props(DynamicPropertyRegistry registry) { registry.add("aihub.data-file", () -> FILE.toString()); }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void caseLifecycleValidationAndPersistence() throws Exception {
        var body = Map.of("number", "GRID-TEST-02", "grid", "测试网格", "category", "设施维护", "title", "演示测试事件",
            "description", "虚构数据", "assignee", "演示人员");
        mvc.perform(post("/api/cases").contentType(MediaType.APPLICATION_JSON).content("{}")) .andExpect(status().isBadRequest());
        String created = mvc.perform(post("/api/cases").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(body)))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(created).get("id").asLong();
        mvc.perform(post("/api/cases").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(
            Map.of("number", " grid-test-02 ", "grid", "测试网格", "category", "设施维护", "title", "重复", "description", "虚构", "assignee", "演示"))))
            .andExpect(status().isConflict());
        mvc.perform(put("/api/cases/"+id+"/transition").contentType(MediaType.APPLICATION_JSON).content("{\"to\":\"已办结\",\"note\":\"跳级\"}"))
            .andExpect(status().isConflict());
        for (String state : new String[]{"处理中", "待核验", "处理中", "待核验", "已办结"})
            mvc.perform(put("/api/cases/"+id+"/transition").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of("to", state, "note", "虚构处理记录"))))
                .andExpect(status().isOk());
        mvc.perform(delete("/api/cases/"+id)).andExpect(status().isConflict());
        assertEquals(6, mapper.readTree(Files.readString(FILE)).get("cases").get(1).get("history").size());
        assertEquals("已办结", new GridController(mapper, FILE.toString()).list().get(1).get("status"));
        mvc.perform(get("/api/cases/9999")).andExpect(status().isNotFound());
    }
}
