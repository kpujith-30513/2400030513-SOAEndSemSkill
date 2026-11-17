package soa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tourist Service API")
                        .version("1.0.0")
                        .description(
                            "SOA Lab - Tourist Records API with Token Validation. " +
                            "Use the Token header with value 'MY_VALID_TOKEN' to authenticate."
                        )
                        .contact(new Contact()
                                .name("SOA Lab")
                                .email("lab@soa.edu")));
    }
}
