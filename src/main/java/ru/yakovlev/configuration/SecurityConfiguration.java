package ru.yakovlev.configuration;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Spring Security configuration.
 *
 * @author Yakovlev Alexander (sanyakovlev@yandex.ru)
 * @since 0.3.1
 */
@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder argon2Encoder() {
        return new DelegatingPasswordEncoder(
                "argon2", Map.of("argon2", new Argon2PasswordEncoder()));
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
        return http.build();
    }
}
