package dev.aihub.finance;
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

@SpringBootApplication public class FinanceApplication { public static void main(String[] args){SpringApplication.run(FinanceApplication.class,args);} }
@RestController @RequestMapping("/api") class FinanceController {
 private final Map<String,Object> metrics=new LinkedHashMap<>(); private final Map<String,List<Map<String,Object>>> data=new LinkedHashMap<>(); private long nextId=1;
 private final ObjectMapper mapper; private final Path file;
 FinanceController(ObjectMapper mapper,@Value("${aihub.data-file}") String fileName){this.mapper=mapper;this.file=Path.of(fileName).toAbsolutePath(); metrics.put("primary","accounts"); metrics.put("title","fin · 金融经营台"); metrics.put("资产总额","¥ 8,420,600");metrics.put("本月收入","¥ 1,286,400");metrics.put("待处理事项","16");metrics.put("风险提示","3"); data.computeIfAbsent("accounts",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","星河科技","detail","经营账户 · 6222","status","正常","amount","¥ 3,260,000")));data.computeIfAbsent("accounts",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","森野生活","detail","结算账户 · 1088","status","正常","amount","¥ 1,180,500")));data.computeIfAbsent("accounts",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"owner","远航教育","detail","备用金账户 · 7721","status","关注","amount","¥ 268,900")));data.computeIfAbsent("transactions",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","回款入账","detail","星河科技 · 服务款","status","已入账","amount","+¥ 280,000")));data.computeIfAbsent("transactions",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","供应商付款","detail","云服务采购","status","已完成","amount","-¥ 62,400")));data.computeIfAbsent("transactions",k->new ArrayList<>()).add(new LinkedHashMap<>(Map.of("id",++nextId,"name","工资发放","detail","9 月薪酬批次","status","待复核","amount","-¥ 416,800"))); load();}

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
