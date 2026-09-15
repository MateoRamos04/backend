package utnfc.isi.back.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import utnfc.isi.back.jpa.config.DataInitializer;
import utnfc.isi.back.jpa.config.LocalEntityManagerProvider;
import utnfc.isi.back.jpa.domain.Artist;
import utnfc.isi.back.jpa.repository.base.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArtistRepositoryTest {

    private EntityManager em;
    private ArtistRepository repository;

    @BeforeAll
    static void initializeDatabase() {
        DataInitializer.initialize();
    }

    @BeforeEach
    void setUp() {
        em = LocalEntityManagerProvider.em();
        repository = new JpaArtistRepository(em);
    }

    @AfterEach
    void tearDown() {
        EntityTransaction tx = em.getTransaction();
        if (tx.isActive()) {
            tx.rollback();
        }
        LocalEntityManagerProvider.closeCurrent();
    }

    @AfterAll
    static void shutdown() {
        LocalEntityManagerProvider.shutdown();
    }

    @Test
    void findAllReturnsArtists() {
        assertFalse(repository.findAll().isEmpty());
    }

    @Test
    void findByIdReturnsExistingArtist() {
        Artist artist = repository.findById(1L).orElseThrow();

        assertEquals("AC/DC", artist.getName());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        assertTrue(repository.findById(-1L).isEmpty());
        assertFalse(repository.existsById(-1L));
    }

    @Test
    void findAllWithPaginationRespectsLimit() {
        assertEquals(10, repository.findAll(0, 10).size());
        assertEquals(5, repository.findAll(PageRequest.ofPage(1, 5)).size());
    }

    @Test
    void findAllRejectsInvalidPage() {
        assertThrows(IllegalArgumentException.class, () -> repository.findAll(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> repository.findAll(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new PageRequest(0, 0));
    }

    @Test
    void findByNameContainsIgnoreCaseAppliesCriteria() {
        List<Artist> result = repository.findByNameContainsIgnoreCase("QUEEN", 0, 10);

        assertFalse(result.isEmpty());
        assertTrue(result.stream()
            .allMatch(a -> a.getName().toLowerCase().contains("queen")));
    }

    @Test
    void savePersistsArtist() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Artist saved = repository.save(
            Artist.builder().name("Test Artist " + System.nanoTime()).build());

        tx.commit();

        assertNotNull(saved.getArtistId());
        assertTrue(repository.existsById(saved.getArtistId()));
    }

    @Test
    void deleteRemovesArtist() {
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Artist saved = repository.save(
            Artist.builder().name("To Delete " + System.nanoTime()).build());
        tx.commit();

        long before = repository.count();

        tx.begin();
        repository.delete(saved);
        tx.commit();

        assertFalse(repository.existsById(saved.getArtistId()));
        assertEquals(before - 1, repository.count());
    }
}
