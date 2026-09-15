package utnfc.isi.back.jpa.repository;

import jakarta.persistence.EntityManager;
import utnfc.isi.back.jpa.domain.Track;
import utnfc.isi.back.jpa.repository.base.JpaBaseRepository;

import java.util.List;

public class JpaTrackRepository extends JpaBaseRepository<Track, Long>
        implements TrackRepository {

    public JpaTrackRepository(EntityManager em) {
        super(Track.class, em);
    }

    @Override
    public List<Track> findByAlbumId(Long albumId, int offset, int limit) {
        String jpql = """
            select t
            from Track t
            where t.album.albumId = :albumId
            order by t.name
            """;

        return em.createQuery(jpql, Track.class)
                 .setParameter("albumId", albumId)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }

    @Override
    public List<Track> findByNameContainsIgnoreCase(String text, int offset, int limit) {
        String jpql = """
            select t
            from Track t
            where lower(t.name) like lower(:text)
            order by t.name
            """;

        return em.createQuery(jpql, Track.class)
                 .setParameter("text", "%" + text + "%")
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }
}
