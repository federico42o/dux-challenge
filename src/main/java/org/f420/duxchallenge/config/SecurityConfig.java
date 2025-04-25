package org.f420.duxchallenge.config;

import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.config.filter.JwtTokenValidatorFilter;
import org.f420.duxchallenge.config.security.CustomAccessDeniedHandler;
import org.f420.duxchallenge.utils.JWTUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
public class SecurityConfig {

    private final JWTUtils jwtUtils;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JWTUtils jwtUtils, CustomAccessDeniedHandler accessDeniedHandler) {
        this.jwtUtils = jwtUtils;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("entra al security filter");
        log.info("http: {}", http.getSharedObjects());
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/teams/**").hasAnyAuthority("admin", "teams"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) ->
                        exceptions.accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(new JwtTokenValidatorFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() throws Exception {
            return new BCryptPasswordEncoder();
    }
}