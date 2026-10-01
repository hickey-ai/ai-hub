package dev.aihub.dining;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

@SpringBootApplication public class DiningApplication { public static void main(String[] args){SpringApplication.run(DiningApplication.class,args);} }
@RestController @RequestMapping("/api") class DiningController {
 record Dish(int id,String name,String category,String desc,int price,String icon,String badge){}
 record Line(int dishId,int quantity){}
 record OrderInput(String tableNo,String note,List<Line> items){}
 record OrderLine(int dishId,String name,int quantity,int unitPrice,int subtotal){}
 record Order(long id,String tableNo,String note,List<OrderLine> items,int total,String status,String createdAt){}
 private final List<Dish> dishes=List.of(
   new Dish(1,"招牌和牛饭","主食","慢炖牛肉 · 温泉蛋 · 米饭",48,"🍚","招牌"),new Dish(2,"照烧鸡腿饭","主食","现烤鸡腿 · 秘制酱汁",38,"🍗","热卖"),
   new Dish(3,"番茄牛腩面","主食","浓汤现煮 · 弹牙面条",42,"🍜",""),new Dish(4,"香酥薯条","小食","现炸外脆里软",18,"🍟",""),
   new Dish(5,"日式章鱼烧","小食","六粒装 · 木鱼花",26,"🐙","人气"),new Dish(6,"鲜榨橙汁","饮品","鲜橙现榨 · 甜度自然",22,"🍊",""),
   new Dish(7,"抹茶拿铁","饮品","抹茶香气 · 丝滑奶泡",28,"🍵","") );
 private final ObjectMapper mapper; private final Path file; private final List<Order> orders;
 DiningController(ObjectMapper mapper,@Value("${aihub.data-file:data/dining.json}") String fileName){this.mapper=mapper;file=Path.of(fileName);try{orders=Files.exists(file)?new ArrayList<>(mapper.readValue(file.toFile(),new TypeReference<List<Order>>(){})):new ArrayList<>();}catch(Exception e){throw new IllegalStateException("订单数据读取失败，请先备份："+file,e);}}
 @GetMapping("/dishes") List<Dish> dishes(){return dishes;}
 @GetMapping("/orders") synchronized List<Order> orders(@RequestParam String tableNo){return orders.stream().filter(o->o.tableNo().equals(tableNo)).sorted(Comparator.comparingLong(Order::id).reversed()).toList();}
 @PostMapping("/orders") @ResponseStatus(HttpStatus.CREATED) synchronized Order order(@RequestBody OrderInput input){
   if(input.tableNo()==null||!input.tableNo().matches("[A-Za-z0-9-]{1,12}")||input.items()==null||input.items().isEmpty()||input.items().size()>20||input.note()!=null&&input.note().length()>100) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"桌号、菜品或备注无效");
   var merged=new LinkedHashMap<Integer,Integer>();for(Line l:input.items()){
     if(l.quantity()<1||l.quantity()>20||dishes.stream().noneMatch(d->d.id()==l.dishId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"菜品或数量无效");
     merged.merge(l.dishId(),l.quantity(),Integer::sum);if(merged.get(l.dishId())>20) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"单品数量超限");
   }
   var lines=new ArrayList<OrderLine>();int total=0;for(var entry:merged.entrySet()){Dish dish=dishes.stream().filter(d->d.id()==entry.getKey()).findFirst().orElseThrow();int sub=dish.price()*entry.getValue();total+=sub;lines.add(new OrderLine(dish.id(),dish.name(),entry.getValue(),dish.price(),sub));}
   Order result=new Order(orders.stream().mapToLong(Order::id).max().orElse(0)+1,input.tableNo(),input.note()==null?"":input.note().trim(),lines,total,"待制作",Instant.now().toString());orders.add(result);save();return result;
 }
 private void save(){try{Files.createDirectories(file.toAbsolutePath().getParent());Path tmp=file.resolveSibling(file.getFileName()+".tmp");mapper.writeValue(tmp.toFile(),orders);Files.move(tmp,file,StandardCopyOption.REPLACE_EXISTING);}catch(Exception e){throw new IllegalStateException("订单保存失败",e);}}
}
