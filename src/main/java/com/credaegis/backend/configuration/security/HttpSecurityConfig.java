package com.credaegis.backend.configuration.security;

import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {


    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{

        http.formLogin(customizer->customizer.disable());
        http.httpBasic(customizer->customizer.disable());
        http.csrf(customizer->customizer.disable());

        http.
                authorizeHttpRequests(request->request.requestMatchers
                                (Constants.ROUTEV1+"/auth/**",Constants.ROUTEV1+"/test/**").
                permitAll().requestMatchers(Constants.ROUTEV1+"/admin/**").hasAuthority("ADMIN").
                        anyRequest().authenticated()).
                        logout((logout)->logout.logoutUrl(Constants.ROUTEV1+"/auth/logout").
                                logoutSuccessHandler(customLogoutSuccessHandler));


        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }




    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
