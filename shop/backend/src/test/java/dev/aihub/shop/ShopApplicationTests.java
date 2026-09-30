package dev.aihub.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class ShopApplicationTests {
    @Autowired MockMvc mvc;
    @Test void catalogAndCheckout() throws Exception {
        mvc.perform(get("/api/products")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Arc 台灯"));
        String body = """ 
            {"customer":"测试用户","email":"test@example.com","items":[{"productId":1,"quantity":2},{"productId":1,"quantity":1}]}
            """;
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.total").value(987.00)).andExpect(jsonPath("$.items[0].quantity").value(3));
        mvc.perform(get("/api/products")).andExpect(jsonPath("$[0].stock").value(15));
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("""
            {"customer":"测试用户","email":"test@example.com","items":[{"productId":1,"quantity":100}]}
            """)).andExpect(status().isConflict());
    }
}
