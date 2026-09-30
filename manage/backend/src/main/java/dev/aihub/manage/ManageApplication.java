package dev.aihub.manage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
record Metrics(int users, int active, int roles, int departments) {}

@RestController
@RequestMapping("/api")
class ManageController {
    private final Map<Long, User> users = new LinkedHashMap<>();
    private long nextId = 9;
    private static final Set<String> ROLES = Set.of("超级管理员", "运营主管", "编辑", "观察员");
    private static final Set<String> STATUSES = Set.of("正常", "停用");
    ManageController() {
        seed(1,"林晓雨","xiaoyu@example.com","超级管理员","产品研发","正常");
        seed(2,"陈一鸣","yiming@example.com","运营主管","运营中心","正常");
        seed(3,"王若琳","ruolin@example.com","编辑","内容团队","正常");
        seed(4,"张可欣","kexin@example.com","观察员","财务部","正常");
        seed(5,"李知远","zhiyuan@example.com","编辑","产品研发","正常");
        seed(6,"赵思齐","siqi@example.com","运营主管","运营中心","停用");
        seed(7,"周沐阳","muyang@example.com","观察员","内容团队","正常");
        seed(8,"何佳宁","jianing@example.com","编辑","市场部","正常");
    }
    private void seed(long id,String name,String email,String role,String department,String status) { users.put(id,new User(id,name,email,role,department,status)); }
    @GetMapping("/users") synchronized List<User> users() { return List.copyOf(users.values()); }
    @GetMapping("/metrics") synchronized Metrics metrics() { return new Metrics(users.size(), (int)users.values().stream().filter(u->u.status().equals("正常")).count(), ROLES.size(), (int)users.values().stream().map(User::department).distinct().count()); }
    @GetMapping("/roles") synchronized List<Role> roles() {
        return List.of(new Role("超级管理员","管理所有资源与配置",count("超级管理员"),"全部权限"), new Role("运营主管","管理日常运营与报表",count("运营主管"),"运营范围"), new Role("编辑","维护内容与基础资料",count("编辑"),"内容范围"), new Role("观察员","只读查看业务数据",count("观察员"),"只读权限"));
    }
    private int count(String role) { return (int)users.values().stream().filter(u->u.role().equals(role)).count(); }
    private void validate(UserInput input, long exclude) {
        if (!ROLES.contains(input.role()) || !STATUSES.contains(input.status())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"角色或状态无效");
        if (users.values().stream().anyMatch(u->u.id()!=exclude && u.email().equalsIgnoreCase(input.email().trim()))) throw new ResponseStatusException(HttpStatus.CONFLICT,"邮箱已存在");
    }
    @PostMapping("/users") @ResponseStatus(HttpStatus.CREATED)
    synchronized User create(@Valid @RequestBody UserInput input) { validate(input,-1); User u=new User(nextId++,input.name().trim(),input.email().trim(),input.role(),input.department().trim(),input.status()); users.put(u.id(),u); return u; }
    @PutMapping("/users/{id}") synchronized User update(@PathVariable long id,@Valid @RequestBody UserInput input) {
        if (!users.containsKey(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"用户不存在");
        validate(input,id); User u=new User(id,input.name().trim(),input.email().trim(),input.role(),input.department().trim(),input.status()); users.put(id,u); return u;
    }
    @DeleteMapping("/users/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    synchronized void delete(@PathVariable long id) { if (users.remove(id)==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"用户不存在"); }
}
