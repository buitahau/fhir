package owt.training.fhir.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import owt.training.fhir.security.JwtTokenProvider;

@Configuration
public class BeanConfiguration {

    @Bean
    public JwtTokenProvider tokenProvider(@Value("${jwt.secret}") String secret,
                                          @Value("${jwt.token.validity}") long tokenValidity) {
        return new JwtTokenProvider(secret, tokenValidity);
    }
}
