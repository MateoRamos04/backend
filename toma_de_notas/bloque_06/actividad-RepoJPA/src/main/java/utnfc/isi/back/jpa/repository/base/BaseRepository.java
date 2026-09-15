package utnfc.isi.back.jpa.repository.base;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    List<T> findAll(int offset, int limit);

    default List<T> findAll(PageRequest page) {
        return findAll(page.offset(), page.limit());
    }

    void delete(T entity);

    long count();

    boolean existsById(ID id);
}
