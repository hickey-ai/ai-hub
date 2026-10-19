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
    @Test void customerEditValidatesAndPreservesReferences() throws Exception {
        String body = """
            {"name":" Acme Studio ","contact":" Alex ","industry":" Software ","owner":" Morgan "}
            """;
        mvc.perform(put("/api/customers/1").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Acme Studio"))
            .andExpect(jsonPath("$.contact").value("Alex"))
            .andExpect(jsonPath("$.industry").value("Software"))
            .andExpect(jsonPath("$.owner").value("Morgan"));
        mvc.perform(put("/api/customers/1").contentType(MediaType.APPLICATION_JSON)
            .content(body.replace(" Acme Studio ", "   "))).andExpect(status().isBadRequest());
        mvc.perform(put("/api/customers/999").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isNotFound());
        mvc.perform(get("/api/customers")).andExpect(jsonPath("$[0].name").value("Acme Studio"));
        mvc.perform(get("/api/opportunities")).andExpect(jsonPath("$[0].customerId").value(1));
    }
    @Test void opportunityEditPreservesHistoryAndRejectsClosedOrInvalidChanges() throws Exception {
        String original = mvc.perform(get("/api/opportunities")).andReturn().getResponse().getContentAsString();
        var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        var before = mapper.readTree(original).get(1);
        String body = """
            {"title":" Revised support agreement ","amount":145000,"owner":" Morgan ","customerId":999,"stage":"已赢单"}
            """;
        mvc.perform(put("/api/opportunities/2").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.title").value("Revised support agreement"))
            .andExpect(jsonPath("$.amount").value(145000)).andExpect(jsonPath("$.owner").value("Morgan"))
            .andExpect(jsonPath("$.customerId").value(before.get("customerId").asInt()))
            .andExpect(jsonPath("$.stage").value(before.get("stage").asText()))
            .andExpect(jsonPath("$.createdAt").value(before.get("createdAt").asText()));
        mvc.perform(get("/api/opportunities/2/activities"))
            .andExpect(jsonPath("$[0].note").value("确认会员积分对接需求"));
        for (String invalid : new String[]{body.replace(" Revised support agreement ", "   "),
                body.replace(" Morgan ", "   "), body.replace("145000", "0"),
                body.replace("145000", "-1"), body.replace("145000", "1.5")}) {
            mvc.perform(put("/api/opportunities/2").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest());
        }
        mvc.perform(post("/api/opportunities").contentType(MediaType.APPLICATION_JSON)
            .content("{\"customerId\":1,\"title\":\"Fractional value\",\"amount\":1.5,\"owner\":\"QA\"}"))
            .andExpect(status().isBadRequest());
        mvc.perform(put("/api/opportunities/999").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isNotFound());
        mvc.perform(patch("/api/opportunities/3/stage").contentType(MediaType.APPLICATION_JSON).content("{\"stage\":\"已流失\"}"))
            .andExpect(status().isOk());
        mvc.perform(put("/api/opportunities/3").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict());
        mvc.perform(get("/api/opportunities")).andExpect(jsonPath("$[2].title").value("线上课程管理平台"));
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
