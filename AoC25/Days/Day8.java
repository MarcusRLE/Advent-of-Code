package Days;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Day8 extends Day {
    Box[] boxes;
    List<BoxPair> boxPairs = new java.util.ArrayList<>();
    Long result = 1L;
    BoxPair unitingPair;

    public Day8(String filename) {
        super(filename);
    }

    @Override
    protected void followInstructions() {
        partOne();
    }

    private void partOne(){
        cleanData();
        Set<BoxPair> boxPairsSet = pairBoxes();
        boxPairs = new java.util.ArrayList<>(boxPairsSet);
        System.out.println("Total box pairs in list: " + boxPairs.size());
        boxPairs.sort((a, b) -> Float.compare(a.distance, b.distance));
        List<Circuit> circuits = putInCircuits();
        circuits.sort((a, b) -> Integer.compare(b.boxMap.size(), a.boxMap.size()));
        result = (long) circuits.size();
    }

    private void cleanData() {
        boxes = new Box[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            boxes[i] = new Box(x, y, z);
        }
    }

    private Set<BoxPair> pairBoxes() {
        Set<BoxPair> boxPairsSet = new java.util.HashSet<>();
        for (Box box1 : boxes) {
            for (Box box2 : boxes) {
                if (box1 != box2) {
                    float distance = box1.distanceTo(box2);
                    boxPairsSet.add(new BoxPair(box1, box2, distance));
                }
            }
        }
        System.out.println("Total box pairs: " + boxPairsSet.size());
        return boxPairsSet;
    }

    private List<Circuit> putInCircuits() {
        List<Circuit> circuits = new java.util.ArrayList<>();
        int circuitId = 1;
        int addedToCircuit = 0;
        for (int i = 0; i < boxPairs.size(); i++) {
            BoxPair pair = boxPairs.get(i);
            Box box1 = pair.box1;
            Box box2 = pair.box2;
            // log += "Checking pair " + box1.toString() + " and " + box2.toString() + "\n";

            if (box1.circuit == null && box2.circuit == null) {
                Circuit newCircuit = new Circuit("" + circuitId++);
                box1.addToCircuit(newCircuit);
                box2.addToCircuit(newCircuit);
                circuits.add(newCircuit);
                // log += "  - Both boxes added to new circuit with id " + newCircuit.circuitId + ".\n";
            } else if (box1.circuit != null && box2.circuit == null) {
                box2.addToCircuit(box1.circuit);
                unitingPair = pair;
                // log += "  - Box " + box2.toString() + " added to existing circuit with id " + box1.circuit.circuitId + ". Now size " + box1.circuit.boxMap.size() + "\n";
            } else if (box1.circuit == null && box2.circuit != null) {
                box1.addToCircuit(box2.circuit);
                unitingPair = pair;
                // log += "  - Box " + box1.toString() + " added to existing circuit with id " + box2.circuit.circuitId + ". Now size " + box2.circuit.boxMap.size() + "\n";
            } else if (box1.circuit != box2.circuit) {
                // log += "  - Merging circuits " + box1.circuit.circuitId + " (size: " + box1.circuit.boxMap.size() + ") and " + box2.circuit.circuitId + " (size: " + box2.circuit.boxMap.size() + ").\n";
                boolean removed2 = circuits.remove(box2.circuit);
                boolean removed1 = circuits.remove(box1.circuit);
                // log += "    - Removed circuits [" + box1.circuit.circuitId + ", " + box2.circuit.circuitId + "]: [" + removed1 + ", " + removed2 + "].\n";
                Circuit merged = box1.circuit.mergeWith(box2.circuit);
                // log += "    - New merged circuit " + merged.circuitId + " has size " + merged.boxMap.size() + ".\n";
                circuits.add(merged);
                unitingPair = pair;
            } else if (box1.circuit == box2.circuit) {
                // log += "  - Both boxes already in the same circuit. No action taken.\n";
                continue;
            }
            addedToCircuit++;
        }
        return circuits;
    }

    @Override
    protected String logResults() {
        return "Product of uniting pair X values: " + Long.toString((long)((long) unitingPair.box1.x * (long) unitingPair.box2.x)) + "\n";
    }

    private class Box {
        int x;
        int y;
        int z;
        Circuit circuit;

        public Box(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float distanceTo(Box other) {
            return (float) Math.sqrt(Math.pow(this.x - other.x, 2) +
                                     Math.pow(this.y - other.y, 2) +
                                     Math.pow(this.z - other.z, 2));
        }

        public void addToCircuit(Circuit circuit) {
            this.circuit = circuit;
            circuit.addBox(this);
        }

        public String toString() {
            return "[" + x + ", " + y + ", " + z + "]";
        }
    }

    private class BoxPair {
        Box box1;
        Box box2;
        float distance;

        public BoxPair(Box box1, Box box2, float distance) {
            this.box1 = box1;
            this.box2 = box2;
            this.distance = distance;
        }

        public int hashCode() {
            return box1.hashCode() + box2.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof BoxPair)) return false;
            BoxPair other = (BoxPair) obj;
            return (this.box1 == other.box1 && this.box2 == other.box2) ||
                   (this.box1 == other.box2 && this.box2 == other.box1);
        }
    }

    private class Circuit {
        HashMap<Coordinate, Box> boxMap = new HashMap<>();
        String circuitId;

        public Circuit(String circuitId) {
            this.circuitId = circuitId;
        }


        public void addBox(Box box) {
            Coordinate coord = new Coordinate(box.x, box.y, box.z);
            if(boxMap.containsKey(coord)) {
                return;
            }
            boxMap.put(coord, box);
        }

        public Circuit mergeWith(Circuit other) {
            Circuit merged = new Circuit("" + this.circuitId + "+" + other.circuitId);
            for(Box box : this.boxMap.values()) {
                box.addToCircuit(merged);
            }
            for(Box box : other.boxMap.values()) {
                box.addToCircuit(merged);
            }
            return merged;
        }


        private class Coordinate {
            int x;
            int y;
            int z;

            public Coordinate(int x, int y, int z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }
        }
    }
    
}
