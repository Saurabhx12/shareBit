package com.example.FileSharingApp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
public class SecurityConfig
{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",                 // custom login page
                                "/download/**",      // use Ant patterns, not {id}
                                "/share/**",
                                "/styles/**",        // leading slash for static assets
                                "/oauth2/authorization/**", // allow OAuth2 initiation
                                "/login/oauth2/code/**"     // allow OAuth2 redirect/callback
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/") // custom login page
                        .successHandler(customSuccessHandler()) // pass a handler instance
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler()
    {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/files");
        return successHandler;
    }
}
