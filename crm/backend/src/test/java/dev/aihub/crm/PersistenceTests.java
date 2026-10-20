package dev.aihub.crm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
class PersistenceTests {
    @TempDir Path dir;
    @Test void customerDealAndActivitySurviveRestart() {
        String file = dir.resolve("crm.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        CrmController first = new CrmController(mapper, file);
        Customer customer = first.createCustomer(new CustomerInput("青禾", "陈经理", "零售", "顾晨"));
        Opportunity deal = first.createOpportunity(new OpportunityInput(customer.id(), "年度采购", 20000, "顾晨"));
        first.addActivity(deal.id(), new ActivityInput("首次拜访"));
        first.changeStage(deal.id(), new StageInput("方案沟通"));
        first.updateCustomer(customer.id(), new CustomerInput("青禾更新", "陈经理新联系方式", "零售", "顾晨"));
        Opportunity editedDeal = first.updateOpportunity(deal.id(), new OpportunityDetailsInput("更新采购", 25000, "新负责人"));
        CrmController restarted = new CrmController(mapper, file);
        Customer edited = restarted.customers().stream().filter(c -> c.id() == customer.id()).findFirst().orElseThrow();
        assertEquals("青禾更新", edited.name());
        assertEquals("陈经理新联系方式", edited.contact());
        assertEquals(customer.id(), restarted.opportunities().getLast().customerId());
        assertEquals(editedDeal, restarted.opportunities().getLast());
        assertEquals(deal.createdAt(), editedDeal.createdAt());
        assertEquals(526000, restarted.stats().pipeline());
        assertEquals("方案沟通", restarted.opportunities().getLast().stage());
        assertEquals("首次拜访", restarted.activities(deal.id()).getFirst().note());
        assertEquals(5, restarted.createCustomer(new CustomerInput("新客户", "李经理", "教育", "顾晨")).id());
    }
    @Test void failedWriteDoesNotChangeMemory() throws Exception {
        String file = dir.resolve("blocked.json").toString();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        CrmController app = new CrmController(mapper, file);
        java.nio.file.Files.createDirectory(Path.of(file));
        assertThrows(IllegalStateException.class, () -> app.createCustomer(new CustomerInput("青禾", "陈经理", "零售", "顾晨")));
        assertEquals(3, app.customers().size());
        Customer unchanged = app.customers().getFirst();
        assertThrows(IllegalStateException.class, () -> app.updateCustomer(unchanged.id(), new CustomerInput("Changed", "Alex", "Software", "Morgan")));
        assertEquals(unchanged, app.customers().getFirst());
        Opportunity originalDeal = app.opportunities().getFirst();
        assertThrows(IllegalStateException.class, () -> app.updateOpportunity(originalDeal.id(), new OpportunityDetailsInput("Changed", 1, "Morgan")));
        assertEquals(originalDeal, app.opportunities().getFirst());
    }
    @Test void wonOpportunityCannotBeEdited() {
        CrmController app = new CrmController(new ObjectMapper().findAndRegisterModules(), dir.resolve("won.json").toString());
        Opportunity won = app.changeStage(1, new StageInput("已赢单"));
        var error = assertThrows(org.springframework.web.server.ResponseStatusException.class,
            () -> app.updateOpportunity(1, new OpportunityDetailsInput("Changed", 1, "Morgan")));
        assertEquals(409, error.getStatusCode().value());
        assertEquals(won, app.opportunities().getFirst());
    }
    @Test void deletionSurvivesRestartAndFailedWriteKeepsCustomer() throws Exception {
        Path file = dir.resolve("delete.json");
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        CrmController app = new CrmController(mapper, file.toString());
        Customer removed = app.createCustomer(new CustomerInput("Duplicate demo", "Alex", "Software", "Morgan"));
        Customer retained = app.createCustomer(new CustomerInput("Retained demo", "Sam", "Software", "Morgan"));
        var deals = app.opportunities();
        var history = app.activities(1);
        app.deleteCustomer(removed.id());
        CrmController restarted = new CrmController(mapper, file.toString());
        assertFalse(restarted.customers().stream().anyMatch(c -> c.id() == removed.id()));
        assertEquals(deals, restarted.opportunities());
        assertEquals(history, restarted.activities(1));
        assertEquals(retained.id() + 1,
            restarted.createCustomer(new CustomerInput("Next demo", "A", "S", "M")).id());
        java.nio.file.Files.delete(file);
        java.nio.file.Files.createDirectory(file);
        assertThrows(IllegalStateException.class, () -> restarted.deleteCustomer(retained.id()));
        assertTrue(restarted.customers().contains(retained));
        assertEquals(deals, restarted.opportunities());
    }
    @Test void corruptFileRefusesStartup() throws Exception {
        Path file = dir.resolve("corrupt.json");
        java.nio.file.Files.writeString(file, "not-json");
        assertThrows(IllegalStateException.class, () -> new CrmController(new ObjectMapper().findAndRegisterModules(), file.toString()));
    }
}
