package utnfc.backend.parcial;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CentralImpresion {
    private final List<PedidoImpresion> pedidos;
    public CentralImpresion(List<PedidoImpresion> pedidos) { this.pedidos = List.copyOf(pedidos); }
    public List<PedidoImpresion> getPedidos() { return pedidos; }
    public List<PedidoImpresion> filtrar(Predicate<PedidoImpresion> criterio) {
        return pedidos.stream().filter(criterio).toList();
    }
    public long hojasSolicitadas() {
        long total = 0;
        for (PedidoImpresion p : pedidos) total += p.hojasNecesarias();
        return total;
    }
    public Map<String, Long> demandaPorSector() {
        Map<String, Long> total = new TreeMap<>();
        for (PedidoImpresion p : pedidos)
            total.put(p.getSector(), total.getOrDefault(p.getSector(), 0L) + p.hojasNecesarias());
        return total;
    }
    public Map <String, Long> conteoPorAcabado() {
        return pedidos.stream().collect(Collectors.groupingBy(PedidoImpresion::getAcabado, Collectors.counting()) );
    }

    public long hojasAhorradas() {
        long ahorro = 0;
        for (PedidoImpresion p : pedidos)
            ahorro += p.getPaginas() * p.getCopias() - p.hojasNecesarias();
        return ahorro;
            
        
        
    }
}
