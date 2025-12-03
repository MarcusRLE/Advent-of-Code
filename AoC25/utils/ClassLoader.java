package utils;

import java.util.HashMap;
import java.util.Map;

import Days.Day;

public class ClassLoader {
    public static Map<Integer, Day[]> loadAllDays(int dayNumber) {
        Map<Integer, Day[]> days = new HashMap<>();
        if (dayNumber < 0 || dayNumber > 12){
            throw new IllegalArgumentException("Day number must be between 0 and 12");
        }

        for (int i = 1; i <= dayNumber; i++) {
            String simpleName = "Day" + i;          // e.g. "Day1"
            String fqcn       = "Days." + simpleName; // e.g. "Days.Day1"

            Day[] dayInstances = new Day[2];

            try {
                Class<?> dayClass = Class.forName(fqcn);

                dayInstances[0] = (Day) dayClass
                        .getConstructor(String.class)
                        .newInstance("input/" + simpleName + "_input.txt");

                dayInstances[1] = (Day) dayClass
                        .getConstructor(String.class)
                        .newInstance("input/" + simpleName + "_test.txt");

            } catch (Exception e) {
                e.printStackTrace();
            }

            days.put(i, dayInstances); 
        }
        return days;
    }
}
