package Days;
import java.util.List;
import utils.Reader;

public abstract class Day {
    protected List<String> lines;
    protected Boolean isSolved = false;
    protected String log = "";

    protected Day(String filename) {
        this.lines = Reader.readLines(filename);
    }

    protected abstract void followInstructions();
    abstract public String resultInfo();
    
    public Boolean isSolved() {
        return isSolved;
    }
    public String getLog() {
        return log;
    }
    public void solve() {
        float startTime = System.currentTimeMillis();
        followInstructions();
        isSolved = true;
        float endTime = System.currentTimeMillis();
        log += "\n------\nSolved in " + (endTime - startTime) + " milliseconds.\n";
    }
}
