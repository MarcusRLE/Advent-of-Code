package Days;

public class Day3 extends Day {
    private Long sum = 0L;

    public Day3(String filename) {
        super(filename);
    }

    @Override
    protected void followInstructions() {
        proccessLinesPartTwo();
    }

    private void proccessLinesPartOne(){
        for(String line: this.lines){
            sum += findLargestTwoDigit(line);
        }
        resultInfo = "Sum of largest two digit numbers in each line: " + sum;
    }

    private void proccessLinesPartTwo(){
        for(String line: this.lines){
            String largestNumber = findLargestDigit(line, 12, "");
            log += "- Largest twelve digit number in line \"" + line + "\" is: " + largestNumber + "\n";
            sum += Long.parseLong(largestNumber);
        }
        
    }

    private int findLargestTwoDigit(String input){
        char largestChar = 47;
        char secondLargestChar = 46;
        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            if (c > largestChar && i != input.length() - 1){
                largestChar = c;
                secondLargestChar = 46;
            } else if (c > secondLargestChar){
                secondLargestChar = c;  
            }
        }
        log += "- Largest two digits in line \"" + input + "\" are: " + largestChar + secondLargestChar + "\n";
        return Integer.parseInt("" + largestChar + secondLargestChar);
    }

    private String findLargestDigit(String input, int placement, String output){
        char largestChar = 47;
        if (placement < 1) {
            return output;
        }

        int maxDepth = input.length() - placement + 1;
        if (maxDepth <= 0){
            throw new IllegalArgumentException("Placement " + placement + " is larger than input length " + input.length());
        }
        String subInput = input.substring(0, Math.min(input.length(), maxDepth));
        
        int idx = 0;
        for(int i = 0; i < subInput.length(); i++){
            char c = subInput.charAt(i);
            if (c > largestChar){
                largestChar = c;
                idx = i;
            }
        }
        output += largestChar;
        output = findLargestDigit(input.substring(idx+1, input.length()), placement - 1, output);

        return output;
    }
}
