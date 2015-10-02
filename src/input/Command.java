package input;
import java.util.ArrayList;
import logic.data.Task;
import util.Pair;

public interface Command {
	public Pair<ArrayList<Task>,Boolean> execute();
}
