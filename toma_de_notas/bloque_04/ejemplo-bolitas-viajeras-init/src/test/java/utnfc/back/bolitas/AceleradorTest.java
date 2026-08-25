package utnfc.back.bolitas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AceleradorTest {
    @Test
    @DisplayName("Un acelerador registra el tramo, demora 10 segundos, acelera 3 m/s y retorna la salida")
    void avanzar() {
        Acelerador acelerador = new Acelerador("acelerador");
        Tramo salida = new Tubo(10, "salida");
        acelerador.encastrarSalida(salida);
        Bolita bolita = new Bolita(true, 20, 2);

        assertSame(salida, acelerador.avanzar(bolita));
        assertSame(acelerador, bolita.getTramoActual());
        assertEquals(10, bolita.getTiempoTranscurrido());
        assertEquals(5, bolita.getVelocidad());
    }
}
