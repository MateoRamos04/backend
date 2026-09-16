package ar.edu.univ.repository;

import ar.edu.univ.model.*;
import ar.edu.univ.util.JPAUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TarjetaRepositoryTest {

    private TarjetaRepository tarjetaRepository;

    @BeforeEach
    public void setUp() {
        tarjetaRepository = new TarjetaRepository();
    }

    @Test
    public void testFindAll_retornaTodasLasTarjetas() {
        List<TarjetaEntity> tarjetas = tarjetaRepository.findAll();

        assertNotNull(tarjetas);
        assertEquals(10, tarjetas.size(), "Deben existir 10 tarjetas cargadas por data.sql");
    }

    @Test
    public void testFindByNumero_tarjetaExistente() {
        TarjetaEntity tarjeta = tarjetaRepository.findByNumero("4500123412340001");

        assertNotNull(tarjeta);
        assertEquals("Juan Perez", tarjeta.getTitular());
    }

    @Test
    public void testFindSinLiquidacion_sinLiquidacionesCreadas_retornaTodasLasTarjetas() {
        // Como no se crearon liquidaciones, todas deben aparecer
        List<TarjetaEntity> sinLiquidacion = tarjetaRepository.findSinLiquidacion(5, 2026);

        assertNotNull(sinLiquidacion);
        assertFalse(sinLiquidacion.isEmpty());
    }

    @Test
    public void testFindById_existente() {
        TarjetaEntity tarjeta = tarjetaRepository.findById(1L);

        assertNotNull(tarjeta);
        assertEquals("4500123412340001", tarjeta.getNumero());
    }

    @Test
    public void testFindById_noExistente_retornaNull() {
        TarjetaEntity tarjeta = tarjetaRepository.findById(999L);

        assertNull(tarjeta);
    }
}
