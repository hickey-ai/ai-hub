package dev.aihub.html;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HtmlApplicationTest {
  @Autowired MockMvc mvc;
  @Test void directoryIsAvailable() throws Exception { mvc.perform(get("/api/directory")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("云栖咖啡实验室")); }
  @Test void categoriesAndOverviewAreAvailable() throws Exception { mvc.perform(get("/api/categories")).andExpect(status().isOk()).andExpect(jsonPath("$[0]").value("全部")); mvc.perform(get("/api/overview")).andExpect(status().isOk()).andExpect(jsonPath("$.localOnly").value(true)); }
}
