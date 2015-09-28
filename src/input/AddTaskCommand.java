package input;

import java.util.ArrayList;

import logic.data.Task;

public class AddTaskCommand implements Command {

	//may change to date time
	private String _startDate, _startTime, _endDate, _endTime;
	private String _taskName;
	//private String _label;
	
	@Override
	public ArrayList<Task> execute() {
		//Task userTask = new Task(_taskName, _startDate, _startTime, _endDate, _endTime);
		//Task.add(userTask);
		//return new ArrayList<Task>().add(userTask);
		return null;
	}
	
}
