package dev.aihub.manufacturing;
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
class ManufacturingApplicationTests {
    static final Path FILE;
    static { try { FILE=Files.createTempDirectory("aihub-manufacturing-").resolve("data.json"); } catch(Exception e) { throw new ExceptionInInitializerError(e); } }
    @DynamicPropertySource static void props(DynamicPropertyRegistry r) { r.add("aihub.data-file",()->FILE.toString()); }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Test void simulatedDeviceTraceIsDurableIdempotentAndStateChecked() throws Exception {
        String order = "{\"materialId\":1,\"orderNo\":\"SIM-CASE\",\"product\":\"测试件\",\"quantity\":3,\"dueDate\":\"2026-10-15\",\"status\":\"待排产\",\"notes\":\"\"}";
        long id = mapper.readTree(mvc.perform(post("/api/workorders").contentType(MediaType.APPLICATION_JSON).content(order))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString()).get("id").asLong();
        String endpoint = "/api/workorders/" + id + "/simulate";
        mvc.perform(get("/api/workorders/" + id + "/trace")).andExpect(status().isOk())
            .andExpect(jsonPath("$.produced").value(0)).andExpect(jsonPath("$.events.length()").value(0));
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("OUTPUT",1,"early")))
            .andExpect(status().isConflict());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content("null"))
            .andExpect(status().isBadRequest());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("OUTPUT",-1,"negative")))
            .andExpect(status().isBadRequest());
        String startId = java.util.UUID.randomUUID().toString();
        String start = event("START",0,startId);
        String first = mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(start))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.deviceId").value("SIM-01"))
            .andReturn().getResponse().getContentAsString();
        String retry = mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(start))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        assertEquals(mapper.readTree(first), mapper.readTree(retry));
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("OUTPUT",1,startId)))
            .andExpect(status().isConflict());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("OUTPUT",4,"over")))
            .andExpect(status().isConflict());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("OUTPUT",2,"out-2")))
            .andExpect(status().isCreated());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("FAULT",0,"fault")))
            .andExpect(status().isCreated());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("OUTPUT",1,"paused")))
            .andExpect(status().isConflict());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("RESUME",0,"resume")))
            .andExpect(status().isCreated());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("QC",0,"too-soon")))
            .andExpect(status().isConflict());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("OUTPUT",1,"out-3")))
            .andExpect(status().isCreated());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("QC",0,"qc")))
            .andExpect(status().isCreated());
        mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(event("FINISH",0,"finish")))
            .andExpect(status().isCreated());
        mvc.perform(delete("/api/workorders/"+id)).andExpect(status().isConflict());
        mvc.perform(put("/api/workorders/"+id).contentType(MediaType.APPLICATION_JSON).content(order))
            .andExpect(status().isConflict());
        var notesOnly = (com.fasterxml.jackson.databind.node.ObjectNode)mapper.readTree(order);
        notesOnly.put("status", "已完成");
        notesOnly.put("notes", "允许补充备注");
        mvc.perform(put("/api/workorders/"+id).contentType(MediaType.APPLICATION_JSON).content(notesOnly.toString()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.notes").value("允许补充备注"));
        mvc.perform(get("/api/workorders/"+id+"/trace")).andExpect(jsonPath("$.produced").value(3))
            .andExpect(jsonPath("$.events.length()").value(7)).andExpect(jsonPath("$.order.status").value("已完成"));
        var reloaded = new RecordsController(mapper, FILE.toString());
        assertEquals(7, ((java.util.List<?>)reloaded.trace(id).get("events")).size());
        assertEquals(mapper.readTree(first).get("eventId").asText(),
            reloaded.simulate(id, mapper.readValue(start, java.util.Map.class)).get("eventId"));
    }
    private static String event(String kind, long quantity, String id) {
        return "{\"eventId\":\""+id+"\",\"kind\":\""+kind+"\",\"quantity\":"+quantity+"}";
    }

    @Test void crudValidationAndPersistence() throws Exception {
        String resource="workorders";
        int baseline = mapper.readTree(mvc.perform(get("/api/"+resource)).andReturn().getResponse().getContentAsString()).size();
        mvc.perform(get("/api/materials")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/unknown")).andExpect(status().isNotFound());
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
        String payload=mapper.writeValueAsString(mapper.readValue("{\"materialId\": 1, \"orderNo\": \"WO-2026-01\", \"product\": \"智能控制盒\", \"quantity\": 80, \"dueDate\": \"2026-10-15\", \"status\": \"生产中\", \"notes\": \"演示工单\"}",java.util.Map.class));
        var invalidRelation = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree(payload);
        invalidRelation.put("materialId", 999999);
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(invalidRelation))).andExpect(status().isBadRequest());
        var invalidField = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree(payload);
        invalidField.put("dueDate", "not-a-valid-value");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(invalidField))).andExpect(status().isBadRequest());
        mvc.perform(get("/api/"+resource)).andExpect(jsonPath("$.length()").value(baseline));
        String created=mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id=mapper.readTree(created).get("id").asLong();
        mvc.perform(delete("/api/materials/1")).andExpect(status().isConflict());
        mvc.perform(put("/api/"+resource+"/"+id).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isOk());
        assertTrue(Files.exists(FILE));
        assertEquals(baseline+1,new RecordsController(mapper,FILE.toString()).list(resource).size());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNotFound());
    }
}
