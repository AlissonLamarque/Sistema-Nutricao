package com.example.sistemanutricao.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.sistemanutricao.model.Usuario;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationSuccessHandler successHandler;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(
            AuthenticationSuccessHandler successHandler,
            UserDetailsService userDetailsService
    ) {
        this.successHandler = successHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/nutricionista/**").hasAnyRole("NUTRICIONISTA")
                        .requestMatchers("/producao/**").hasAnyRole("PRODUCAO")
                        .requestMatchers("/perfil", "/editar").hasAnyRole("ADMIN", "NUTRICIONISTA", "PRODUCAO")
                        .anyRequest().permitAll()
                )

                .formLogin(form -> form
                        .loginPage("/")
                        .successHandler(successHandler)
                        .permitAll()
                )

                .rememberMe(remember -> remember
                        .key("sistemaNutricaoRememberMeKey")
                        .tokenValiditySeconds(60 * 60 * 24 * 30) // 30 dias
                        .rememberMeParameter("remember-me")
                        .userDetailsService(userDetailsService)
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .deleteCookies("remember-me")
                        .permitAll()
                )

                .exceptionHandling(handling -> handling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                                    if (!usuario.isAtivo()) {
                                        response.sendRedirect("/conta-pendente");
                                    } else {
                                        response.sendRedirect("/acesso-negado");
                                    }
                                }
                        ));

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}

