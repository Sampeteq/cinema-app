package code.auth;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
@Log4j2
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String MAIN_ADMIN_USERNAME = "MainAdmin";
    private static final String MAIN_ADMIN_PASSWORD = "12345";

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/films/**", "/screenings/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/screenings-tickets").permitAll()
                .mvcMatchers(HttpMethod.PATCH, "/screenings-tickets/{ticketId}/cancel").permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2/console/**").permitAll()
                .anyRequest().hasAuthority("ROLE_ADMIN")
                .and()
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        var configurer = auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);

        var userDetailsService = configurer.getUserDetailsService();

        if (userDetailsService.userExists(MAIN_ADMIN_USERNAME)) {
            log.info("Main admin already exists.Username: {}.Password: {}",
                    MAIN_ADMIN_USERNAME,
                    MAIN_ADMIN_PASSWORD);
        } else {
            configurer
                    .withUser(MAIN_ADMIN_USERNAME)
                    .password(passwordEncoder.encode(MAIN_ADMIN_PASSWORD))
                    .authorities("ROLE_ADMIN");
            log.info("Main admin added.Username: {}.Password: {}",
                    MAIN_ADMIN_USERNAME,
                    MAIN_ADMIN_PASSWORD);
        }
    }
}
