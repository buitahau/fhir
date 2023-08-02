package owt.training.opa.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import owt.training.opa.config.security.opa.OPAAuthorizationManager;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

    @Autowired
    private OPAAuthorizationManager opaAuthorizationManager;

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsService = new InMemoryUserDetailsManager();

        var adminUser = User.withUsername("admin-user")
                .password(getPasswordEncoder().encode("password"))
                .authorities("all")
                .build();
        var readUser = User.withUsername("read-user")
                .password(getPasswordEncoder().encode("password"))
                .authorities("read", "write")
                .build();
        var writeUser = User.withUsername("write-user")
                .password(getPasswordEncoder().encode("password"))
                .authorities("write")
                .build();

        userDetailsService.createUser(adminUser);
        userDetailsService.createUser(readUser);
        userDetailsService.createUser(writeUser);

        return userDetailsService;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(EndpointRequest.to("health")).permitAll()
                                .requestMatchers("/v*/users").access(opaAuthorizationManager)
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
