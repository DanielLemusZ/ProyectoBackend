package co.edu.autonoma.reservasapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService usuarios(PasswordEncoder encoder) {
        UserDetails voluntario = User.withUsername("voluntario")
                .password(encoder.encode("voluntario123."))
                .roles("USUARIO")
                .build();
        UserDetails coordinador = User.withUsername("coordinador")
                .password(encoder.encode("coordinador123."))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(voluntario, coordinador);
    }

    @Bean
    public SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/estado", "/api/actividades",
                                "/api/actividades/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/voluntarios", "/api/voluntarios/{id}",
                                "/api/voluntarios/documento/{documento}", "/api/voluntarios/{id}/inscripciones",
                                "/api/voluntarios/{id}/asistencias", "/api/coordinadores", "/api/coordinadores/{id}",
                                "/api/coordinadores/email/{email}", "/api/organizaciones/{id}/coordinadores",
                                "/api/actividades/{id}/inscripciones").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/organizaciones/{id}", "/api/coordinadores/{id}",
                                "/api/voluntarios/{id}", "/api/actividades/{id}", "/api/inscripciones/{id}",
                                "/api/asistencias/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/organizaciones", "/api/coordinadores",
                                "/api/actividades").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/organizaciones/{id}", "/api/coordinadores/{id}",
                                "/api/actividades/{id}", "/api/asistencias/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/actividades/{id}/estado",
                                "/api/inscripciones/{id}/asistencia").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}

