package com.twothree.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TwoThree Church API")
                        .description("교회, 부서, 회원, 게시물 관리 API 문서")
                        .version("v1.0.0")
                );
    }
} 