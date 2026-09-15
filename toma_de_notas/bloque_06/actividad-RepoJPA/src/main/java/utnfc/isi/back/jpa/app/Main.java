package utnfc.isi.back.jpa.app;

import jakarta.persistence.EntityManager;
import utnfc.isi.back.jpa.config.DataInitializer;
import utnfc.isi.back.jpa.config.LocalEntityManagerProvider;
import utnfc.isi.back.jpa.repository.JpaAlbumRepository;
import utnfc.isi.back.jpa.repository.JpaArtistRepository;
import utnfc.isi.back.jpa.repository.JpaTrackRepository;
import utnfc.isi.back.jpa.service.CatalogService;

public class Main {

    public static void main(String[] args) {

        // 1. Infraestructura: crear Chinook y luego abrir JPA sobre la misma base
        DataInitializer.initialize();
        EntityManager em = LocalEntityManagerProvider.em();

        try {
            // 2. Repositorios
            var artistRepository = new JpaArtistRepository(em);
            var albumRepository = new JpaAlbumRepository(em);
            var trackRepository = new JpaTrackRepository(em);

            // 3. Servicio
            var service = new CatalogService(em, artistRepository, albumRepository, trackRepository);

            // 4. Consultas y caso de uso
            System.out.println("Primeros 10 artistas:");
            artistRepository.findAll(0, 10)
                .forEach(artist -> System.out.println(artist.getName()));

            System.out.println();
            System.out.println("Artistas que contienen 'queen':");
            artistRepository.findByNameContainsIgnoreCase("queen", 0, 10)
                .forEach(artist -> System.out.println(artist.getName()));

            System.out.println();
            System.out.println("Álbumes del artista 1:");
            albumRepository.findByArtistId(1L, 0, 10)
                .forEach(album -> System.out.println(album.getTitle()));

            System.out.println();
            System.out.println("Primeros 5 tracks del álbum 1:");
            trackRepository.findByAlbumId(1L, 0, 5)
                .forEach(track -> System.out.println(track.getName()));

            var album = service.createAlbumForArtist(1L, "Álbum creado desde el ejemplo");

            System.out.println();
            System.out.println("Nuevo álbum: " + album.getTitle() + " (id " + album.getAlbumId() + ")");

        } finally {
            // 5. Cerrar recursos
            LocalEntityManagerProvider.closeCurrent();
            LocalEntityManagerProvider.shutdown();
        }
    }
}
