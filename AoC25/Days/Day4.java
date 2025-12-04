package Days;

import java.util.HashMap;
import java.util.Set;

public class Day4 extends Day{
    Node[][] nodes;
    HashMap<Integer[], Node> rolls = new HashMap<>();
    int totalRolls = 0;
    int invalidRolls = 0;
    int totalRemoved = 0;
    final char validRollChar = 'x';
    final char invalidRollChar = '@';
    int iterations = 1;


    public Day4(String filename) {
        super(filename);
    }


    @Override
    protected void followInstructions() {
        createGrid();
        log += "Initial grid:\n";
        for (String line : this.lines){
            log += line + "\n";
        }
        log += "\n";
        fillGrid();
        partTwoInst();
    }

    private Boolean removeRolls(){
        Boolean removedAny = false;
        HashMap<Integer[], Node> rollsCopy = new HashMap<>(rolls);
        for (Node roll : rolls.values()){
            if(roll.isRemovable){
                roll.imOut();
                totalRemoved++;
                rollsCopy.remove(roll.position);
                removedAny = true;
            }
        }
        rolls = rollsCopy;
        return removedAny;
    }

    private void partOneInst(){
        removeRolls();
        logGrid();
    }

    private void partTwoInst(){
        Boolean breakLoop = true;
        while (breakLoop){
            breakLoop = removeRolls();
        }
    }

    private void logGrid(){
        for (Node[] row : nodes){
            for (Node node : row){
                log += node.c;
            }
            log += "\n";
        }
        log += "\n";
    }

    private void createGrid(){
        int rows = this.lines.size();
        int cols = this.lines.get(0).length();
        nodes = new Node[rows][cols];
    }

    private void fillGrid(){
        int rows = nodes.length;
        int cols = nodes[0].length;
        nodes[0][0] = new Node(new Integer[]{0,0}, this.lines.get(0).charAt(0));
        for (int y = 0; y < rows; y++){
            String row = this.lines.get(y);
            for (int x = 0; x < cols; x++){
                Node current = nodes[y][x];
                if (x != cols - 1){
                    nodes[y][x + 1] = new Node(new Integer[]{y,x + 1}, row.charAt(x + 1)); 
                } else if(y != rows - 1){
                    nodes[y + 1][0] = new Node(new Integer[]{y + 1,0}, this.lines.get(y + 1).charAt(0));
                }
                proposeNeighbours(current);
            }

        }
    }

    private boolean inBounds(int y, int x) {
        return y >= 0 && y < nodes.length && x >= 0 && x < nodes[0].length;
    }

    private void proposeNeighbours(Node node) {
        if (!isRoll(node.c)) return;

        int y = node.position[0];
        int x = node.position[1];

        int[][] offsets = {{-1,-1}, {-1,0}, {0,-1}, {-1,1}};

        for (int[] d : offsets) {
            int ny = y + d[0];
            int nx = x + d[1];

            if (!inBounds(ny, nx)) continue;

            nodes[ny][nx].proposeNeighbour(node, true);
        }
    }

    protected Boolean isRoll(char c){
        return c == invalidRollChar || c == validRollChar;
    }
    
    class Node {
        char c;
        Set<Node> rollNeighbours = new java.util.HashSet<>();
        Integer[] position;
        Boolean isRemovable;
        Boolean isValidRoll;

        public Node(Integer[] position, char c) {
            if(isRoll(c)){
                isValidRoll = true;
                c = validRollChar;
                rolls.put(position, this);
            } else {
                isValidRoll = false;
            }
            isRemovable = true;
            this.position = position;
            this.c = c;
        }

        


        public void proposeNeighbour(Node neighbour, Boolean viceVersa) {
            if (!isRoll(c)){
                return;
            }
            this.rollNeighbours.add(neighbour);
            if (viceVersa){
                neighbour.proposeNeighbour(this, false);
            }
            if (rollNeighbours.size() >= 4 && isValidRoll){
                isValidRoll = false;
                isRemovable = false;
                c = invalidRollChar;
            }
        }

        public Boolean reset(){
            Boolean stillValid = false;
            if(c == validRollChar){
                c = '.';
                isValidRoll = false;
                stillValid = true;
            } else if (c == invalidRollChar){
                c = validRollChar;
                isValidRoll = true;
            }
            rollNeighbours = new java.util.HashSet<>();
            return stillValid;
        }

        public void removeNeighbour(Node neighbour) {
            this.rollNeighbours.remove(neighbour);
            if (rollNeighbours.size() < 4 && !isValidRoll){
                isRemovable = true;
                isValidRoll = true;
                c = validRollChar;
            }
        }

        public void imOut(){
            for (Node neighbour : rollNeighbours){
                neighbour.removeNeighbour(this);
            }
            c = '.';
        }

    }

    @Override
    protected String logResults() {
        return "Total rolls removed: " + totalRemoved + ".";
    }

}
