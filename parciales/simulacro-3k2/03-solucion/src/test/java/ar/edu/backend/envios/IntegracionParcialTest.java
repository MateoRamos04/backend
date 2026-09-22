package ar.edu.backend.envios;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
class IntegracionParcialTest {
    @Test void datosCompletos() throws IOException {
        var r=new ParserEnvios().leer(Path.of("src/resources/datos-parcial.csv"));
        assertEquals(140,r.getLeidas()); assertEquals(100,r.getProcesadas());
        assertEquals(16,r.getDescartadas()); assertEquals(24,r.getInvalidas());
        var c=new CentralEnvios(r.getEnvios());
        assertEquals(214800,c.total()); assertEquals(Map.of("NORMAL",55L,"PRIORITARIO",45L),c.cantidadesPorModalidad());
        assertEquals(Map.of("Norte",72800,"Sur",67900,"Centro",74100),c.totalesPorZona());
        assertEquals(29,c.filtrar(e -> e.getGramos() <= 1000).size());
        assertEquals(r.getLeidas(),r.getProcesadas()+r.getDescartadas()+r.getInvalidas());
        assertEquals(700,c.filtrar(e -> e.getId().equals("P001")).get(0).costo());
        assertEquals(1100,c.filtrar(e -> e.getId().equals("P002")).get(0).costo());
        assertEquals(5000,c.filtrar(e -> e.getId().equals("P003")).get(0).getGramos());
        assertEquals(1900,c.filtrar(e -> e.getId().equals("P003")).get(0).costo());
        assertEquals(30000,c.filtrar(e -> e.getId().equals("P006")).get(0).getGramos());
        assertTrue(c.filtrar(e -> e.getId().equals("P117")).isEmpty());
        assertTrue(r.getErrores().stream().anyMatch(e -> e.startsWith("Línea 118:")));
        assertTrue(r.getDescartes().contains("Línea 102: CANCELADO"));
        assertTrue(r.getDescartes().contains("Línea 103: CANCELADO"));
        assertTrue(r.getErrores().stream().anyMatch(e -> e.startsWith("Línea 134:")));
        assertTrue(new CentralEnvios(java.util.List.of()).cantidadesPorModalidad().isEmpty());
    }
}
