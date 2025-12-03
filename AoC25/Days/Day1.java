package Days;
public class Day1 extends Day {
    private int dialPosition = 50;
    private int count = 0;
    private int additionalCount = 0;
    private String currentInst = "";
    private String calibrated = "";


    public Day1(String filename) {
        super(filename);
    }

    protected void followInstructions(){
        log += "- The dial starts by pointing at " + dialPosition + ".\n";
        for(String instruction : lines){
            calibrated = "";
            Boolean wasZero = (dialPosition == 0);
            turnDial(instruction);
            if(dialPosition < 0 || dialPosition > 99){
                calibrateDial(wasZero);
            }
            if(dialPosition == 0){
                count++;
            }
            count += additionalCount;
            log += "- The dial is rotated " + currentInst + " to point at " + dialPosition + calibrated + ".\n";
        }
        log += "\n";
    }

    private void turnDial(String instruction){
        char direction = instruction.charAt(0);
        int steps = Integer.parseInt(instruction.substring(1));
        additionalCount = Math.floorDiv(steps, 100);
        steps = steps % 100;

        if(direction == 'R'){
            dialPosition += steps;
            currentInst = "R" + steps;
        } else if(direction == 'L'){
            dialPosition -= steps;
            currentInst = "L" + steps;
        }
    }

    private void calibrateDial(Boolean wasZero){
        dialPosition = Math.abs(Math.abs(dialPosition) - 100);
        if(dialPosition !=0 && !wasZero) {
            calibrated = "; during this rotation, it points at 0 once";
            count++;
        }

    }

    public String resultInfo(){
        return "The dial points at 0 a total of " + count + " times.";
    }
}