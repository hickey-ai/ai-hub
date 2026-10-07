package dev.aihub.authcenter;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class AuthcenterApplication {
    public static void main(String[] args) { SpringApplication.run(AuthcenterApplication.class, args); }
}

@Configuration
class AuthSecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
        return http.build();
    }
}

record LoginRequest(@NotBlank String username, @NotBlank String password, String audience) {}
record LoginResponse(String accessToken, String tokenType, long expiresIn, String issuer, String subject, String audience, List<String> roles) {}
record UserView(String username, String displayName, String role, boolean enabled) {}
record AuditEvent(String time, String action, String subject, String audience, String result) {}

@RestController
class AuthController {
    private final String issuer;
    private final Set<String> allowedAudiences;
    private final long tokenMinutes;
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final String keyId = UUID.randomUUID().toString();
    private final BCryptPasswordEncoder passwords = new BCryptPasswordEncoder();
    private final Map<String, Account> accounts = new LinkedHashMap<>();
    private final List<AuditEvent> audits = new CopyOnWriteArrayList<>();

    AuthController(@Value("${aihub.auth.issuer}") String issuer,
                   @Value("${aihub.auth.allowed-audiences}") String audiences,
                   @Value("${aihub.auth.token-minutes:60}") long tokenMinutes) {
        this.issuer = issuer;
        this.allowedAudiences = new LinkedHashSet<>(Arrays.stream(audiences.split(","))
                .map(String::trim).filter(s -> !s.isBlank()).toList());
        this.tokenMinutes = tokenMinutes;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();
            this.privateKey = (RSAPrivateKey) pair.getPrivate();
            this.publicKey = (RSAPublicKey) pair.getPublic();
        } catch (Exception e) {
            throw new IllegalStateException("无法初始化签名密钥", e);
        }
        accounts.put("admin", new Account("admin", "系统管理员", "管理员", passwords.encode("admin123"), true));
        accounts.put("operator", new Account("operator", "运营人员", "运营员", passwords.encode("operator123"), true));
    }

    @GetMapping("/api/health")
    Map<String, Object> health() { return Map.of("status", "UP", "issuer", issuer, "keyId", keyId, "audiences", allowedAudiences); }

    @PostMapping("/api/auth/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request) {
        String audience = request.audience() == null || request.audience().isBlank() ? allowedAudiences.iterator().next() : request.audience().trim();
        Account account = accounts.get(request.username().trim());
        if (account == null || !account.enabled() || !passwords.matches(request.password(), account.passwordHash())) {
            audits.add(new AuditEvent(Instant.now().toString(), "LOGIN", request.username(), audience, "DENIED"));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "账号或密码错误");
        }
        if (!allowedAudiences.contains(audience)) {
            audits.add(new AuditEvent(Instant.now().toString(), "TOKEN", account.username(), audience, "DENIED"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "未登记的应用受众");
        }
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(tokenMinutes * 60);
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .issuer(issuer).subject(account.username()).audience(audience)
                    .claim("name", account.displayName()).claim("roles", List.of(account.role()))
                    .claim("scope", "openid profile " + account.role())
                    .issueTime(Date.from(now)).expirationTime(Date.from(expires)).build();
            SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(keyId).type(JOSEObjectType.JWT).build(), claims);
            jwt.sign(new RSASSASigner(privateKey));
            audits.add(new AuditEvent(now.toString(), "LOGIN", account.username(), audience, "GRANTED"));
            return new LoginResponse(jwt.serialize(), "Bearer", expires.getEpochSecond() - now.getEpochSecond(), issuer, account.username(), audience, List.of(account.role()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "令牌签发失败", e);
        }
    }

    @GetMapping("/api/auth/me")
    UserView me(@RequestHeader(value = "Authorization", required = false) String authorization) {
        SignedJWT jwt = parseBearer(authorization);
        String subject;
        try { subject = jwt.getJWTClaimsSet().getSubject(); }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "令牌载荷无效"); }
        Account account = accounts.get(subject);
        if (account == null || !account.enabled()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "账号不可用");
        return new UserView(account.username(), account.displayName(), account.role(), account.enabled());
    }

    @PostMapping("/api/auth/logout")
    Map<String, String> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        String subject = me(authorization).username();
        audits.add(new AuditEvent(Instant.now().toString(), "SESSION_CLEAR", subject, "-", "RECORDED"));
        return Map.of("status", "client_clear_required", "revoked", "false");
    }

    @GetMapping("/api/audit")
    List<AuditEvent> audit(@RequestHeader(value = "Authorization", required = false) String authorization) {
        requireAdmin(authorization);
        return List.copyOf(audits);
    }

    @GetMapping("/api/users")
    List<UserView> users(@RequestHeader(value = "Authorization", required = false) String authorization) {
        requireAdmin(authorization);
        return accounts.values().stream().map(a -> new UserView(a.username(), a.displayName(), a.role(), a.enabled())).toList();
    }

    private void requireAdmin(String authorization) {
        UserView actor = me(authorization);
        if (!"管理员".equals(actor.role())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "需要管理员权限");
    }

    @GetMapping("/.well-known/openid-configuration")
    Map<String, Object> discovery() {
        return Map.of("issuer", issuer, "jwks_uri", issuer + "/oauth2/jwks");
    }

    @GetMapping("/oauth2/jwks")
    Map<String, Object> jwks() { return Map.of("keys", List.of(new RSAKey.Builder(publicKey).keyID(keyId).algorithm(JWSAlgorithm.RS256).build().toPublicJWK().toJSONObject())); }

    private SignedJWT parseBearer(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "需要 Bearer 令牌");
        try {
            SignedJWT jwt = SignedJWT.parse(authorization.substring(7));
            if (!jwt.verify(new RSASSAVerifier(publicKey))) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "令牌签名无效");
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            if (!issuer.equals(claims.getIssuer()) || claims.getExpirationTime() == null || !claims.getExpirationTime().after(new Date()) ||
                    claims.getIssueTime() == null || claims.getIssueTime().after(new Date()) ||
                    claims.getSubject() == null || claims.getSubject().isBlank() ||
                    claims.getAudience().stream().noneMatch(allowedAudiences::contains)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "令牌已过期或签发者不匹配");
            }
            return jwt;
        } catch (ResponseStatusException e) { throw e; }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "令牌格式无效"); }
    }

    record Account(String username, String displayName, String role, String passwordHash, boolean enabled) {}
}
