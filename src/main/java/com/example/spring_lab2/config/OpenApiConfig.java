package com.example.spring_lab2.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API управління валютою")
                        .description("Документація RESTful веб-сервісів для управління валютою")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .addServersItem(new Server().url("http://localhost:8080").description("Локальний сервер"))
                .externalDocs(new ExternalDocumentation()
                        .description("Додаткові ресурси")
                        .url("https://swagger.io/"))
                .addTagsItem(new Tag().name("Currency").description("Операції, пов'язані з валютами"))
                .components(new Components()
                        .addSchemas("CurrencyUpdateDTO", new Schema<>()
                                .type("object")
                                .addProperty("currencyName", new Schema<String>().type("string").description("Назва валюти"))
                                .addProperty("date", new Schema<String>().type("string").format("date").description("Дата курсу"))
                                .addProperty("initialRate", new Schema<Double>().type("number").format("double").description("Початковий курс"))
                                .required(Arrays.asList("currencyName", "date", "initialRate")))
                        .addResponses("200", new ApiResponse()
                                .description("Успішна операція")
                                .content(new Content().addMediaType("application/json", 
                                new MediaType().schema(new Schema<>().type("string").description("Повідомлення про успіх")))))
                        .addResponses("400", new ApiResponse()
                                .description("Некоректні дані"))
                        .addResponses("404", new ApiResponse()
                                .description("Валюта не знайдена"))
                        .addResponses("500", new ApiResponse()
                                .description("Внутрішня помилка сервера")))                
                    .tags(Arrays.asList(
                            new Tag().name("Currency").description("Операції, пов'язані з валютами"),
                            new Tag().name("Admin").description("Адміністративні операції")
                    ));
    }
}