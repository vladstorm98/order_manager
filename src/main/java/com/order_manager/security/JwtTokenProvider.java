package com.order_manager.security;

import com.order_manager.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;

    public String createToken(String username) {
        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("order-manager")
                .subject(username)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtConfig.getExpiration()))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims))
                .getTokenValue();
    }

    public String extractUsername(String token) {
        return jwtDecoder.decode(token)
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return !Objects.requireNonNull(jwt.getExpiresAt()).isBefore(Instant.now());
        } catch (Exception e) {
            return false;
        }
    }
}

