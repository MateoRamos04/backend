package utnfc.isi.back.jpa.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Crea e inicializa Chinook en H2 ejecutando los scripts de src/main/resources/sql.
 * Usa la MISMA URL JDBC que persistence.xml; si no, JPA vería otra base vacía.
 */
public final class DataInitializer {

    public static final String JDBC_URL =
        "jdbc:h2:mem:chinook;DB_CLOSE_DELAY=-1;MODE=PostgreSQL";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static final List<String> SCRIPTS = List.of(
        "sql/01_chinook_tables.sql",
        "sql/02_chinook_data.sql",
        "sql/03_chinook_constraints_indexes.sql",
        "sql/03_chinook_sequences.sql"
    );

    private static boolean initialized = false;

    private DataInitializer() {
    }

    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             Statement st = conn.createStatement()) {

            if (!schemaExists(st)) {
                for (String script : SCRIPTS) {
                    st.execute("RUNSCRIPT FROM 'classpath:/" + script + "' CHARSET 'UTF-8'");
                }
            }
            initialized = true;

        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo inicializar Chinook", e);
        }
    }

    private static boolean schemaExists(Statement st) throws SQLException {
        try (ResultSet rs = st.executeQuery(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = 'ARTIST'")) {
            rs.next();
            return rs.getInt(1) > 0;
        }
    }
}
