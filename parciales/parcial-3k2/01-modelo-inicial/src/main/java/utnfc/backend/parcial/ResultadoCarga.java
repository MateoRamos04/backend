package utnfc.backend.parcial;

import java.util.List;

public class ResultadoCarga {
    private final List<PedidoImpresion> pedidos;
    private final int leidas;
    private final int descartadas;
    private final List<String> errores;
    public ResultadoCarga(List<PedidoImpresion> pedidos, int leidas, int descartadas, List<String> errores) {
        this.pedidos = List.copyOf(pedidos);
        this.leidas = leidas;
        this.descartadas = descartadas;
        this.errores = List.copyOf(errores);
    }
    public List<PedidoImpresion> getPedidos() { return pedidos; }
    public int getLeidas() { return leidas; }
    public int getProcesadas() { return pedidos.size(); }
    public int getDescartadas() { return descartadas; }
    public int getInvalidas() { return errores.size(); }
    public List<String> getErrores() { return errores; }
    public String resumen() {
        return "Leidas=%d; procesadas=%d; descartadas=%d; invalidas=%d"
                .formatted(leidas, getProcesadas(), descartadas, getInvalidas());
    }
}
