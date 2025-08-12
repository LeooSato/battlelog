package com.leoosato.project.services;
import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordService {
    public String hash(String raw){ return BCrypt.hashpw(raw, BCrypt.gensalt(12)); }
    public boolean matches(String raw, String hash){ return BCrypt.checkpw(raw, hash); }
}
