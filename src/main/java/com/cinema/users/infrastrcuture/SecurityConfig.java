package com.cinema.users.infrastrcuture;

import com.cinema.users.domain.UserRepository;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@SecurityScheme(
        name = "basic",
        scheme = "basic",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        configurer -> configurer
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/films/**",
                                        "/screenings/**",
                                        "/seats/**"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/sign-up"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/password/reset",
                                        "/password/new"
                                ).permitAll()
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/films",
                                        "/screenings"
                                ).hasAuthority("ADMIN")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/films/{title}"
                                ).hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        )
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository
                .readyByMail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
