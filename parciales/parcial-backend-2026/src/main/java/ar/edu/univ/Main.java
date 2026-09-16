package ar.edu.univ;

import ar.edu.univ.dto.LiquidacionDTO;
import ar.edu.univ.model.ConsumoEntity;
import ar.edu.univ.model.LiquidacionEntity;
import ar.edu.univ.model.TarjetaEntity;
import ar.edu.univ.repository.ConsumoRepository;
import ar.edu.univ.repository.LiquidacionRepository;
import ar.edu.univ.repository.TarjetaRepository;
import ar.edu.univ.service.LiquidacionService;
import ar.edu.univ.service.LiquidacionServiceImpl;
import ar.edu.univ.util.JPAUtil;
import org.h2.tools.Server;

import java.util.List;
import java.util.Optional;

public class Main {
    private static final LiquidacionService liquiService = new LiquidacionServiceImpl(new LiquidacionRepository(), new TarjetaRepository(), new ConsumoRepository());

    public static void main(String[] args) throws Exception {


        iniciarBaseDeDatos();

        List<String> liquisPendientes = liquiService.getLiquidacionesPendientes(2026, 5);
        System.out.println("liquis pendiente: " + liquisPendientes);

        List<LiquidacionDTO> lotes = liquiService.liquidarLote("liquidaciones.csv");
        System.out.println("lotes "+ lotes);

        System.out.println("\nPresioná ENTER para cerrar...");
        System.in.read();

        JPAUtil.getEntityManagerFactory().close();
    }

    // ── Base de datos ─────────────────────────────────────────────────────────

    private static void iniciarBaseDeDatos() throws Exception {
        Server server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        System.out.println("H2 Console disponible en: http://localhost:8082");
        JPAUtil.getEntityManagerFactory();
        System.out.println("Base de datos inicializada.\n");
    }









}
