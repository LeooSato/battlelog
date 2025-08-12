package com.leoosato.project.services;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class TokenService {
    public String generate(String subject, String role){
        return Jwt.issuer("battlelog")
                .upn(subject).subject(subject)
                .groups(Set.of(role))
                .expiresIn(Duration.ofHours(8))
                .sign();
    }
}