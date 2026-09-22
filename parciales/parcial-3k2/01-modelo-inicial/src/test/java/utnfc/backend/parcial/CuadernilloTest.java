package utnfc.backend.parcial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de la evolucion: columna acabado, pedidos en cuadernillo y reglas revisadas. */
class CuadernilloTest {
    @TempDir Path dir;

    private ResultadoCarga leer(String filas) throws IOException {
        Path p = dir.resolve("nuevo.csv");
        Files.writeString(p, ParserPedidos.CABECERA + ",acabado\n" + filas);
        return new ParserPedidos().leer(p);
    }

    // ---- 1. Modelo y calculo -------------------------------------------------

    @Test void cuadernilloCuentaCuatroPaginasPorHoja() {
        assertEquals(1, new PedidoCuadernillo("A", "S", 4, 1).hojasNecesarias());
        assertEquals(2, new PedidoCuadernillo("A", "S", 5, 1).hojasNecesarias());
        assertEquals(2, new PedidoCuadernillo("A", "S", 8, 1).hojasNecesarias());
        assertEquals(6, new PedidoCuadernillo("A", "S", 7, 3).hojasNecesarias());
        assertEquals(20, new PedidoCuadernillo("A", "S", 40, 2).hojasNecesarias());
        assertEquals(18, new PedidoCuadernillo("A", "S", 33, 2).hojasNecesarias());
        assertEquals(2500, new PedidoCuadernillo("A", "S", 400, 25).hojasNecesarias());
    }

    @Test void cuadernilloInformaPaginasEnBlancoPorEjemplar() {
        assertEquals(0, new PedidoCuadernillo("A", "S", 4, 7).paginasEnBlanco());
        assertEquals(3, new PedidoCuadernillo("A", "S", 5, 7).paginasEnBlanco());
        assertEquals(2, new PedidoCuadernillo("A", "S", 6, 1).paginasEnBlanco());
        assertEquals(1, new PedidoCuadernillo("A", "S", 7, 1).paginasEnBlanco());
        assertEquals(0, new PedidoCuadernillo("A", "S", 40, 2).paginasEnBlanco());
        assertEquals(3, new PedidoCuadernillo("A", "S", 33, 2).paginasEnBlanco());
    }

    @Test void cuadernilloHeredaYAgregaInvariantes() {
        var p = new PedidoCuadernillo(" A ", " Docencia ", 8, 2);
        assertTrue(p instanceof PedidoImpresion);
        assertEquals("A", p.getId());
        assertEquals("Docencia", p.getSector());
        assertEquals("CUADERNILLO", p.getAcabado());
        assertEquals("SIMPLE", new PedidoImpresion("A", "S", 8, 2).getAcabado());
        // propias del cuadernillo: 4..400 paginas y hasta 25 copias
        for (int[] n : new int[][]{{3, 1}, {401, 1}, {4, 26}})
            assertThrows(IllegalArgumentException.class, () -> new PedidoCuadernillo("A", "S", n[0], n[1]));
        // heredadas de la clase base
        for (int[] n : new int[][]{{0, 1}, {1001, 1}, {4, 0}, {4, 101}})
            assertThrows(IllegalArgumentException.class, () -> new PedidoCuadernillo("A", "S", n[0], n[1]));
        assertThrows(IllegalArgumentException.class, () -> new PedidoCuadernillo(" ", "S", 8, 2));
        assertThrows(IllegalArgumentException.class, () -> new PedidoCuadernillo("A", null, 8, 2));
        // el acabado SIMPLE no cambia: 3 paginas y 100 copias siguen siendo validas
        assertEquals(9, new PedidoImpresion("A", "S", 3, 3).hojasNecesarias());
        assertEquals(100000, new PedidoImpresion("A", "S", 1000, 100).hojasNecesarias());
    }

    // ---- 2. Lectura unificada y agregados ------------------------------------

    @Test void lecturaNuevaYCalculoPolimorfico() throws IOException {
        var r = leer("""
                A,Docencia,40,2,CONFIRMADO,CUADERNILLO
                B,Docencia,10,3,CONFIRMADO,SIMPLE
                C,Biblioteca,7,1,CONFIRMADO,CUADERNILLO
                """);
        assertEquals(3, r.getProcesadas());
        assertEquals(0, r.getInvalidas());
        assertEquals(20, r.getPedidos().get(0).hojasNecesarias());
        assertEquals(30, r.getPedidos().get(1).hojasNecesarias());
        assertEquals(2, r.getPedidos().get(2).hojasNecesarias());

        var c = new CentralImpresion(r.getPedidos());
        assertEquals(52, c.hojasSolicitadas());
        assertEquals(Map.of("Docencia", 50L, "Biblioteca", 2L), c.demandaPorSector());
        assertEquals(Map.of("CUADERNILLO", 2L, "SIMPLE", 1L), c.conteoPorAcabado());
        assertEquals(65, c.hojasAhorradas());
        //assertEquals(Optional.of("Docencia"), c.sectorConMayorDemanda());
        assertEquals(2, c.filtrar(p -> p instanceof PedidoCuadernillo).size());
    }

    @Test void formatoDeCincoColumnasSigueVigente() throws IOException {
        Path p = dir.resolve("viejo.csv");
        Files.writeString(p, ParserPedidos.CABECERA + "\nA,Docencia,10,2,CONFIRMADO\nB,Docencia,10,2,ANULADO\n");
        var r = new ParserPedidos().leer(p);
        assertEquals(1, r.getProcesadas());
        assertEquals(1, r.getDescartadas());
        assertEquals(20, r.getPedidos().getFirst().hojasNecesarias());
        assertEquals("SIMPLE", r.getPedidos().getFirst().getAcabado());
        assertEquals(0, new CentralImpresion(r.getPedidos()).hojasAhorradas());
    }

