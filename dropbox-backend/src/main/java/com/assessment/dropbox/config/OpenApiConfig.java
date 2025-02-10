package com.assessment.dropbox.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dropboxOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dropbox Clone API")
                        .description("REST API for a simplified Dropbox clone with file upload/download capabilities")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your.email@example.com")));
    }
}