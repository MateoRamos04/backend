package utnfc.isi.backend;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DisplayNameLogger.class)
@DisplayName("Test unitarios proceso con la clase Fraccion")
public class FraccionNonInitTest {

    @Test
    @DisplayName("Suma y promedio de una lista de fracciones")
    void testSumaYPromedioFracciones() {
        Fraccion[] fracciones = new Fraccion[]{
            new Fraccion(1, 3), // ≈ 0.333...
            new Fraccion(2, 7), // ≈ 0.2857...
            new Fraccion(5, 6), // ≈ 0.8333...
            new Fraccion(7, 9), // ≈ 0.777...
            new Fraccion(4, 11), // ≈ 0.3636...
            new Fraccion(8, 13), // ≈ 0.6154...
            new Fraccion(3, 8), // = 0.375
            new Fraccion(9, 14), // ≈ 0.6428...
            new Fraccion(11, 16), // = 0.6875
            new Fraccion(6, 7) // ≈ 0.8571...
        };

        Fraccion suma = new Fraccion(0);
        for (Fraccion f : fracciones) {
            suma = suma.sumarA(f);
        }

        suma.simplificar();
        final Fraccion promedio = suma.dividirPor(new Fraccion(fracciones.length));
        promedio.simplificar();
        final Fraccion sumaResult = suma;

        assertAll("Suma y promedio",
                () -> assertTrue(new Fraccion(831953, 144144).equals(sumaResult), "Suma esperada: [1/5]] " + sumaResult),
                () -> assertTrue(new Fraccion(831953, 1441440).equals(promedio), "Promedio esperado: 0.5" + promedio)
        );
    }
}
