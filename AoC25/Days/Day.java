package Days;
import java.util.List;
import utils.Reader;

public abstract class Day {
    protected List<String> lines;
    protected Boolean isSolved = false;
    protected String log = "";
    protected String resultInfo = "";

    protected Day(String filename) {
        this.lines = Reader.readLines(filename);
    }

    protected abstract void followInstructions();

    public String resultInfo(){
        return resultInfo;
    }
    
    public Boolean isSolved() {
        return isSolved;
    }
    public String getLog() {
        return log;
    }

    protected abstract String logResults();

    public void solve() {
        float startTime = System.currentTimeMillis();
        followInstructions();
        isSolved = true;
        float endTime = System.currentTimeMillis();
        resultInfo += logResults();
        log += "\n------\nSolved in " + (endTime - startTime) + " milliseconds.\n";
    }
}
