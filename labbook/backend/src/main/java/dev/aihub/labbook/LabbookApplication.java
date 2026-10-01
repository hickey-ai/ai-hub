package dev.aihub.labbook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.*;

@SpringBootApplication
public class LabbookApplication {
    public static void main(String[] args) { SpringApplication.run(LabbookApplication.class, args); }
}

@RestController
@RequestMapping("/api")
class LabbookController {
    private static final List<Map<String,String>> TEMPLATES = List.of(
        template("生命科学", "培养与观察", "记录样本来源、观察条件与时间点，关联原始数据。"),
        template("化学", "反应与分析", "记录批次、条件、仪器与表征结果；危险操作遵循机构 SOP。"),
        template("物理", "测量与验证", "记录装置、校准、变量、误差与测量数据。"),
        template("材料科学", "制备与表征", "记录样本批次、制备条件与表征文件。"),
        template("环境科学", "采样与监测", "记录采样位置、时间、方法与检测数据。"),
        template("地球科学", "野外调查", "记录地点、地层/样本、仪器与原始观察。"),
        template("天文学", "观测计划", "记录目标、设备、时段、环境与图像文件。"),
        template("计算科学", "模拟与复现", "记录代码版本、参数、数据集与输出文件。")
    );
    private final ObjectMapper mapper;
    private final Path file;
    private final List<Map<String,Object>> experiments = new ArrayList<>();
    private final List<Map<String,Object>> samples = new ArrayList<>();
    private final List<Map<String,Object>> revisions = new ArrayList<>();
    private long nextId = 1;

    LabbookController(ObjectMapper mapper, @Value("${aihub.data-file}") String fileName) {
        this.mapper = mapper;
        this.file = Path.of(fileName).toAbsolutePath();
        if (Files.exists(file)) load(); else seed();
    }

    private static Map<String,String> template(String discipline, String name, String hint) {
        return Map.of("discipline", discipline, "name", name, "hint", hint);
    }
    @GetMapping("/templates") List<Map<String,String>> templates() { return TEMPLATES; }
    @GetMapping("/experiments") synchronized List<Map<String,Object>> experiments() { return copies(experiments); }
    @GetMapping("/samples") synchronized List<Map<String,Object>> samples() { return copies(samples); }
    @GetMapping("/experiments/{id}") synchronized Map<String,Object> experiment(@PathVariable long id) { return copy(find(experiments, id)); }
    @GetMapping("/experiments/{id}/history") synchronized List<Map<String,Object>> history(@PathVariable long id) {
        find(experiments, id);
        return revisions.stream().filter(r -> ((Number)r.get("experimentId")).longValue() == id)
            .sorted((a,b) -> Integer.compare(((Number)b.get("revision")).intValue(), ((Number)a.get("revision")).intValue()))
            .map(LabbookController::copyRevision).toList();
    }
    @GetMapping(value="/export", produces=MediaType.APPLICATION_JSON_VALUE)
    synchronized ResponseEntity<Map<String,Object>> export() {
        var result = new LinkedHashMap<String,Object>();
        result.put("format", "ai-hub-labbook-v1");
        result.put("exportedAt", OffsetDateTime.now().toString());
        result.put("experiments", copies(experiments));
        result.put("samples", copies(samples));
        result.put("revisions", revisions.stream().map(LabbookController::copyRevision).toList());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=labbook-export.json").body(result);
    }

