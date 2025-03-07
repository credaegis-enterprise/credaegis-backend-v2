package com.credaegis.backend.configuration.security;

import com.credaegis.backend.configuration.session.CustomInvalidSessionStrategy;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.configuration.security.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class HttpSecurityConfig {


    private final CustomUserDetailsService customUserDetailsService;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomInvalidSessionStrategy customInvalidSessionStrategy;


    @Bean
    @Order(1)
    public SecurityFilterChain configureOrganizationSecurity(HttpSecurity http) throws Exception {

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.securityMatcher(Constants.ROUTE_V1_ORGANIZATION + "/**");

        http.authorizeHttpRequests(request -> request.requestMatchers
                                (Constants.ROUTE_V1_ORGANIZATION + "/auth/**", Constants.ROUTE_V1_ORGANIZATION + "/test/**", Constants.ROUTE_V1_ORGANIZATION + "/external/**").
                        permitAll().requestMatchers(Constants.ROUTE_V1_ORGANIZATION + "/**").hasRole(Constants.ADMIN).
                        anyRequest().authenticated()).
                logout((logout) ->
                        logout.logoutUrl(Constants.ROUTE_V1_ORGANIZATION + "/auth/logout").
                                logoutSuccessHandler(customLogoutSuccessHandler))
                .exceptionHandling(handler -> handler.
                        authenticationEntryPoint(customAuthenticationEntryPoint));

//        http.sessionManagement(session->session.invalidSessionStrategy(customInvalidSessionStrategy));

        return http.build();
    }


    @Order(2)
    @Bean
    public SecurityFilterChain configureMemberSecurity(HttpSecurity http) throws Exception {

        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.securityMatcher(Constants.ROUTE_V1_MEMBER + "/**");

        http.authorizeHttpRequests(request -> request.requestMatchers
                                (Constants.ROUTE_V1_MEMBER + "/auth/**", Constants.ROUTE_V1_MEMBER + "/test/**", Constants.ROUTE_V1_MEMBER + "/external/**").
                        permitAll().
                        requestMatchers(
                                Constants.ROUTE_V1_MEMBER + "/approval-control/**",
                                Constants.ROUTE_V1_MEMBER + "/account/**",
                                Constants.ROUTE_V1_MEMBER + "/event-control/**").
                        hasAnyRole(Constants.CLUSTER_ADMIN, Constants.MEMBER).
                        requestMatchers(Constants.ROUTE_V1_MEMBER + "/**").hasRole(Constants.CLUSTER_ADMIN)
                        .anyRequest().authenticated()).
                logout((logout) ->
                        logout.logoutUrl(Constants.ROUTE_V1_MEMBER + "/auth/logout").
                                logoutSuccessHandler(customLogoutSuccessHandler))
                .exceptionHandling(handler -> handler.
                        authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
