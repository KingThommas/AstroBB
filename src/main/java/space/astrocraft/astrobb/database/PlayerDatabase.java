package space.astrocraft.astrobb.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

// TODO: Set up a database
// TODO: Change this to use our custom database

public class PlayerDatabase {
    private static Connection connection;

    /**
     * Connect to the database
     * @throws SQLException This happens when the code couldn't establish a connection to the database.
     * @implSpec You have to use this at the start of the code, in this case in the `onEnable()` method
     */
    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/hectus_playerdata", "marcpg1905", "Mg.19052010");
    }

    /**
     * Disconnect from the database
     * @throws SQLException This happens when the code can't find the database or the connection is corrupted
     * @implSpec You have to use this at the end of the code, in this case in the `onDisable()` method
     */
    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    /**
     * Change or set the value of a field in the database
     * @param player Which player's row you want to modify
     * @param field What field of the row you want to modify
     * @param value What you want the field to be set/changed to
     */
    public static void setField(String player, @NotNull Field field, Object value) {
        String sql = "UPDATE players SET " + field.field + " = ? WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            statement.setString(2, player);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the value of a field
     * @param player Which player's row you want to get the value from
     * @param field What field to get the value from
     * @return Returns the fields value. May be null!
     */
    public static @Nullable Object getField(String player, @NotNull Field field) {
        String sql = "SELECT " + field.field + " FROM players WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return resultSet.getObject(field.field);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Add a value to a field that can contain multiple values. <br>
     * Fields with values that can store multiple data are: <br>
     * - FRIENDS
     * - FRIEND_REQUESTS
     * @param player Which player's row you want to add the value to
     * @param field What field you want to add the value to
     * @param addition What you wanna add
     */
    public static void addToField(String player, Field field, String addition) {
        setField(player, field, getField(player, field) + ", " + addition);
    }

    /**
     * @param player Which player's row you want to remove the value from
     * @param field What field you want to remove the value from
     * @param target What you want to remove
     */
    public static void removeFromField(String player, Field field, String target) {
        String current = (String) getField(player, field);

        if (current == null || !current.contains(", ")) current = null;
        else current = current.replace(target + ", ", "").replace("," + target, "");

        setField(player, field, current);
    }

    /**
     * All valid fields that exist in the player database
     */
    public enum Field {
        UUID("uuid", "VARCHAR(36)", null),
        USERNAME("username", "VARCHAR(255)", null),
        FRIENDS("friends", "TEXT", null),
        FRIEND_REQUESTS("friend_requests", "TEXT", null),
        FIRST_PLAYED("first_played", "DATETIME", null),
        LAST_PLAYED("last_played", "DATETIME", null),
        SS_FIRST_PLAYED("ss_first_played", "DATETIME", null),
        SS_LAST_PLAYED("ss_last_played", "DATETIME", null),
        SS_KILLS("ss_total_kills", "INT(11)", 0),
        SS_DEATHS("ss_total_deaths", "INT(11)", 0),
        SS_WINS("ss_total_wins", "INT(11)", 0),
        SS_POINTS("ss_total_points", "INT(11)", 0),
        SS_ITEMS_COLLECTED("ss_total_items_collected", "INT(11)", 0),
        SS_ELO("ss_elo", "INT(11)", 100);

        public final String field;
        public final String type;
        public final Object defaultValue;

        Field(String f, String t, Object d) {
            field = f;
            type = t;
            defaultValue = d;
        }
    }
}
