package com.cafetito.config;

import com.cafetito.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Públicos
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/estado").permitAll()

                        // 2. Panel del Beneficio (Endpoints compartidos con prefijos específicos)
                        // Usamos hasAnyAuthority para evitar conflictos con el prefijo "ROLE_"
                        .requestMatchers("/api/transportes/filtrar", "/api/transportes/buscar", "/api/transportes/todos").hasAnyAuthority("BENEFICIO", "ROLE_BENEFICIO")
                        .requestMatchers("/api/transportistas/filtrar", "/api/transportistas/buscar", "/api/transportistas/todos").hasAnyAuthority("BENEFICIO", "ROLE_BENEFICIO")

                        // 3. Rutas exclusivas de administración
                        .requestMatchers("/api/admin/**").hasAnyAuthority("BENEFICIO", "ROLE_BENEFICIO")

                        // 4. Rutas exclusivas de Agricultor
                        .requestMatchers("/api/pesajes", "/api/pesajes/**").hasAnyAuthority("AGRICULTOR", "ROLE_AGRICULTOR")
                        .requestMatchers("/api/parcialidades", "/api/parcialidades/**").hasAnyAuthority("AGRICULTOR", "ROLE_AGRICULTOR")
                        .requestMatchers("/api/transportes/**").hasAnyAuthority("AGRICULTOR", "ROLE_AGRICULTOR")
                        .requestMatchers("/api/transportistas/**").hasAnyAuthority("AGRICULTOR", "ROLE_AGRICULTOR")
                        .requestMatchers("/api/transportes/disponibles").hasRole("AGRICULTOR")
                        .requestMatchers("/api/transportistas/disponibles").hasRole("AGRICULTOR")

                        // 5. General / Autenticado
                        .requestMatchers("/api/catalogos/**").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Es mejor permitir el origen de tu frontend específicamente
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}