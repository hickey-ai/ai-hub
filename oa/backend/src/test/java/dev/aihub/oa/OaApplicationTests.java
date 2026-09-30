package dev.aihub.oa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class OaApplicationTests {
    @Autowired MockMvc mvc;
    @Test void draftSubmitApprove() throws Exception {
        mvc.perform(post("/api/requests").contentType(MediaType.APPLICATION_JSON).content("""
            {"type":"报销","title":"车费","applicant":"王小明","detail":"客户拜访","amount":85}
            """)).andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("草稿"));
        mvc.perform(post("/api/requests/5/submit")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("待审批"));
        mvc.perform(post("/api/requests/5/review").contentType(MediaType.APPLICATION_JSON).content("{\"decision\":\"已通过\",\"note\":\"已核实\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("已通过"));
        mvc.perform(post("/api/requests/5/submit")).andExpect(status().isConflict());
        mvc.perform(post("/api/requests/5/review").contentType(MediaType.APPLICATION_JSON).content("{\"decision\":\"已驳回\",\"note\":\"重复\"}"))
            .andExpect(status().isConflict());
    }
    @Test void invalidReviewAndInput() throws Exception {
        mvc.perform(post("/api/requests/4/review").contentType(MediaType.APPLICATION_JSON).content("{\"decision\":\"已通过\"}"))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/requests/1/review").contentType(MediaType.APPLICATION_JSON).content("{\"decision\":\"已驳回\",\"note\":\"\"}"))
            .andExpect(status().isBadRequest());
        mvc.perform(post("/api/requests").contentType(MediaType.APPLICATION_JSON).content("{\"type\":\"请假\",\"title\":\"T\",\"applicant\":\"A\",\"detail\":\"D\",\"amount\":10}"))
            .andExpect(status().isBadRequest());
        mvc.perform(post("/api/requests/999/submit")).andExpect(status().isNotFound());
    }
}
