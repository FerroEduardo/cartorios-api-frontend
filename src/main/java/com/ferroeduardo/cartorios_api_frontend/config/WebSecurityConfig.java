package com.ferroeduardo.cartorios_api_frontend.config;

import com.ferroeduardo.cartorios_api_frontend.service.ApiUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private ApiUserDetailsService userDetailsService;

    public WebSecurityConfig(ApiUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/signin/**", "/signup/**", "/css/**")
                    .permitAll()
                .antMatchers("/api/access")
                    .hasRole("USER")
                .and()
            .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/signin")
                .failureHandler(loginFailureHandler())
                .usernameParameter("inputUsername").passwordParameter("inputPassword")
                .defaultSuccessUrl("/api/access")
                .permitAll()
                .and()
            .cors()
                .disable()
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
            .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler(){
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
                    response.sendRedirect("/signin?credentials");
                } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
                    response.sendRedirect("/signin?disabled");
                } else {
                    response.sendRedirect("/signin?error");
                }
            }
        };
        return failureHandler;
    }

}
