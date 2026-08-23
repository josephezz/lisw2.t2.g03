package co.edu.unicauca.lisw2_t02_g03.access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseInitializer {

    private final DataBaseManager databaseManager;

    public DataBaseInitializer(DataBaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void initialize() {

        String sql = """
            CREATE TABLE IF NOT EXISTS Usuario (
                Login String PRIMARY KEY,
                NombreCompleto String NOT NULL,
                Rol TEXT NOT NULL,
                Estado TEXT NOT NULL,
                Contrasena String NOT NULL
            );
            """;

        try (
            Connection conn = databaseManager.getConnection();
            Statement stmt = conn.createStatement()
        ) {

            stmt.execute(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}