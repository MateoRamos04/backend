package utnfc.backend.parcial;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.IOException;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;
class ParserPedidosTest {
    @TempDir Path dir;
    @Test void clasificaYContinua() throws IOException {
        Path p=dir.resolve("x.csv");
        Files.writeString(p, ParserPedidos.CABECERA+"\nA,S,2,3,CONFIRMADO\nD,,x,-1,ANULADO\nE,S,x,1,CONFIRMADO\nB,S,1,1,CONFIRMADO\n");
        var r=new ParserPedidos().leer(p);
        assertEquals(4,r.getLeidas()); assertEquals(2,r.getProcesadas());
        assertEquals(1,r.getDescartadas()); assertEquals(1,r.getInvalidas());
        assertTrue(r.getErrores().getFirst().startsWith("Linea 4:"));
        assertEquals("B",r.getPedidos().getLast().getId());
    }
    @Test void anchoPrecedeAlDescarteYBlancoEsInvalido() throws IOException {
        Path p=dir.resolve("x.csv");
        Files.writeString(p,ParserPedidos.CABECERA+"\nD,S,1,1,ANULADO,extra\n\n A , S , 2 , 2 , CONFIRMADO \n");
        var r=new ParserPedidos().leer(p);
        assertEquals(2,r.getInvalidas());assertEquals(0,r.getDescartadas());
        assertEquals(4,r.getPedidos().getFirst().hojasNecesarias());
    }
    @Test void cabeceraYEstructuraDeArchivo() throws IOException {
        Path p=dir.resolve("x.csv");Files.writeString(p,ParserPedidos.CABECERA+"\n");
        assertEquals(0,new ParserPedidos().leer(p).getLeidas());
        Files.writeString(p,"incorrecta\n");
        assertThrows(IllegalArgumentException.class,()->new ParserPedidos().leer(p));
        assertThrows(IOException.class,()->new ParserPedidos().leer(dir.resolve("ausente.csv")));
    }
    @Test void datasetPrevioCompleto() throws IOException {
        var e=new Properties();try(var in=getClass().getResourceAsStream("/previo.properties")){e.load(in);}
        var r=new ParserPedidos().leer(Path.of("datos/pedidos-previo.csv"));
        assertEquals(Integer.parseInt(e.getProperty("leidas")),r.getLeidas());
        assertEquals(Integer.parseInt(e.getProperty("procesadas")),r.getProcesadas());
        assertEquals(Integer.parseInt(e.getProperty("descartadas")),r.getDescartadas());
        assertEquals(Integer.parseInt(e.getProperty("invalidas")),r.getInvalidas());
        var c=new CentralImpresion(r.getPedidos());
        assertEquals(Long.parseLong(e.getProperty("demanda")),c.hojasSolicitadas());
        for(String s:new String[]{"Biblioteca","Docencia","Administracion","Extension"})
            assertEquals(Long.parseLong(e.getProperty("demanda."+s)),c.demandaPorSector().get(s));
    }
}
