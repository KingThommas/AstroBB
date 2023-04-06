package net.hectus.hectusblockbattles.Database;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import net.hectus.hectusblockbattles.HectusBlockBattles;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HectusDatabase {

    private DataSource dataSource;

    public void initialize(FileConfiguration config) {
        dataSource = this.initMySQLDataSource(config);

        try {
            this.testConnection(dataSource);
        } catch (SQLException e) {
            HectusBlockBattles.LOGGER.severe("Could not connect to database.");
            HectusBlockBattles.disablePlugin();
        }

        String setup = "";
        try (InputStream stream = HectusBlockBattles.class.getResourceAsStream("/setup.sql")) {
            setup = new String(stream.readAllBytes());
        } catch (IOException e) {
            HectusBlockBattles.LOGGER.severe("Could not read database setup file.");
            HectusBlockBattles.disablePlugin();
        }

        try (Connection conn = dataSource.getConnection()) {
            HectusBlockBattles.LOGGER.info("Executing database setup...");
            for (String query : setup.split(";")) {
                conn.createStatement().execute(query);
            }
        } catch (SQLException e) {
            HectusBlockBattles.LOGGER.info(e.getMessage());
            HectusBlockBattles.LOGGER.severe("Could not execute database setup.");
            HectusBlockBattles.disablePlugin();
        }

        HectusBlockBattles.LOGGER.info("Database initialized.");
    }

    private DataSource initMySQLDataSource(FileConfiguration config) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            HectusBlockBattles.LOGGER.severe("Could not find MySQL database driver.");
            HectusBlockBattles.disablePlugin();
        }

        MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();

        dataSource.setServerName(config.getString("database.host"));
        dataSource.setPortNumber(config.getInt("database.port"));
        dataSource.setDatabaseName(config.getString("database.name"));
        dataSource.setUser(config.getString("database.user"));
        dataSource.setPassword(config.getString("database.password"));

        return dataSource;
    }

    private void testConnection(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            int seconds = 1;
            if (!conn.isValid(seconds)) {
                throw new SQLException("Connection is not valid.");
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void query(String query) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement().execute(query);
        }
    }

    public ResultSet queryResult(String query) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return conn.createStatement().executeQuery(query);
        }
    }

}
