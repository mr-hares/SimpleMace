package ctx.revoid.simpleMace.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static ctx.revoid.simpleMace.SimpleMace.*;

public class DataBase {
    private Connection connection;

    public DataBase(Plugin plugin) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() +
                    "/database.db");

            try (Statement stmt = connection.createStatement()) {
                stmt.execute(String.format("CREATE TABLE IF NOT EXISTS %s (" +
                        "uuid TEXT NOT NULL," +
                        "name TEXT NOT NULL," +
                        "date TEXT NOT NULL)", "maces"));
            }
            sendConsole("(SimpleMace) Connection to the database has been completed");
        } catch (SQLException e) {
            sendConsole("(SimpleMace) &cDatabase connection error");
        }
    }

    public void addMace(Player player) {
        String sql = "INSERT INTO maces (uuid, name, date) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, player.getName());

            LocalDateTime now = LocalDateTime.now();
            String date = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(now);
            stmt.setString(3, date);

            stmt.executeUpdate();
        } catch (SQLException ignored) {}
    }

    public List<MaceDate> getMaces() {
        String sql = "SELECT * FROM maces";
        List<MaceDate> maces = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                maces.add(new MaceDate(rs.getString("uuid"), rs.getString("name"), rs.getString("date")));
            }
        } catch (SQLException ignored) {
        }

        return maces;
    }
}