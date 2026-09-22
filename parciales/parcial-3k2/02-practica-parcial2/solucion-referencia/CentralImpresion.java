package utnfc.backend.parcial;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;

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

    public Map<String, Long> conteoPorAcabado() {
        Map<String, Long> conteo = new TreeMap<>();
        for (PedidoImpresion p : pedidos)
            conteo.put(p.getAcabado(), conteo.getOrDefault(p.getAcabado(), 0L) + 1);
        return conteo;
    }

    public long hojasAhorradas() {
        long ahorro = 0;
        for (PedidoImpresion p : pedidos)
            ahorro += (long) p.getPaginas() * p.getCopias() - p.hojasNecesarias();
        return ahorro;
    }

    public Optional<String> sectorConMayorDemanda() {
        String mayor = null;
        long max = Long.MIN_VALUE;
        for (Map.Entry<String, Long> e : demandaPorSector().entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); mayor = e.getKey(); }
        }
        return Optional.ofNullable(mayor);
    }
}
