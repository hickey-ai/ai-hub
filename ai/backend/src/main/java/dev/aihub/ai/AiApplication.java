package dev.aihub.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@SpringBootApplication
public class AiApplication { public static void main(String[] args){SpringApplication.run(AiApplication.class,args);} }

@RestController @RequestMapping("/api")
class AiController {
  private final List<Map<String,Object>> skills = List.of(
    skill("prompt-engineering","提示词工程","把模糊需求拆成角色、目标、约束与输出格式。","规划、写作、分析","中"),
    skill("frontend-craft","前端页面生成","从页面结构到交互状态，快速产出 Vue / HTML 原型。","Vue、HTML、CSS","中"),
    skill("java-api","Java API 设计","生成 Spring Boot 接口、校验、状态流转与测试骨架。","Java 21、REST","高"),
    skill("data-cleaning","数据整理助手","把表格字段、JSON 和自然语言记录整理成统一结构。","JSON、CSV、ETL","中"),
    skill("research-notes","科研笔记","把实验过程、观察、结果和下一步整理成可复用记录。","实验、复盘、知识库","高"),
    skill("release-check","发布检查","生成发布前检查清单，减少遗漏和回归风险。","测试、文档、发布","低")
  );
  private final List<Map<String,String>> commands = List.of(
    cmd("初始化 Vue 页面","vue","npm create vite@latest my-app -- --template vue","创建一个 Vue 3 + Vite 页面项目"),
    cmd("初始化 Spring Boot","java","mvn -B archetype:generate -DgroupId=dev.demo -DartifactId=demo-api -DinteractiveMode=false","创建 Java API 工程后再接入业务"),
    cmd("构建并测试前端","build","npm ci && npm run build","安装锁定依赖并生成生产构建"),
    cmd("运行本地技能","skill","aihub skill run prompt-engineering --input ./brief.md","通过统一命令调用一个本地技能模板"),
    cmd("导出项目提示词","prompt","aihub prompt export --name release-check --format markdown","将技能配置导出为 Markdown 方便协作")
  );
  private static Map<String,Object> skill(String id,String name,String desc,String tags,String level){return map("id",id,"name",name,"description",desc,"tags",tags,"level",level,"steps",List.of("明确输入与目标","选择适合的输出格式","执行后检查结果并记录反馈"));}
  private static Map<String,String> cmd(String name,String family,String command,String note){return Map.of("name",name,"family",family,"command",command,"note",note);}
  private static LinkedHashMap<String,Object> map(Object... values){var m=new LinkedHashMap<String,Object>();for(int i=0;i<values.length;i+=2)m.put((String)values[i],values[i+1]);return m;}
  @GetMapping("/skills") List<Map<String,Object>> skills(){return skills;}
  @GetMapping("/commands") List<Map<String,String>> commands(){return commands;}
  @GetMapping("/overview") Map<String,Object> overview(){return map("skills",skills.size(),"commands",commands.size(),"modes",List.of("CLI 工具箱","Skill 技能库","Prompt 工作台"),"local",true);}
  @PostMapping("/commands/preview") Map<String,String> preview(@RequestBody Map<String,String> input){String family=Optional.ofNullable(input.get("family")).orElse("build");String project=Optional.ofNullable(input.get("project")).orElse("my-app");String extra=Optional.ofNullable(input.get("extra")).orElse("").trim();if(!List.of("vue","java","build","skill","prompt").contains(family))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"不支持的命令类型");String command=switch(family){case "vue"->"npm create vite@latest "+safe(project)+" -- --template vue";case "java"->"mvn -B archetype:generate -DgroupId=dev.demo -DartifactId="+safe(project)+" -DinteractiveMode=false";case "build"->"npm ci && npm run build"+(extra.isEmpty()?"":" && "+safe(extra));case "skill"->"aihub skill run "+safe(input.getOrDefault("skill","prompt-engineering"))+" --input ./brief.md";default->"aihub prompt export --name "+safe(input.getOrDefault("skill","release-check"))+" --format markdown";};return Map.of("command",command,"note","命令仅在本机终端执行；请先检查参数与当前目录。","copyable","true");}
  private static String safe(String value){if(value==null||value.isBlank()||!value.matches("[A-Za-z0-9_./:@+-]+"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"参数包含不安全字符");return value;}
}
