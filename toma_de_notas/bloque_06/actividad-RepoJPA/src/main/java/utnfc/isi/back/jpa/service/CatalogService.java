package utnfc.isi.back.jpa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utnfc.isi.back.jpa.domain.Album;
import utnfc.isi.back.jpa.repository.AlbumRepository;
import utnfc.isi.back.jpa.repository.ArtistRepository;
import utnfc.isi.back.jpa.repository.TrackRepository;

/**
 * Casos de uso del catálogo. El servicio define el límite transaccional
 * (Variante B): varias operaciones de repositorio forman una unidad atómica.
 */
public class CatalogService {

    private final EntityManager em;
    private final ArtistRepository artists;
    private final AlbumRepository albums;
    private final TrackRepository tracks;

    public CatalogService(EntityManager em,
                          ArtistRepository artists,
                          AlbumRepository albums,
                          TrackRepository tracks) {
        this.em = em;
        this.artists = artists;
        this.albums = albums;
        this.tracks = tracks;
    }

    public Album createAlbumForArtist(Long artistId, String title) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            var artist = artists.findById(artistId)
                .orElseThrow(() ->
                    new IllegalArgumentException("No existe el artista " + artistId));

            var album = Album.builder()
                .artist(artist)
                .title(title)
                .build();

            Album saved = albums.save(album);

            tx.commit();
            return saved;

        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }
}
