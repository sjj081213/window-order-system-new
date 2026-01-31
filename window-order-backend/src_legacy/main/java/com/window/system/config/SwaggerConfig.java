package com.window.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI windowOrderOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Window Order System API")
                        .description("API Documentation for Window Order System")
                        .version("v1.0"));
    }
}
