package ar.edu.univ.repository;

import ar.edu.univ.model.LiquidacionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class LiquidacionRepository extends GenericRepository<LiquidacionEntity, Long> {

    public LiquidacionRepository(EntityManager em) {
        super();
    }

    @Override
    protected Class<LiquidacionEntity> getEntityClass() {
        return LiquidacionEntity.class;
    }

    public Optional<LiquidacionEntity> findByNumeroTarjetaAnioMes(String numero, Integer anio, Integer mes) {
        EntityManager em = getEntityManager();
        try {
            LiquidacionEntity result = em.createQuery(
                            "SELECT l FROM LiquidacionEntity l WHERE l.tarjeta.numero = :numero " +
                                    "AND l.anio = :anio AND l.mes = :mes", LiquidacionEntity.class)
                    .setParameter("numero", numero)
                    .setParameter("anio", anio)
                    .setParameter("mes", mes)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}