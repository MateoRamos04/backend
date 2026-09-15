package utnfc.isi.back.jpa.repository.base;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

/**
 * Implementación genérica con JPA. No abre transacciones:
 * el límite transaccional lo decide el servicio / consumidor.
 */
public class JpaBaseRepository<T, ID> implements BaseRepository<T, ID> {

    private final Class<T> entityClass;
    protected final EntityManager em;

    public JpaBaseRepository(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    @Override
    public T save(T entity) {
        return em.merge(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        String jpql = "select e from " + entityClass.getSimpleName() + " e";

        return em.createQuery(jpql, entityClass)
                 .getResultList();
    }

    @Override
    public List<T> findAll(int offset, int limit) {
        validatePage(offset, limit);

        String jpql = "select e from " + entityClass.getSimpleName() + " e";

        return em.createQuery(jpql, entityClass)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }

    @Override
    public void delete(T entity) {
        T managed = entity;

        if (!em.contains(entity)) {
            managed = em.merge(entity);
        }

        em.remove(managed);
    }

    @Override
    public long count() {
        String jpql = "select count(e) from " + entityClass.getSimpleName() + " e";

        return em.createQuery(jpql, Long.class)
                 .getSingleResult();
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    private void validatePage(int offset, int limit) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset no puede ser negativo");
        }

        if (limit <= 0) {
            throw new IllegalArgumentException("limit debe ser mayor que cero");
        }
    }
}
