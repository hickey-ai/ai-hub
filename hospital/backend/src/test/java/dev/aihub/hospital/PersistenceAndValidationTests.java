package dev.aihub.hospital;

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
  var first=new HospitalController(mapper,file.toString());
  int before=first.list("patients").size();
  assertThrows(ResponseStatusException.class,()->first.list("not-a-resource"));
  assertThrows(ResponseStatusException.class,()->first.create("patients",Map.of()));
  assertThrows(ResponseStatusException.class,()->first.create("patients",null));
  assertThrows(ResponseStatusException.class,()->first.create("patients",Map.of("name","   ")));
  assertThrows(ResponseStatusException.class,()->first.create("patients",Map.of("name",123)));
  var created=first.create("patients",Map.of("name","验收记录","id",999999));
  long id=((Number)created.get("id")).longValue();
  assertNotEquals(999999,id);
  assertEquals(before+1,first.list("patients").size());
  assertThrows(ResponseStatusException.class,()->first.update("patients",999999,Map.of("name","无")));
  first.update("patients",id,Map.of("name","已修改"));
  var restarted=new HospitalController(mapper,file.toString());
  assertEquals("已修改",restarted.list("patients").stream().filter(row->((Number)row.get("id")).longValue()==id).findFirst().orElseThrow().get("name"));
  restarted.delete("patients",id);
  assertEquals(before,new HospitalController(mapper,file.toString()).list("patients").size());
  Files.writeString(file,"broken-json");
  assertThrows(IllegalStateException.class,()->new HospitalController(mapper,file.toString()));
 }
 @Test void failedWriteRollsBackMemory(@TempDir Path dir) throws Exception {
  Path blocked=dir.resolve("blocked.json");
  var service=new HospitalController(new ObjectMapper(),blocked.toString());
  int before=service.list("patients").size();
  Files.createDirectory(blocked);
  assertThrows(IllegalStateException.class,()->service.create("patients",Map.of("name","不应保留")));
  assertEquals(before,service.list("patients").size());
 }
}
