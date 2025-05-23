package com.perfulandia.service.user.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    private final Dotenv dotenv = Dotenv.configure()
            .directory("C:/Users/vifar/OneDrive/Documentos/GitHub/code_crafter/service") // <- asegúrate que apunta a la
                                                                                         // carpeta raíz donde está el
                                                                                         // .env
            .ignoreIfMissing()
            .load();

    public String getSecret() {
        System.out.println("JWT_SECRET (desde dotenv): " + dotenv.get("JWT_SECRET"));
        return dotenv.get("JWT_SECRET");
    }

    public long getExpiration() {
        return Long.parseLong(dotenv.get("JWT_EXPIRATION", "3600000"));
    }
}
