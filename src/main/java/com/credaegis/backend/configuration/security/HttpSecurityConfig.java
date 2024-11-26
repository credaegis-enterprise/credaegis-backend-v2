package com.credaegis.backend.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{

        http.formLogin(customizer->customizer.disable());
        http.httpBasic(customizer->customizer.disable());
        http.csrf(customizer->customizer.disable());

        http.authorizeHttpRequests(request->request.anyRequest().permitAll());
        return http.build();

    }




    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
