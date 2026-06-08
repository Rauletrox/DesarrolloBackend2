package com.minimarket.security.config;

import com.minimarket.security.filter.JwtAuthenticationFilter;
import com.minimarket.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Cambio: CSRF se deshabilita porque la actividad protege endpoints REST con usuario/contrasena.
                .csrf(csrf -> csrf.disable())
                // Cambio: permite visualizar la consola H2 durante las pruebas locales.
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                // Cambio: JWT permite operar la API sin guardar sesiones en el servidor.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Cambio: endpoints publicos, login REST y H2 quedan disponibles sin autenticacion.
                        .requestMatchers("/public/**", "/api/auth/**", "/h2-console/**").permitAll()
                        // Cambio: clientes, empleados y administradores pueden consultar catalogo.
                        .requestMatchers(HttpMethod.GET, "/api/productos/**", "/api/categorias/**")
                        .hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")
                        // Cambio: el carrito queda disponible para usuarios autenticados del negocio.
                        .requestMatchers("/api/carrito/**").hasAnyRole("CLIENTE", "EMPLEADO", "ADMINISTRADOR")
                        // Cambio: ventas y detalle de ventas se restringen a empleados y administradores.
                        .requestMatchers("/api/ventas/**", "/api/detalle-ventas/**")
                        .hasAnyRole("EMPLEADO", "ADMINISTRADOR")
                        // Cambio: inventario se restringe a personal interno.
                        .requestMatchers("/api/inventario/**").hasAnyRole("EMPLEADO", "ADMINISTRADOR")
                        // Cambio: la administracion de usuarios queda reservada a administradores.
                        .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")
                        // Cambio: cualquier otro endpoint requiere autenticacion.
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Cambio: se deshabilitan mecanismos con estado o credenciales por request.
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());
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
