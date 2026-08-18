package utnfc.isi.backend;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Hemos agregado el logger de nombres de tests solo a efectos didacticos
//   No es necesario para el funcionamiento de los tests
@ExtendWith(DisplayNameLogger.class)
@DisplayName("Test unitarios sobre la clase Fraccion")
public class FraccionTest {

    Fraccion f1;
    Fraccion f2;

    @BeforeEach
    void init() {
        f1 = new Fraccion(2, 4);
        f2 = new Fraccion(3, 9);
    }

    @Test
    @DisplayName("Simplificación correcta de fracciones")
    void testSimplificar() {
        f1.simplificar();
        assertAll("Fracción f1 simplificada",
                () -> assertEquals(1, f1.getNumerador(), "Numerador esperado: 1"),
                () -> assertEquals(2, f1.getDenominador(), "Denominador esperado: 2")
        );
        f2.simplificar();
        assertAll("Fracción f2 simplificada",
                () -> assertEquals(1, f2.getNumerador(), "Numerador esperado: 1"),
                () -> assertEquals(3, f2.getDenominador(), "Denominador esperado: 3")
        );
    }

    @Test
    @DisplayName("Excepción si el denominador es cero")
    void testDenominadorCero() {
        ArithmeticException ex = assertThrows(ArithmeticException.class,
                () -> new Fraccion(5, 0),
                "Se esperaba excepción por denominador cero"
        );
        assertEquals("El denominador no puede ser cero", ex.getMessage());
    }

    @Test
    @DisplayName("Cálculo correcto del valor real")
    void testValorReal() {
        assertEquals(0.5, f1.valorReal(), 0.0001);
        assertEquals(0.3333, f2.valorReal(), 0.0001);
    }

    @ParameterizedTest
    @CsvSource({
        "1,2,0.5",
        "3,6,0.5",
        "10,20,0.5",
        "0,7,0.0",
        "7,4,1.75",
        "9,3,3.0",
        "8,2,4.0",
        "13,13,1.0"
    })
    @DisplayName("Cálculo del valor real para múltiples fracciones")
    void testFraccionesParametrizadas(int numerador, int denominador, double esperado) {
        Fraccion f = new Fraccion(numerador, denominador);
        assertEquals(esperado, f.valorReal(), 0.0001,
                () -> "Esperado: " + esperado + ", pero fue: " + f.valorReal());
    }

}
