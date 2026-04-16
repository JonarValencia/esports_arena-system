package com.esports.dao;

import com.esports.model.Reservation;
import com.esports.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private static final String JOIN_SQL =
        """
        SELECT r.*, s.seat_number, e.name AS event_name,
               e.event_date, e.event_time
        FROM reservations r
        JOIN seats  s ON r.seat_id  = s.id
        JOIN events e ON r.event_id = e.id
        """;

    public List<Reservation> getAll() throws SQLException {
        List<Reservation> list = new ArrayList<>();
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(JOIN_SQL + " ORDER BY r.reserved_at DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Reservation> getByEmail(String email) throws SQLException {
        List<Reservation> list = new ArrayList<>();
        String sql = JOIN_SQL + " WHERE r.user_email = ? ORDER BY r.reserved_at DESC";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Reservation getById(int id) throws SQLException {
        String sql = JOIN_SQL + " WHERE r.id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int create(int seatId, int eventId, String userName, String userEmail) throws SQLException {
        String sql = """
            INSERT INTO reservations (seat_id, event_id, user_name, user_email)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, seatId);
            ps.setInt(2, eventId);
            ps.setString(3, userName);
            ps.setString(4, userEmail.toLowerCase());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public boolean delete(int reservationId) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            return ps.executeUpdate() > 0;
        }
    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getInt("id"));
        r.setSeatId(rs.getInt("seat_id"));
        r.setEventId(rs.getInt("event_id"));
        r.setUserName(rs.getString("user_name"));
        r.setUserEmail(rs.getString("user_email"));
        r.setReservedAt(rs.getString("reserved_at"));
        r.setSeatNumber(rs.getString("seat_number"));
        r.setEventName(rs.getString("event_name"));
        r.setEventDate(rs.getString("event_date"));
        r.setEventTime(rs.getString("event_time"));
        return r;
    }
}