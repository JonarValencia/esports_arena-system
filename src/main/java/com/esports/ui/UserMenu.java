package com.esports.ui;

import com.esports.dao.*;
import com.esports.model.*;
import com.esports.payment.SeatPayment;
import com.esports.util.ConsoleUtil;
import java.sql.SQLException;
import java.util.List;

public class UserMenu {

    // ── Seat price (PHP) ────────────────────────────────────────────────────
    private static final double SEAT_PRICE = 500.00;

    private final EventDAO       eventDAO       = new EventDAO();
    private final SeatDAO        seatDAO        = new SeatDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final PaymentDAO     paymentDAO     = new PaymentDAO();

    // -----------------------------------------------------------------------
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

    // ── 1. View Events ──────────────────────────────────────────────────────
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

    // ── 2. View Seating Layout ──────────────────────────────────────────────
    private void viewSeating() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("View Seating Layout");
        try {
            List<Event> events = eventDAO.getAllEvents();
            if (events.isEmpty()) {
                ConsoleUtil.printInfo("No events found.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }
            for (Event e : events) System.out.println("  " + e);
            ConsoleUtil.printDivider();

            int eventId = ConsoleUtil.promptInt("Enter Event ID:");
            Event event = eventDAO.getById(eventId);
            if (event == null) {
                ConsoleUtil.printError("Event not found.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }

            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("Seating: " + event.getName());
            List<Seat> seats = seatDAO.getSeatsByEvent(eventId);
            if (seats.isEmpty()) {
                ConsoleUtil.printInfo("No seats configured.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }

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
                    ? ConsoleUtil.RED   + "[" + s.getSeatNumber() + "]" + ConsoleUtil.RESET
                    : ConsoleUtil.GREEN + "[" + s.getSeatNumber() + "]" + ConsoleUtil.RESET;
                rowBuf.append(label).append(" ");
            }
            System.out.println(rowBuf);
            System.out.println();
            System.out.println(ConsoleUtil.GREEN + "  [XX] = Available   "
                    + ConsoleUtil.RED + "[XX] = Reserved" + ConsoleUtil.RESET);
        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    // ── 3. Reserve a Seat (with integrated payment) ─────────────────────────
    private void reserveSeat() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Reserve a Seat");
        try {
            // ── Step 1: pick event ──────────────────────────────────────────
            List<Event> events = eventDAO.getAllEvents();
            if (events.isEmpty()) {
                ConsoleUtil.printInfo("No events available.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }
            for (Event e : events) System.out.println("  " + e);
            ConsoleUtil.printDivider();

            int eventId = ConsoleUtil.promptInt("Enter Event ID:");
            Event event = eventDAO.getById(eventId);
            if (event == null) {
                ConsoleUtil.printError("Event not found.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }

            // ── Step 2: pick seat ───────────────────────────────────────────
            List<Seat> seats = seatDAO.getSeatsByEvent(eventId);
            System.out.println("\n  Available seats:");
            seats.stream().filter(s -> !s.isReserved())
                 .forEach(s -> System.out.print(
                     "  " + ConsoleUtil.GREEN + "[" + s.getSeatNumber() + "]"
                     + ConsoleUtil.RESET + " "));
            System.out.println();
            ConsoleUtil.printDivider();

            String seatNum = ConsoleUtil.prompt("Enter Seat Number (e.g. A1):").toUpperCase();
            Seat seat = seatDAO.getByEventAndNumber(eventId, seatNum);
            if (seat == null) {
                ConsoleUtil.printError("Seat not found.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }
            if (seat.isReserved()) {
                ConsoleUtil.printError("Seat is already reserved.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }

            // ── Step 3: user info ───────────────────────────────────────────
            String name  = ConsoleUtil.prompt("Your Full Name:");
            String email = ConsoleUtil.prompt("Your Email:");
            if (name.isEmpty() || email.isEmpty()) {
                ConsoleUtil.printError("Name and email are required.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }

            // ── Step 4: PAYMENT METHOD SELECTION ───────────────────────────
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("Payment");
            ConsoleUtil.printInfo(String.format("Seat Price: PHP %,.2f", SEAT_PRICE));
            System.out.println();
            System.out.println("  Select Payment Method:");
            System.out.println("  1. Credit / Debit Card");
            System.out.println("  2. GCash");
            System.out.println("  3. Cash");
            ConsoleUtil.printDivider();

            int payChoice = ConsoleUtil.promptInt("Choose Payment Method:");
            String paymentMethod = switch (payChoice) {
                case 1  -> "Credit/Debit Card";
                case 2  -> "GCash";
                case 3  -> "Cash";
                default -> { ConsoleUtil.printError("Invalid choice — defaulting to Cash."); yield "Cash"; }
            };

            // ── Step 5: DISCOUNT CODE ───────────────────────────────────────
            System.out.println();
            System.out.println("  Discount Codes:");
            System.out.println("  STUDENT10 — PHP 50.00 off");
            System.out.println("  VIP20     — PHP 100.00 off");
            String discountCode = ConsoleUtil.prompt("Enter Discount Code (Enter to skip):");
            double discount = switch (discountCode.trim().toUpperCase()) {
                case "STUDENT10" -> 50.0;
                case "VIP20"     -> 100.0;
                default          -> 0.0;
            };
            if (discount > 0)
                ConsoleUtil.printSuccess("Discount code applied! PHP " + discount + " off.");
            else
                ConsoleUtil.printInfo("No discount applied.");

            // ── Step 6: enter balance ───────────────────────────────────────
            System.out.println();
            double balance = ConsoleUtil.promptDouble(
                    "Enter your " + paymentMethod + " available balance (PHP):");

            // ── Step 7: create reservation (hold seat) ──────────────────────
            seatDAO.markReserved(seat.getId(), true);
            int resId = reservationDAO.create(seat.getId(), eventId, name, email);

            // ── Step 8: PROCESS PAYMENT ─────────────────────────────────────
            String txnId  = "TXN" + System.currentTimeMillis();
            SeatPayment payment = new SeatPayment(
                name, txnId, SEAT_PRICE,
                true,        // hasValidPaymentMethod
                discount, balance,
                paymentMethod,
                seat.getSeatNumber(),
                event.getName()
            );

            System.out.println();
            ConsoleUtil.printDivider();
            payment.processInvoice();   // ← PaymentFramework method (unchanged)
            ConsoleUtil.printDivider();

            // ── Step 9: evaluate result ─────────────────────────────────────
            if (!payment.isPaymentSuccess()) {
                // Roll back reservation
                reservationDAO.delete(resId);
                seatDAO.markReserved(seat.getId(), false);
                System.out.println();
                ConsoleUtil.printError("Reservation cancelled — payment failed.");
                ConsoleUtil.printInfo("Please check your balance and try again.");

                // Still save a FAILED transaction record for audit
                paymentDAO.saveTransaction(
                    resId, txnId, name, email,
                    seat.getSeatNumber(), event.getName(),
                    paymentMethod, SEAT_PRICE, discount,
                    payment.getCapturedVAT(), payment.getCapturedFinal(),
                    "FAILED"
                );

            } else {
                // Save SUCCESS transaction
                paymentDAO.saveTransaction(
                    resId, txnId, name, email,
                    seat.getSeatNumber(), event.getName(),
                    paymentMethod, SEAT_PRICE, discount,
                    payment.getCapturedVAT(), payment.getCapturedFinal(),
                    "SUCCESS"
                );

                // ── Step 10: PAYMENT RECEIPT ────────────────────────────────
                ConsoleUtil.clearScreen();
                ConsoleUtil.printHeader("Payment Receipt & Reservation Confirmed!");
                ConsoleUtil.printSuccess("Your seat has been reserved and payment processed.");
                System.out.println();
                System.out.printf("  %-20s %s%n",  "Transaction ID:",  txnId);
                System.out.printf("  %-20s %s%n",  "Name:",            name);
                System.out.printf("  %-20s %s%n",  "Email:",           email);
                System.out.printf("  %-20s %s%n",  "Event:",           event.getName());
                System.out.printf("  %-20s %s %s%n","Date/Time:",
                        event.getEventDate(), event.getEventTime());
                System.out.printf("  %-20s %s%n",  "Seat:",            seat.getSeatNumber());
                System.out.printf("  %-20s %s%n",  "Payment Method:",  paymentMethod);
                ConsoleUtil.printDivider();
                System.out.printf("  %-22s PHP %,.2f%n", "Base Price (VAT incl.):", SEAT_PRICE);
                System.out.printf("  %-22s PHP %,.2f%n", "Discount:",               discount);
                System.out.printf("  %-22s PHP %,.2f%n", "TOTAL PAID:",             payment.getCapturedFinal());
                ConsoleUtil.printDivider();
                // VAT is INCLUSIVE — already inside the total, not added on top
                System.out.printf("  %-22s PHP %,.2f%n", "VAT incl. (12%%):",       payment.getCapturedVAT());
                System.out.printf("  %-22s PHP %,.2f%n", "Amount ex-VAT:",
                        payment.getCapturedFinal() - payment.getCapturedVAT());
                ConsoleUtil.printDivider();
                System.out.printf("  %-22s PHP %,.2f%n", "Remaining Balance:",
                        balance - payment.getCapturedFinal());
                System.out.println();
                ConsoleUtil.printSuccess("Status: PAYMENT SUCCESSFUL");
            }

        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    // ── 4. View My Reservations ─────────────────────────────────────────────
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

    // ── 5. Cancel a Reservation ─────────────────────────────────────────────
    private void cancelReservation() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Cancel a Reservation");
        String email = ConsoleUtil.prompt("Enter your Email:");
        try {
            List<Reservation> list = reservationDAO.getByEmail(email);
            if (list.isEmpty()) {
                ConsoleUtil.printInfo("No reservations found.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }
            for (Reservation r : list) { ConsoleUtil.printDivider(); r.printDetails(); }
            ConsoleUtil.printDivider();

            int resId = ConsoleUtil.promptInt("Enter Reservation ID to cancel (0 = back):");
            if (resId == 0) return;

            Reservation target = reservationDAO.getById(resId);
            if (target == null || !target.getUserEmail().equalsIgnoreCase(email)) {
                ConsoleUtil.printError("Reservation not found or does not belong to your email.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }

            String confirm = ConsoleUtil.prompt(
                "Cancel seat " + target.getSeatNumber() + "? (yes/no):");
            if (!confirm.equalsIgnoreCase("yes")) {
                ConsoleUtil.printInfo("Cancellation aborted.");
                ConsoleUtil.pressEnterToContinue();
                return;
            }

            reservationDAO.delete(resId);
            seatDAO.markReserved(target.getSeatId(), false);
            ConsoleUtil.printSuccess("Reservation #" + resId + " cancelled. Seat "
                    + target.getSeatNumber() + " is now available.");

        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }
}