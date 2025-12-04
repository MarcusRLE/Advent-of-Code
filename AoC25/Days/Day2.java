package Days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day2 extends Day {
    private List<Range> ranges = new ArrayList<>();
    private Long sum = 0L;

    public Day2(String filename) {
        super(filename);
    }

    @Override
    protected void followInstructions() {
        cleanData();
        instruction2();

    }

    private void instruction1(){
        for(Range range : ranges){
            String start = range.start;
            if(start.length() != 1){
                start = start.substring(0, start.length() / 2);
            } 
            range.findInvalidIds(start);
            sum += range.sum;
            log += range.log();
        }
    }

    private void instruction2(){
        for(Range range : ranges){
            range.findInvalidIdsv2();
            sum += range.sum;
            log += range.log();
        }
    }


    private void cleanData() {
        if(lines.size() != 1){
            System.out.println("Invalid input for Day 2");
            return;
        }
        String[] rangesStr = lines.get(0).split(",");
        ranges = new ArrayList<>();
        for (int i = 0; i < rangesStr.length; i++) {
            String[] parts = rangesStr[i].split("-");
            ranges.add(new Range(parts[0], parts[1]));
        }
    }

    private class Range {
        String start;
        Long end;
        Long diff;
        int digits;
        Set<Long> invalidIds = new HashSet<>();
        Long sum = 0L;

        public Range(String start, String end){
            this.start = start;
            this.end = Long.parseLong(end);
            this.diff = this.end - Long.parseLong(start);
            this.digits = start.length(); 
            
        }
        
        public String log(){
            List<Long> invalidIdsList = new ArrayList<>(this.invalidIds);
            int listSize = invalidIdsList.size();
            String invalidIdsStr = listSize != 1 ? "invalid IDs" : "invalid ID";
            invalidIdsStr += listSize > 0 ? ": " : "";
            for(int i = 0; i < listSize; i++){
                invalidIdsStr += invalidIdsList.get(i);
                if (listSize == 1) {
                    break;
                }
                if(i == listSize -2){
                     invalidIdsStr += " and ";
                } else if(i < listSize -2){
                    invalidIdsStr += ", ";
                }
            } 
            return "- Range from " + start + " to " + end + " has " + listSize + " " + invalidIdsStr + ".\n";
        }

        public void findInvalidIds(String half){
            String nextInvalidStr = half.repeat(2);
            Long nextInvalid = Long.parseLong(nextInvalidStr);
            if(isWithinRange(nextInvalidStr)){
                invalidIds.add(nextInvalid);
                sum += nextInvalid;
            } else if (nextInvalid > end){
                return;
            }
            findInvalidIds(incrementWithOne(half));
        }

        public void findInvalidIdsv2(){
            String currentDigit = "1";
            outer: while(true){
                String nextInvalidStr = currentDigit.repeat(2);
                if(Long.parseLong(nextInvalidStr) > end){
                    break outer;
                }
                String repititions = nextInvalidStr;
                inner: while (true) {
                    if(Long.parseLong(repititions) > end){
                        break inner;
                    }
                    if (isWithinRange(repititions)){
                        Long nextInvalid = Long.parseLong(repititions);
                        invalidIds.add(nextInvalid);
                    }
                    repititions += currentDigit;
                }
                currentDigit = incrementWithOne(currentDigit);
            }

            for(Long invalidId : invalidIds){
                sum += invalidId;
            } 
                    
        }

        private Boolean isWithinRange(String digits){
            Long num = Long.parseLong(digits);
            return num >= Long.parseLong(start) && num <= end;
        }

        private String incrementWithOne(String longStr){
            Long longNum = Long.parseLong(longStr);
            longNum += 1;
            String incrementedStr = longNum.toString();
            return incrementedStr;

        }

        private Boolean isEndInvalid(String end){
            if(end.length() == 1){
                return false;
            }
            String halfEnd = end.substring(0, end.length() / 2);
            String halfEndRepeated = halfEnd.repeat(2);
            return halfEndRepeated.equals(end);

        }

        private Boolean isAQuotient(int divisor){
            String endStr = end.toString();
            int charDiff = endStr.length() - start.length();
            Boolean isQuotient = false;
            for(int i = 0; i <= charDiff; i++){
                if((start.length() + i) % divisor == 0){
                    isQuotient = true;
                    break;
                } 
            }
            return isQuotient;
        }
    }

    @Override
    protected String logResults() {
        return "Total ranges processed: " + ranges.size() + ". Sum of all invalid IDs: " + sum + ".\n";
    }
    
}
