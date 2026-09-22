package utnfc.backend.parcial;

public class PedidoCuadernillo extends PedidoImpresion {
    public PedidoCuadernillo (String id, String sector, int paginas, int copias) {
        super(id, sector, paginas, copias);

        if (paginas < 4 || paginas > 400){
            throw new IllegalArgumentException("paginas erroneas: " + paginas);
        }
        if (copias > 25) {
            throw new IllegalArgumentException("copias excedidas: " + copias );
        }
    }

    @Override public int hojasNecesarias() {return ((getPaginas()+3) / 4 ) * getCopias();}
    public int paginasEnBlanco() {return (getPaginas()+3 / 4 ) * 4 - getPaginas();}
    @Override public String getAcabado() { return "CUADERNILLO";}
}
