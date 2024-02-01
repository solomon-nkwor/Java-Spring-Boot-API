package com.springbootlearning.backenddev.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition(info = @Info(
        title = "User Registration Application",
        version = "1.0",
        description = "Documentation for User Registration API Application",
        license = @License(name = "apache 2.0", url = "http://foo.bar"),
        contact = @Contact(url = "http://www.solomon-nkwor.com", name = "Solomon", email = "hello@solomon-nkwor.com")
))
public class ApiDocConfig {
}
