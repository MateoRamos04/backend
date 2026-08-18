package ar.edu.utn.frc.back;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EjemploTest {

    public int suma(int a, int b) {
        return a+b;
    }

    @Test
    public void testSuma() {
        int suma = suma(1, 3);
        Assertions.assertEquals(4, suma);
    }

}
