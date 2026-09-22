package utnfc.backend.parcial;

/** Un trabajo completo. El acabado SIMPLE usa una hoja por pagina y por copia. */
public class PedidoImpresion {
    private final String id;
    private final String sector;
    private final int paginas;
    private final int copias;

    public PedidoImpresion(String id, String sector, int paginas, int copias) {
        if (id == null || id.isBlank() || sector == null || sector.isBlank())
            throw new IllegalArgumentException("id y sector obligatorios");
        if (paginas < 1 || paginas > 1000 || copias < 1 || copias > 100)
            throw new IllegalArgumentException("paginas o copias fuera de rango");
        this.id = id.strip();
        this.sector = sector.strip();
        this.paginas = paginas;
        this.copias = copias;
    }

    public static PedidoImpresion desdeCampos(String[] campos) {
        if (campos.length != 5 && campos.length != 6) throw new IllegalArgumentException("se esperan 5 o 6 campos");
        String acabado = campos.length == 5 ? "SIMPLE" : campos[5];
        int paginas = Integer.parseInt(campos[2]);
        int copias = Integer.parseInt(campos[3]);
        return switch (acabado) {
            case "SIMPLE" -> new PedidoImpresion(campos[0], campos[1], paginas, copias);
            case "CUADERNILLO" -> new PedidoCuadernillo(campos[0], campos[1], paginas, copias);
            default -> throw new IllegalArgumentException("acabado invalido");
        };
    }

    public String getId() { return id; }
    public String getSector() { return sector; }
    public int getPaginas() { return paginas; }
    public int getCopias() { return copias; }
    public int hojasNecesarias() { return paginas * copias; }
    public String getAcabado() { return "SIMPLE"; }
}
