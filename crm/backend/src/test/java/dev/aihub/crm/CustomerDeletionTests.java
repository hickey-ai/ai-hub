package dev.aihub.crm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "aihub.data-file=target/test-data/delete-${random.uuid}.json")
@AutoConfigureMockMvc
class CustomerDeletionTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void deletesOnlyUnlinkedCustomersAndKeepsHistory() throws Exception {
        long id = mapper.readTree(mvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Duplicate demo\",\"contact\":\"Alex\",\"industry\":\"Software\",\"owner\":\"Morgan\"}"))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString()).get("id").asLong();
        String deals = mvc.perform(get("/api/opportunities")).andReturn().getResponse().getContentAsString();
        String history = mvc.perform(get("/api/opportunities/1/activities")).andReturn().getResponse().getContentAsString();
        mvc.perform(delete("/api/customers/" + id)).andExpect(status().isNoContent()).andExpect(content().string(""));
        mvc.perform(get("/api/stats")).andExpect(jsonPath("$.customers").value(3));
        mvc.perform(delete("/api/customers/" + id)).andExpect(status().isNotFound());
        mvc.perform(delete("/api/customers/999999")).andExpect(status().isNotFound());
        mvc.perform(delete("/api/customers/1")).andExpect(status().isConflict());
        mvc.perform(get("/api/opportunities")).andExpect(content().json(deals));
        mvc.perform(get("/api/opportunities/1/activities")).andExpect(content().json(history));
        mvc.perform(patch("/api/opportunities/1/stage").contentType(MediaType.APPLICATION_JSON)
            .content("{\"stage\":\"已赢单\"}")).andExpect(status().isOk());
        mvc.perform(delete("/api/customers/1")).andExpect(status().isConflict());
        mvc.perform(patch("/api/opportunities/2/stage").contentType(MediaType.APPLICATION_JSON)
            .content("{\"stage\":\"已流失\"}")).andExpect(status().isOk());
        mvc.perform(delete("/api/customers/2")).andExpect(status().isConflict());
        mvc.perform(get("/api/customers")).andExpect(jsonPath("$.length()").value(3));
        mvc.perform(get("/api/opportunities/1/activities")).andExpect(content().json(history));
        long nextId = mapper.readTree(mvc.perform(post("/api/customers").contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Next demo\",\"contact\":\"Sam\",\"industry\":\"Software\",\"owner\":\"Morgan\"}"))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString()).get("id").asLong();
        assertTrue(nextId > id, "Deleted customer IDs must not be reused");
    }
}
