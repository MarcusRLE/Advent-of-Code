package Days;

import java.util.List;
import java.util.ArrayList;

public class Day5 extends Day {
    List<Long> freshIngredients = new ArrayList<>();
    List<Range> ingredientRanges = new ArrayList<>();
    Long totalIngredients = 0L;

    public Day5(String filename) {
        super(filename);
    }

    @Override
    protected void followInstructions() {
        partTwo();
    }

    private void partOne(){
        Boolean isRange = true;
        for (String line : lines){
            if (line.isBlank()) {
                isRange = false;
                continue;
            }
            if(isRange){
                String[] parts = line.split("-");
                Long lower = Long.parseLong(parts[0]);
                Long upper = Long.parseLong(parts[1]);
                ingredientRanges.add(new Range(lower, upper));
            } else {
                Long id = Long.parseLong(line);
                for (Range range : ingredientRanges){
                    Boolean isWithin = id >= range.lower && id <= range.upper;
                    if(isWithin){
                        log += "- Ingredient ID " + id + " fits within range " + range.toString() + ".\n";
                        freshIngredients.add(id);
                        break;
                    }
                }
            }
        }
    }

    public void partTwo(){
        seperateData();
        while (mergeSortedRanges());
        logRanges();
    }

    private void logRanges(){
        for (Range range : ingredientRanges){
            Long diff = range.upper - range.lower + 1;
            if(diff < 0){
                log += "--- WARNING: Negative range detected: " + range.toString() + "---\n";
            }
            totalIngredients += diff;
            log += "- Range: " + range.toString() + " with " + diff + "fresh ingredients\n";
        }
        log += "Total seperate ranges: " + ingredientRanges.size() + "\n";
    }

    private Boolean mergeSortedRanges(){
        ingredientRanges.sort(new RangeComparator());
        List<Range> copy = new ArrayList<>(ingredientRanges);
        for (int i = 0; i < ingredientRanges.size(); i++){
            Range current = ingredientRanges.get(i);
            if(i < ingredientRanges.size() - 1){
                Range next = ingredientRanges.get(i + 1);
                if(current.upper >= next.lower - 1){
                    current.upper = Math.max(current.upper, next.upper);
                    copy.remove(i + 1);
                    ingredientRanges = copy;
                    return true;
                }
            }
            if(i > 0){
                Range previous = ingredientRanges.get(i - 1);
                if(current.lower <= previous.upper + 1){
                    current.lower = Math.min(current.lower, previous.lower);
                    copy.remove(i - 1);                    
                    ingredientRanges = copy;
                    return true;
                }
            }
        }
        return false;
    }

    private void seperateData(){
        for (String line : lines){
            if(line.isBlank()){
                return;
            }
            String[] parts = line.split("-");
            Long lower = Long.parseLong(parts[0]);
            Long upper = Long.parseLong(parts[1]);
            ingredientRanges.add(new Range(lower, upper));
        }
    }

    private void insertIntoRanges(Long lower, Long upper){
        Boolean noRangeFitted = true;
        Range newRange = new Range(lower, upper);
        List<Range> copyRanges = new ArrayList<>(ingredientRanges);
        for (int i = 0; i < ingredientRanges.size(); i++){
            Range range = ingredientRanges.get(i);
            RangeStatus status = range.fit(newRange);
            switch (status) {
                case OVERLAPFULL:
                    copyRanges.remove(i);
                    copyRanges.add(i, range);
                    noRangeFitted = false;
                    return;
                case INSIDE:
                    noRangeFitted = false;
                    return;
                case OUTSIDE:
                    break;
                case OVERLAPSTART:
                    noRangeFitted = false;
                    return;
                case OVERLAPEND:
                    noRangeFitted = false;
                    return;
                default:
                    break;
            }
        }
        if (noRangeFitted){
            copyRanges.add(newRange);
        }
        ingredientRanges = copyRanges;
    }


    @Override
    protected String logResults() {
        int count = freshIngredients.size();
        return "Total fresh ingredients: " + totalIngredients + ".";
    }
    
    private class Range implements Comparable<Range>{
        Long lower;
        Long upper;

        public Range(Long lower, Long upper){
            if(lower > upper){
                throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
            }
            if(lower == upper){
                System.out.println("Warning: Lower bound is equal to upper bound.");
            }   
            this.lower = lower;
            this.upper = upper;
        }

        public RangeStatus fit(Range other){
            if(this.lower <= other.lower && this.upper >= other.upper){
                return RangeStatus.INSIDE;
            }
            if(this.upper < other.lower || this.lower > other.upper){
                return RangeStatus.OUTSIDE;
            }
            if(this.lower >= other.lower && this.upper <= other.upper){
                return RangeStatus.OVERLAPFULL;
            }
            if(this.lower > other.lower){
                this.lower = other.lower;
                return RangeStatus.OVERLAPSTART;
            }
            if(this.upper < other.upper){
                this.upper = other.upper;
                return RangeStatus.OVERLAPEND;
            }
            throw new IllegalStateException("Unreachable state reached when fitting " + this.toString() + " and " + other.toString() + " ranges.");


        }

        @Override
        public String toString(){
            return "[" + lower + ", " + upper + "]";
        }

        @Override
        public int compareTo(Range arg0) {
            return Long.compare(this.lower, arg0.lower);
        }
    }

    private enum RangeStatus {
        INSIDE,
        OUTSIDE,
        OVERLAPSTART,
        OVERLAPEND,
        OVERLAPFULL
    }

    private class RangeComparator implements java.util.Comparator<Range> {
        @Override
        public int compare(Range r1, Range r2) {
            return r1.compareTo(r2);
        }
    }
}