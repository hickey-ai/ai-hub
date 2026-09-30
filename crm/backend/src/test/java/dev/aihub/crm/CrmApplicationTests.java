package dev.aihub.crm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "aihub.data-file=target/test-data/crm-${random.uuid}.json" ) @AutoConfigureMockMvc
class CrmApplicationTests {
    @Autowired MockMvc mvc;
    @Test void customerOpportunityFollowUpAndWin() throws Exception {
        mvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON).content("""
            {"name":"新客户","contact":"王先生","industry":"教育","owner":"林知夏"}
            """)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(4));
        mvc.perform(post("/api/opportunities").contentType(MediaType.APPLICATION_JSON).content("""
            {"customerId":4,"title":"采购项目","amount":68000,"owner":"林知夏"}
            """)).andExpect(status().isCreated()).andExpect(jsonPath("$.stage").value("发现需求"));
        mvc.perform(post("/api/opportunities/4/activities").contentType(MediaType.APPLICATION_JSON).content("{" + "\"note\":\"首次拜访\"}"))
            .andExpect(status().isCreated());
        for (String stage : new String[]{"方案沟通", "商务谈判", "已赢单"}) {
            mvc.perform(patch("/api/opportunities/4/stage").contentType(MediaType.APPLICATION_JSON).content("{\"stage\":\"" + stage + "\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.stage").value(stage));
        }
        mvc.perform(post("/api/opportunities/4/activities").contentType(MediaType.APPLICATION_JSON).content("{\"note\":\"迟到的记录\"}"))
            .andExpect(status().isConflict());
    }
    @Test void invalidReferencesAndTransitions() throws Exception {
        mvc.perform(post("/api/opportunities").contentType(MediaType.APPLICATION_JSON).content("{\"customerId\":999,\"title\":\"X\",\"amount\":1,\"owner\":\"A\"}"))
            .andExpect(status().isNotFound());
        mvc.perform(patch("/api/opportunities/1/stage").contentType(MediaType.APPLICATION_JSON).content("{\"stage\":\"发现需求\"}"))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/opportunities").contentType(MediaType.APPLICATION_JSON).content("{\"customerId\":1,\"title\":\"\",\"amount\":0,\"owner\":\"\"}"))
            .andExpect(status().isBadRequest());
    }
}
