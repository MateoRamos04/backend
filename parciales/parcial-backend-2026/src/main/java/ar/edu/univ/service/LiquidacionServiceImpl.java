package ar.edu.univ.service;

import ar.edu.univ.dto.LiquidacionDTO;
import ar.edu.univ.excepciones.TarjetaInexistenteException;
import ar.edu.univ.model.ConsumoEntity;
import ar.edu.univ.model.LiquidacionEntity;
import ar.edu.univ.model.TarjetaEntity;
import ar.edu.univ.repository.ConsumoRepository;
import ar.edu.univ.repository.LiquidacionRepository;
import ar.edu.univ.repository.TarjetaRepository;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@AllArgsConstructor
public class LiquidacionServiceImpl implements LiquidacionService{
    private final LiquidacionRepository liquidacionRepository;
    private final TarjetaRepository tarjetaRepository;
    private final ConsumoRepository consumoRepository;
    @Override
    public LiquidacionDTO generarLiquidacion(long idTarjeta, int anio, int mes) throws TarjetaInexistenteException {
        TarjetaEntity tarjeta = tarjetaRepository.findById(idTarjeta);
        if (tarjeta == null){
            throw new TarjetaInexistenteException(idTarjeta);
        }

        List<ConsumoEntity> consumos = consumoRepository.findByTarjetaAnioMes(idTarjeta, anio, mes);

        /*double descCombustibles = consumos.stream()
                .filter(c->c.getRubro().equals("COMBUSTIBLE"))
                .mapToDouble(c -> c.getMonto() * 15 / 100)
                .sum();

        double descSupermercados = consumos.stream()
                .filter(c->c.getRubro().equals("SUPERMERCADO"))
                .mapToDouble(c -> c.getMonto() * 20 / 100)
                .sum();

        double descRestaurantes = consumos.stream()
                .filter(c->c.getRubro().equals("RESTAURANTES"))
                .mapToDouble(c -> c.getMonto() * 25 / 100)
                .sum();*/
        double descCombustibles = consumos.stream()
                .filter(c -> c.getRubro().equals("COMBUSTIBLE") && c.getMoneda().equals("ARS"))
                .mapToDouble(c -> Math.min(c.getMonto() * 15 / 100, 750))
                .sum();

        double descSupermercados = consumos.stream()
                .filter(c -> c.getRubro().equals("SUPERMERCADO") && c.getMoneda().equals("ARS"))
                .mapToDouble(c -> Math.min(c.getMonto() * 20 / 100, 3000))
                .sum();

        double descRestaurantes = consumos.stream()
                .filter(c -> c.getRubro().equals("RESTAURANTES")
                        && c.getMoneda().equals("ARS")
                        && c.getDia() >= 10
                        && c.getDia() <= 15)
                .mapToDouble(c -> c.getMonto() * 25 / 100)
                .sum();


        double totalConsumos = consumos.stream()
                .mapToDouble(c -> c.getMonto() * c.getCotizacion().getTasaCambio())
                .sum();

        /*double descTotal = descSupermercados + descCombustibles + descRestaurantes;
        double totalDescuentos = totalConsumos - descTotal;
        */

        double totalDescuentos = descSupermercados + descCombustibles + descRestaurantes;

        double resolBDA = consumos.stream()
                .filter(c->c.getRubro().equals("OTROS"))
                .mapToDouble(c -> c.getMonto() * 12 / 100)
                .sum();
        double monExtranjera = consumos.stream()
                .filter(c-> !c.getMoneda().equals("ARS"))
                .mapToDouble(c -> c.getMonto() * 7.5 / 100)
                .sum();

        //double impIva = 21 * totalConsumos / 100;
        double impIva = consumos.stream()
                .filter(c -> c.getMoneda().equals("ARS"))
                .mapToDouble(c -> c.getMonto() * c.getCotizacion().getTasaCambio() * 21 / 100)
                .sum();

        double totalImpuestos = impIva + resolBDA + monExtranjera;

        double totalAPagar = totalConsumos + totalImpuestos - totalDescuentos;

        LiquidacionEntity liquidacion = new LiquidacionEntity();
        liquidacion.setTarjeta(tarjeta);
        liquidacion.setAnio(anio);
        liquidacion.setMes(mes);
        liquidacion.setTotalConsumos(totalConsumos);
        liquidacion.setTotalImpuestos(totalImpuestos);
        liquidacion.setTotalDescuentos(totalDescuentos);
        liquidacion.setTotalAPagar(totalAPagar);

        liquidacionRepository.save(liquidacion);

        LiquidacionDTO liquidacionDTO = LiquidacionDTO.fromEntity(liquidacionRepository.findByNumeroTarjetaAnioMes(tarjeta.getNumero(), anio, mes).orElseThrow(() -> new RuntimeException("Liquidación no encontrada")));
        System.out.println("Liquidacion guardada: " + liquidacionDTO);

        return liquidacionDTO;
    }

    @Override
    public List<String> getLiquidacionesPendientes(int anio, int mes) {
        List<TarjetaEntity> tarjetas = new TarjetaRepository().findSinLiquidacion(mes, anio);

        return tarjetas.stream()
                .map(TarjetaEntity::getNumero).toList();
    }

    @Override
    public List<LiquidacionDTO> liquidarLote(String rutaArchivo) throws IOException {

        List<LiquidacionDTO> lotes = Files.lines(Path.of(rutaArchivo))
                .map(linea -> {
                    String[] campos = linea.split(";");
                    LiquidacionDTO liquidar = generarLiquidacion(
                            Integer.parseInt(campos[0]),
                            Integer.parseInt(campos[1]),
                            Integer.parseInt(campos[2])

                    );
                    return liquidar;
                })
                .collect(Collectors.toList());
        return lotes;
    }

}
