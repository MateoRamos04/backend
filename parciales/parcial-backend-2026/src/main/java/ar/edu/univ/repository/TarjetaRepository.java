package ar.edu.univ.repository;

import ar.edu.univ.model.TarjetaEntity;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class TarjetaRepository extends GenericRepository<TarjetaEntity, Long> {

    public TarjetaRepository(EntityManager em) {
        super();
    }

    @Override
    protected Class<TarjetaEntity> getEntityClass() {
        return TarjetaEntity.class;
    }

    public TarjetaEntity findByNumero(String numero) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT t FROM TarjetaEntity t WHERE t.numero = :numero", TarjetaEntity.class)
                    .setParameter("numero", numero)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<TarjetaEntity> findSinLiquidacion(Integer mes, Integer anio) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT t FROM TarjetaEntity t WHERE t NOT IN (" +
                                    "  SELECT l.tarjeta FROM LiquidacionEntity l WHERE l.mes = :mes AND l.anio = :anio" +
                                    ")", TarjetaEntity.class)
                    .setParameter("mes", mes)
                    .setParameter("anio", anio)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}