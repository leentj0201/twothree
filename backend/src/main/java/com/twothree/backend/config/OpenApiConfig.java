package com.twothree.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("교회 관리 시스템 API")
                        .description("교회, 멤버, 부서를 관리하는 RESTful API 서비스")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TwoThree Team")
                                .email("admin@twothree.com")
                                .url("https://twothree.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("개발 서버"),
                        new Server()
                                .url("https://api.twothree.com")
                                .description("프로덕션 서버")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
                        .addSecuritySchemes("Basic Authentication", createBasicAuthScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("JWT 토큰을 입력하세요. 'Bearer ' 접두사는 자동으로 추가됩니다.");
    }

    private SecurityScheme createBasicAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic")
                .description("기본 인증 (사용자명:비밀번호)");
    }
} 