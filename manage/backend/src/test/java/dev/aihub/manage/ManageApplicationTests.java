package dev.aihub.manage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "aihub.data-file=target/test-data/manage-${random.uuid}.json" ) @AutoConfigureMockMvc
class ManageApplicationTests {
    @Autowired MockMvc mvc;
    @Test void userLifecycle() throws Exception {
        mvc.perform(get("/api/metrics")).andExpect(status().isOk()).andExpect(jsonPath("$.users").value(8));
        String body = """
            {"name":"测试成员","email":"new@example.com","role":"编辑","department":"研发","status":"正常"}
            """;
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(9));
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict());
        mvc.perform(get("/api/metrics")).andExpect(jsonPath("$.users").value(9));
        mvc.perform(get("/api/logs")).andExpect(status().isOk()).andExpect(jsonPath("$[0].action").value("新增用户：测试成员"));
        mvc.perform(delete("/api/users/9")).andExpect(status().isNoContent());
        mvc.perform(get("/api/metrics")).andExpect(jsonPath("$.users").value(8));
    }
}
