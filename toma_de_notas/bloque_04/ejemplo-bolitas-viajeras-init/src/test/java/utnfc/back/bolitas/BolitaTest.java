package utnfc.back.bolitas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BolitaTest {
    @Test
    @DisplayName("Una bolita conserva sus características y comienza sin recorrido")
    void estadoInicial() {
        Bolita bolita = new Bolita(true, 20, 2.5);

        assertTrue(bolita.isLisa());
        assertEquals(20, bolita.getPeso());
        assertEquals(2.5, bolita.getVelocidad());
        assertEquals(0, bolita.getLongitudRecorrida());
        assertEquals(0, bolita.getTiempoTranscurrido());
        assertNull(bolita.getTramoActual());
    }

    @Test
    @DisplayName("Una bolita texturada informa que no es lisa")
    void bolitaTexturada() {
        assertFalse(new Bolita(false, 20, 1).isLisa());
    }

    @Test
    @DisplayName("Parar acumula el tiempo indicado")
    void parar() {
        Bolita bolita = new Bolita(true, 20, 1);
        bolita.parar(4);
        bolita.parar(6);
        assertEquals(10, bolita.getTiempoTranscurrido());
    }

    @Test
    @DisplayName("Acelerar modifica la velocidad y admite una desaceleración")
    void acelerar() {
        Bolita bolita = new Bolita(true, 20, 5);
        bolita.acelerar(3);
        bolita.acelerar(-2);
        assertEquals(6, bolita.getVelocidad());
    }

    @Test
    @DisplayName("Recorrer convierte centímetros a metros para calcular el tiempo")
    void recorrer() {
        Bolita bolita = new Bolita(true, 20, 2);
        bolita.recorrer(250);
        assertEquals(250, bolita.getLongitudRecorrida());
        assertEquals(1.25, bolita.getTiempoTranscurrido());
    }

    @Test
    @DisplayName("Varios recorridos acumulan longitud y tiempo")
    void variosRecorridos() {
        Bolita bolita = new Bolita(true, 20, 2);
        bolita.recorrer(100);
        bolita.recorrer(300);
        assertEquals(400, bolita.getLongitudRecorrida());
        assertEquals(2, bolita.getTiempoTranscurrido());
    }

    @Test
    @DisplayName("Puede registrarse el tramo actual")
    void tramoActual() {
        Tramo tramo = new Tubo(10, "tubo");
        Bolita bolita = new Bolita(true, 20, 1);
        bolita.setTramoActual(tramo);
        assertSame(tramo, bolita.getTramoActual());
    }
}
