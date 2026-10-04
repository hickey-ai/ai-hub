package dev.aihub.manage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/** Opt-in API resource server. Without an issuer this remains a loopback-only demo. */
@Configuration
class ApiSecurity {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, @Value("${aihub.auth.issuer-uri:}") String issuer) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // The API accepts only Authorization: Bearer; it never authenticates with a browser cookie.
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
        if (issuer.isBlank()) {
            http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
        } else {
            http.authorizeHttpRequests(requests -> requests.requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll());
            http.oauth2ResourceServer(server -> server.jwt(org.springframework.security.config.Customizer.withDefaults()));
        }
        return http.build();
    }

    @Bean
    @ConditionalOnProperty("aihub.auth.issuer-uri")
    JwtDecoder jwtDecoder(@Value("${aihub.auth.issuer-uri}") String issuer,
                          @Value("${aihub.auth.audience:ai-hub-manage}") String audience) {
        if (issuer.isBlank() || audience.isBlank()) {
            throw new IllegalArgumentException("Issuer and audience must not be blank when authentication is enabled");
        }
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withIssuerLocation(issuer).build();
        decoder.setJwtValidator(validator(issuer, audience));
        return decoder;
    }

    static OAuth2TokenValidator<Jwt> validator(String issuer, String audience) {
        OAuth2TokenValidator<Jwt> expectedAudience = token -> token.getAudience().contains(audience)
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Wrong audience", null));
        OAuth2TokenValidator<Jwt> expectedSubject = token -> token.getSubject() != null && !token.getSubject().isBlank()
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Missing subject", null));
        return new DelegatingOAuth2TokenValidator<>(JwtValidators.createDefaultWithIssuer(issuer), expectedAudience, expectedSubject);
    }
}
