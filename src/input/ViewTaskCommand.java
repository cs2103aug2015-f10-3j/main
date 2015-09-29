package input;

import java.time.LocalDateTime;
import java.util.ArrayList;

import logic.data.Task;

public class ViewTaskCommand implements Command {

	private static final int OFFSET_ZERO = 0;
	private static final int OFFSET_ONE = 1;

	@Override
	public ArrayList<Task> execute() {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Task> selectTasksByType(ArrayList<Task> allTask, String type){
		type = type.toLowerCase();
		Class<?> typeOfTask = null;
		switch(type){
		case "floating":
			//typeOfTask = Task.class;
			break;
		case "deadline" :
			//typeOfTask = Task.class;
			break;
		case "timed" :
			//typeOfTask = Task.class;
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


	private ArrayList<Task> selectTaskByPeriod(ArrayList<Task> allTask, String period){
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
			LocalDateTime deadline = null;
			//LocalDateTime deadline = t.getEnd();
			if(deadline.isBefore(beforeThisTime)){
				selectedTask.add(t);
			}
		}

		return selectedTask;
	}
}
