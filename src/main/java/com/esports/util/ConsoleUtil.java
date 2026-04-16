package com.esports.util;

import java.util.Scanner;

public class ConsoleUtil {

    public static final String RESET  = "\u001B[0m";
    public static final String BOLD   = "\u001B[1m";
    public static final String CYAN   = "\u001B[36m";
    public static final String GREEN  = "\u001B[32m";
    public static final String RED    = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";

    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleUtil() {}

    public static void printHeader(String title) {
        String border = "=".repeat(50);
        System.out.println(CYAN + BOLD + border);
        System.out.printf("  %s%n", title.toUpperCase());
        System.out.println(border + RESET);
    }

    public static void printSuccess(String msg) {
        System.out.println(GREEN + "[✔] " + msg + RESET);
    }

    public static void printError(String msg) {
        System.out.println(RED + "[✘] " + msg + RESET);
    }

    public static void printInfo(String msg) {
        System.out.println(YELLOW + "[i] " + msg + RESET);
    }

    public static void printDivider() {
        System.out.println("-".repeat(50));
    }

    public static String prompt(String label) {
        System.out.print(BOLD + label + RESET + " ");
        return scanner.nextLine().trim();
    }

    public static int promptInt(String label) {
        while (true) {
            String input = prompt(label);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printError("Please enter a valid number.");
            }
        }
    }

    public static void pressEnterToContinue() {
        prompt("\nPress ENTER to continue...");
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}