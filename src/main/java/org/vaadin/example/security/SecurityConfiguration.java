package org.vaadin.example.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.vaadin.example.ui.views.login.LoginView;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .requestCache(cache -> cache.requestCache(new CustomRequestCache()))
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers(SecurityUtils::isFrameworkInternalRequest)
                                        .permitAll()
                                        .anyRequest().authenticated())
                .formLogin(
                        login ->
                                login.loginPage(LOGIN_URL)
                                        .permitAll()
                                        .loginProcessingUrl(LOGIN_PROCESSING_URL)
                                        .failureUrl(LOGIN_FAILURE_URL))
                .logout(
                        logout ->
                                logout
                                        .logoutSuccessUrl(LOGOUT_SUCCESS_URL))
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(
                // Client-side JS
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "sw-runtime-resources-precache.js",
                "/offline.html",

                "/frontend/**",
                "/frontend-es5/**", "/frontend-es6/**",

                // icons and images
                "/icons/**",
                "/images/**",
                "/styles/**",

                // (development mode) H2 debugging console
                "/h2-console/**"
        );
    }

    @Bean
    public UserDetailsManager userDetailsService() {
        UserDetails user =
                User.withUsername("user").password("{noop}user").roles("USER").build();
        UserDetails admin =
                User.withUsername("admin").password("{noop}admin").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
