package dev.aihub.school;

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
  var first=new SchoolController(mapper,file.toString());
  int before=first.list("students").size();
  assertThrows(ResponseStatusException.class,()->first.list("not-a-resource"));
  assertThrows(ResponseStatusException.class,()->first.create("students",Map.of()));
  assertThrows(ResponseStatusException.class,()->first.create("students",null));
  assertThrows(ResponseStatusException.class,()->first.create("students",Map.of("name","   ")));
  assertThrows(ResponseStatusException.class,()->first.create("students",Map.of("name",123)));
  var created=first.create("students",Map.of("name","验收记录","id",999999));
  long id=((Number)created.get("id")).longValue();
  assertNotEquals(999999,id);
  assertEquals(before+1,first.list("students").size());
  assertThrows(ResponseStatusException.class,()->first.update("students",999999,Map.of("name","无")));
  first.update("students",id,Map.of("name","已修改"));
  var restarted=new SchoolController(mapper,file.toString());
  assertEquals("已修改",restarted.list("students").stream().filter(row->((Number)row.get("id")).longValue()==id).findFirst().orElseThrow().get("name"));
  restarted.delete("students",id);
  assertEquals(before,new SchoolController(mapper,file.toString()).list("students").size());
  Files.writeString(file,"broken-json");
  assertThrows(IllegalStateException.class,()->new SchoolController(mapper,file.toString()));
 }
 @Test void failedWriteRollsBackMemory(@TempDir Path dir) throws Exception {
  Path blocked=dir.resolve("blocked.json");
  var service=new SchoolController(new ObjectMapper(),blocked.toString());
  int before=service.list("students").size();
  Files.createDirectory(blocked);
  assertThrows(IllegalStateException.class,()->service.create("students",Map.of("name","不应保留")));
  assertEquals(before,service.list("students").size());
 }
}
