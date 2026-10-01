package dev.aihub.selfshop;
import org.junit.jupiter.api.Test;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;import org.springframework.boot.test.context.SpringBootTest;import org.springframework.http.MediaType;import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties="aihub.data-file=target/test-data/selfshop-${random.uuid}.json") @AutoConfigureMockMvc class SelfshopApplicationTests {
 @Autowired MockMvc mvc;
 @Test void scanAndCheckout() throws Exception {
  mvc.perform(get("/api/products/barcode/690000000001")).andExpect(jsonPath("$.name").value("冷萃咖啡"));
  mvc.perform(get("/api/products/barcode/invalid")).andExpect(status().isNotFound());
  mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("{\"shopper\":\"demo-01\",\"items\":[{\"productId\":1,\"quantity\":2},{\"productId\":2,\"quantity\":1}]}"))
    .andExpect(status().isCreated()).andExpect(jsonPath("$.total").value(40)).andExpect(jsonPath("$.status").value("待支付（演示）"));
  mvc.perform(get("/api/products")).andExpect(jsonPath("$[0].stock").value(38));
  mvc.perform(get("/api/orders").param("shopper","demo-01")).andExpect(jsonPath("$[0].total").value(40));
  mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("{\"shopper\":\"demo-01\",\"items\":[{\"productId\":1,\"quantity\":30},{\"productId\":1,\"quantity\":11}]}")) .andExpect(status().isBadRequest());
 }
 @Test void invalidCheckout() throws Exception {mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("{\"shopper\":\"demo-01\",\"items\":[{\"productId\":1,\"quantity\":0}]}")) .andExpect(status().isBadRequest());}
}
