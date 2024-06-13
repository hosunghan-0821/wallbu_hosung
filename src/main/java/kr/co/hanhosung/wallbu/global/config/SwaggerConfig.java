package kr.co.hanhosung.wallbu.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenApi() {

        SecurityRequirement securityItem = new SecurityRequirement()
                .addList("JWT");
        SecurityRequirement securityItem2 = new SecurityRequirement()
                .addList("Refresh-Token");


        return new OpenAPI()
                .info(getApiInfo())
                .components(getComponents())
                .addSecurityItem(securityItem)
                .addSecurityItem(securityItem2);
    }


    private Info getApiInfo() {
        return new Info()
                .title("Wallbu")
                .description("TEST API")
                .version("0.1.0");
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes("JWT", getJwtSecurityScheme())
                .addSecuritySchemes("Refresh-Token", getRefreshSecurityScheme());
    }

    private SecurityScheme getJwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);
    }

    private SecurityScheme getRefreshSecurityScheme() {

        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Refresh-token");
    }
}
