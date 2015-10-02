package input;

import java.time.LocalDateTime;
import java.util.ArrayList;

import logic.data.Task;

public class ViewTaskCommand implements Command {

	private static final int OFFSET_ZERO = 0;
	private static final int OFFSET_ONE = 1;
	private static final String TYPE_FLOATING = "floating";
	private static final String TYPE_DEADLINE= "deadline";
	private static final String TYPE_TIMED = "timed";
	
	private String type = null;
	private String period = null;
	
	public ViewTaskCommand(String type, String period){
		this.type = type;
		this.period = period;
	}
	
	@Override
	public ArrayList<Task> execute() {
		// TODO Auto-generated method stub
		ArrayList<Task> taskList = Task.getTaskList();
		taskList = selectTasksByType(taskList);
		taskList = selectTaskByPeriod(taskList);
		
		return taskList;
	}

	private ArrayList<Task> selectTasksByType(ArrayList<Task> allTask){
		type = type.toLowerCase();
		Class<?> typeOfTask = null;
		//Can use method from Task Controller to retrieve by type
		switch(type){
		case TYPE_FLOATING:
			typeOfTask = FloatingTask.class;
			break;
		case TYPE_DEADLINE :
			typeOfTask = DeadlineTask.class;
			break;
		case TYPE_TIMED :
			typeOfTask = TimedTask.class;
			break;
		case "all": 
		default:
			typeOfTask = Task.class;
		}
		ArrayList<Task> selectedTask = new ArrayList<Task>();
		for(Task t : allTask){
			if(typeOfTask.isInstance(t)){
				selectedTask.add(t);
			}
		}

		return selectedTask;
	}


	private ArrayList<Task> selectTaskByPeriod(ArrayList<Task> allTask){
		ArrayList<Task> selectedTask = new ArrayList<Task>();
		LocalDateTime systemTime = LocalDateTime.now();
		LocalDateTime beforeThisTime = systemTime;
		period = period.toLowerCase();
		switch(period){
		case "tomorrow":
			beforeThisTime = beforeThisTime.withHour(OFFSET_ZERO).withMinute(OFFSET_ZERO).withSecond(OFFSET_ZERO)
							.withNano(OFFSET_ZERO).plusDays(OFFSET_ONE).plusDays(OFFSET_ONE);
			break;

		case "week":
			beforeThisTime = beforeThisTime.withHour(OFFSET_ZERO).withMinute(OFFSET_ZERO).withSecond(OFFSET_ZERO)
							.withNano(OFFSET_ZERO).plusDays(OFFSET_ONE).plusWeeks(OFFSET_ONE);
			break;

		case "month":
			beforeThisTime = beforeThisTime.withHour(OFFSET_ZERO).withMinute(OFFSET_ZERO).withSecond(OFFSET_ZERO)
							.withNano(OFFSET_ZERO).plusDays(OFFSET_ONE).plusMonths(OFFSET_ONE);
			break;

		case "today":
		default:
			beforeThisTime = beforeThisTime.withHour(OFFSET_ZERO).withMinute(OFFSET_ZERO).withSecond(OFFSET_ZERO)
							.withNano(OFFSET_ZERO).plusDays(OFFSET_ONE);
		}
		for(Task t : allTask){
			//LocalDateTime deadline = null;
			if(t.getType().equalsIgnoreCase(TYPE_FLOATING)){
				selectedTask.add(t);
			}else{
				LocalDateTime deadline = t.getEnd();
				if(deadline.isBefore(beforeThisTime)){
					selectedTask.add(t);
				}
			}
		}

		return selectedTask;
	}
}
