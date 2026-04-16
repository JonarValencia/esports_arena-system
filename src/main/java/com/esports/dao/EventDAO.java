package com.esports.dao;

import com.esports.model.Event;
import com.esports.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY event_date, event_time";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(mapRow(rs));
            }
        }
        return events;
    }

    public Event getById(int id) throws SQLException {
        String sql = "SELECT * FROM events WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void addEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (name, game_title, event_date, event_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, event.getName());
            ps.setString(2, event.getGameTitle());
            ps.setString(3, event.getEventDate());
            ps.setString(4, event.getEventTime());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int eventId = keys.getInt(1);
                    event.setId(eventId);
                    generateSeats(eventId);
                }
            }
        }
    }

    /** Auto-generates seats A1–A10 and B1–B10 for every new event. */
    private void generateSeats(int eventId) throws SQLException {
        String sql = "INSERT OR IGNORE INTO seats (event_id, seat_number) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            for (char row : new char[]{'A', 'B', 'C'}) {
                for (int num = 1; num <= 10; num++) {
                    ps.setInt(1, eventId);
                    ps.setString(2, row + String.valueOf(num));
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    public void updateEvent(Event event) throws SQLException {
        String sql = "UPDATE events SET name=?, game_title=?, event_date=?, event_time=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, event.getName());
            ps.setString(2, event.getGameTitle());
            ps.setString(3, event.getEventDate());
            ps.setString(4, event.getEventTime());
            ps.setInt(5, event.getId());
            ps.executeUpdate();
        }
    }

    public boolean deleteEvent(int id) throws SQLException {
        String sql = "DELETE FROM events WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Event mapRow(ResultSet rs) throws SQLException {
        return new Event(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("game_title"),
            rs.getString("event_date"),
            rs.getString("event_time")
        );
    }
}