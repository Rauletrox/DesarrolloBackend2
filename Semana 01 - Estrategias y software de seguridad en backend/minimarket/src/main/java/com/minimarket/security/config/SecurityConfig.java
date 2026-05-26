package com.minimarket.security.config;

import com.minimarket.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Cambio: CSRF se deshabilita porque la actividad protege endpoints REST con usuario/contrasena.
                .csrf(csrf -> csrf.disable())
                // Cambio: permite visualizar la consola H2 durante las pruebas locales.
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                // Cambio: mantiene sesiones cuando se usa formLogin, sin obligar estado para todas las APIs.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // Cambio: endpoints publicos, login REST y H2 quedan disponibles sin autenticacion.
                        .requestMatchers("/public/**", "/api/auth/**", "/h2-console/**").permitAll()
                        // Cambio: clientes, empleados y gerentes pueden consultar catalogo.
                        .requestMatchers(HttpMethod.GET, "/api/productos/**", "/api/categorias/**")
                        .hasAnyRole("CLIENTE", "EMPLEADO", "GERENTE")
                        // Cambio: el carrito queda disponible para usuarios autenticados del negocio.
                        .requestMatchers("/api/carrito/**").hasAnyRole("CLIENTE", "EMPLEADO", "GERENTE")
                        // Cambio: ventas y detalle de ventas se restringen a empleados y gerentes.
                        .requestMatchers("/api/ventas/**", "/api/detalle-ventas/**")
                        .hasAnyRole("EMPLEADO", "GERENTE")
                        // Cambio: inventario se restringe a personal interno.
                        .requestMatchers("/api/inventario/**").hasAnyRole("EMPLEADO", "GERENTE")
                        // Cambio: la administracion de usuarios queda reservada a gerentes.
                        .requestMatchers("/api/usuarios/**").hasRole("GERENTE")
                        // Cambio: cualquier otro endpoint requiere autenticacion.
                        .anyRequest().authenticated()
                )
                // Cambio: httpBasic facilita probar usuario/contrasena desde Postman o curl.
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        // Cambio: se mantiene login por formulario para pruebas desde navegador.
                        .defaultSuccessUrl("/public/hola", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/public/hola")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // proveedor DAO que valida usuarios desde la base de datos usando BCrypt.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt protege las contraseñas almacenadas contra lectura directa.
        return new BCryptPasswordEncoder();
    }
}
