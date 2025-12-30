package com.hibuz.kafka.producer.examples.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Spring Kafka Producer", version = "0.0.1"
, description = "Spring Kafka Producer API 명세서")
, externalDocs = @ExternalDocumentation(description = "Quick Tour", url = "https://docs.spring.io/spring-kafka/reference/quick-tour.html"))
public class SpringdocConfig {
    @Bean
    public OpenAPI openAPI(@Value("#{servletContext.contextPath}") String contextPath) {
        return new OpenAPI().addServersItem(new Server().url(contextPath));
    }
}
