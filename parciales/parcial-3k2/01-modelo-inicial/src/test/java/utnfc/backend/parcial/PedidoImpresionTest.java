package utnfc.backend.parcial;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class PedidoImpresionTest {
    @Test void calculaHojasYNormalizaTextos() {
        var p = new PedidoImpresion(" A ", " Biblioteca ", 8, 3);
        assertEquals(24, p.hojasNecesarias()); assertEquals("A", p.getId());
        assertEquals("Biblioteca", p.getSector());
    }
    @Test void aceptaLimites() {
        assertEquals(1, new PedidoImpresion("A", "S", 1, 1).hojasNecesarias());
        assertEquals(100000, new PedidoImpresion("B", "S", 1000, 100).hojasNecesarias());
    }
    @Test void rechazaDatosInvalidos() {
        for (int[] n : new int[][]{{0,1},{1001,1},{1,0},{1,101}})
            assertThrows(IllegalArgumentException.class, () -> new PedidoImpresion("A","S",n[0],n[1]));
        assertThrows(IllegalArgumentException.class, () -> new PedidoImpresion(" ","S",1,1));
        assertThrows(IllegalArgumentException.class, () -> new PedidoImpresion("A",null,1,1));
    }
}
