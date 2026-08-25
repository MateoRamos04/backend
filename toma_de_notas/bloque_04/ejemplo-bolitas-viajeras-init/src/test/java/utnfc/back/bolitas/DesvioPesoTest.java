package utnfc.back.bolitas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DesvioPesoTest {
    private final Tramo salida = new Tubo(10, "salida");
    private final Tramo alternativa = new Tubo(20, "alternativa");

    private DesvioPeso configurado() {
        DesvioPeso desvio = new DesvioPeso(25, "peso");
        desvio.encastrarSalida(salida);
        desvio.setAlternativa(alternativa);
        return desvio;
    }

    @Test @DisplayName("Un peso menor al límite toma la salida normal")
    void menor() { assertSame(salida, configurado().avanzar(new Bolita(true, 24, 1))); }

    @Test @DisplayName("Un peso igual al límite toma la salida normal")
    void igual() { assertSame(salida, configurado().avanzar(new Bolita(true, 25, 1))); }

    @Test @DisplayName("Un peso mayor al límite toma la alternativa y registra el desvío")
    void mayor() {
        DesvioPeso desvio = configurado();
        Bolita bolita = new Bolita(true, 26, 1);
        assertSame(alternativa, desvio.avanzar(bolita));
        assertSame(desvio, bolita.getTramoActual());
    }

    @Test @DisplayName("El límite de peso puede consultarse y modificarse")
    void limite() {
        DesvioPeso desvio = configurado();
        assertEquals(25, desvio.getPeso());
        desvio.setPeso(30);
        assertEquals(30, desvio.getPeso());
    }

    @Test @DisplayName("Un desvío de peso incompleto permanece en sí mismo")
    void incompleto() {
        DesvioPeso desvio = new DesvioPeso(25, "incompleto");
        assertFalse(desvio.isBienConfigurado());
        assertSame(desvio, desvio.avanzar(new Bolita(true, 30, 1)));
    }
}
