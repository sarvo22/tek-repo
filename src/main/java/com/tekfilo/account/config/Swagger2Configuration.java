package com.tekfilo.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Tekfilo Account Service")
                .description("Account service provides complete flow of Tekfilo Account management")
                .license("MIT")
                .licenseUrl("https://opensource.org/licenses/MIT")
                .version("1.0.0")
                .contact(new Contact("Tekfilo Innovations Pvt ltd", "www.tekfilo.com", "enquery@tekfilo.com"))
                .build();
    }

    @Bean
    public Docket docket() {

        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<Parameter>();

        parameters.clear();
        aParameterBuilder
                .name("X-TenantID")
                .modelRef(new ModelRef("string"))
                .parameterType("header").required(true).build();

        parameters.add(aParameterBuilder.build());
        aParameterBuilder.name("X-CompanyID").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
        parameters.add(aParameterBuilder.build());

        aParameterBuilder.name("X-UserID").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
        parameters.add(aParameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tekfilo.account"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameters)
                .enable(true)
                .apiInfo(apiInfo());
    }
}
