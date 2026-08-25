package utnfc.back.bolitas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DesvioLisaTest {
    private final Tramo salida = new Tubo(10, "salida");
    private final Tramo alternativa = new Tubo(20, "alternativa");

    private DesvioLisa configurado() {
        DesvioLisa desvio = new DesvioLisa("desvío");
        desvio.encastrarSalida(salida);
        desvio.setAlternativa(alternativa);
        return desvio;
    }

    @Test
    @DisplayName("Una bolita lisa toma la salida normal")
    void lisa() {
        DesvioLisa desvio = configurado();
        Bolita bolita = new Bolita(true, 20, 1);
        assertSame(salida, desvio.avanzar(bolita));
        assertSame(desvio, bolita.getTramoActual());
    }

    @Test
    @DisplayName("Una bolita texturada toma la alternativa")
    void texturada() {
        assertSame(alternativa, configurado().avanzar(new Bolita(false, 20, 1)));
    }

    @Test
    @DisplayName("Un desvío sin ambas salidas no está configurado y permanece en sí mismo")
    void incompleto() {
        DesvioLisa desvio = new DesvioLisa("incompleto");
        desvio.encastrarSalida(salida);
        assertFalse(desvio.isBienConfigurado());
        assertSame(desvio, desvio.avanzar(new Bolita(true, 20, 1)));
    }
}
