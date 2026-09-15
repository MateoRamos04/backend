package utnfc.isi.back.jpa.repository;

import utnfc.isi.back.jpa.domain.Album;
import utnfc.isi.back.jpa.repository.base.BaseRepository;

import java.util.List;

public interface AlbumRepository extends BaseRepository<Album, Long> {

    List<Album> findByArtistId(Long artistId, int offset, int limit);
}
