package utnfc.isi.back.jpa.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Administra el ciclo de vida de JPA sin contenedor:
 * un EntityManagerFactory de vida larga y un EntityManager por hilo.
 */
public final class LocalEntityManagerProvider {

    private static final String PU_NAME = "chinookPU";

    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory(PU_NAME);

    private static final ThreadLocal<EntityManager> current =
        new ThreadLocal<>();

    private LocalEntityManagerProvider() {
    }

    public static EntityManager em() {
        EntityManager em = current.get();

        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
            current.set(em);
        }

        return em;
    }

    public static void closeCurrent() {
        EntityManager em = current.get();

        if (em != null) {
            if (em.isOpen()) {
                em.close();
            }
            current.remove();
        }
    }

    public static void shutdown() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
