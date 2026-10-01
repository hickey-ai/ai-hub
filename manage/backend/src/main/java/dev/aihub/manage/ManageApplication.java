package dev.aihub.manage;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@SpringBootApplication
public class ManageApplication {
    public static void main(String[] args) { SpringApplication.run(ManageApplication.class, args); }
}

record User(long id, String name, String email, String role, String department, String status) {}
record UserInput(@NotBlank String name, @Email @NotBlank String email, @NotBlank String role, @NotBlank String department, @NotBlank String status) {}
record Role(String name, String description, int members, String scope) {}
record Menu(long id, String name, String type, String path, String permission, String status) {}
record Department(long id, String name, String leader, int members, String status) {}
record LogEntry(long id, String operator, String action, String module, String ip, String time, String status) {}
record Metrics(int users, int active, int roles, int departments) {}
record ManageState(List<User> users, long nextId) {}
record Profile(String name, String email, String role, String department, String phone, String lastLogin) {}

@RestController
@RequestMapping("/api")
class ManageController {
    private final Map<Long, User> users = new LinkedHashMap<>();
    private long nextId = 9;
    private static final Set<String> ROLES = Set.of("超级管理员", "运营主管", "编辑", "观察员");
    private static final Set<String> STATUSES = Set.of("正常", "停用");
    private final StateFile<ManageState> store;
    ManageController(ObjectMapper mapper, @Value("${aihub.data-file}") String filename) {
        store = new StateFile<>(mapper, filename, ManageState.class);
        var previous = store.read();
        if (previous.isPresent()) { previous.get().users().forEach(u -> users.put(u.id(), u)); nextId = previous.get().nextId(); return; }
        seed(1,"林晓雨","xiaoyu@example.com","超级管理员","产品研发","正常"); seed(2,"陈一鸣","yiming@example.com","运营主管","运营中心","正常");
        seed(3,"王若琳","ruolin@example.com","编辑","内容团队","正常"); seed(4,"张可欣","kexin@example.com","观察员","财务部","正常");
        seed(5,"李知远","zhiyuan@example.com","编辑","产品研发","正常"); seed(6,"赵思齐","siqi@example.com","运营主管","运营中心","停用");
        seed(7,"周沐阳","muyang@example.com","观察员","内容团队","正常"); seed(8,"何佳宁","jianing@example.com","编辑","市场部","正常");
    }
    private void seed(long id,String name,String email,String role,String department,String status) { users.put(id,new User(id,name,email,role,department,status)); }
    @GetMapping("/users") synchronized List<User> users() { return List.copyOf(users.values()); }
    @GetMapping("/metrics") synchronized Metrics metrics() { return new Metrics(users.size(), (int)users.values().stream().filter(u->u.status().equals("正常")).count(), ROLES.size(), (int)users.values().stream().map(User::department).distinct().count()); }
    @GetMapping("/roles") synchronized List<Role> roles() { return List.of(new Role("超级管理员","管理所有资源、菜单和系统配置",count("超级管理员"),"全部数据"), new Role("运营主管","管理日常运营、报表与业务流程",count("运营主管"),"运营范围"), new Role("编辑","维护内容与基础资料",count("编辑"),"内容范围"), new Role("观察员","只读查看业务数据",count("观察员"),"只读权限")); }
    @GetMapping("/menus") List<Menu> menus() { return List.of(new Menu(1,"系统管理","目录","/system","system:manage","正常"),new Menu(2,"用户管理","菜单","/system/user","system:user:list","正常"),new Menu(3,"角色管理","菜单","/system/role","system:role:list","正常"),new Menu(4,"菜单管理","菜单","/system/menu","system:menu:list","正常"),new Menu(5,"部门管理","菜单","/system/dept","system:dept:list","正常"),new Menu(6,"日志管理","目录","/monitor","monitor:log","正常"),new Menu(7,"操作日志","菜单","/monitor/operlog","monitor:operlog:list","正常")); }
    @GetMapping("/departments") List<Department> departments() { return List.of(new Department(1,"总部","林晓雨",users.size(),"正常"),new Department(2,"产品研发","李知远",countDept("产品研发"),"正常"),new Department(3,"运营中心","陈一鸣",countDept("运营中心"),"正常"),new Department(4,"内容团队","王若琳",countDept("内容团队"),"正常"),new Department(5,"财务部","张可欣",countDept("财务部"),"正常"),new Department(6,"市场部","何佳宁",countDept("市场部"),"正常")); }
    @GetMapping("/logs") List<LogEntry> logs() { return List.of(new LogEntry(1,"林晓雨","查询用户列表","用户管理","127.0.0.1","2026-10-01 09:42:18","成功"),new LogEntry(2,"陈一鸣","更新角色权限","角色管理","127.0.0.1","2026-10-01 09:36:04","成功"),new LogEntry(3,"王若琳","导出操作日志","日志管理","127.0.0.1","2026-10-01 09:20:51","成功"),new LogEntry(4,"赵思齐","登录系统","认证中心","127.0.0.1","2026-09-30 18:15:29","失败"),new LogEntry(5,"林晓雨","新增部门","部门管理","127.0.0.1","2026-09-30 17:03:11","成功")); }
    @GetMapping("/profile") Profile profile() { return new Profile("林晓雨","xiaoyu@example.com","超级管理员","产品研发","138****8000","2026-10-01 09:42:18"); }
    private int count(String role) { return (int)users.values().stream().filter(u->u.role().equals(role)).count(); }
    private int countDept(String dept) { return (int)users.values().stream().filter(u->u.department().equals(dept)).count(); }
    private void commit(Map<Long, User> updated, long newNextId) { store.write(new ManageState(List.copyOf(updated.values()), newNextId)); users.clear(); users.putAll(updated); nextId = newNextId; }
    private void validate(UserInput input, long exclude) { if (!ROLES.contains(input.role()) || !STATUSES.contains(input.status())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"角色或状态无效"); if (users.values().stream().anyMatch(u->u.id()!=exclude && u.email().equalsIgnoreCase(input.email().trim()))) throw new ResponseStatusException(HttpStatus.CONFLICT,"邮箱已存在"); }
    @PostMapping("/users") @ResponseStatus(HttpStatus.CREATED) synchronized User create(@Valid @RequestBody UserInput input) { validate(input,-1); User u=new User(nextId,input.name().trim(),input.email().trim(),input.role(),input.department().trim(),input.status()); Map<Long,User> updated=new LinkedHashMap<>(users); updated.put(u.id(),u); commit(updated,nextId+1); return u; }
    @PutMapping("/users/{id}") synchronized User update(@PathVariable long id,@Valid @RequestBody UserInput input) { if(!users.containsKey(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"用户不存在"); validate(input,id); User u=new User(id,input.name().trim(),input.email().trim(),input.role(),input.department().trim(),input.status()); Map<Long,User> updated=new LinkedHashMap<>(users); updated.put(id,u); commit(updated,nextId); return u; }
    @DeleteMapping("/users/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) synchronized void delete(@PathVariable long id) { if(!users.containsKey(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"用户不存在"); Map<Long,User> updated=new LinkedHashMap<>(users); updated.remove(id); commit(updated,nextId); }
}
