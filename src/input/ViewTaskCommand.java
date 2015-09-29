package input;

import java.util.ArrayList;

import logic.data.Task;

public class ViewTaskCommand implements Command {

	@Override
	public ArrayList<Task> execute() {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Task> selectTasksByType(ArrayList<Task> allTask, Class<?> typeOfTask){
		ArrayList<Task> selectedTask = new ArrayList<Task>();
		for(Task t : allTask){
			if(typeOfTask.isInstance(t)){
				selectedTask.add(t);
			}
		}

		return selectedTask;
	}

}
