package Days;

import java.util.ArrayList;
import java.util.List;

public class Day6 extends Day {
    Operator[] operators;
    Long[] numbers;
    List<Integer> colNumb;
    Long result = 0L;

    public Day6(String filename) {
        super(filename);
    }

    @Override
    protected void followInstructions() {
        partTwo();
    }

    private void partOne(){
        parseOperators();
        calculateCols();
        for (Long num : numbers){
            result += num;
            log += "Adding column with sum " + num + ".\n";
        }
    }

    private void partTwo(){
        List<Long> results = matchOperator();
        for (Long r : results){
            log += "Adding sum: " + r + "\n";
            result += r;
        }
    }

    private String[] sortNumbers(){
        String[] colNumbStr = new String[lines.get(0).length()];
        for (int i = 0; i < lines.size() - 1; i++){
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++){
                if(colNumbStr[j] == null){
                    colNumbStr[j] = "";
                }
                colNumbStr[j] += line.charAt(j);
            }
        }
        return colNumbStr;
    }

    private List<Long> matchOperator(){
        String[] colNumStr = sortNumbers();
        List<Long> results = new ArrayList<>();
        char[] operatorLine = lines.get(lines.size() - 1).toCharArray();
        Operator operator = new Operator(operatorLine[0] + "");
        Long result = 0L;
        
        for(int i = 0; i < lines.get(0).length(); i++){
            if(colNumStr[i].isBlank()){
                continue;
            }
            if(operatorLine[i] != ' '){
                if(i > 0){
                    results.add(result);
                }
                result = strToLong(colNumStr[i]);
                operator = new Operator(operatorLine[i] + "");
                continue;
            }
            result = operator.operate(result, strToLong(colNumStr[i]));
            if(i == lines.get(0).length() - 1){
                results.add(result);
            }
        }
        return results;
    }

    private Long strToLong(String longStr){
        longStr = longStr.replace(" ", "");
        return Long.parseLong(longStr);
    }

    private void parseOperators(){
        String lastLine = lines.get(lines.size() - 1);
        String[] parts = cleanLine(lastLine);
        operators = new Operator[parts.length];
        for (int i = 0; i < parts.length; i++){
            operators[i] = new Operator(parts[i]);
        }
        numbers = new Long[operators.length];
    }

    private void calculateCols(){
        String line = lines.get(0);
        String[] numbersStr = cleanLine(line);
        numbers = new Long[numbersStr.length];
        for (int i = 0; i < numbersStr.length; i++){
            numbers[i] = Long.parseLong(numbersStr[i]);
        }

        for (int i = 1; i < lines.size() - 1; i++){
            numbersStr = cleanLine(lines.get(i));
            applyOperator(numbersStr);
        }
    }

    private void applyOperator(String[] numbersStr){
        for (int i = 0; i < numbersStr.length; i++){
            Long num = Long.parseLong(numbersStr[i]);
            numbers[i] = operators[i].operation.operate(numbers[i], num);
        }   
    }

    private String[] cleanLine(String line){
        line = line.replaceAll("\\s+", " ").trim();
        return line.split(" ");
    }

    @Override
    protected String logResults() {
        return "Total sum: " + result + ".\n";
    }

    private class Operator {
        Operation operation;

        public Operator(String c){
            switch (c) {
                case "*":
                    operation = (a, b) -> a * b;
                    break;
                case "+"    :
                    operation = (a, b) -> a + b;
                    break;
                case "-":
                    operation = (a, b) -> a - b;
                    break;
                case "/":
                    operation = (a, b) -> a / b;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operator: " + c);
            }
        }

        public Long operate(Long a, Long b){
            return operation.operate(a, b);
        }

        private interface Operation{
            Long operate(Long a, Long b);
        }
        
    }
    
}
