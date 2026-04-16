package com.esports.ui;

import com.esports.dao.*;
import com.esports.model.*;
import com.esports.util.ConsoleUtil;
import java.sql.SQLException;
import java.util.List;

public class AdminMenu {

    private final EventDAO       eventDAO       = new EventDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    public void show() {
        while (true) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printHeader("Admin Dashboard");
            System.out.println("  1. Add Event");
            System.out.println("  2. Edit Event");
            System.out.println("  3. Delete Event");
            System.out.println("  4. View All Reservation Records");
            System.out.println("  0. Logout");
            ConsoleUtil.printDivider();

            int choice = ConsoleUtil.promptInt("Choose:");
            switch (choice) {
                case 1 -> addEvent();
                case 2 -> editEvent();
                case 3 -> deleteEvent();
                case 4 -> viewAllReservations();
                case 0 -> { return; }
                default -> ConsoleUtil.printError("Invalid option.");
            }
        }
    }

    private void addEvent() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Add New Event");
        String name      = ConsoleUtil.prompt("Event Name:");
        String gameTitle = ConsoleUtil.prompt("Game Title:");
        String date      = ConsoleUtil.prompt("Date (YYYY-MM-DD):");
        String time      = ConsoleUtil.prompt("Time (HH:MM):");

        Event event = new Event(0, name, gameTitle, date, time);
        try {
            eventDAO.addEvent(event);
            ConsoleUtil.printSuccess("Event added with ID " + event.getId() + ". 30 seats generated.");
        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    private void editEvent() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Edit Event");
        try {
            List<Event> events = eventDAO.getAllEvents();
            if (events.isEmpty()) { ConsoleUtil.printInfo("No events found."); ConsoleUtil.pressEnterToContinue(); return; }
            for (Event e : events) System.out.println("  " + e);
            ConsoleUtil.printDivider();

            int id = ConsoleUtil.promptInt("Enter Event ID to edit:");
            Event event = eventDAO.getById(id);
            if (event == null) { ConsoleUtil.printError("Event not found."); ConsoleUtil.pressEnterToContinue(); return; }

            System.out.println("  Leave blank to keep current value.");
            String name  = ConsoleUtil.prompt("New Event Name [" + event.getName() + "]:");
            String game  = ConsoleUtil.prompt("New Game Title [" + event.getGameTitle() + "]:");
            String date  = ConsoleUtil.prompt("New Date [" + event.getEventDate() + "]:");
            String time  = ConsoleUtil.prompt("New Time [" + event.getEventTime() + "]:");

            if (!name.isEmpty()) event.setName(name);
            if (!game.isEmpty()) event.setGameTitle(game);
            if (!date.isEmpty()) event.setEventDate(date);
            if (!time.isEmpty()) event.setEventTime(time);

            eventDAO.updateEvent(event);
            ConsoleUtil.printSuccess("Event updated successfully.");
        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    private void deleteEvent() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("Delete Event");
        try {
            List<Event> events = eventDAO.getAllEvents();
            if (events.isEmpty()) { ConsoleUtil.printInfo("No events found."); ConsoleUtil.pressEnterToContinue(); return; }
            for (Event e : events) System.out.println("  " + e);
            ConsoleUtil.printDivider();

            int id = ConsoleUtil.promptInt("Enter Event ID to delete:");
            String confirm = ConsoleUtil.prompt("This will also delete all seats and reservations. Type 'yes' to confirm:");
            if (!confirm.equalsIgnoreCase("yes")) { ConsoleUtil.printInfo("Deletion cancelled."); ConsoleUtil.pressEnterToContinue(); return; }

            boolean deleted = eventDAO.deleteEvent(id);
            if (deleted) ConsoleUtil.printSuccess("Event deleted.");
            else         ConsoleUtil.printError("Event not found.");
        } catch (SQLException ex) {
            ConsoleUtil.printError("Database error: " + ex.getMessage());
        }
        ConsoleUtil.pressEnterToContinue();
    }

    private void viewAllReservations() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("All Reservation Records");
        try {
            List<Reservation> list = reservationDAO.getAll();
            if (list.isEmpty()) {
                ConsoleUtil.printInfo("No reservations found.");
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
}