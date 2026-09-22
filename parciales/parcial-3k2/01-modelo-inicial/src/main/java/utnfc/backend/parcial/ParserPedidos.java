package utnfc.backend.parcial;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ParserPedidos {
    public static final String CABECERA = "id,sector,paginas,copias,estado";
    public ResultadoCarga leer(Path ruta) throws IOException {
        var pedidos = new ArrayList<PedidoImpresion>();
        var errores = new ArrayList<String>();
        int leidas = 0;
        int descartadas = 0;
        try (var lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            
            String cabeceraCSV = lector.readLine();
            int columnas = cabeceraCSV.equals(CABECERA + ",acabado") ? 6 : 5;
            if (!CABECERA.equals(cabeceraCSV) && columnas != 6) throw new IllegalArgumentException("encabezado incorrecto");
            String linea;
            
            while ((linea = lector.readLine()) != null) {
                leidas++;
                try {
                    String[] c = linea.split(",", -1);
                    for (int i = 0; i < c.length; i++) c[i] = c[i].strip();
                    if (c.length != columnas) throw new IllegalArgumentException("ancho incorrecto");
                    if (c[4].equals("ANULADO") || c[4].equals("RECHAZADO")) { descartadas++; continue; }
                    if (!c[4].equals("CONFIRMADO")) throw new IllegalArgumentException("estado desconocido");

                    pedidos.add(PedidoImpresion.desdeCampos(c));
                    
                } catch (IllegalArgumentException e) {
                    errores.add("Linea " + (leidas + 1) + ": " + e.getMessage());
                }
            }
        }
        return new ResultadoCarga(pedidos, leidas, descartadas, errores);
    }
}
