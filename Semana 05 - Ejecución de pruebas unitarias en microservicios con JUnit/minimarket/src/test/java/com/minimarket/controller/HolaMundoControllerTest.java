package com.minimarket.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HolaMundoControllerTest {

    @Test
    void holaMundo_debeRetornarMensaje() {
        HolaMundoController controller = new HolaMundoController();

        assertEquals("¡Hola Mundo!", controller.holaMundo());
    }
}
