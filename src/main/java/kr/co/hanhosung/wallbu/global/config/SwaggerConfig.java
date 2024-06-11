package kr.co.hanhosung.wallbu.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiV2Docket() {

        ApiInfo apiInfo = apiInfo("WALLBU", "api", "0.1.0");

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName(apiInfo.getVersion())
                .securitySchemes(Arrays.asList(apikey()))
                .securityContexts(Arrays.asList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("kr.co.hanhosung.wallbu.controller"))
                .build();
    }

    private ApiInfo apiInfo(String title, String description, String version) {
        return new ApiInfo(
                title,
                description,
                "version." + version,
                "",
                ApiInfo.DEFAULT_CONTACT,
                "licence",
                "hr.nexmoa.com",
                new ArrayList<>()
        );
    }

    // JWT SecurityContext 구성
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }
    private ApiKey apikey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

}
