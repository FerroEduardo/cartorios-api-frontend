package com.ferroeduardo.cartorios_api_frontend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/**")
                    .permitAll()
                .antMatchers("/css/**")
                    .permitAll()
                .and()
            .cors()
                .disable()
            .csrf()
                .disable();
    }
}
