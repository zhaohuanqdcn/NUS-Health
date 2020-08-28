package com.orbidroid.orbidroid_backend.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "NUS Health portal for admin",
                "This site assists admin staff to manage the reservation system and also provides the API format for the backend. " +
                        "For admin staff and developers, please refer to the " +
                        "<a href=\"https://docs.google.com/document/d/1hmv0W-5rbM8IUqGwLG9437LapqaViyxvI5_STkfYe7c/edit?usp=sharing\">portal handbook</a> " +
                        "or refer to the " +
                        "<a href=https://docs.google.com/document/d/1iIbiapFN6RAjPKxxNcAZPN4N1xmPW9oRm7FlTIgp-F0/edit>backend documentation</a> for help. " +
                        "If you want to try out our testing product, please download the apk " +
                        "<a href=\"https://drive.google.com/file/d/1oryVnUCIeQO6q5iYhB1_xGMvGDODhSkY/view?usp=sharing\">here</a>. " +
                        "Some possible caveats are listed " +
                        "<a href=\"https://docs.google.com/document/d/1TDmKJKx5UJkuq5fW4cpWl6Q_s7YYmwJ1vF7ZTgdtd1M/edit?usp=sharing\">here</a>." +
                        "Thanks a lot!",
                "API V2.0",
                "Terms of service",
                new Contact("Orbidroid", "https://nusskylab-dev.comp.nus.edu.sg/public_views/public_projects", "E0493630@u.nus.edu"),
                "Apache", "http://www.apache.org/", Collections.emptyList());
    }
}
