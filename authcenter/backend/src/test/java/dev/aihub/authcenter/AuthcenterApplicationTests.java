package dev.aihub.authcenter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthcenterApplicationTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void discoveryAndJwksArePublic() throws Exception {
        mvc.perform(get("/.well-known/openid-configuration")).andExpect(status().isOk()).andExpect(jsonPath("$.issuer").value("http://127.0.0.1:8182"));
        mvc.perform(get("/oauth2/jwks")).andExpect(status().isOk()).andExpect(jsonPath("$.keys[0].alg").value("RS256"));
    }

    @Test void loginIssuesTokenAndMeRequiresValidBearer() throws Exception {
        String json = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"admin123\",\"audience\":\"ai-hub-manage\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.accessToken", not(isEmptyOrNullString()))).andReturn().getResponse().getContentAsString();
        JsonNode body = mapper.readTree(json);
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + body.get("accessToken").asText()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.username").value("admin"));
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/audit")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/audit").header("Authorization", "Bearer " + body.get("accessToken").asText()))
                .andExpect(status().isOk()).andExpect(jsonPath("$[-1:].result").value("GRANTED"));
    }

    @Test void operatorCannotReadAdminAuditAndForgedTokenFails() throws Exception {
        String json = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"operator\",\"password\":\"operator123\",\"audience\":\"ai-hub-manage\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String bearer = "Bearer " + mapper.readTree(json).get("accessToken").asText();
        mvc.perform(get("/api/audit").header("Authorization", bearer)).andExpect(status().isForbidden());
        mvc.perform(get("/api/users").header("Authorization", bearer)).andExpect(status().isForbidden());
        mvc.perform(get("/api/auth/me").header("Authorization", bearer + "x")).andExpect(status().isUnauthorized());
    }

    @Test void logoutCannotClaimRevocationAndRequiresValidToken() throws Exception {
        mvc.perform(post("/api/auth/logout")).andExpect(status().isUnauthorized());
        String json = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"admin123\",\"audience\":\"ai-hub-manage\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String bearer = "Bearer " + mapper.readTree(json).get("accessToken").asText();
        mvc.perform(post("/api/auth/logout").header("Authorization", bearer)).andExpect(status().isOk())
                .andExpect(jsonPath("$.revoked").value("false"));
        mvc.perform(get("/api/auth/me").header("Authorization", bearer)).andExpect(status().isOk());
    }

    @Test void invalidCredentialsAndAudienceAreRejected() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"wrong\",\"audience\":\"ai-hub-manage\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"admin123\",\"audience\":\"unknown-app\"}"))
                .andExpect(status().isBadRequest());
    }
}
