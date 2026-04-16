package com.esports.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:esports_arena.db";
    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
        }
        return connection;
    }

    public static void initializeDatabase() throws SQLException {
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS admins (
                    id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS events (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    name       TEXT NOT NULL,
                    game_title TEXT NOT NULL,
                    event_date TEXT NOT NULL,
                    event_time TEXT NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS seats (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    event_id     INTEGER NOT NULL,
                    seat_number  TEXT NOT NULL,
                    is_reserved  INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
                    UNIQUE (event_id, seat_number)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS reservations (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    seat_id      INTEGER NOT NULL UNIQUE,
                    event_id     INTEGER NOT NULL,
                    user_name    TEXT NOT NULL,
                    user_email   TEXT NOT NULL,
                    reserved_at  TEXT NOT NULL DEFAULT (datetime('now')),
                    FOREIGN KEY (seat_id)  REFERENCES seats(id)  ON DELETE CASCADE,
                    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
                )
            """);

            // Seed default admin if not present
            stmt.executeUpdate("""
                INSERT OR IGNORE INTO admins (username, password)
                VALUES ('admin', 'admin123')
            """);
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}