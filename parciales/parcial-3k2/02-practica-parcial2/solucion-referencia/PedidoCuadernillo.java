package utnfc.backend.parcial;

/** Cuadernillo: doble faz y dos paginas por cara, es decir 4 paginas por hoja fisica. */
public class PedidoCuadernillo extends PedidoImpresion {

    public PedidoCuadernillo(String id, String sector, int paginas, int copias) {
        super(id, sector, paginas, copias);
        if (paginas < 4) throw new IllegalArgumentException("un cuadernillo necesita al menos 4 paginas");
        if (paginas > 400) throw new IllegalArgumentException("un cuadernillo admite hasta 400 paginas");
        if (copias > 25) throw new IllegalArgumentException("un cuadernillo admite hasta 25 copias");
    }

    public int hojasPorEjemplar() { return (getPaginas() + 3) / 4; }

    @Override public int hojasNecesarias() { return hojasPorEjemplar() * getCopias(); }

    public int paginasEnBlanco() { return hojasPorEjemplar() * 4 - getPaginas(); }

    @Override public String getAcabado() { return "CUADERNILLO"; }
}
