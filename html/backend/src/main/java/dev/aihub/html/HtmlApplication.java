package dev.aihub.html;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@SpringBootApplication
public class HtmlApplication { public static void main(String[] args) { SpringApplication.run(HtmlApplication.class, args); } }

@RestController
@RequestMapping("/api")
class HtmlController {
  private final List<Map<String, Object>> directory = List.of(
      entry("云栖咖啡实验室", "餐饮", "咖啡 · 轻食", "杭州市西湖区云栖路 18 号", "0571-8800-1024", "07:30 - 21:30", "适合办公和小型分享会的社区咖啡空间。"),
      entry("安心家电快修", "维修", "家电 · 上门", "上海市静安区新闸路 88 号", "021-6200-7721", "09:00 - 19:00", "提供空调、冰箱和洗衣机的预约检修服务。"),
      entry("青禾成长课堂", "教育", "少儿 · 编程", "深圳市南山区科苑路 36 号", "0755-8600-2310", "09:00 - 20:00", "面向 6—16 岁孩子的编程与科学实践课程。"),
      entry("禾木健康管理", "医疗健康", "体检 · 营养", "成都市高新区天府三街 66 号", "028-8500-4567", "08:00 - 18:00", "提供健康评估、营养咨询和生活方式建议。"),
      entry("邻里到家", "家政", "保洁 · 收纳", "北京市朝阳区望京街 9 号", "010-6400-9088", "08:30 - 20:00", "透明报价的家庭保洁、整理收纳与家电清洗。"),
      entry("像素工坊", "数码服务", "维修 · 设计", "广州市海珠区新港中路 12 号", "020-8900-3421", "10:00 - 22:00", "电脑维护、数据备份和品牌视觉小单快交。")
  );
  private static Map<String,Object> entry(String name,String category,String tags,String address,String phone,String hours,String intro) {
    return map("id", UUID.nameUUIDFromBytes(name.getBytes()).toString(), "name", name, "category", category, "tags", tags, "address", address, "phone", phone, "hours", hours, "intro", intro, "rating", "4.9");
  }
  private static LinkedHashMap<String,Object> map(Object... values) { var m = new LinkedHashMap<String,Object>(); for (int i=0;i<values.length;i+=2) m.put((String) values[i], values[i+1]); return m; }
  @GetMapping("/directory") List<Map<String,Object>> directory() { return directory; }
  @GetMapping("/categories") List<String> categories() { return List.of("全部", "餐饮", "维修", "教育", "医疗健康", "家政", "数码服务"); }
  @GetMapping("/overview") Map<String,Object> overview() { return map("templates", 5, "games", 2, "directory", directory.size(), "localOnly", true); }
}
