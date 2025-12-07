package Days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day7 extends Day {
    int splitCount = 0;
    boolean hasSplit = false;
    Long pathCount = 0L;

    public Day7(String filename) {
        super(filename);
    }
    

    @Override
    protected void followInstructions() {
        partTwo();
    }

    private void partTwo(){
        HashMap<Integer, Long> beamPositions = findFirstBeamList();
        for (int i = 1; i < lines.size(); i++){
            beamPositions = countPaths(beamPositions, lines.get(i));
        }
        for (Long count : beamPositions.values()){
            pathCount += count;
        }


    }

    private void partOne(){
        Set<Integer> beamPositions = findFirstBeam();
        for (int i = 1; i < lines.size(); i++){
            log += "Number of beams at row " + i + ": " + beamPositions.size() + "\n";
            beamPositions = countSplits(beamPositions, lines.get(i));
            //logRow(i, beamPositions);
        }
    }

    private void logRow(int i, Set<Integer> beamPositions){
        char[] line = lines.get(i).toCharArray();
        for (Integer pos : beamPositions){
            line[pos] = '|';
        }
        log += new String(line) + "\n";
    }

    private HashMap<Integer, Long> countPaths(HashMap<Integer, Long> beamIdx, String line){
        HashMap<Integer, Long> beamsIdxCopy = new HashMap<>(beamIdx);
        for (Integer pos : beamIdx.keySet()){
            if(line.charAt(pos) == '^'){
                if(pos > 0){
                    if (!beamsIdxCopy.containsKey(pos - 1)){
                        beamsIdxCopy.put(pos - 1, beamIdx.get(pos));
                    } else {
                        beamsIdxCopy.put(pos - 1, beamsIdxCopy.get(pos - 1) + beamIdx.get(pos));
                    }
                }
                if(pos < line.length() - 1){
                    if (!beamsIdxCopy.containsKey(pos + 1)){
                        beamsIdxCopy.put(pos + 1, beamIdx.get(pos));
                    } else {
                        beamsIdxCopy.put(pos + 1, beamsIdxCopy.get(pos + 1) + beamIdx.get(pos));
                    }
                }
                beamsIdxCopy.remove(pos);
            }
        }
        return beamsIdxCopy;
    }

    private Set<Integer> countSplits(Set<Integer> beamPositions, String line){
        Set<Integer> newBeamPositions = new HashSet<>(beamPositions);
        for (Integer pos : beamPositions){
            if(line.charAt(pos) == '^'){
                splitCount++;
                hasSplit = true;
                if(pos > 0){
                    newBeamPositions.add(pos - 1);
                }
                if(pos < line.length() - 1){
                    newBeamPositions.add(pos + 1);
                }
                newBeamPositions.remove(pos);
            }
        }
        return newBeamPositions;
    }

    private Set<Integer> findFirstBeam(){
        // initiate all beam positions to false
        String firstLine = lines.get(0);
        Set<Integer> beamPostions = new HashSet<>();
        for(int i = 0; i < firstLine.length(); i++){
            if(firstLine.charAt(i) == 'S'){
                beamPostions.add(i);
                continue;
            }
        }
        return beamPostions;
    }

    private HashMap<Integer, Long> findFirstBeamList(){
        // initiate all beam positions to false
        String firstLine = lines.get(0);
        HashMap<Integer, Long> beamPostions = new HashMap<>();
        for(int i = 0; i < firstLine.length(); i++){
            if(firstLine.charAt(i) == 'S'){
                beamPostions.put(i, 1L);
                continue;
            }
        }
        return beamPostions;
    }

    @Override
    protected String logResults() {
        return "\nTotal possible paths: " + pathCount ;
    }

}