    @PostMapping("/experiments") @ResponseStatus(HttpStatus.CREATED)
    synchronized Map<String,Object> createExperiment(@RequestBody Map<String,Object> input) {
        var row = validateExperiment(input);
        row.put("id", nextId++);
        row.put("revision", 1);
        String now = OffsetDateTime.now().toString();
        row.put("createdAt", now); row.put("updatedAt", now);
        var revision = revision(row, "创建");
        experiments.add(row); revisions.add(revision);
        try { persist(); }
        catch (RuntimeException ex) { experiments.remove(row); revisions.remove(revision); nextId--; throw ex; }
        return copy(row);
    }
    @PutMapping("/experiments/{id}")
    synchronized Map<String,Object> updateExperiment(@PathVariable long id, @RequestBody Map<String,Object> input) {
        int index = indexOf(experiments, id);
        var old = experiments.get(index);
        if ("已归档".equals(old.get("status"))) throw new ResponseStatusException(HttpStatus.CONFLICT, "已归档实验不可修改");
        var row = validateExperiment(input);
        row.put("id", id);
        row.put("revision", ((Number)old.get("revision")).intValue()+1);
        row.put("createdAt", old.get("createdAt"));
        row.put("updatedAt", OffsetDateTime.now().toString());
        var rev = revision(row, "已归档".equals(row.get("status")) ? "归档" : "修订");
        experiments.set(index,row); revisions.add(rev);
        try { persist(); }
        catch (RuntimeException ex) { experiments.set(index,old); revisions.remove(rev); throw ex; }
        return copy(row);
    }
    @PostMapping("/samples") @ResponseStatus(HttpStatus.CREATED)
    synchronized Map<String,Object> createSample(@RequestBody Map<String,Object> input) {
        var row = validateSample(input, -1);
        row.put("id", nextId++);
        row.put("createdAt", OffsetDateTime.now().toString());
        samples.add(row);
        try { persist(); }
        catch (RuntimeException ex) { samples.remove(row); nextId--; throw ex; }
        return copy(row);
    }
    @PutMapping("/samples/{id}")
    synchronized Map<String,Object> updateSample(@PathVariable long id, @RequestBody Map<String,Object> input) {
        int index = indexOf(samples,id);
        var old = samples.get(index);
        var row = validateSample(input, id);
        row.put("id", id); row.put("createdAt", old.get("createdAt"));
        samples.set(index,row);
        try { persist(); }
        catch (RuntimeException ex) { samples.set(index,old); throw ex; }
        return copy(row);
    }
    @DeleteMapping("/samples/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    synchronized void deleteSample(@PathVariable long id) {
        int index = indexOf(samples,id);
        if (experiments.stream().anyMatch(e -> Objects.equals(e.get("sampleId"), id)))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "样本已被实验引用，不能删除");
        var old = samples.remove(index);
        try { persist(); }
        catch (RuntimeException ex) { samples.add(index,old); throw ex; }
    }

    private Map<String,Object> validateExperiment(Map<String,Object> input) {
        var row = new LinkedHashMap<String,Object>();
        text(input,row,"title",true,120);
        choice(input,row,"discipline", TEMPLATES.stream().map(t -> t.get("discipline")).toList());
        text(input,row,"researcher",true,80);
        date(input,row,"date");
        choice(input,row,"status",List.of("计划中","进行中","已完成","已归档"));
        text(input,row,"hypothesis",false,2000);
        text(input,row,"protocol",true,4000);
        Object sampleId = input.get("sampleId");
        if (sampleId == null || "".equals(sampleId)) row.put("sampleId",null);
        else {
            if (!(sampleId instanceof Number number) || number.longValue() <= 0 || number.doubleValue() != number.longValue()) bad("无效的样本编号");
            long id = ((Number)sampleId).longValue();
            if (samples.stream().noneMatch(s -> ((Number)s.get("id")).longValue() == id)) bad("关联样本不存在");
            row.put("sampleId",id);
        }
        text(input,row,"observations",false,4000);
        text(input,row,"results",false,4000);
        text(input,row,"conclusion",false,4000);
        text(input,row,"dataLocation",false,500);
        text(input,row,"tags",false,200);
        if (List.of("已完成","已归档").contains(row.get("status")) &&
            ("".equals(row.get("results")) || "".equals(row.get("conclusion")))) bad("完成或归档前需要结果与结论");
        return row;
    }
    private Map<String,Object> validateSample(Map<String,Object> input, long currentId) {
        var row = new LinkedHashMap<String,Object>();
        text(input,row,"code",true,60);
        text(input,row,"name",true,120);
        text(input,row,"type",true,80);
        text(input,row,"source",false,300);
        text(input,row,"storage",false,300);
        text(input,row,"notes",false,500);
        // Only the server-assigned path ID can exempt the current sample from uniqueness checks.
        if (samples.stream().anyMatch(s -> s.get("code").equals(row.get("code")) && ((Number)s.get("id")).longValue() != currentId))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "样本编号已存在");
        return row;
    }
    private static void text(Map<String,Object> input,Map<String,Object> out,String key,boolean required,int max) {
        Object raw=input.get(key);
        if (raw != null && !(raw instanceof String)) bad(key+" 必须为文本");
        String value=raw==null?"":((String)raw).trim();
        if (required && value.isEmpty()) bad(key+" 不能为空");
        if (value.length()>max) bad(key+" 超过长度限制");
        out.put(key,value);
    }
    private static void date(Map<String,Object> input,Map<String,Object> out,String key) {
        text(input,out,key,true,10);
        try { out.put(key,LocalDate.parse((String)out.get(key)).toString()); }
        catch (Exception ex) { bad("日期必须为 YYYY-MM-DD"); }
    }
    private static void choice(Map<String,Object> input,Map<String,Object> out,String key,List<String> choices) {
        text(input,out,key,true,80);
        if (!choices.contains(out.get(key))) bad(key+" 不是可选值");
    }
    private static void bad(String message) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message); }
    private static Map<String,Object> copy(Map<String,Object> row) { return new LinkedHashMap<>(row); }
    private static Map<String,Object> copyRevision(Map<String,Object> row) {
        var result = copy(row);
        @SuppressWarnings("unchecked") var snap = (Map<String,Object>)row.get("snapshot");
        result.put("snapshot",copy(snap));
        return result;
    }
    private static List<Map<String,Object>> copies(List<Map<String,Object>> rows) { return rows.stream().map(LabbookController::copy).toList(); }
    private static Map<String,Object> revision(Map<String,Object> row,String operation) {
        var rev=new LinkedHashMap<String,Object>();
        rev.put("experimentId",row.get("id")); rev.put("revision",row.get("revision"));
        rev.put("operation",operation); rev.put("recordedAt",row.get("updatedAt")); rev.put("snapshot",copy(row));
        return rev;
    }
    private static Map<String,Object> find(List<Map<String,Object>> rows,long id) { return rows.get(indexOf(rows,id)); }
    private static int indexOf(List<Map<String,Object>> rows,long id) {
        for (int i=0;i<rows.size();i++) if (((Number)rows.get(i).get("id")).longValue()==id) return i;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"记录不存在");
    }
    private void seed() {
        var sample=new LinkedHashMap<String,Object>();
        sample.put("code","DEMO-S01"); sample.put("name","演示水样"); sample.put("type","环境样本");
        sample.put("source","虚构采样点"); sample.put("storage","演示架 A1"); sample.put("notes","全部为虚构数据");
        sample.put("id",nextId++); sample.put("createdAt",OffsetDateTime.now().toString()); samples.add(sample);
        demo("环境科学","示例：水质指标观察","研究员 A","进行中",sample.get("id"),"比较不同时间的指标变化","记录采样时间与设备；原始数据另存","记录了第一批示例数据","","");
        demo("材料科学","示例：涂层外观对比","研究员 B","已完成",null,"不同条件可能影响外观","按机构流程制备并记录样本批次","观察到差异（虚构）","A 组表面更均匀（虚构）","完成对比记录，后续需重复验证");
    }
    private void demo(String discipline,String title,String researcher,String status,Object sampleId,String hypothesis,String protocol,String observations,String results,String conclusion) {
        var row=new LinkedHashMap<String,Object>();
        row.put("title",title); row.put("discipline",discipline); row.put("researcher",researcher);
        row.put("date",LocalDate.now().toString()); row.put("status",status); row.put("hypothesis",hypothesis);
        row.put("protocol",protocol); row.put("sampleId",sampleId); row.put("observations",observations);
        row.put("results",results); row.put("conclusion",conclusion); row.put("dataLocation",""); row.put("tags","演示");
        row.put("id",nextId++); row.put("revision",1);
        String now=OffsetDateTime.now().toString(); row.put("createdAt",now); row.put("updatedAt",now);
        experiments.add(row); revisions.add(revision(row,"创建"));
    }
    private void load() {
        try {
            Map<String,Object> saved=mapper.readValue(file.toFile(),new TypeReference<>(){});
            if (!(saved.get("nextId") instanceof Number n) || n.longValue()<1) throw new IOException("invalid nextId");
            nextId=n.longValue();
            readRows(saved,"experiments",experiments);
            readRows(saved,"samples",samples);
            readRows(saved,"revisions",revisions);
        } catch (IOException | ClassCastException ex) { throw new IllegalStateException("实验数据文件损坏，请先备份再修复："+file,ex); }
    }
    private static void readRows(Map<String,Object> saved,String key,List<Map<String,Object>> target) throws IOException {
        if (!(saved.get(key) instanceof List<?> rows)) throw new IOException("missing "+key);
        for (var value:rows) {
            if (!(value instanceof Map<?,?> row) || !(row.get(key.equals("revisions")?"experimentId":"id") instanceof Number)) throw new IOException("invalid "+key);
            @SuppressWarnings("unchecked") Map<String,Object> item=(Map<String,Object>)row;
            target.add(new LinkedHashMap<>(item));
        }
    }
    private void persist() {
        try {
            Files.createDirectories(file.getParent());
            Path temp=Files.createTempFile(file.getParent(),"labbook-",".tmp");
            try {
                var data=new LinkedHashMap<String,Object>();
                data.put("nextId",nextId); data.put("experiments",experiments);
                data.put("samples",samples); data.put("revisions",revisions);
                mapper.writerWithDefaultPrettyPrinter().writeValue(temp.toFile(),data);
                try { Files.move(temp,file,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE); }
                catch (AtomicMoveNotSupportedException ex) { Files.move(temp,file,StandardCopyOption.REPLACE_EXISTING); }
            } finally { Files.deleteIfExists(temp); }
        } catch (IOException ex) { throw new IllegalStateException("无法写入实验数据："+file,ex); }
    }
}
