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

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Currency Management API")
                        .description("RESTful web service documentation for currency management")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .addServersItem(new Server().url("http://localhost:8080").description("Local Server"))
                .externalDocs(new ExternalDocumentation()
                        .description("Additional Resources")
                        .url("https://swagger.io/"))
                .addTagsItem(new Tag().name("Currency").description("Operations related to currencies"))
                .components(new Components()
                        .addParameters("currencyName", new Parameter()
                                .name("currencyName")
                                .description("Name of the currency")
                                .required(true)
                                .in("path")
                                .schema(new Schema<String>().type("string")))
                        .addResponses("200", new ApiResponse()
                                .description("Successful operation")
                                .content(new Content().addMediaType("application/json", new MediaType().schema(new Schema<>().type("object")))))
                        .addResponses("201", new ApiResponse()
                                .description("Resource created"))
                        .addResponses("400", new ApiResponse()
                                .description("Invalid input"))
                        .addResponses("404", new ApiResponse()
                                .description("Resource not found"))
                        .addResponses("500", new ApiResponse()
                                .description("Internal server error")));
    }
}