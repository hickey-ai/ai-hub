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
        CrmController restarted = new CrmController(mapper, file);
        Customer edited = restarted.customers().stream().filter(c -> c.id() == customer.id()).findFirst().orElseThrow();
        assertEquals("青禾更新", edited.name());
        assertEquals("陈经理新联系方式", edited.contact());
        assertEquals(customer.id(), restarted.opportunities().getLast().customerId());
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
    }
    @Test void corruptFileRefusesStartup() throws Exception {
        Path file = dir.resolve("corrupt.json");
        java.nio.file.Files.writeString(file, "not-json");
        assertThrows(IllegalStateException.class, () -> new CrmController(new ObjectMapper().findAndRegisterModules(), file.toString()));
    }
}
