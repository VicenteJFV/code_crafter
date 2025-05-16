package com.perfulandia.service.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    private final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public String getSecret() {
        return dotenv.get("JWT_SECRET");
    }

    public long getExpiration() {
        return Long.parseLong(dotenv.get("JWT_EXPIRATION", "3600000"));
    }
}
