package utnfc.back.bolitas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DetencionTest {
    @Test
    @DisplayName("Los segundos de una detención pueden consultarse y modificarse")
    void segundos() {
        Detencion detencion = new Detencion(7, "pausa");
        assertEquals(7, detencion.getSegundos());
        detencion.setSegundos(9);
        assertEquals(9, detencion.getSegundos());
    }

    @Test
    @DisplayName("Una detención registra el tramo, suma tiempo y retorna la salida")
    void avanzar() {
        Detencion detencion = new Detencion(7, "pausa");
        Tramo salida = new Tubo(10, "salida");
        detencion.encastrarSalida(salida);
        Bolita bolita = new Bolita(true, 20, 1);

        assertSame(salida, detencion.avanzar(bolita));
        assertSame(detencion, bolita.getTramoActual());
        assertEquals(7, bolita.getTiempoTranscurrido());
    }
}
