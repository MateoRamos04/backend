package utnfc.back.bolitas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircuitoTest {
    @Test
    @DisplayName("transportar recorre polimórficamente tubo, desvío, acelerador y detención")
    void circuitoConTransportar() {
        Tubo inicio = new Tubo(100, "inicio");
        DesvioLisa desvio = new DesvioLisa("lisa");
        Tubo caminoLiso = new Tubo(200, "camino liso");
        Tubo caminoTexturado = new Tubo(500, "camino texturado");
        Acelerador acelerador = new Acelerador("acelerador");
        Detencion finalCircuito = new Detencion(5, "final");
        inicio.encastrarSalida(desvio);
        desvio.encastrarSalida(caminoLiso);
        desvio.setAlternativa(caminoTexturado);
        caminoLiso.encastrarSalida(acelerador);
        acelerador.encastrarSalida(finalCircuito);
        Bolita bolita = new Bolita(true, 20, 2);

        inicio.transportar(bolita);

        assertEquals(300, bolita.getLongitudRecorrida());
        assertEquals(16.5, bolita.getTiempoTranscurrido());
        assertEquals(5, bolita.getVelocidad());
        assertSame(finalCircuito, bolita.getTramoActual());
    }

    @Test
    @DisplayName("El recorrido iterativo elige la alternativa para una bolita texturada")
    void circuitoIterativoTexturada() {
        Tubo inicio = new Tubo(100, "inicio");
        DesvioLisa desvio = new DesvioLisa("lisa");
        Tubo caminoLiso = new Tubo(200, "liso");
        Tubo caminoTexturado = new Tubo(500, "texturado");
        inicio.encastrarSalida(desvio);
        desvio.encastrarSalida(caminoLiso);
        desvio.setAlternativa(caminoTexturado);
        Bolita bolita = new Bolita(false, 20, 2);

        Tramo actual = inicio;
        while (actual != null) {
            actual = actual.avanzar(bolita);
        }

        assertEquals(600, bolita.getLongitudRecorrida());
        assertEquals(3, bolita.getTiempoTranscurrido());
        assertSame(caminoTexturado, bolita.getTramoActual());
    }

    @Test
    @DisplayName("DesvioPeso permite construir recorridos completos diferentes")
    void circuitoPorPeso() {
        DesvioPeso desvio = new DesvioPeso(25, "peso");
        Tubo livianas = new Tubo(100, "livianas");
        Tubo pesadas = new Tubo(300, "pesadas");
        desvio.encastrarSalida(livianas);
        desvio.setAlternativa(pesadas);
        Bolita liviana = new Bolita(true, 25, 1);
        Bolita pesada = new Bolita(true, 26, 1);

        desvio.transportar(liviana);
        desvio.transportar(pesada);

        assertEquals(100, liviana.getLongitudRecorrida());
        assertEquals(300, pesada.getLongitudRecorrida());
        assertSame(livianas, liviana.getTramoActual());
        assertSame(pesadas, pesada.getTramoActual());
    }

    @Test
    @DisplayName("DesvioVelocidad distingue menor, igual y mayor dentro de circuitos completos")
    void circuitoPorVelocidad() {
        DesvioVelocidad desvio = new DesvioVelocidad(10, "velocidad");
        Tubo lentas = new Tubo(100, "lentas");
        Tubo rapidas = new Tubo(200, "rápidas");
        desvio.encastrarSalida(lentas);
        desvio.setAlternativa(rapidas);
        Bolita menor = new Bolita(true, 20, 9);
        Bolita igual = new Bolita(true, 20, 10);
        Bolita mayor = new Bolita(true, 20, 11);

        desvio.transportar(menor);
        desvio.transportar(igual);
        desvio.transportar(mayor);

        assertEquals(100, menor.getLongitudRecorrida());
        assertEquals(100, igual.getLongitudRecorrida());
        assertEquals(200, mayor.getLongitudRecorrida());
    }

    @Test
    @DisplayName("Un ciclo como el de App termina cuando el tubo curvo reduce la velocidad")
    void cicloAcotadoComoApp() {
        TuboCurvo curvo = new TuboCurvo("curvo", 100, 3);
        DesvioVelocidad desvio = new DesvioVelocidad(10, "velocidad");
        Tubo finalCircuito = new Tubo(50, "final");
        curvo.encastrarSalida(desvio);
        desvio.encastrarSalida(finalCircuito);
        desvio.setAlternativa(curvo);
        Bolita bolita = new Bolita(true, 20, 14);

        Tramo actual = curvo;
        while (actual != null) {
            actual = actual.avanzar(bolita);
        }

        assertEquals(250, bolita.getLongitudRecorrida());
        assertEquals(8, bolita.getVelocidad());
        assertSame(finalCircuito, bolita.getTramoActual());
    }

    @Test
    @DisplayName("transportar se detiene con seguridad ante un desvío incompleto")
    void desvioIncompleto() {
        DesvioLisa desvio = new DesvioLisa("incompleto");
        Bolita bolita = new Bolita(true, 20, 1);
        desvio.transportar(bolita);
        assertSame(desvio, bolita.getTramoActual());
        assertEquals(0, bolita.getLongitudRecorrida());
    }

    @Test
    @DisplayName("Una BolitaConHistoria sustituye a Bolita durante todo un circuito")
    void circuitoConHistoria() {
        Tubo tubo = new Tubo(100, "tubo");
        Acelerador acelerador = new Acelerador("acelerador");
        TuboCurvo curvo = new TuboCurvo("curvo", 200, 1);
        Detencion finalCircuito = new Detencion(2, "final");
        tubo.encastrarSalida(acelerador);
        acelerador.encastrarSalida(curvo);
        curvo.encastrarSalida(finalCircuito);
        BolitaConHistoria bolita = new BolitaConHistoria(true, 20, 2);

        tubo.transportar(bolita);

        assertEquals(300, bolita.getLongitudRecorrida());
        assertEquals(4, bolita.getVelocidad());
        assertEquals(13, bolita.getTiempoTranscurrido());
        String historia = bolita.mostrarRecorrido();
        assertTrue(historia.indexOf("tubo") < historia.indexOf("acelerador"));
        assertTrue(historia.indexOf("acelerador") < historia.indexOf("curvo"));
        assertTrue(historia.indexOf("curvo") < historia.indexOf("final"));
    }
}
