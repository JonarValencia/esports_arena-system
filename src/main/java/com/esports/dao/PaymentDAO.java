package com.esports.dao;

import com.esports.model.Transaction;
import com.esports.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public void saveTransaction(int reservationId, String transactionId,
            String userName, String userEmail, String seatNumber,
            String eventName, String paymentMethod, double amount,
            double discount, double vatAmount, double finalAmount,
            String status) throws SQLException {

        String sql = """
            INSERT INTO transactions
              (reservation_id, transaction_id, user_name, user_email,
               seat_number, event_name, payment_method, amount,
               discount, vat_amount, final_amount, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps =
                 DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1,    reservationId);
            ps.setString(2, transactionId);
            ps.setString(3, userName);
            ps.setString(4, userEmail.toLowerCase());
            ps.setString(5, seatNumber);
            ps.setString(6, eventName);
            ps.setString(7, paymentMethod);
            ps.setDouble(8, amount);
            ps.setDouble(9, discount);
            ps.setDouble(10, vatAmount);
            ps.setDouble(11, finalAmount);
            ps.setString(12, status);
            ps.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public double getTotalEarnings() throws SQLException {
        String sql =
            "SELECT COALESCE(SUM(final_amount),0) FROM transactions WHERE status='SUCCESS'";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    public int getTotalSuccessCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM transactions WHERE status='SUCCESS'";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getInt("id"));
        t.setReservationId(rs.getInt("reservation_id"));
        t.setTransactionId(rs.getString("transaction_id"));
        t.setUserName(rs.getString("user_name"));
        t.setUserEmail(rs.getString("user_email"));
        t.setSeatNumber(rs.getString("seat_number"));
        t.setEventName(rs.getString("event_name"));
        t.setPaymentMethod(rs.getString("payment_method"));
        t.setAmount(rs.getDouble("amount"));
        t.setDiscount(rs.getDouble("discount"));
        t.setVatAmount(rs.getDouble("vat_amount"));
        t.setFinalAmount(rs.getDouble("final_amount"));
        t.setStatus(rs.getString("status"));
        t.setCreatedAt(rs.getString("created_at"));
        return t;
    }
}