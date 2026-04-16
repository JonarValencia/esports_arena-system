package com.esports.dao;

import com.esports.model.Seat;
import com.esports.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    public List<Seat> getSeatsByEvent(int eventId) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE event_id = ? ORDER BY seat_number";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) seats.add(mapRow(rs));
            }
        }
        return seats;
    }

    public Seat getById(int id) throws SQLException {
        String sql = "SELECT * FROM seats WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public Seat getByEventAndNumber(int eventId, String seatNumber) throws SQLException {
        String sql = "SELECT * FROM seats WHERE event_id = ? AND seat_number = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ps.setString(2, seatNumber.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void markReserved(int seatId, boolean reserved) throws SQLException {
        String sql = "UPDATE seats SET is_reserved = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reserved ? 1 : 0);
            ps.setInt(2, seatId);
            ps.executeUpdate();
        }
    }

    private Seat mapRow(ResultSet rs) throws SQLException {
        return new Seat(
            rs.getInt("id"),
            rs.getInt("event_id"),
            rs.getString("seat_number"),
            rs.getInt("is_reserved") == 1
        );
    }
}