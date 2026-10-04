package dev.aihub.barber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties="aihub.data-file=target/test-data/barber-${random.uuid}.json") @AutoConfigureMockMvc
class BarberApplicationTests {
 @Autowired MockMvc mvc;
 @Test void bookingLifecycle() throws Exception {
  String date=LocalDate.now().plusDays(1).toString();
  mvc.perform(get("/api/services")).andExpect(status().isOk()).andExpect(jsonPath("$[0].price").value(68));
  mvc.perform(get("/api/slots").param("barberId","1").param("date",date)).andExpect(jsonPath("$.available[0]").value("10:00"));
  String body="{\"serviceId\":1,\"barberId\":1,\"date\":\""+date+"\",\"time\":\"10:00\",\"customer\":\"测试\",\"phone\":\"13800138000\"}";
  mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));
  mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict());
  mvc.perform(get("/api/bookings").param("phone","13800138000")).andExpect(jsonPath("$[0].status").value("已预约"));
  mvc.perform(get("/api/bookings/1").param("phone","13800138000")).andExpect(jsonPath("$.customer").value("测试"));
  mvc.perform(get("/api/bookings/1").param("phone","13900139000")).andExpect(status().isNotFound());
  mvc.perform(post("/api/bookings/1/cancel").param("phone","13800138000")).andExpect(jsonPath("$.status").value("已取消"));
  mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated());
 }
 @Test void rejectsInvalidData() throws Exception {
  mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON).content("{\"serviceId\":99}")).andExpect(status().isBadRequest());
  mvc.perform(get("/api/slots").param("barberId","1").param("date","2000-01-01")).andExpect(status().isBadRequest());
 }
}
