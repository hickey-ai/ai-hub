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
        CrmController restarted = new CrmController(mapper, file);
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
    }
    @Test void corruptFileRefusesStartup() throws Exception {
        Path file = dir.resolve("corrupt.json");
        java.nio.file.Files.writeString(file, "not-json");
        assertThrows(IllegalStateException.class, () -> new CrmController(new ObjectMapper().findAndRegisterModules(), file.toString()));
    }
}
