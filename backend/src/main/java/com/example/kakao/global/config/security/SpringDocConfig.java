package com.example.kakao.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocConfig {

    @Bean
    OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("My API")
                        .description("API Documentation")
                        .version("v0.0.1"));
    }
    
}

