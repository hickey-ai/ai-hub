package dev.aihub.selfshop;

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

@SpringBootApplication public class SelfshopApplication {public static void main(String[] args){SpringApplication.run(SelfshopApplication.class,args);}}
@RestController @RequestMapping("/api") class SelfshopController {
 record Product(int id,String barcode,String name,String category,int price,int stock,String icon,String badge){}
 record Line(int productId,int quantity){}
 record CheckoutInput(String shopper,List<Line> items){}
 record OrderLine(int productId,String name,int quantity,int unitPrice,int subtotal){}
 record Order(long id,String shopper,List<OrderLine> items,int total,String status,String createdAt){}
 private final ObjectMapper mapper;private final Path file;private final List<Order> orders;private final List<Product> products=new ArrayList<>(List.of(
   new Product(1,"690000000001","冷萃咖啡","饮品",16,40,"☕","热卖"),new Product(2,"690000000002","鲜牛奶 250ml","乳品",8,36,"🥛",""),
   new Product(3,"690000000003","全麦三明治","食品",19,24,"🥪","早餐"),new Product(4,"690000000004","原味坚果","零食",22,30,"🥜",""),
   new Product(5,"690000000005","气泡水","饮品",7,60,"🫧",""),new Product(6,"690000000006","酸奶杯","乳品",12,28,"🍶","") ));
 SelfshopController(ObjectMapper mapper,@Value("${aihub.data-file:data/selfshop.json}") String fileName){this.mapper=mapper;file=Path.of(fileName);try{orders=Files.exists(file)?new ArrayList<>(mapper.readValue(file.toFile(),new TypeReference<List<Order>>(){})):new ArrayList<>();for(Order o:orders)for(OrderLine line:o.items()){int i=index(line.productId());Product p=products.get(i);if(p.stock()<line.quantity())throw new IllegalStateException("库存数据不一致");products.set(i,new Product(p.id(),p.barcode(),p.name(),p.category(),p.price(),p.stock()-line.quantity(),p.icon(),p.badge()));}}catch(Exception e){throw new IllegalStateException("购物数据读取失败，请先备份："+file,e);}}
 @GetMapping("/products") synchronized List<Product> products(){return List.copyOf(products);}
 @GetMapping("/products/barcode/{barcode}") synchronized Product barcode(@PathVariable String barcode){return products.stream().filter(p->p.barcode().equals(barcode)).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"未找到该条码商品"));}
 @GetMapping("/orders") synchronized List<Order> orders(@RequestParam String shopper){return orders.stream().filter(o->o.shopper().equals(shopper)).sorted(Comparator.comparingLong(Order::id).reversed()).toList();}
 @PostMapping("/orders") @ResponseStatus(HttpStatus.CREATED) synchronized Order checkout(@RequestBody CheckoutInput input){
   if(input.shopper()==null||!input.shopper().matches("[A-Za-z0-9-]{1,24}")||input.items()==null||input.items().isEmpty()||input.items().size()>30)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"购物码或商品清单无效");
   var merged=new LinkedHashMap<Integer,Integer>();for(Line l:input.items()){if(l.quantity()<1||l.quantity()>30||products.stream().noneMatch(p->p.id()==l.productId()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"商品或数量无效");merged.merge(l.productId(),l.quantity(),Integer::sum);if(merged.get(l.productId())>30)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"单品数量超限");}
   var lines=new ArrayList<OrderLine>();int total=0;for(var entry:merged.entrySet()){Product p=products.get(index(entry.getKey()));if(p.stock()<entry.getValue())throw new ResponseStatusException(HttpStatus.CONFLICT,"库存不足："+p.name());int sub=p.price()*entry.getValue();total+=sub;lines.add(new OrderLine(p.id(),p.name(),entry.getValue(),p.price(),sub));}
   Order result=new Order(orders.stream().mapToLong(Order::id).max().orElse(0)+1,input.shopper(),lines,total,"待支付（演示）",Instant.now().toString());orders.add(result);save();for(var line:lines){int i=index(line.productId());Product p=products.get(i);products.set(i,new Product(p.id(),p.barcode(),p.name(),p.category(),p.price(),p.stock()-line.quantity(),p.icon(),p.badge()));}return result;
 }
 private int index(int id){for(int i=0;i<products.size();i++)if(products.get(i).id()==id)return i;throw new ResponseStatusException(HttpStatus.NOT_FOUND,"商品不存在");}
 private void save(){try{Files.createDirectories(file.toAbsolutePath().getParent());Path tmp=file.resolveSibling(file.getFileName()+".tmp");mapper.writeValue(tmp.toFile(),orders);Files.move(tmp,file,StandardCopyOption.REPLACE_EXISTING);}catch(Exception e){throw new IllegalStateException("订单保存失败",e);}}
}
