package utnfc.isi.back.jpa.repository.base;

/**
 * offset = registros que se omiten; limit = máximo a recuperar.
 */
public record PageRequest(int offset, int limit) {

    public PageRequest {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException(
                "Valores de paginación inválidos"
            );
        }
    }

    public static PageRequest ofPage(int page, int size) {
        return new PageRequest(page * size, size);
    }
}
