package com.esports;

import com.esports.dao.AdminDAO;
import com.esports.ui.AdminMenu;
import com.esports.ui.UserMenu;
import com.esports.util.ConsoleUtil;
import com.esports.util.DatabaseConnection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseConnection.initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::close));

        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("E-Sports Arena Reservation System");
            System.out.println("  1. Continue as User");
            System.out.println("  2. Admin Login");
            System.out.println("  0. Exit");
            ConsoleUtil.printDivider();

            int choice = ConsoleUtil.promptInt("Choose:");
            switch (choice) {
                case 1 -> new UserMenu().show();
                case 2 -> adminLogin();
                case 0 -> {
                    ConsoleUtil.printInfo("Goodbye!");
                    System.exit(0);
                }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private static void adminLogin() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Admin Login");
        String username = ConsoleUtil.prompt("Username:");
        String password = ConsoleUtil.prompt("Password:");
        try {
            AdminDAO adminDAO = new AdminDAO();
            if (adminDAO.authenticate(username, password)) {
                ConsoleUtil.printSuccess("Login successful. Welcome, " + username + "!");
                ConsoleUtil.pressEnterToContinue();
                new AdminMenu().show();
            } else {
                ConsoleUtil.printError("Invalid credentials.");
                ConsoleUtil.pressEnterToContinue();
            }
        } catch (SQLException e) {
            ConsoleUtil.printError("Database error: " + e.getMessage());
            ConsoleUtil.pressEnterToContinue();
        }
    }
}