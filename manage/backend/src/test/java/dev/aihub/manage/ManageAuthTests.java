package dev.aihub.manage;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"aihub.auth.issuer-uri=https://idp.example.test/realm",
        "aihub.data-file=target/test-data/manage-auth-${random.uuid}.json"})
@AutoConfigureMockMvc
class ManageAuthTests {
    @Autowired MockMvc mvc;
    @MockitoBean JwtDecoder decoder;

    private static Jwt token(String audience) {
        Instant now = Instant.now();
        return Jwt.withTokenValue("good").header("alg", "RS256")
                .issuer("https://idp.example.test/realm")
                .audience(List.of(audience)).subject("demo-user")
                .issuedAt(now.minusSeconds(10)).expiresAt(now.plusSeconds(600)).build();
    }

    @Test void apiRequiresBearerEvenForReadsAndWrites() throws Exception {
        mvc.perform(get("/api/users")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andExpect(status().isUnauthorized());
        when(decoder.decode("broken")).thenThrow(new BadJwtException("bad token"));
        mvc.perform(get("/api/users").header("Authorization", "Bearer broken"))
                .andExpect(status().isUnauthorized());
        when(decoder.decode("good")).thenReturn(token("ai-hub-manage"));
        mvc.perform(get("/api/users").header("Authorization", "Bearer good"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/metrics").header("Authorization", "Bearer good"))
                .andExpect(status().isOk());
    }

    @Test void issuerAudienceAndExpiryAreValidated() {
        assertFalse(ApiSecurity.validator("https://idp.example.test/realm", "ai-hub-manage")
                .validate(token("ai-hub-manage")).hasErrors());
        assertTrue(ApiSecurity.validator("https://idp.example.test/realm", "ai-hub-manage")
                .validate(token("another-service")).hasErrors());
        assertTrue(ApiSecurity.validator("https://other.example.test", "ai-hub-manage")
                .validate(token("ai-hub-manage")).hasErrors());
        Jwt expired = Jwt.withTokenValue("expired").header("alg", "RS256")
                .issuer("https://idp.example.test/realm").audience(List.of("ai-hub-manage"))
                .issuedAt(Instant.now().minusSeconds(600)).expiresAt(Instant.now().minusSeconds(300)).build();
        assertTrue(ApiSecurity.validator("https://idp.example.test/realm", "ai-hub-manage")
                .validate(expired).hasErrors());
    }
}
