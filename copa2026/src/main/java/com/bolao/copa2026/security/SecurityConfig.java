package com.bolao.copa2026.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()

                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()

                        .requestMatchers(HttpMethod.PUT, "/usuarios/me").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/me/senha").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/usuarios/**").hasRole("ADMIN")

                        .requestMatchers("/estadios/**").hasRole("ADMIN")
                        .requestMatchers("/selecoes/**").hasRole("ADMIN")
                        .requestMatchers("/configuracao-pontuacao/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/jogos/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/jogos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/jogos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/jogos/**").hasRole("ADMIN")

                        .requestMatchers("/boloes/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/palpites/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/ranking/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/classificacao/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/mata-mata/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "https://bolao-copa-2026-frontend.vercel.app"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}