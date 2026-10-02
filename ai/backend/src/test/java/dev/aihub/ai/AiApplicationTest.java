package dev.aihub.ai;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc class AiApplicationTest {
 @Autowired MockMvc mvc;
 @Test void catalogAndPreview() throws Exception { mvc.perform(get("/api/skills")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(6)); mvc.perform(get("/api/commands")).andExpect(jsonPath("$.length()").value(5)); mvc.perform(post("/api/commands/preview").contentType(MediaType.APPLICATION_JSON).content("{\"family\":\"vue\",\"project\":\"demo-ui\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.command").value("npm create vite@latest demo-ui -- --template vue")); mvc.perform(post("/api/commands/preview").contentType(MediaType.APPLICATION_JSON).content("{\"family\":\"vue\",\"project\":\"bad;rm\"}")).andExpect(status().isBadRequest()); }
}
