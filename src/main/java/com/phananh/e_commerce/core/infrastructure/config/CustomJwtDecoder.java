package com.phananh.e_commerce.core.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final JwtDecoder delegate;

    public CustomJwtDecoder(@Value("${application.security.jwt.secret-key}") String secretKey) {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.delegate = NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        return delegate.decode(token);
    }
}
