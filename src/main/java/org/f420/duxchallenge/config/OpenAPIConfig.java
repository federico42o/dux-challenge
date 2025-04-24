package org.f420.duxchallenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Dux Challenge")
                        .description("Challenge técnico para Backend Developer")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Federico Silva")
                                .url("https://www.linkedin.com/in/federico42o/")
                                .email("fedes7777@gmail.com")));
    }
}
