package com.esports.ui;

import com.esports.dao.*;
import com.esports.model.*;
import com.esports.util.ConsoleUtil;

import java.sql.SQLException;
import java.util.List;

public class UserMenu {

    private final EventDAO       eventDAO       = new EventDAO();
    private final SeatDAO        seatDAO        = new SeatDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    public void show() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("E-Sports Arena — User Menu");
            System.out.println("  1. View Events");
            System.out.println("  2. View Seating Layout");
            System.out.println("  3. Reserve a Seat");
            System.out.println("  4. View My Reservations");
            System.out.println("  5. Cancel a Reservation");
            System.out.println("  0. Back to Main Menu");
            ConsoleUtil.printDivider();

            int choice = ConsoleUtil.promptInt("Choose:");
            switch (choice) {
                case 1 -> viewEvents();
                case 2 -> viewSeating();
                case 3 -> reserveSeat();
                case 4 -> viewMyReservations();
                case 5 -> cancelReservation();
                case 0 -> { return; }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    // -----------------------------------------------------------------------
    // 1. View Events
    // -----------------------------------------------------------------------
    private void viewEvents() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Available Events");
        try {
            List<Event> events = eventDAO.getAllEvents();
            if (events.isEmpty()) {
                ConsoleUtil.printInfo("No events scheduled.");
            } else {
                System.out.printf("  %-4s %-25s %-20s %-12s %-8s%n",
                        "ID", "Event Name", "Game", "Date", "Time");
                ConsoleUtil.printDivider();
                for (Event e : events) System.out.println("  " + e);
            }
        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    // -----------------------------------------------------------------------
    // 2. View Seating Layout
    // -----------------------------------------------------------------------
    private void viewSeating() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("View Seating Layout");
        try {
            List<Event> events = eventDAO.getAllEvents();
            if (events.isEmpty()) { ConsoleUtil.printInfo("No events found."); ConsoleUtil.pressEnterToContinue(); return; }
            for (Event e : events) System.out.println("  " + e);
            ConsoleUtil.printDivider();

            int eventId = ConsoleUtil.promptInt("Enter Event ID:");
            Event event = eventDAO.getById(eventId);
            if (event == null) { ConsoleUtil.printError("Event not found."); ConsoleUtil.pressEnterToContinue(); return; }

            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("Seating: " + event.getName());
            List<Seat> seats = seatDAO.getSeatsByEvent(eventId);
            if (seats.isEmpty()) { ConsoleUtil.printInfo("No seats configured."); ConsoleUtil.pressEnterToContinue(); return; }

            // Print grid-style layout
            char currentRow = 0;
            StringBuilder rowBuf = new StringBuilder();
            for (Seat s : seats) {
                char row = s.getSeatNumber().charAt(0);
                if (row != currentRow) {
                    if (currentRow != 0) System.out.println(rowBuf);
                    currentRow = row;
                    rowBuf = new StringBuilder(String.format("  Row %c: ", currentRow));
                }
                String label = s.isReserved()
                    ? ConsoleUtil.RED  + "[" + s.getSeatNumber() + "]" + ConsoleUtil.RESET
                    : ConsoleUtil.GREEN + "[" + s.getSeatNumber() + "]" + ConsoleUtil.RESET;
                rowBuf.append(label).append(" ");
            }
            System.out.println(rowBuf);
            System.out.println();
            System.out.println(ConsoleUtil.GREEN + "  [XX] = Available   " + ConsoleUtil.RED + "[XX] = Reserved" + ConsoleUtil.RESET);
        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    // -----------------------------------------------------------------------
    // 3. Reserve a Seat
    // -----------------------------------------------------------------------
    private void reserveSeat() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Reserve a Seat");
        try {
            List<Event> events = eventDAO.getAllEvents();
            if (events.isEmpty()) { ConsoleUtil.printInfo("No events available."); ConsoleUtil.pressEnterToContinue(); return; }
            for (Event e : events) System.out.println("  " + e);
            ConsoleUtil.printDivider();

            int eventId = ConsoleUtil.promptInt("Enter Event ID:");
            Event event = eventDAO.getById(eventId);
            if (event == null) { ConsoleUtil.printError("Event not found."); ConsoleUtil.pressEnterToContinue(); return; }

            // Show available seats
            List<Seat> seats = seatDAO.getSeatsByEvent(eventId);
            System.out.println("\n  Available seats:");
            seats.stream()
                 .filter(s -> !s.isReserved())
                 .forEach(s -> System.out.print("  " + ConsoleUtil.GREEN + "[" + s.getSeatNumber() + "]" + ConsoleUtil.RESET + " "));
            System.out.println();
            ConsoleUtil.printDivider();

            String seatNum = ConsoleUtil.prompt("Enter Seat Number (e.g. A1):").toUpperCase();
            Seat seat = seatDAO.getByEventAndNumber(eventId, seatNum);
            if (seat == null)         { ConsoleUtil.printError("Seat not found."); ConsoleUtil.pressEnterToContinue(); return; }
            if (seat.isReserved())    { ConsoleUtil.printError("Seat is already reserved."); ConsoleUtil.pressEnterToContinue(); return; }

            String name  = ConsoleUtil.prompt("Your Full Name:");
            String email = ConsoleUtil.prompt("Your Email:");
            if (name.isEmpty() || email.isEmpty()) { ConsoleUtil.printError("Name and email are required."); ConsoleUtil.pressEnterToContinue(); return; }

            // Atomic: mark seat reserved + create reservation record
            seatDAO.markReserved(seat.getId(), true);
            int resId = reservationDAO.create(seat.getId(), eventId, name, email);

            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("Reservation Confirmed!");
            ConsoleUtil.printSuccess("Your seat has been reserved successfully.");
            System.out.println();
            Reservation res = reservationDAO.getById(resId);
            if (res != null) res.printDetails();

        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    // -----------------------------------------------------------------------
    // 4. View My Reservations
    // -----------------------------------------------------------------------
    private void viewMyReservations() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("My Reservations");
        String email = ConsoleUtil.prompt("Enter your Email:");
        try {
            List<Reservation> list = reservationDAO.getByEmail(email);
            if (list.isEmpty()) {
                ConsoleUtil.printInfo("No reservations found for: " + email);
            } else {
                for (Reservation r : list) {
                    ConsoleUtil.printDivider();
                    r.printDetails();
                }
            }
        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    // -----------------------------------------------------------------------
    // 5. Cancel a Reservation
    // -----------------------------------------------------------------------
    private void cancelReservation() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Cancel a Reservation");
        String email = ConsoleUtil.prompt("Enter your Email:");
        try {
            List<Reservation> list = reservationDAO.getByEmail(email);
            if (list.isEmpty()) { ConsoleUtil.printInfo("No reservations found."); ConsoleUtil.pressEnterToContinue(); return; }

            for (Reservation r : list) { ConsoleUtil.printDivider(); r.printDetails(); }
            ConsoleUtil.printDivider();

            int resId = ConsoleUtil.promptInt("Enter Reservation ID to cancel (0 to go back):");
            if (resId == 0) return;

            Reservation target = reservationDAO.getById(resId);
            if (target == null || !target.getUserEmail().equalsIgnoreCase(email)) {
                ConsoleUtil.printError("Reservation not found or does not belong to your email.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }

            String confirm = ConsoleUtil.prompt("Cancel reservation for seat " + target.getSeatNumber() + "? (yes/no):");
            if (!confirm.equalsIgnoreCase("yes")) { ConsoleUtil.printInfo("Cancellation aborted."); ConsoleUtil.pressEnterToContinue(); return; }

            reservationDAO.delete(resId);
            seatDAO.markReserved(target.getSeatId(), false);
            ConsoleUtil.printSuccess("Reservation #" + resId + " cancelled. Seat " + target.getSeatNumber() + " is now available.");

        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }
}