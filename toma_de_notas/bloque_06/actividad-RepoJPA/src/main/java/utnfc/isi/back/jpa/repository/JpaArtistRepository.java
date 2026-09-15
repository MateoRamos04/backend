package utnfc.isi.back.jpa.repository;

import jakarta.persistence.EntityManager;
import utnfc.isi.back.jpa.domain.Artist;
import utnfc.isi.back.jpa.repository.base.JpaBaseRepository;

import java.util.List;

public class JpaArtistRepository extends JpaBaseRepository<Artist, Long>
        implements ArtistRepository {

    public JpaArtistRepository(EntityManager em) {
        super(Artist.class, em);
    }

    @Override
    public List<Artist> findByNameContainsIgnoreCase(String text, int offset, int limit) {
        String jpql = """
            select a
            from Artist a
            where lower(a.name) like lower(:text)
            order by a.name
            """;

        return em.createQuery(jpql, Artist.class)
                 .setParameter("text", "%" + text + "%")
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }
}
