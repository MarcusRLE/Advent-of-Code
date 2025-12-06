import java.util.HashMap;
import java.util.Map;

import Days.Day;
import utils.UserPrompter;
import utils.ClassLoader;

public class Main {
    public static final String GREEN_BRIGHT = "\033[92m";
    public static final String RESET = "\033[0m";
    private static Map<Integer, Day[]> days = new HashMap<>();
    public static void main(String[] args) {
        int totalDays = 6;
        if (totalDays == -1) {
            return;
        }
        days = ClassLoader.loadAllDays(totalDays);
        System.out.println("-----");
        while (true) {
            int selectedDay = UserPrompter.getUserSelectedDay(totalDays);
            if (selectedDay == -1) {
                break;
            }
            System.out.println("-----");
            while (true) {
                int testOrTrue = UserPrompter.testOrTrue(selectedDay);
                if (testOrTrue == -1) {
                    break;
                }
                Boolean printLog = UserPrompter.printWithLog();
                solveDay(selectedDay, testOrTrue, printLog);
            }
        }

        
    }     
    
    private static void solveDay(int selectedDay, int testOrTrue, Boolean printLog) {
        Day[] dayInstances = days.get(selectedDay);
        String type = testOrTrue == 1 ? "test" : "true";
        Day day = dayInstances[testOrTrue];

        System.out.println(GREEN_BRIGHT + "\n\n======== RESULT OUTPUT ========\n" + RESET);

        if (!day.isSolved()) day.solve();

        if (printLog) {
            System.out.println(GREEN_BRIGHT + day.getLog() + RESET);
        }

        System.out.println(
            GREEN_BRIGHT +
            "Result Day " + selectedDay + " (" + type + " input): " + day.resultInfo() +
            RESET
        );

        System.out.println(GREEN_BRIGHT + "\n===============================\n\n" + RESET);
    }
}
