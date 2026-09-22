package utnfc.backend.parcial;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class CentralImpresionTest {
    @Test void agregaYFiltra() {
        var c=new CentralImpresion(List.of(new PedidoImpresion("A","N",2,3),new PedidoImpresion("B","N",1,2),new PedidoImpresion("C","S",1,1)));
        assertEquals(9,c.hojasSolicitadas());assertEquals(Map.of("N",8L,"S",1L),c.demandaPorSector());
        assertEquals(1,c.filtrar(p->p.hojasNecesarias()>2).size());
    }
    @Test void vacia() {
        var c=new CentralImpresion(List.of());assertEquals(0,c.hojasSolicitadas());
        assertTrue(c.demandaPorSector().isEmpty());assertTrue(c.filtrar(p->true).isEmpty());
    }
    @Test void copiaDefensiva() {
        var l=new ArrayList<PedidoImpresion>();l.add(new PedidoImpresion("A","N",1,1));
        var c=new CentralImpresion(l);l.clear();assertEquals(1,c.hojasSolicitadas());
        assertThrows(UnsupportedOperationException.class,()->c.getPedidos().clear());
        assertThrows(UnsupportedOperationException.class,()->c.filtrar(p->true).clear());
    }
    @Test void resultadoDeCargaNoComparteListas() {
        var pedidos=new ArrayList<PedidoImpresion>();var errores=new ArrayList<String>();
        pedidos.add(new PedidoImpresion("A","S",1,1));errores.add("error");
        var r=new ResultadoCarga(pedidos,2,0,errores);pedidos.clear();errores.clear();
        assertEquals(1,r.getProcesadas());assertEquals(1,r.getInvalidas());
        assertThrows(UnsupportedOperationException.class,()->r.getPedidos().clear());
    }
}
