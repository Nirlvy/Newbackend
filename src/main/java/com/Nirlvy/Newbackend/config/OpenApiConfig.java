package com.Nirlvy.Newbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Newbackend").version("0.0.1"))
                .addServersItem(new Server().url("http://8.130.47.235:8080"));
    }

    @Bean
    public GroupedOpenApi NewbackendApi() {
        return GroupedOpenApi.builder()
                .group("Newbackend")
                .packagesToScan("com.Nirlvy.Newbackend.controller")
                .build();
    }

}
