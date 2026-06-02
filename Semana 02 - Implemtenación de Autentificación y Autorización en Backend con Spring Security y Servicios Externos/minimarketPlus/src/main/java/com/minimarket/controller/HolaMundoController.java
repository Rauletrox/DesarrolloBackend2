package com.minimarket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaMundoController {

    @GetMapping("/public/hola")
    public String holaMundo() {
        // Esto lo modifique porque se vé más profesional al autentificar un Login!
        return "Usuario Autentificado correctamente!!!!";
    }
}
