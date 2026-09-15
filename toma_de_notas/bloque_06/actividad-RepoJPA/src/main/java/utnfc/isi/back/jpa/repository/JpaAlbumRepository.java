package utnfc.isi.back.jpa.repository;

import jakarta.persistence.EntityManager;
import utnfc.isi.back.jpa.domain.Album;
import utnfc.isi.back.jpa.repository.base.JpaBaseRepository;

import java.util.List;

public class JpaAlbumRepository extends JpaBaseRepository<Album, Long>
        implements AlbumRepository {

    public JpaAlbumRepository(EntityManager em) {
        super(Album.class, em);
    }

    @Override
    public List<Album> findByArtistId(Long artistId, int offset, int limit) {
        String jpql = """
            select a
            from Album a
            where a.artist.artistId = :artistId
            order by a.title
            """;

        return em.createQuery(jpql, Album.class)
                 .setParameter("artistId", artistId)
                 .setFirstResult(offset)
                 .setMaxResults(limit)
                 .getResultList();
    }
}
