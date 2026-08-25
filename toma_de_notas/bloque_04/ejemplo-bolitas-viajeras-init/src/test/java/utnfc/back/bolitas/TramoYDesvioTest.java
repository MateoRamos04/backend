package utnfc.back.bolitas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TramoYDesvioTest {
    @Test
    @DisplayName("Un tramo normal está bien configurado aunque su salida sea null")
    void tramoFinalBienConfigurado() {
        assertTrue(new Tubo(10, "final").isBienConfigurado());
    }

    @Test
    @DisplayName("Los tramos se encastran por medio del tipo base Tramo")
    void encastrarSalida() {
        Tramo primero = new Tubo(10, "primero");
        Tramo segundo = new Acelerador("segundo");
        primero.encastrarSalida(segundo);
        assertSame(segundo, primero.getSalida());
    }

    @Test
    @DisplayName("Un desvío sólo está bien configurado con salida y alternativa")
    void configuracionDesvio() {
        Desvio desvio = new DesvioLisa("desvío");
        Tramo salida = new Tubo(10, "salida");
        Tramo alternativa = new Tubo(20, "alternativa");

        assertFalse(desvio.isBienConfigurado());
        desvio.encastrarSalida(salida);
        assertFalse(desvio.isBienConfigurado());
        desvio.setAlternativa(alternativa);
        assertTrue(desvio.isBienConfigurado());
        assertSame(alternativa, desvio.getAlternativa());
    }
}
