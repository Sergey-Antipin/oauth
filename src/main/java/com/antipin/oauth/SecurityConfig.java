package com.antipin.oauth;

import com.antipin.oauth.controller.OAuth2LogoutHandler;
import com.antipin.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html").permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .oauth2Login(login -> login.userInfoEndpoint(endpoint -> endpoint.userService(userService))
                        .successHandler(loggingAuthenticationSuccessHandler())
                        .failureHandler(loggingAuthenticationFailureHandler()))
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll()
                        .addLogoutHandler(oAuth2LogoutHandler())
                        .logoutSuccessHandler(loggingLogoutSuccessHandler()))
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OAuth2LogoutHandler oAuth2LogoutHandler() {
        return new OAuth2LogoutHandler(oAuth2AuthorizedClientService, restTemplate());
    }

    @Bean
    public AuthenticationSuccessHandler loggingAuthenticationSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler loggingAuthenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler loggingLogoutSuccessHandler() {
        return new SimpleUrlLogoutSuccessHandler();
    }

}
