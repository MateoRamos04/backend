package utnfc.isi.back.jpa.repository;

import utnfc.isi.back.jpa.domain.Track;
import utnfc.isi.back.jpa.repository.base.BaseRepository;

import java.util.List;

public interface TrackRepository extends BaseRepository<Track, Long> {

    List<Track> findByAlbumId(Long albumId, int offset, int limit);

    List<Track> findByNameContainsIgnoreCase(String text, int offset, int limit);
}
