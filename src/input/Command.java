package input;
import java.util.ArrayList;
import logic.data.Task;

public interface Command {
	public ArrayList<Task> execute();
}
