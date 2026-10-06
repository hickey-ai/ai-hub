package dev.aihub.parcelstation;
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
class ParcelstationApplicationTests {
    static final Path FILE;
    static { try { FILE=Files.createTempDirectory("aihub-parcelstation-").resolve("data.json"); } catch(Exception e) { throw new ExceptionInInitializerError(e); } }
    @DynamicPropertySource static void props(DynamicPropertyRegistry r) { r.add("aihub.data-file",()->FILE.toString()); }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Test void shelfCapacityCountsOnlyParcelsStillOnShelf() throws Exception {
        String shelf = "{\"code\":\"CAP-ONE\",\"name\":\"单格货架\",\"capacity\":1,\"location\":\"演示区\"}";
        long shelfId = mapper.readTree(mvc.perform(post("/api/shelves").contentType(MediaType.APPLICATION_JSON).content(shelf))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString()).get("id").asLong();
        String first = parcel(shelfId, "CAP-P1", "CAP-C1", "待取件");
        long firstId = mapper.readTree(mvc.perform(post("/api/parcels").contentType(MediaType.APPLICATION_JSON).content(first))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString()).get("id").asLong();
        mvc.perform(post("/api/parcels").contentType(MediaType.APPLICATION_JSON).content(parcel(shelfId, "CAP-P2", "CAP-C2", "待取件")))
                .andExpect(status().isConflict());
        String smaller = shelf.replace("\"capacity\":1", "\"capacity\":0");
        mvc.perform(put("/api/shelves/" + shelfId).contentType(MediaType.APPLICATION_JSON).content(smaller))
                .andExpect(status().isConflict());
        mvc.perform(put("/api/parcels/" + firstId).contentType(MediaType.APPLICATION_JSON)
                .content(parcel(shelfId, "CAP-P1", "CAP-C1", "异常件"))).andExpect(status().isOk());
        mvc.perform(post("/api/parcels").contentType(MediaType.APPLICATION_JSON).content(parcel(shelfId, "CAP-P2", "CAP-C2", "待取件")))
                .andExpect(status().isConflict());
        mvc.perform(put("/api/parcels/" + firstId).contentType(MediaType.APPLICATION_JSON)
                .content(parcel(shelfId, "CAP-P1", "CAP-C1", "已签收"))).andExpect(status().isConflict());
        // Exception parcels must return to pending before they can be signed under the existing transition rule.
        mvc.perform(put("/api/parcels/" + firstId).contentType(MediaType.APPLICATION_JSON).content(first)).andExpect(status().isOk());
        mvc.perform(put("/api/parcels/" + firstId).contentType(MediaType.APPLICATION_JSON)
                .content(parcel(shelfId, "CAP-P1", "CAP-C1", "已签收"))).andExpect(status().isOk());
        mvc.perform(post("/api/parcels").contentType(MediaType.APPLICATION_JSON).content(parcel(shelfId, "CAP-P2", "CAP-C2", "待取件")))
                .andExpect(status().isCreated());
        mvc.perform(put("/api/parcels/" + firstId).contentType(MediaType.APPLICATION_JSON).content(first))
                .andExpect(status().isConflict());
        assertEquals(2, new RecordsController(mapper, FILE.toString()).list("parcels").stream()
                .filter(row -> ((Number)row.get("shelfId")).longValue() == shelfId).count());
        assertEquals(1, new RecordsController(mapper, FILE.toString()).list("shelves").stream()
                .filter(row -> ((Number)row.get("id")).longValue() == shelfId).findFirst().orElseThrow().get("capacity"));
    }

    private String parcel(long shelfId, String tracking, String code, String status) throws Exception {
        return mapper.writeValueAsString(java.util.Map.of("shelfId", shelfId, "trackingNo", tracking,
                "recipient", "演示顾客", "pickupCode", code, "eventDate", "2026-10-04", "status", status,
                "notes", status.equals("异常件") ? "等待处理" : ""));
    }

    @Test void crudValidationAndPersistence() throws Exception {
        String resource="parcels";
        mvc.perform(get("/api/shelves")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/unknown")).andExpect(status().isNotFound());
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
        String payload=mapper.writeValueAsString(mapper.readValue("{\"shelfId\": 1, \"trackingNo\": \"DEMO-P01\", \"recipient\": \"演示顾客\", \"pickupCode\": \"DEMO-01\", \"eventDate\": \"2026-10-04\", \"status\": \"待取件\", \"notes\": \"虚构包裹\"}",java.util.Map.class));
        var badRelation=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badRelation).put("shelfId", 99999);
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badRelation))).andExpect(status().isBadRequest());
        var badStatus=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badStatus).put("status", "INVALID_STATUS");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badStatus))).andExpect(status().isBadRequest());
        var badDate=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badDate).put("eventDate", "bad-date");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badDate))).andExpect(status().isBadRequest());
        var uniquePayload=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)uniquePayload).put("trackingNo", "DEMO-P02");
        ((com.fasterxml.jackson.databind.node.ObjectNode)uniquePayload).put("pickupCode", "DEMO-02");
        payload=mapper.writeValueAsString(uniquePayload);
        String created=mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id=mapper.readTree(created).get("id").asLong();
        mvc.perform(delete("/api/shelves/1")).andExpect(status().isConflict());
        mvc.perform(put("/api/"+resource+"/"+id).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isOk());
        assertTrue(Files.exists(FILE));
        assertEquals(2,new RecordsController(mapper,FILE.toString()).list(resource).size());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNotFound());
    }
}
