package com.dcap;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Bean
    public Docket apiUserDocket() {

        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new BasicAuth("basicAuth"));


        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dcap.restService.admin"))
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .useDefaultResponseMessages(false)
                .securitySchemes(schemeList)
                //.securitySchemes(Arrays.asList(securityScheme()))
                //.securityContexts(Arrays.asList(securityContext()))
                .apiInfo(getApiInfoAdmin());

    }

    @Bean
    public Docket apiAdminDocket() {


        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("user")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dcap.restService.user"))
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .useDefaultResponseMessages(false)
                //.securitySchemes(Arrays.asList(securityScheme()))
                //.securityContexts(Arrays.asList(securityContext()))
                .apiInfo(getApiInfoUser());

    }

    private ApiInfo getApiInfoUser() {
        return new ApiInfo(
                "New Cheetah-Web",
                "This is the description of the api for the roles \"user\".",
                "1.0.0",
                "TERMS OF SERVICE URL",
                new Contact("Uli", "http://uibk.ac.at", "ulrich.lobis@uibk.ac.at"),
                "To think about",
                "LICENSE URL",
                Collections.emptyList()
        );
    }

    private ApiInfo getApiInfoAdmin() {
        return new ApiInfo(
                "New Cheetah-Web",
                "This is the description of the api for the roles \"user\" and \"admin\".",
                "1.0.0",
                "TERMS OF SERVICE URL",
                new Contact("Uli", "http://uibk.ac.at", "ulrich.lobis@uibk.ac.at"),
                "To think about",
                "LICENSE URL",
                Collections.emptyList()
        );
    }
}