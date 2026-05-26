package com.minimarket.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

@Service
public class SecurityAuditService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAuditService.class);

    @EventListener
    public void registrarLoginExitoso(AuthenticationSuccessEvent event) {
        // registra accesos exitosos para monitorear actividad de autenticacion.
        logger.info("Login exitoso para usuario: {}", event.getAuthentication().getName());
    }

    @EventListener
    public void registrarLoginFallido(AbstractAuthenticationFailureEvent event) {
        // registra intentos fallidos como actividad sospechosa para revision posterior.
        logger.warn("Intento de login fallido para usuario: {}", event.getAuthentication().getName());
    }
}
