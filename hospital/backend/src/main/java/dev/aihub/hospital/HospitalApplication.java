package dev.aihub.hospital;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;


@SpringBootApplication public class HospitalApplication { public static void main(String[] args){SpringApplication.run(HospitalApplication.class,args);} }
@RestController @RequestMapping("/api") class HospitalController {
 private final Map<String,Object> metrics=new LinkedHashMap<>(); private final Map<String,List<Map<String,Object>>> data=new LinkedHashMap<>(); private long nextId=1;
 private final ObjectMapper mapper; private final Path file;
 HospitalController(ObjectMapper mapper,@Value("${aihub.data-file}") String fileName){this.mapper=mapper;this.file=Path.of(fileName).toAbsolutePath(); metrics.put("primary","patients"); metrics.put("title","medix · 医院运营台"); metrics.put("今日门诊","1,286");metrics.put("在院患者","342");metrics.put("床位使用率","82.6%");metrics.put("待处理医嘱","18"); data.computeIfAbsent("patients",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","张某某","detail","心内科 · 住院 03-28","status","治疗中","amount","A-1208")));data.computeIfAbsent("patients",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","李某某","detail","骨科 · 待复诊","status","待复查","amount","B-0602")));data.computeIfAbsent("patients",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","王某某","detail","消化科 · 门诊","status","已完成","amount","门诊-3")));data.computeIfAbsent("appointments",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","门诊签到","detail","张医生 · 心内科","status","已签到","amount","32 人")));data.computeIfAbsent("appointments",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","检查报告","detail","影像科 · 08:30 批次","status","待审核","amount","8 份")));data.computeIfAbsent("appointments",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","病区交班","detail","3 号病区 · 夜班","status","已完成","amount","无异常"))); load();}
 private void load() {
  if (!Files.exists(file)) return;
  try {
   Map<String,Object> saved=mapper.readValue(file.toFile(),new TypeReference<>(){});
   if (!(saved.get("nextId") instanceof Number count) || !(saved.get("data") instanceof Map<?,?> rows)) throw new IOException("invalid header");
   Map<String,List<Map<String,Object>>> parsed=new LinkedHashMap<>();
   long max=0;
   for (String resource:data.keySet()) {
    if (!(rows.get(resource) instanceof List<?> items)) throw new IOException("missing "+resource);
    List<Map<String,Object>> list=new ArrayList<>();
    for(Object value:items) {
     if (!(value instanceof Map<?,?> item) || !(item.get("id") instanceof Number id) || id.longValue()<=0) throw new IOException("invalid record");
     Map<String,Object> copy=new LinkedHashMap<>();
     for(var entry:item.entrySet()) {
      if (!(entry.getKey() instanceof String key)) throw new IOException("invalid key");
      copy.put(key,entry.getValue());
     }
     max=Math.max(max,id.longValue()); list.add(copy);
    }
    parsed.put(resource,list);
   }
   if(count.longValue()<max) throw new IOException("invalid nextId");
   data.clear();data.putAll(parsed);nextId=count.longValue();
  } catch(Exception ex) {throw new IllegalStateException("数据文件损坏，请先备份并修复："+file,ex);}
 }
 private void persist() {
  Path temp=null;
  try {
   Files.createDirectories(file.getParent());
   temp=Files.createTempFile(file.getParent(),"aihub-",".tmp");
   mapper.writeValue(temp.toFile(),Map.of("nextId",nextId,"data",data));
   try {Files.move(temp,file,StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);}
   catch(AtomicMoveNotSupportedException ex) {Files.move(temp,file,StandardCopyOption.REPLACE_EXISTING);}
  } catch(IOException ex) {throw new IllegalStateException("本机数据保存失败："+file,ex);}
  finally {if(temp!=null)try{Files.deleteIfExists(temp);}catch(IOException ignored){}}
 }
 private List<Map<String,Object>> records(String resource) {
  var result=data.get(resource);
  if(result==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"资源不存在");
  return result;
 }
 private Map<String,Object> validated(Map<String,Object> body) {
  if(body==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"请求体不能为空");
  Map<String,Object> result=new LinkedHashMap<>();
  for(String key:List.of("name","owner","detail","status","amount")) {
   Object value=body.get(key);
   if(value!=null) {
    if(!(value instanceof String text)||text.length()>500)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,key+" 格式不正确");
    result.put(key,text.trim());
   }
  }
  if(String.valueOf(result.getOrDefault("name",result.getOrDefault("owner",""))).isBlank())
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"名称不能为空");
  result.putIfAbsent("status","待处理"); result.putIfAbsent("detail","");result.putIfAbsent("amount","—");
  return result;
 }
 private int indexOf(List<Map<String,Object>> rows,long id) {
  for(int i=0;i<rows.size();i++)if(((Number)rows.get(i).get("id")).longValue()==id)return i;
  throw new ResponseStatusException(HttpStatus.NOT_FOUND,"记录不存在");
 }
 @GetMapping("/metrics") synchronized Map<String,Object> metrics(){return Map.copyOf(metrics);}
 @GetMapping("/{resource}") synchronized List<Map<String,Object>> list(@PathVariable String resource){return records(resource).stream().map(x->(Map<String,Object>)new LinkedHashMap<>(x)).toList();}
 @PostMapping("/{resource}") @ResponseStatus(HttpStatus.CREATED) synchronized Map<String,Object> create(@PathVariable String resource,@RequestBody Map<String,Object> body){
  var rows=records(resource);var item=validated(body);item.put("id",++nextId);rows.add(item);
  try{persist();}catch(RuntimeException ex){rows.remove(item);nextId--;throw ex;}return item;
 }
 @PutMapping("/{resource}/{id}") synchronized Map<String,Object> update(@PathVariable String resource,@PathVariable long id,@RequestBody Map<String,Object> body){
  var rows=records(resource);int index=indexOf(rows,id);var item=validated(body);item.put("id",id);var old=rows.set(index,item);
  try{persist();}catch(RuntimeException ex){rows.set(index,old);throw ex;}return item;
 }
 @DeleteMapping("/{resource}/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) synchronized void delete(@PathVariable String resource,@PathVariable long id){
  var rows=records(resource);int index=indexOf(rows,id);var old=rows.remove(index);
  try{persist();}catch(RuntimeException ex){rows.add(index,old);throw ex;}
 }
}
