package dev.aihub.wellness;
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


@SpringBootApplication public class WellnessApplication { public static void main(String[] args){SpringApplication.run(WellnessApplication.class,args);} }
@RestController @RequestMapping("/api") class WellnessController {
 private final Map<String,Object> metrics=new LinkedHashMap<>(); private final Map<String,List<Map<String,Object>>> data=new LinkedHashMap<>(); private long nextId=1;
 private final ObjectMapper mapper; private final Path file;
 WellnessController(ObjectMapper mapper,@Value("${aihub.data-file}") String fileName){this.mapper=mapper;this.file=Path.of(fileName).toAbsolutePath(); metrics.put("primary","plans"); metrics.put("title","vital · 养生服务台"); metrics.put("活跃会员","1,208");metrics.put("今日打卡","734");metrics.put("课程完成率","78.2%");metrics.put("服务提醒","42"); data.computeIfAbsent("plans",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","晨光轻食计划","detail","21 天 · 饮食","status","进行中","amount","186 人")));data.computeIfAbsent("plans",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","舒压睡眠计划","detail","14 天 · 睡眠","status","进行中","amount","92 人")));data.computeIfAbsent("plans",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","四季运动计划","detail","30 天 · 运动","status","待发布","amount","54 人")));data.computeIfAbsent("checkins",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","完成打卡","detail","陈女士 · 晨间拉伸","status","连续 12 天","amount","运动")));data.computeIfAbsent("checkins",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","课程预约","detail","李先生 · 秋季食养课","status","明日 10:00","amount","中医养生")));data.computeIfAbsent("checkins",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","服务回访","detail","王女士 · 睡眠计划","status","待联系","amount","会员关怀"))); load();}
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
