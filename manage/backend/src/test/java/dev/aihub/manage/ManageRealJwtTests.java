package dev.aihub.manage;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/** End-to-end resource-server check against a local OIDC discovery and JWKS endpoint. */
@SpringBootTest(properties = "aihub.data-file=target/test-data/manage-jwt-${random.uuid}.json")
@AutoConfigureMockMvc
class ManageRealJwtTests {
    @Autowired MockMvc mvc;
    private static final HttpServer center;
    private static final RSAKey key;
    private static final String issuer;
    static {
        try {
            var pair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            key = new RSAKey.Builder((java.security.interfaces.RSAPublicKey) pair.getPublic())
                    .privateKey((java.security.interfaces.RSAPrivateKey) pair.getPrivate())
                    .keyID("local-test-key").build();
            center = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            issuer = "http://127.0.0.1:" + center.getAddress().getPort();
            center.createContext("/.well-known/openid-configuration", exchange -> respond(exchange,
                    "{\"issuer\":\"" + issuer + "\",\"jwks_uri\":\"" + issuer + "/jwks\"}"));
            center.createContext("/jwks", exchange -> respond(exchange,
                    "{\"keys\":[" + key.toPublicJWK().toJSONString() + "]}"));
            center.start();
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    private static void respond(com.sun.net.httpserver.HttpExchange exchange, String body) throws java.io.IOException {
        byte[] content = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, content.length);
        try (var output = exchange.getResponseBody()) { output.write(content); }
    }
    @DynamicPropertySource static void properties(DynamicPropertyRegistry registry) {
        registry.add("aihub.auth.issuer-uri", () -> issuer);
    }
    @AfterAll static void stopCenter() { center.stop(0); }
    private static String token(String issuingCenter, String audience, long expiresInSeconds) throws Exception {
        return token(issuingCenter, audience, expiresInSeconds, key, "test-operator");
    }
    private static String token(String issuingCenter, String audience, long expiresInSeconds, RSAKey signingKey) throws Exception {
        return token(issuingCenter, audience, expiresInSeconds, signingKey, "test-operator");
    }
    private static String token(String issuingCenter, String audience, long expiresInSeconds, RSAKey signingKey, String subject) throws Exception {
        Instant now = Instant.now();
        var claims = new JWTClaimsSet.Builder().issuer(issuingCenter).subject(subject)
                .audience(List.of(audience)).issueTime(Date.from(now.minusSeconds(10)))
                .expirationTime(Date.from(now.plusSeconds(expiresInSeconds))).build();
        var jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID("local-test-key").build(), claims);
        jwt.sign(new RSASSASigner(signingKey));
        return jwt.serialize();
    }
    @Test void signedJwtFromCenterRequiredAndValidated() throws Exception {
        mvc.perform(get("/api/users")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",600)))
                .andExpect(status().isOk());
        mvc.perform(post("/api/users").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",600))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        var otherPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        var wrongKey = new RSAKey.Builder((java.security.interfaces.RSAPublicKey) otherPair.getPublic())
                .privateKey((java.security.interfaces.RSAPrivateKey) otherPair.getPrivate()).keyID("local-test-key").build();
        mvc.perform(get("/api/users").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",600,wrongKey)))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/profile").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",600)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("test-operator"))
                .andExpect(jsonPath("$.role").value("已认证（权限未配置）"));
        mvc.perform(get("/api/logs").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",600)))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
        String email = "jwt-" + UUID.randomUUID() + "@example.test";
        mvc.perform(post("/api/users").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",600))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"JWT user\",\"email\":\"" + email + "\",\"role\":\"编辑\",\"department\":\"产品研发\",\"status\":\"正常\"}"))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/logs").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",600)))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].operator").value("test-operator"));
        mvc.perform(get("/api/users").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",600,key,"")))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users").header("Authorization", "Bearer " + token(issuer,"other-app",600)))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users").header("Authorization", "Bearer " + token("https://wrong.example", "ai-hub-manage",600)))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/users").header("Authorization", "Bearer " + token(issuer,"ai-hub-manage",-600)))
                .andExpect(status().isUnauthorized());
    }
}
