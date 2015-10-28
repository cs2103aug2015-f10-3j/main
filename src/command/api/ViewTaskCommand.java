package command.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import task.api.TaskController;
import task.entity.DeadlineTask;
import task.entity.FloatingTask;
import task.entity.Task;
import task.entity.TimedTask;
import task.entity.Task.TASK_TYPE;

public class ViewTaskCommand extends Command {

	/*** Variables ***/
	private static final int OFFSET_ZERO = 0;
	private static final int OFFSET_ONE = 1;
	
	private static final String TYPE_ALL = "all";
	private static final String TYPE_FLOATING = "floating";
	private static final String TYPE_DEADLINE = "deadline";
	private static final String TYPE_TIMED = "timed";
	private static final String TYPE_COMPLETE = "complete";
	
	private static final String PERIOD_TODAY = "today";
	private static final String PERIOD_TOMORROW = "tomorrow";
	private static final String PERIOD_WEEK = "week";
	private static final String PERIOD_MONTH = "month";
	private static final String PERIOD_ALL = "all";
	
	
	private String type = null;
	private String period = null;
	
	private TaskController taskController = TaskController.getInstance();

	/*** Methods ***/
    /**
     * This method retrieves an array list of Task based on user's input on filters of the tasks.
     * 
     * 
     * @return Pair
     * 				Array list of task
     * 				returns <code>True</code> if the operation is a success,
	 * 		   		returns <code>False</code> otherwise
     * 	
     * 
     */
	@Override
	public ArrayList<Task> execute() {
		// TODO Auto-generated method stub
		
		determineTypePeriod();
		
		@SuppressWarnings("unused")
		boolean result = true;
		ArrayList<Task> taskList = null;
		try{
			taskList = selectTasksByType(taskList);
			taskList = selectTaskByPeriod(taskList);
		}catch(Exception e){
			result = false;
		}
		setChanged();
		notifyObservers(null);
		
		return taskList;
	}
	
	private void determineTypePeriod() {
		if (hasOption(TYPE_FLOATING)) {
			this.type = TYPE_FLOATING;
		} else if (hasOption(TYPE_DEADLINE)) {
			this.type = TYPE_DEADLINE;
		} else if (hasOption(TYPE_TIMED)) {
			this.type = TYPE_TIMED;
		} else if (hasOption(TYPE_COMPLETE)) {
			this.type = TYPE_COMPLETE;
		} else {
			this.type = TYPE_ALL;
		}
		
		if (hasOption(PERIOD_TODAY)) {
			this.period = PERIOD_TODAY;
		} else if (hasOption(PERIOD_TOMORROW)) {
			this.period = PERIOD_TOMORROW;
		} else if (hasOption(PERIOD_WEEK)) {
			this.period = PERIOD_WEEK;
		} else if (hasOption(PERIOD_MONTH)) {
			this.period = PERIOD_MONTH;
		} else {
			this.period = PERIOD_ALL;
		}
	}

    /**
     * This method selects task types based on variable type from the array list given,
     * and return a list of selected Tasks.
     * 
     * @param ArrayList<Task>
     *            a list of tasks to select from
     * @return Array list of selected tasks
     */
	private ArrayList<Task> selectTasksByType(ArrayList<Task> allTask) throws Exception{
		ArrayList<Task> selectedTask = new ArrayList<Task>();
		if(type.equals(TYPE_COMPLETE)){
			allTask = taskController.getTask();
			for(Task t : allTask){
				if(t.isComplete()){
					selectedTask.add(t);
				}
			}
		} else{
			TASK_TYPE taskType = Task.determineType(type);
			allTask = taskController.getTask(taskType);
			for(Task t : allTask){
				if(!t.isComplete()){
					selectedTask.add(t);
				}
			}
		}
		return selectedTask;
	}

    /**
     * This method selects task based on variable period from the array list given,
     * and return a list of selected Tasks.
     * All tasks with deadline before the period variable will be returned.
     * 
     * @param ArrayList<Task>
     *            a list of tasks to select from
     * @return Array list of selected tasks
     */
	public ArrayList<Task> selectTaskByPeriod(ArrayList<Task> allTask) throws Exception{
		if (this.period == PERIOD_ALL) {
			return allTask;
		}
		ArrayList<Task> selectedTask = new ArrayList<Task>();
		LocalDateTime beforeThisTime = checkPeriod();
		for(Task t : allTask){
			//LocalDateTime deadline = null;
			if(FloatingTask.class.isInstance(t)){
				selectedTask.add(t);
			}else{
				if(DeadlineTask.class.isInstance(t)){
					LocalDateTime deadline = null;
					DeadlineTask task = (DeadlineTask) t;
					deadline = task.getEnd();
					
					if(deadline.isBefore(beforeThisTime)){
						selectedTask.add(t);
					}
				}else if(TimedTask.class.isInstance(t)){
					LocalDateTime deadline = null;
					TimedTask task = (TimedTask) t;
					deadline = task.getEnd();
					
					if(deadline.isBefore(beforeThisTime)){
						selectedTask.add(t);
					}
				} else{
					assert false : t; //Task should be belong to either floating, deadline or timed. 
				}
			}
		}

		return selectedTask;
	}

    /**
     * This method offset the period into a LocalDateTime object.
     *    
     * @return LocalDateTime offset by period
     */
	public LocalDateTime checkPeriod() {
		LocalDateTime beforeThisTime = LocalDateTime.now();
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
			beforeThisTime = beforeThisTime.withHour(OFFSET_ZERO).withMinute(OFFSET_ZERO).withSecond(OFFSET_ZERO)
				.withNano(OFFSET_ZERO).plusDays(OFFSET_ONE);
			break;
		default:
			assert false : period;		//This switch should not have any default;
		}
		return beforeThisTime;
	}

	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}
}
