package com.service.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@Slf4j
public class SecurityConfiguration {

    @Autowired
    AuthorizationHeaderRequestFilter authorizationHeaderRequestFilter;

    /**
     * Inserts bcrypt password encoder into spring application context
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Main security filter chain for market spark. This is where you should configure permissions.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Entering security filter chain:");
        //configure session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //disable csrf; normally this isn't a great idea, but since we have a mobile application, it's ok.
        http.csrf(csrf -> csrf.disable());

        //configure cors
        http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(Collections.singletonList("*")); //allow all
                config.setAllowedMethods(Collections.singletonList("*")); //allow all http types
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(Arrays.asList("Authorization")); //expose auth header
                config.setMaxAge(3600L);
                return config;
            }
        }));

        //filters: JWT Validation
        http.addFilterBefore(authorizationHeaderRequestFilter, UsernamePasswordAuthenticationFilter.class);

        //routes (WHERE YOU IDENTIFY WHAT ROLES & AUTHENTICATION CAN ACCESS WHAT)
        http.authorizeHttpRequests(auth -> auth
                //SECURITY ENDPOINTS
                .requestMatchers(HttpMethod.GET, "/security/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/security/register").permitAll()
                // // TEST (TO BE REMOVED)
                .requestMatchers("/test/**").hasAnyRole("USER")
                // .requestMatchers(HttpMethod.GET, "/test").hasAnyRole("USER")
                // // PRODUCT ENDPOINTS
                // .requestMatchers( "/product/**").hasAnyRole("USER")
                // // GENERAL CONVERSATION HTTP ENDPOINTS
                // .requestMatchers("/conversation/**").hasAnyRole("USER")

        );

        return http.build();
    }

}
