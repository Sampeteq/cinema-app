package code.user.infrastrcuture;

import code.user.UserFacade;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@AllArgsConstructor
@Log4j2
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/films/**", "/screenings/**")
                .permitAll()
                .mvcMatchers(HttpMethod.POST, "/signup", "/signin")
                .permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2/console/**")
                .permitAll()
                .mvcMatchers(HttpMethod.POST, "/films", "/screenings-rooms", "/screenings")
                .hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable();
    }

    @Bean
    public UserDetailsService userDetailsService(UserFacade userFacade) {
        return username -> userFacade
                .readUserDetails(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
