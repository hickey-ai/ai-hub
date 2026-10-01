package dev.aihub.dining;
import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.http.MediaType;import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties="aihub.data-file=target/test-data/dining-${random.uuid}.json") @AutoConfigureMockMvc class DiningApplicationTests {
 @Autowired MockMvc mvc;
 @Test void orderFlow() throws Exception {
  mvc.perform(get("/api/dishes")).andExpect(status().isOk()).andExpect(jsonPath("$[0].price").value(48));
  mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("{\"tableNo\":\"A08\",\"note\":\"少辣\",\"items\":[{\"dishId\":1,\"quantity\":2},{\"dishId\":4,\"quantity\":1}]}"))
    .andExpect(status().isCreated()).andExpect(jsonPath("$.total").value(114)).andExpect(jsonPath("$.status").value("待制作"));
  mvc.perform(get("/api/orders").param("tableNo","A08")).andExpect(jsonPath("$[0].items[0].quantity").value(2));
  mvc.perform(get("/api/orders").param("tableNo","B01")).andExpect(jsonPath("$").isEmpty());
 }
 @Test void invalidOrder() throws Exception {
  mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("{\"tableNo\":\"A08\",\"items\":[{\"dishId\":999,\"quantity\":1}]}")) .andExpect(status().isBadRequest());
  mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("{\"tableNo\":\"A08\",\"items\":[]}")) .andExpect(status().isBadRequest());
 }
}
