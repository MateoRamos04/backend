package utnfc.backend.parcial;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path ruta = Path.of(args.length == 0 ? "datos/pedidos-parcial2.csv" : args[0]);
        ResultadoCarga carga = new ParserPedidos().leer(ruta);
        CentralImpresion central = new CentralImpresion(carga.getPedidos());
        System.out.println(carga.resumen());
        carga.getErrores().forEach(System.out::println);
        System.out.println("Hojas solicitadas=" + central.hojasSolicitadas());
        System.out.println("Demanda por sector=" + central.demandaPorSector());
        System.out.println("Cantidad por acabado=" + central.conteoPorAcabado());
        System.out.println("hojas ahorradas=" + central.hojasAhorradas());
    }
}
