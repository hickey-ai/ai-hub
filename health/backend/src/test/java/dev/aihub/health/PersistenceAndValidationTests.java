package dev.aihub.health;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PersistenceAndValidationTests {
 @Test void crudSurvivesRestartAndRejectsBadInputs(@TempDir Path dir) throws Exception {
  Path file=dir.resolve("records.json");
  ObjectMapper mapper=new ObjectMapper();
  var first=new HealthController(mapper,file.toString());
  int before=first.list("members").size();
  assertThrows(ResponseStatusException.class,()->first.list("not-a-resource"));
  assertThrows(ResponseStatusException.class,()->first.create("members",Map.of()));
  assertThrows(ResponseStatusException.class,()->first.create("members",null));
  assertThrows(ResponseStatusException.class,()->first.create("members",Map.of("name","   ")));
  assertThrows(ResponseStatusException.class,()->first.create("members",Map.of("name",123)));
  var created=first.create("members",Map.of("name","验收记录","id",999999));
  long id=((Number)created.get("id")).longValue();
  assertNotEquals(999999,id);
  assertEquals(before+1,first.list("members").size());
  assertThrows(ResponseStatusException.class,()->first.update("members",999999,Map.of("name","无")));
  first.update("members",id,Map.of("name","已修改"));
  var restarted=new HealthController(mapper,file.toString());
  assertEquals("已修改",restarted.list("members").stream().filter(row->((Number)row.get("id")).longValue()==id).findFirst().orElseThrow().get("name"));
  restarted.delete("members",id);
  assertEquals(before,new HealthController(mapper,file.toString()).list("members").size());
  Files.writeString(file,"broken-json");
  assertThrows(IllegalStateException.class,()->new HealthController(mapper,file.toString()));
 }
 @Test void failedWriteRollsBackMemory(@TempDir Path dir) throws Exception {
  Path blocked=dir.resolve("blocked.json");
  var service=new HealthController(new ObjectMapper(),blocked.toString());
  int before=service.list("members").size();
  Files.createDirectory(blocked);
  assertThrows(IllegalStateException.class,()->service.create("members",Map.of("name","不应保留")));
  assertEquals(before,service.list("members").size());
 }
}
