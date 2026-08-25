package utnfc.back.bolitas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TuboTest {
    @Test
    @DisplayName("Un tubo expone la longitud configurada")
    void longitud() {
        assertEquals(250, new Tubo(250, "tubo").getLongitud());
    }

    @Test
    @DisplayName("Atravesar un tubo registra el tramo, recorre y retorna su salida")
    void avanzar() {
        Tubo tubo = new Tubo(250, "tubo");
        Tramo salida = new Detencion(1, "salida");
        tubo.encastrarSalida(salida);
        Bolita bolita = new Bolita(true, 20, 2);

        Tramo proximo = tubo.avanzar(bolita);

        assertSame(tubo, bolita.getTramoActual());
        assertEquals(250, bolita.getLongitudRecorrida());
        assertEquals(1.25, bolita.getTiempoTranscurrido());
        assertSame(salida, proximo);
    }

    @Test
    @DisplayName("Un tubo sin salida finaliza el circuito")
    void sinSalida() {
        assertNull(new Tubo(100, "final").avanzar(new Bolita(true, 20, 1)));
    }
}
