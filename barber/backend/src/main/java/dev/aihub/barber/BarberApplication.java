package dev.aihub.barber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class BarberApplication { public static void main(String[] args) { SpringApplication.run(BarberApplication.class, args); } }

@RestController @RequestMapping("/api")
class BarberController {
  record Service(int id, String name, String desc, int price, int minutes, String icon) {}
  record Barber(int id, String name, String level, String specialty, String avatar) {}
  record BookingInput(int serviceId, int barberId, String date, String time, String customer, String phone) {}
  record Booking(long id, int serviceId, String serviceName, int barberId, String barberName, String date, String time, String customer, String phone, int price, String status) {}
  private final List<Service> services = List.of(
    new Service(1,"精致剪发","洗剪吹 · 脸型设计",68,45,"✂"), new Service(2,"男士造型","清爽修剪 · 精细打理",58,35,"♠"),
    new Service(3,"染发焕色","色彩咨询 · 专业染护",268,120,"✦"), new Service(4,"烫发设计","纹理塑形 · 柔顺护理",328,150,"〰"));
  private final List<Barber> barbers = List.of(new Barber(1,"阿哲","资深设计师","短发 / 质感造型","哲"),new Barber(2,"小森","首席设计师","染发 / 层次剪裁","森"),new Barber(3,"Mia","造型总监","烫发 / 长发设计","M"));
  private final List<String> times = List.of("10:00","11:00","13:00","14:00","15:00","16:00","17:00","18:00");
  private final List<Booking> bookings;
  private final ObjectMapper mapper;
  private final Path file;
  BarberController(ObjectMapper mapper, @Value("${aihub.data-file:data/barber.json}") String fileName) {
    this.mapper=mapper; this.file=Path.of(fileName);
    try { bookings=Files.exists(file) ? new ArrayList<>(mapper.readValue(file.toFile(),new TypeReference<List<Booking>>(){})) : new ArrayList<>(); }
    catch(Exception e) { throw new IllegalStateException("预约数据读取失败，请检查文件并先备份："+file,e); }
  }
  @GetMapping("/services") List<Service> services(){return services;}
  @GetMapping("/barbers") List<Barber> barbers(){return barbers;}
  @GetMapping("/slots") synchronized Map<String,Object> slots(@RequestParam int barberId,@RequestParam String date) {
    if(barbers.stream().noneMatch(b->b.id()==barberId)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"发型师不存在");
    checkDate(date);
    List<String> available=times.stream().filter(t->bookings.stream().noneMatch(b->b.barberId()==barberId && b.date().equals(date) && b.time().equals(t) && b.status().equals("已预约"))).toList();
    return Map.of("date",date,"barberId",barberId,"available",available);
  }
  @GetMapping("/bookings") synchronized List<Booking> bookings(@RequestParam String phone){return bookings.stream().filter(b->b.phone().equals(phone)).sorted(Comparator.comparingLong(Booking::id).reversed()).toList();}
  @GetMapping("/bookings/{id}") synchronized Booking booking(@PathVariable long id,@RequestParam String phone){return bookings.stream().filter(b->b.id()==id && b.phone().equals(phone)).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"预约不存在"));}
  @PostMapping("/bookings") @ResponseStatus(HttpStatus.CREATED) synchronized Booking book(@RequestBody BookingInput input){
    Service s=services.stream().filter(x->x.id()==input.serviceId()).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"服务不存在"));
    Barber b=barbers.stream().filter(x->x.id()==input.barberId()).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"发型师不存在"));
    checkDate(input.date());
    if(!times.contains(input.time()) || blank(input.customer()) || input.phone()==null || !input.phone().matches("1\\d{10}")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"请填写姓名、11 位手机号和有效时段");
    if(bookings.stream().anyMatch(x->x.barberId()==b.id() && x.date().equals(input.date()) && x.time().equals(input.time()) && x.status().equals("已预约"))) throw new ResponseStatusException(HttpStatus.CONFLICT,"该时段已被预约");
    Booking booking=new Booking(bookings.stream().mapToLong(Booking::id).max().orElse(0)+1,s.id(),s.name(),b.id(),b.name(),input.date(),input.time(),input.customer().trim(),input.phone(),s.price(),"已预约");
    List<Booking> updated = new ArrayList<>(bookings);
    updated.add(booking);
    save(updated);
    bookings.clear();
    bookings.addAll(updated);
    return booking;
  }
  @PostMapping("/bookings/{id}/cancel") synchronized Booking cancel(@PathVariable long id,@RequestParam String phone){
    for(int i=0;i<bookings.size();i++){Booking x=bookings.get(i); if(x.id()==id && x.phone().equals(phone)){
      if(!x.status().equals("已预约")) throw new ResponseStatusException(HttpStatus.CONFLICT,"预约已取消");
      Booking changed=new Booking(x.id(),x.serviceId(),x.serviceName(),x.barberId(),x.barberName(),x.date(),x.time(),x.customer(),x.phone(),x.price(),"已取消");
      List<Booking> updated = new ArrayList<>(bookings);
      updated.set(i, changed);
      save(updated);
      bookings.clear();
      bookings.addAll(updated);
      return changed;
    }}throw new ResponseStatusException(HttpStatus.NOT_FOUND,"预约不存在");
  }
  private void checkDate(String value){try {LocalDate day=LocalDate.parse(value);if(day.isBefore(LocalDate.now())||day.isAfter(LocalDate.now().plusDays(14))) throw new IllegalArgumentException();}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"仅能预约未来 14 天");}}
  private boolean blank(String s){return s==null||s.isBlank();}
  private void save(List<Booking> snapshot){try{Files.createDirectories(file.toAbsolutePath().getParent());Path temp=file.resolveSibling(file.getFileName()+".tmp");mapper.writeValue(temp.toFile(),snapshot);Files.move(temp,file,StandardCopyOption.REPLACE_EXISTING);}catch(Exception e){throw new IllegalStateException("预约保存失败",e);}}
}
