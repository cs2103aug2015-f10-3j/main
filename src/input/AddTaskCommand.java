package input;

import java.util.ArrayList;
import logic.data.Task;
import util.Pair;

public class AddTaskCommand implements Command {

	//may change to date time
	private String _startDate, _startTime, _endDate, _endTime;
	private String _taskName;
	//private String _label;
	
	public AddTaskCommand(String taskName) {
		_taskName = taskName;
	}
	
	@Override
<<<<<<< HEAD
	public ArrayList<Task> execute() {
		//Task userTask = new Task(_taskName, _startDate, _startTime, _endDate, _endTime);
		//Task.add(userTask);
		//return new ArrayList<Task>().add(userTask);
=======
	public Pair<ArrayList<Task>,Boolean> execute() {
		// TODO Auto-generated method stub
>>>>>>> d8660f4208c5a6dfcf21559ca5a9e831df86ffc3
		return null;
	}
	
}
