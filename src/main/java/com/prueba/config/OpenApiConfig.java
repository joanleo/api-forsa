package com.prueba.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;


/*package com.prueba.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;


public class SpringFoxConfig {
	
	private ApiKey apiKey() { 
	    return new ApiKey("JWT", "Authorization", "header"); 
	}
	
	private SecurityContext securityContext() { 
	    return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	} 

	private List<SecurityReference> defaultAuth() { 
	    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
	    authorizationScopes[0] = authorizationScope; 
	    return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
	}
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
        	.apiInfo(apiInfo())
        	.securityContexts(Arrays.asList(securityContext()))
        	.securitySchemes(Arrays.asList(apiKey()))
        	.select()                                  
        	.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))              
        	.paths(PathSelectors.any())
          	.build();                                           
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
              .title("API Aplicacion Forza")
              .description("Esta es la documentacion para el uso de la API de acceso para la aplicacion **")
              .version("1.0.0")
              .build();
    }
}*/
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI(@Value("1.6.6") String appVersion) {
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title("Prueba api forsa").version(appVersion)
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
}
