package dev.aihub.crm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "aihub.data-file=target/test-data/activity-edit-${random.uuid}.json")
@AutoConfigureMockMvc
class ActivityEditingTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void correctsOnlyTheNoteAndRejectsInvalidReferencesAndClosedOpportunities() throws Exception {
        var original = mapper.readTree(mvc.perform(get("/api/opportunities/1/activities"))
            .andReturn().getResponse().getContentAsString()).get(0);
        String deals = mvc.perform(get("/api/opportunities")).andReturn().getResponse().getContentAsString();
        String otherHistory = mvc.perform(get("/api/opportunities/2/activities")).andReturn().getResponse().getContentAsString();
        String body = mapper.writeValueAsString(java.util.Map.of("note", " Corrected demo note\nNext step: review scope ",
            "id", 999, "opportunityId", 2, "date", "2000-01-01"));
        mvc.perform(put("/api/opportunities/1/activities/1").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(original.get("id").asInt()))
            .andExpect(jsonPath("$.opportunityId").value(1))
            .andExpect(jsonPath("$.date").value(original.get("date").asText()))
            .andExpect(jsonPath("$.note").value("Corrected demo note\nNext step: review scope"));
        mvc.perform(get("/api/opportunities/1/activities")).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/api/opportunities")).andExpect(content().json(deals));
        mvc.perform(get("/api/opportunities/2/activities")).andExpect(content().json(otherHistory));
        for (String invalid : new String[]{"{}", "{\"note\":null}", "{\"note\":\"   \"}"}) {
            mvc.perform(put("/api/opportunities/1/activities/1").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest());
        }
        for (String path : new String[]{"/1/activities/999", "/2/activities/1", "/999/activities/1"}) {
            mvc.perform(put("/api/opportunities" + path).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNotFound());
        }
        mvc.perform(patch("/api/opportunities/1/stage").contentType(MediaType.APPLICATION_JSON)
            .content("{\"stage\":\"已赢单\"}")).andExpect(status().isOk());
        mvc.perform(put("/api/opportunities/1/activities/1").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict());
        mvc.perform(patch("/api/opportunities/2/stage").contentType(MediaType.APPLICATION_JSON)
            .content("{\"stage\":\"已流失\"}")).andExpect(status().isOk());
        mvc.perform(put("/api/opportunities/2/activities/2").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict());
        mvc.perform(get("/api/opportunities/2/activities")).andExpect(content().json(otherHistory));
    }
}
