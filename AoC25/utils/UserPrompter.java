package utils;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserPrompter {

    private static final Scanner scanner = new Scanner(System.in);

    public static int getUserTotalDays() {
        int totalDays = 0;

        while (true) {
            System.out.print("Enter total number of days (1–12) or 'q' to quit: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                return -1; // Indicate user wants to quit
            }
            try {
                totalDays = Integer.parseInt(input);
                if (totalDays >= 1 && totalDays <= 12) {
                    break;
                } else {
                    System.out.println("❌ Error: total days must be between 1 and 12.");
                }
            } catch (NumberFormatException  e) {
                System.out.println("❌ Error: please enter an integer number.");
            }
        }

        return totalDays;
    }

    public static int getUserSelectedDay(int totalDays){
        int selectedDay = 0;

        while (true) {
            System.out.print("Select a day between 1 and " + totalDays + " or 'q' to quit: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                return -1; // Indicate user wants to quit
            }
            try {
                selectedDay = Integer.parseInt(input);
                if (selectedDay >= 1 && selectedDay <= totalDays) {
                    break;
                } else {
                    System.out.println("❌ Error: selected day must be between 1 and " + totalDays + ".");
                }
            } catch (NumberFormatException  e) {
                System.out.println("❌ Error: please enter an integer number.");
            }
        }

        return selectedDay;
    }

    public static int testOrTrue(int selectedDay){
        int idx = 0;
        while (true) {
            System.out.print(
                "Day " + selectedDay + ": \nEnter: \n" +
                "- '0' for true input \n" +
                "- '1' for test input \n" +
                "- 'q' to select another day\n" +
                "Your input: "
            );
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                return -1; // Indicate user wants to quit
            }
            try {
                idx = Integer.parseInt(input);
                if (idx == 0 || idx == 1) {
                    break;
                } else {
                    System.out.println("❌ Error: input must be 0 or 1.");
                }
            } catch (NumberFormatException  e) {
                System.out.println("❌ Error: please enter an integer number.");
            }
           
        }

        return idx;
        
    }

    public static Boolean printWithLog() {
        while (true) {
            System.out.print("Do you want to see the detailed log? (y/n): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y")) {
                return true;
            } else if (input.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("❌ Error: please enter 'y' or 'n'.");
            }
        }
    }
}
