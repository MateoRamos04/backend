package ar.edu.univ.repository;

import ar.edu.univ.model.*;
import ar.edu.univ.util.JPAUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ConsumoRepositoryTest {

    private EntityManager em;
    private ConsumoRepository consumoRepository;
    private TarjetaRepository tarjetaRepository;

    @BeforeEach
    public void setUp() {
        em = JPAUtil.getEntityManagerFactory().createEntityManager();
        consumoRepository = new ConsumoRepository();
        tarjetaRepository = new TarjetaRepository();
    }

    @AfterEach
    public void tearDown() {
        if (em != null && em.isOpen()) em.close();
    }

    @Test
    public void testFindByTarjetaAnioMes_retornaConsumosDelMes() {
        // La tarjeta ID=1 tiene consumos en 5/2026 según data.sql
        List<ConsumoEntity> consumos = consumoRepository.findByTarjetaAnioMes(1L, 2026, 5);

        assertNotNull(consumos);
        assertFalse(consumos.isEmpty(), "Debe haber consumos para tarjeta 1 en 5/2026");
        consumos.forEach(c -> {
            assertEquals(Integer.valueOf(5), c.getMes());
            assertEquals(Integer.valueOf(2026), c.getAnio());
        });
    }

    @Test
    public void testFindByTarjetaAnioMes_sinConsumos_retornaListaVacia() {
        // Mes/año sin datos
        List<ConsumoEntity> consumos = consumoRepository.findByTarjetaAnioMes(1L, 2020, 1);

        assertNotNull(consumos);
        assertTrue(consumos.isEmpty(), "No debe haber consumos para ese período");
    }

    @Test
    public void testFindByTarjetaAnioMes_cotizacionCargada() {
        // Verifica que el JOIN FETCH funciona (no lanza LazyInitializationException)
        List<ConsumoEntity> consumos = consumoRepository.findByTarjetaAnioMes(1L, 2026, 5);

        assertFalse(consumos.isEmpty());
        consumos.forEach(c -> {
            assertNotNull(c.getCotizacion(), "Cotización debe estar cargada");
            assertNotNull(c.getCotizacion().getTasaCambio());
        });
    }
}
