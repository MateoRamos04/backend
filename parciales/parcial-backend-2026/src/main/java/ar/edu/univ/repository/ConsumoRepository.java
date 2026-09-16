package ar.edu.univ.repository;

import ar.edu.univ.model.ConsumoEntity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ConsumoRepository extends GenericRepository<ConsumoEntity, Long> {

    public ConsumoRepository(EntityManager em) {
        super();
    }

    @Override
    protected Class<ConsumoEntity> getEntityClass() {
        return ConsumoEntity.class;
    }

    public List<ConsumoEntity> findByTarjetaAnioMes(Long idTarjeta, Integer anio, Integer mes) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM ConsumoEntity c JOIN FETCH c.cotizacion " +
                                    "WHERE c.tarjeta.id = :idTarjeta " +
                                    "AND c.anio = :anio AND c.mes = :mes", ConsumoEntity.class)
                    .setParameter("idTarjeta", idTarjeta)
                    .setParameter("anio", anio)
                    .setParameter("mes", mes)
                    .getResultList();
        } finally {
            em.close();
        }
    }
//extras
    /*public List<Object[]> sumaPorRubro(Long idTarjeta, Integer anio, Integer mes) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c.rubro, SUM(c.monto * c.cotizacion.tasaCambio) " +
                                    "FROM ConsumoEntity c " +
                                    "WHERE c.tarjeta.id = :idTarjeta AND c.anio = :anio AND c.mes = :mes " +
                                    "GROUP BY c.rubro", Object[].class)
                    .setParameter("idTarjeta", idTarjeta)
                    .setParameter("anio", anio)
                    .setParameter("mes", mes)
                    .getResultList();
        } finally {
            em.close();
        }
    }*/
}