    @Test void colectorVacioYDesempateAlfabetico() {
        var vacia = new CentralImpresion(List.of());
        assertTrue(vacia.conteoPorAcabado().isEmpty());
        assertEquals(0, vacia.hojasAhorradas());
        //assertTrue(vacia.sectorConMayorDemanda().isEmpty());

        //var empate = new CentralImpresion(List.of(
                //new PedidoImpresion("A", "Zeta", 10, 1),
                //new PedidoImpresion("B", "Alfa", 10, 1)));
        //assertEquals(Optional.of("Alfa"), empate.sectorConMayorDemanda());
    }

    // ---- 3. Reglas de parseo -------------------------------------------------

    @Test void acabadoDebeSerExactoYElAnchoSeguirALaCabecera() throws IOException {
        var r = leer("A,S,8,2,CONFIRMADO,CUADERNILLO\n"
                + "B,S,8,2,CONFIRMADO,ANILLADO\n"
                + "C,S,8,2,CONFIRMADO,\n"
                + "D,S,8,2,CONFIRMADO,simple\n"
                + "E,S,8,2,CONFIRMADO\n"
                + "F,S,8,2,CONFIRMADO,SIMPLE,extra\n"
                + " G , S , 8 , 2 , CONFIRMADO , CUADERNILLO \n");
        assertEquals(7, r.getLeidas());
        assertEquals(2, r.getProcesadas());
        assertEquals(0, r.getDescartadas());
        assertEquals(5, r.getInvalidas());
        assertTrue(r.getErrores().getFirst().startsWith("Linea 3:"));
        assertEquals("G", r.getPedidos().getLast().getId());
        assertEquals("CUADERNILLO", r.getPedidos().getLast().getAcabado());
        assertEquals(r.getLeidas(), r.getProcesadas() + r.getDescartadas() + r.getInvalidas());
    }

    @Test void anuladoYRechazadoSeDescartanSinValidarElResto() throws IOException {
        var r = leer("""
                A,,x,0,ANULADO,CUALQUIERA
                B,,x,0,RECHAZADO,
                C,S,8,2,PENDIENTE,SIMPLE
                D,S,8,2,ANULADO
                """);
        assertEquals(4, r.getLeidas());
        assertEquals(0, r.getProcesadas());
        assertEquals(2, r.getDescartadas());
        assertEquals(2, r.getInvalidas());
        assertTrue(r.getErrores().getFirst().startsWith("Linea 4:"));
        assertTrue(r.getErrores().getLast().startsWith("Linea 5:"));
    }

    @Test void elIdNoPuedeRepetirseEntrePedidosAceptados() throws IOException {
        var r = leer("""
                A,Docencia,8,2,CONFIRMADO,SIMPLE
                A,Docencia,4,1,CONFIRMADO,CUADERNILLO
                B,Docencia,8,2,ANULADO,SIMPLE
                B,Docencia,8,2,CONFIRMADO,SIMPLE
                C,Docencia,0,2,CONFIRMADO,SIMPLE
                C,Docencia,8,2,CONFIRMADO,SIMPLE
                 A ,Docencia,8,2,CONFIRMADO,SIMPLE
                """);
        assertEquals(7, r.getLeidas());
        assertEquals(3, r.getProcesadas());
        assertEquals(1, r.getDescartadas());
        assertEquals(3, r.getInvalidas());
        assertEquals(List.of("A", "B", "C"), r.getPedidos().stream().map(PedidoImpresion::getId).toList());
        assertTrue(r.getErrores().getFirst().startsWith("Linea 3:"));
        assertTrue(r.getErrores().getFirst().toLowerCase().contains("duplicad"));
        assertTrue(r.getErrores().getLast().startsWith("Linea 8:"));
    }

    // ---- 4. Dataset completo -------------------------------------------------

    @Test void datasetDelParcialCompleto() throws IOException {
        var e = new Properties();
        try (var in = getClass().getResourceAsStream("/parcial2.properties")) { e.load(in); }
        var r = new ParserPedidos().leer(Path.of("datos/pedidos-parcial2.csv"));
        assertEquals(Integer.parseInt(e.getProperty("leidas")), r.getLeidas());
        assertEquals(Integer.parseInt(e.getProperty("procesadas")), r.getProcesadas());
        assertEquals(Integer.parseInt(e.getProperty("descartadas")), r.getDescartadas());
        assertEquals(Integer.parseInt(e.getProperty("invalidas")), r.getInvalidas());

        var c = new CentralImpresion(r.getPedidos());
        assertEquals(Long.parseLong(e.getProperty("demanda")), c.hojasSolicitadas());
        for (String s : new String[]{"Administracion", "Biblioteca", "Docencia", "Extension"})
            assertEquals(Long.parseLong(e.getProperty("demanda." + s)), c.demandaPorSector().get(s));
        assertEquals(Long.parseLong(e.getProperty("cuadernillos")), c.conteoPorAcabado().get("CUADERNILLO"));
        assertEquals(Long.parseLong(e.getProperty("simples")), c.conteoPorAcabado().get("SIMPLE"));
        assertEquals(Long.parseLong(e.getProperty("ahorradas")), c.hojasAhorradas());
        //assertEquals(Optional.of(e.getProperty("sector.mayor")), c.sectorConMayorDemanda());

        var lineas = r.getErrores().stream().map(x -> x.split(":")[0].replace("Linea ", "").strip()).toList();
        assertEquals(List.of(e.getProperty("invalidas.lineas").split(",")), lineas);
        assertEquals(Integer.parseInt(e.getProperty("cuadernillos")),
                c.filtrar(p -> p instanceof PedidoCuadernillo).size());
    }
}
