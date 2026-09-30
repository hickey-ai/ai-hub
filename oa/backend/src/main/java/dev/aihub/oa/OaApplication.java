package dev.aihub.oa;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class OaApplication {
    public static void main(String[] args) { SpringApplication.run(OaApplication.class, args); }
}
record Request(long id, String type, String title, String applicant, String detail, long amount, String status, LocalDate createdAt, String reviewNote) {}
record RequestInput(@NotBlank String type, @NotBlank String title, @NotBlank String applicant, @NotBlank String detail, @PositiveOrZero long amount) {}
record DecisionInput(@NotBlank String decision, String note) {}
record OaStats(int total, int draft, int pending, int approved) {}
record OaState(List<Request> requests, long nextId) {}

@RestController
@RequestMapping("/api")
class OaController {
    private final Map<Long, Request> requests = new LinkedHashMap<>();
    private long nextId = 1;
    private final StateFile<OaState> store;
    OaController(ObjectMapper mapper, @Value("${aihub.data-file}") String filename) {
        store = new StateFile<>(mapper, filename, OaState.class);
        var previous = store.read();
        if (previous.isPresent()) {
            OaState state = previous.get();
            state.requests().forEach(r -> requests.put(r.id(), r));
            nextId = state.nextId();
            return;
        }
        seed("请假", "十一假期调休申请", "林知夏", "调休 2 天，工作已交接", 0, "待审批", "");
        seed("报销", "客户拜访交通费", "顾晨", "市内拜访往返交通费用", 368, "待审批", "");
        seed("报销", "设计素材采购", "许一诺", "活动页设计素材订阅", 299, "已通过", "符合预算");
        seed("请假", "项目结束后休假", "陈小满", "年假 3 天", 0, "草稿", "");
    }
    private void seed(String type, String title, String applicant, String detail, long amount, String status, String note) {
        long id = nextId++; requests.put(id, new Request(id, type, title, applicant, detail, amount, status, LocalDate.now().minusDays(id), note));
    }
    @GetMapping("/requests") synchronized List<Request> requests() { return List.copyOf(requests.values()); }
    @GetMapping("/stats") synchronized OaStats stats() {
        return new OaStats(requests.size(), count("草稿"), count("待审批"), count("已通过"));
    }
    private int count(String status) { return (int) requests.values().stream().filter(r -> r.status().equals(status)).count(); }
    @PostMapping("/requests") @ResponseStatus(HttpStatus.CREATED) synchronized Request create(@Valid @RequestBody RequestInput input) {
        if (!Set.of("请假", "报销").contains(input.type())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "不支持的类型");
        if (input.type().equals("请假") && input.amount() != 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请假金额必须为零");
        long id = nextId;
        Request request = new Request(id, input.type(), input.title().trim(), input.applicant().trim(), input.detail().trim(), input.amount(), "草稿", LocalDate.now(), "");
        Map<Long, Request> updated = new LinkedHashMap<>(requests); updated.put(id, request);
        commit(updated, nextId + 1); return request;
    }
    @PostMapping("/requests/{id}/submit") synchronized Request submit(@PathVariable long id) {
        Request old = get(id);
        if (!old.status().equals("草稿")) throw new ResponseStatusException(HttpStatus.CONFLICT, "只有草稿可提交");
        return update(old, "待审批", "");
    }
    @PostMapping("/requests/{id}/review") synchronized Request review(@PathVariable long id, @Valid @RequestBody DecisionInput input) {
        Request old = get(id);
        if (!old.status().equals("待审批")) throw new ResponseStatusException(HttpStatus.CONFLICT, "只有待审批申请可处理");
        if (!Set.of("已通过", "已驳回").contains(input.decision())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "未知审批决定");
        if (input.decision().equals("已驳回") && (input.note() == null || input.note().isBlank())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "驳回需要原因");
        return update(old, input.decision(), input.note() == null ? "" : input.note().trim());
    }
    private void commit(Map<Long, Request> updated, long newNextId) {
        store.write(new OaState(List.copyOf(updated.values()), newNextId));
        requests.clear(); requests.putAll(updated); nextId = newNextId;
    }
    private Request get(long id) { Request result = requests.get(id); if (result == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "申请不存在"); return result; }
    private Request update(Request old, String status, String note) {
        Request next = new Request(old.id(), old.type(), old.title(), old.applicant(), old.detail(), old.amount(), status, old.createdAt(), note);
        Map<Long, Request> updated = new LinkedHashMap<>(requests); updated.put(old.id(), next);
        commit(updated, nextId); return next;
    }
}
