package dev.aihub.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "aihub.data-file=target/test-data/shop-${random.uuid}.json" ) @AutoConfigureMockMvc
class ShopApplicationTests {
    @Autowired MockMvc mvc;
    @Test void searchAndDetail() throws Exception {
        mvc.perform(get("/api/products/search").param("keyword", "台灯").param("category", "家居生活"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.items[0].id").value(1));
        mvc.perform(get("/api/products/search").param("sort", "price_desc").param("page", "1").param("size", "2"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(6))
            .andExpect(jsonPath("$.items[0].price").value(199));
        mvc.perform(get("/api/products/search").param("minPrice", "100").param("maxPrice", "200"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(3));
        mvc.perform(get("/api/products/1")).andExpect(status().isOk()).andExpect(jsonPath("$.stock").isNumber());
        mvc.perform(get("/api/products/999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/products/search").param("sort", "invalid")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/products/search").param("size", "101")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/products/search").param("minPrice", "200").param("maxPrice", "100")).andExpect(status().isBadRequest());
    }
    @Test void duplicateCartLinesCannotOverflowQuantity() throws Exception {
        String email = "overflow@example.com";
        String before = mvc.perform(get("/api/products/2"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content("""
            {"customer":"溢出测试","email":"overflow@example.com","items":[{"productId":2,"quantity":2147483647},{"productId":2,"quantity":2147483647}]}
            """))
            .andExpect(status().isBadRequest());
        mvc.perform(get("/api/products/2"))
            .andExpect(status().isOk()).andExpect(content().json(before));
        mvc.perform(get("/api/orders").param("email", email))
            .andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }
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
        mvc.perform(get("/api/orders").param("email","test@example.com")).andExpect(status().isOk()).andExpect(jsonPath("$[0].email").value("test@example.com"));
        mvc.perform(get("/api/orders").param("email","bad-email")).andExpect(status().isBadRequest());
    }
}
