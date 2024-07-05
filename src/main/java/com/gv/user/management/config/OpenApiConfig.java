package com.gv.user.management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(
        info =
                @Info(
                        contact = @Contact(name = "Giorgi Odishvili", email = "odishvili.giorgi@gmail.com"),
                        description = "OpenApi documentation for User Management",
                        title = "OpenApi specification - User Management",
                        version = "1.0",
                        license = @License(name = "Licence name", url = "https://some-url.com"),
                        termsOfService = "Terms of service"),
        security = {@SecurityRequirement(name = "bearerAuth")})
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
@RequiredArgsConstructor
public class OpenApiConfig {}
