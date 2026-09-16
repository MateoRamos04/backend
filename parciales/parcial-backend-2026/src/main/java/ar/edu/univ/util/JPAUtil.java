package ar.edu.univ.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    // Se crea UNA sola vez, como el ApplicationContext de Spring
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("parcialPU");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void close() {
        emf.close();
    }
}
