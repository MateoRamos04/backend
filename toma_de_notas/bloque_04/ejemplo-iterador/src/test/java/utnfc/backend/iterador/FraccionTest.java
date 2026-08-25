package utnfc.backend.iterador;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Operaciones de Fraccion usadas por las demostraciones")
class FraccionTest
{
    @Test @DisplayName("valorReal calcula el cociente")
    void valorReal()
    {
        assertEquals(0.75, new Fraccion(3, 4).valorReal(), 0.0001);
    }

    @Test @DisplayName("sumarA devuelve la suma simplificada")
    void sumarA()
    {
        Fraccion resultado = new Fraccion(1, 2).sumarA(new Fraccion(1, 4));
        assertEquals("[3/4]", resultado.toString());
    }

    @Test @DisplayName("multiplicarA devuelve el producto simplificado")
    void multiplicarA()
    {
        Fraccion resultado = new Fraccion(2, 3).multiplicarA(new Fraccion(3, 4));
        assertEquals("[1/2]", resultado.toString());
    }

    @Test @DisplayName("dividirPor devuelve el cociente")
    void dividirPor()
    {
        Fraccion resultado = new Fraccion(1, 2).dividirPor(new Fraccion(1, 4));
        assertEquals(2.0, resultado.valorReal(), 0.0001);
    }

    @Test @DisplayName("compareTo distingue menor, igual y mayor")
    void compareToFracciones()
    {
        Fraccion referencia = new Fraccion(1, 2);
        assertAll(
                () -> assertTrue(new Fraccion(1, 4).compareTo(referencia) < 0),
                () -> assertEquals(0, new Fraccion(2, 4).compareTo(referencia)),
                () -> assertTrue(new Fraccion(3, 4).compareTo(referencia) > 0));
    }

    @Test @DisplayName("simplificar reduce numerador y denominador")
    void simplificar()
    {
        Fraccion fraccion = new Fraccion(8, 12);
        fraccion.simplificar();
        assertEquals("[2/3]", fraccion.toString());
    }
}
