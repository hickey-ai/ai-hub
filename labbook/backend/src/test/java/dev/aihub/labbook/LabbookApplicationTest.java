package dev.aihub.labbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LabbookApplicationTest {
    @TempDir static Path dataDir;
    @DynamicPropertySource static void dataFile(DynamicPropertyRegistry registry) {
        registry.add("aihub.data-file", () -> dataDir.resolve("labbook.json").toString());
    }
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    private String json(Object value) throws Exception { return mapper.writeValueAsString(value); }
    private Map<String,Object> experiment(String title) {
        var row = new LinkedHashMap<String,Object>();
        row.put("title", title); row.put("discipline", "物理"); row.put("researcher", "测试研究员");
        row.put("date", "2026-10-01"); row.put("status", "计划中"); row.put("protocol", "记录装置与校准方法");
        return row;
    }
    private Map<String,Object> sample(String code) {
        var row = new LinkedHashMap<String,Object>();
        row.put("code", code); row.put("name", "演示样本"); row.put("type", "材料");
        return row;
    }
    @SuppressWarnings("unchecked") private Map<String,Object> parse(String body) throws Exception { return mapper.readValue(body, Map.class); }

    @Test void fullWorkflowAndReload() throws Exception {
        mvc.perform(get("/api/templates")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(8));
        mvc.perform(get("/api/experiments")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
        var sample = parse(mvc.perform(post("/api/samples").contentType(MediaType.APPLICATION_JSON).content(json(sample("TEST-001"))))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());
        long sampleId = ((Number)sample.get("id")).longValue();
        mvc.perform(put("/api/samples/"+sampleId).contentType(MediaType.APPLICATION_JSON)
            .content(json(Map.of("id",999,"code","TEST-001","name","改名","type","材料"))))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(sampleId)).andExpect(jsonPath("$.name").value("改名"));
        mvc.perform(post("/api/samples").contentType(MediaType.APPLICATION_JSON).content(json(sample("TEST-001"))))
            .andExpect(status().isConflict());
        var other = parse(mvc.perform(post("/api/samples").contentType(MediaType.APPLICATION_JSON).content(json(sample("TEST-002"))))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString());
        long otherId = ((Number)other.get("id")).longValue();
        mvc.perform(put("/api/samples/"+otherId).contentType(MediaType.APPLICATION_JSON).content(json(sample("TEST-001"))))
            .andExpect(status().isConflict());
        mvc.perform(delete("/api/samples/"+otherId)).andExpect(status().isNoContent());
        var row = experiment("测量对比实验"); row.put("sampleId",sampleId);
        var created = parse(mvc.perform(post("/api/experiments").contentType(MediaType.APPLICATION_JSON).content(json(row)))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.revision").value(1)).andReturn().getResponse().getContentAsString());
        long id = ((Number)created.get("id")).longValue();
        mvc.perform(delete("/api/samples/"+sampleId)).andExpect(status().isConflict());
        row.put("id",999); row.put("status","已完成"); row.put("results","8.2 ± 0.1 单位"); row.put("conclusion","仅用于测试");
        mvc.perform(put("/api/experiments/"+id).contentType(MediaType.APPLICATION_JSON).content(json(row)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.revision").value(2));
        mvc.perform(get("/api/experiments/"+id+"/history")).andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].snapshot.results").value("8.2 ± 0.1 单位"))
            .andExpect(jsonPath("$[1].snapshot.status").value("计划中"));
        row.put("status","已归档");
        mvc.perform(put("/api/experiments/"+id).contentType(MediaType.APPLICATION_JSON).content(json(row)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.revision").value(3));
        mvc.perform(put("/api/experiments/"+id).contentType(MediaType.APPLICATION_JSON).content(json(row)))
            .andExpect(status().isConflict());
        mvc.perform(get("/api/export")).andExpect(status().isOk()).andExpect(header().string("Content-Disposition", "attachment; filename=labbook-export.json"))
            .andExpect(jsonPath("$.format").value("ai-hub-labbook-v1"))
            .andExpect(jsonPath("$.revisions.length()").value(5));
        assertThat(Files.exists(dataDir.resolve("labbook.json"))).isTrue();
        var reloaded = new LabbookController(mapper,dataDir.resolve("labbook.json").toString());
        assertThat(reloaded.experiment(id).get("status")).isEqualTo("已归档");
        assertThat(reloaded.history(id)).hasSize(3);
        assertThat(reloaded.samples()).anySatisfy(s -> assertThat(s.get("code")).isEqualTo("TEST-001"));
    }

    @Test void rejectsInvalidInputsAndHandlesFailureWithoutMutatingMemory(@TempDir Path dir) throws Exception {
        var row = experiment("校验测试");
        row.put("date","2026-02-30");
        mvc.perform(post("/api/experiments").contentType(MediaType.APPLICATION_JSON).content(json(row))).andExpect(status().isBadRequest());
        row.put("date","2026-10-01"); row.put("discipline","未知学科");
        mvc.perform(post("/api/experiments").contentType(MediaType.APPLICATION_JSON).content(json(row))).andExpect(status().isBadRequest());
        row.put("discipline","物理"); row.put("status","已完成");
        mvc.perform(post("/api/experiments").contentType(MediaType.APPLICATION_JSON).content(json(row))).andExpect(status().isBadRequest());
        row.put("status","计划中"); row.put("sampleId",999999);
        mvc.perform(post("/api/experiments").contentType(MediaType.APPLICATION_JSON).content(json(row))).andExpect(status().isBadRequest());
        mvc.perform(get("/api/experiments/999999")).andExpect(status().isNotFound());
        var blocked = dir.resolve("blocked"); Files.writeString(blocked,"not a directory");
        var controller = new LabbookController(mapper,blocked.resolve("labbook.json").toString());
        int before = controller.experiments().size();
        var valid = experiment("不能持久化");
        assertThatThrownBy(() -> controller.createExperiment(valid)).isInstanceOf(IllegalStateException.class);
        assertThat(controller.experiments()).hasSize(before);
        assertThat(controller.history(((Number)controller.experiments().getFirst().get("id")).longValue())).hasSize(1);
    }
}
