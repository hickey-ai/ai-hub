package dev.aihub.crm;

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
public class CrmApplication {
    public static void main(String[] args) { SpringApplication.run(CrmApplication.class, args); }
}
record Customer(long id, String name, String contact, String industry, String owner) {}
record CustomerInput(@NotBlank String name, @NotBlank String contact, @NotBlank String industry, @NotBlank String owner) {}
record Opportunity(long id, long customerId, String title, long amount, String stage, String owner, LocalDate createdAt) {}
record OpportunityInput(@Positive long customerId, @NotBlank String title, @Positive long amount, @NotBlank String owner) {}
record Activity(long id, long opportunityId, String note, LocalDate date) {}
record ActivityInput(@NotBlank String note) {}
record StageInput(@NotBlank String stage) {}
record CrmStats(int customers, int active, int won, long pipeline) {}
record CrmState(List<Customer> customers, List<Opportunity> opportunities, List<Activity> activities, long customerId, long opportunityId, long activityId) {}

@RestController
@RequestMapping("/api")
class CrmController {
    private final Map<Long, Customer> customers = new LinkedHashMap<>();
    private final Map<Long, Opportunity> opportunities = new LinkedHashMap<>();
    private final List<Activity> activities = new ArrayList<>();
    private long customerId = 1, opportunityId = 1, activityId = 1;
    private static final Set<String> STAGES = Set.of("发现需求", "方案沟通", "商务谈判", "已赢单", "已流失");
    private final StateFile<CrmState> store;
    CrmController(ObjectMapper mapper, @Value("${aihub.data-file}") String filename) {
        store = new StateFile<>(mapper, filename, CrmState.class);
        var previous = store.read();
        if (previous.isPresent()) {
            CrmState state = previous.get();
            state.customers().forEach(c -> customers.put(c.id(), c));
            state.opportunities().forEach(o -> opportunities.put(o.id(), o));
            activities.addAll(state.activities());
            customerId = state.customerId(); opportunityId = state.opportunityId(); activityId = state.activityId();
            return;
        }
        seedCustomer("星河科技", "李经理 · 138****6021", "科技互联网", "林知夏");
        seedCustomer("森野生活", "陈女士 · 139****1178", "零售消费", "顾晨");
        seedCustomer("远航教育", "张老师 · 137****2406", "教育培训", "林知夏");
        seedOpportunity(1, "企业协作平台年度采购", 280000, "商务谈判", "林知夏");
        seedOpportunity(2, "会员增长系统升级", 125000, "方案沟通", "顾晨");
        seedOpportunity(3, "线上课程管理平台", 96000, "发现需求", "林知夏");
        activities.add(new Activity(activityId++, 1, "已完成方案演示，等待采购确认", LocalDate.now().minusDays(1)));
        activities.add(new Activity(activityId++, 2, "确认会员积分对接需求", LocalDate.now().minusDays(2)));
    }
    private void seedCustomer(String name, String contact, String industry, String owner) { long id = customerId++; customers.put(id, new Customer(id, name, contact, industry, owner)); }
    private void seedOpportunity(long customerId, String title, long amount, String stage, String owner) { long id = opportunityId++; opportunities.put(id, new Opportunity(id, customerId, title, amount, stage, owner, LocalDate.now().minusDays(id * 3))); }
    @GetMapping("/customers") synchronized List<Customer> customers() { return List.copyOf(customers.values()); }
    @PostMapping("/customers") @ResponseStatus(HttpStatus.CREATED) synchronized Customer createCustomer(@Valid @RequestBody CustomerInput input) {
        long id = customerId;
        Customer customer = new Customer(id, input.name().trim(), input.contact().trim(), input.industry().trim(), input.owner().trim());
        Map<Long, Customer> updated = new LinkedHashMap<>(customers); updated.put(id, customer);
        commit(updated, opportunities, activities, customerId + 1, opportunityId, activityId); return customer;
    }
    @GetMapping("/opportunities") synchronized List<Opportunity> opportunities() { return List.copyOf(opportunities.values()); }
    @PostMapping("/opportunities") @ResponseStatus(HttpStatus.CREATED) synchronized Opportunity createOpportunity(@Valid @RequestBody OpportunityInput input) {
        if (!customers.containsKey(input.customerId())) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "客户不存在");
        long id = opportunityId;
        Opportunity opportunity = new Opportunity(id, input.customerId(), input.title().trim(), input.amount(), "发现需求", input.owner().trim(), LocalDate.now());
        Map<Long, Opportunity> updated = new LinkedHashMap<>(opportunities); updated.put(id, opportunity);
        commit(customers, updated, activities, customerId, opportunityId + 1, activityId); return opportunity;
    }
    @PatchMapping("/opportunities/{id}/stage") synchronized Opportunity changeStage(@PathVariable long id, @Valid @RequestBody StageInput input) {
        Opportunity old = opportunity(id);
        if (!STAGES.contains(input.stage())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "未知阶段");
        List<String> flow = List.of("发现需求", "方案沟通", "商务谈判");
        int current = flow.indexOf(old.stage());
        boolean valid = current >= 0 && (input.stage().equals("已流失") || current == 2 && input.stage().equals("已赢单") || current < 2 && input.stage().equals(flow.get(current + 1)));
        if (!valid) throw new ResponseStatusException(HttpStatus.CONFLICT, "不允许的阶段转换");
        Opportunity next = new Opportunity(id, old.customerId(), old.title(), old.amount(), input.stage(), old.owner(), old.createdAt());
        Map<Long, Opportunity> updated = new LinkedHashMap<>(opportunities); updated.put(id, next);
        commit(customers, updated, activities, customerId, opportunityId, activityId); return next;
    }
    @GetMapping("/opportunities/{id}/activities") synchronized List<Activity> activities(@PathVariable long id) {
        opportunity(id); return activities.stream().filter(a -> a.opportunityId() == id).toList();
    }
    @PostMapping("/opportunities/{id}/activities") @ResponseStatus(HttpStatus.CREATED) synchronized Activity addActivity(@PathVariable long id, @Valid @RequestBody ActivityInput input) {
        Opportunity opportunity = opportunity(id);
        if (opportunity.stage().startsWith("已")) throw new ResponseStatusException(HttpStatus.CONFLICT, "已结束商机不可跟进");
        Activity activity = new Activity(activityId, id, input.note().trim(), LocalDate.now());
        List<Activity> updated = new ArrayList<>(activities); updated.add(activity);
        commit(customers, opportunities, updated, customerId, opportunityId, activityId + 1); return activity;
    }
    @GetMapping("/stats") synchronized CrmStats stats() {
        return new CrmStats(customers.size(), (int) opportunities.values().stream().filter(o -> !o.stage().startsWith("已")).count(), (int) opportunities.values().stream().filter(o -> o.stage().equals("已赢单")).count(), opportunities.values().stream().filter(o -> !o.stage().startsWith("已")).mapToLong(Opportunity::amount).sum());
    }
    private void commit(Map<Long, Customer> newCustomers, Map<Long, Opportunity> newOpportunities, List<Activity> newActivities, long newCustomerId, long newOpportunityId, long newActivityId) {
        CrmState state = new CrmState(List.copyOf(newCustomers.values()), List.copyOf(newOpportunities.values()), List.copyOf(newActivities), newCustomerId, newOpportunityId, newActivityId);
        store.write(state);
        customers.clear(); state.customers().forEach(c -> customers.put(c.id(), c));
        opportunities.clear(); state.opportunities().forEach(o -> opportunities.put(o.id(), o));
        activities.clear(); activities.addAll(state.activities());
        customerId = newCustomerId; opportunityId = newOpportunityId; activityId = newActivityId;
    }
    private Opportunity opportunity(long id) { Opportunity result = opportunities.get(id); if (result == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "商机不存在"); return result; }
}
