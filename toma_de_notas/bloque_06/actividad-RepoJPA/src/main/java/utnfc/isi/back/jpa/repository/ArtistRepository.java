package utnfc.isi.back.jpa.repository;

import utnfc.isi.back.jpa.domain.Artist;
import utnfc.isi.back.jpa.repository.base.BaseRepository;

import java.util.List;

public interface ArtistRepository extends BaseRepository<Artist, Long> {

    List<Artist> findByNameContainsIgnoreCase(String text, int offset, int limit);
}
