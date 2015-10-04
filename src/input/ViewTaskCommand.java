package input;

import java.time.LocalDateTime;
import java.util.ArrayList;

import logic.TaskController;
import logic.data.DeadlineTask;
import logic.data.FloatingTask;
import logic.data.Task;
import logic.data.TimedTask;
import logic.data.Task.TASK_TYPE;
import util.Pair;

public class ViewTaskCommand extends Command {

	/*** Variables ***/
	private static final int OFFSET_ZERO = 0;
	private static final int OFFSET_ONE = 1;
	
	private String type = null;
	private String period = null;
	
	private static final TaskController taskController = new TaskController();	

	/*** Constructor ***/
	public ViewTaskCommand() {
		this.type = getOption("all").getStringValue();
		this.type = getOption("floating").getStringValue();
		this.type = getOption("deadline").getStringValue();
		this.type = getOption("timed").getStringValue();
		this.period = getOption("today").getStringValue();
		this.period = getOption("tomorrow").getStringValue();
		this.period = getOption("week").getStringValue();
		this.period = getOption("month").getStringValue();
	}
	
	public ViewTaskCommand(String type, String period){
		this.type = type;
		this.period = period;
	}

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
	public Pair<ArrayList<Task>,Boolean> execute() {
		// TODO Auto-generated method stub
		boolean result = true;
		ArrayList<Task> taskList = null;
		try{
			taskList = selectTasksByType(taskList);
			taskList = selectTaskByPeriod(taskList);
		}catch(Exception e){
			result = false;
		}
		
		return new Pair<ArrayList<Task>,Boolean>(taskList, result);
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
		TASK_TYPE taskType = Task.determineType(type);
		ArrayList<Task> selectedTask = taskController.getTask(taskType);
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
	private ArrayList<Task> selectTaskByPeriod(ArrayList<Task> allTask) throws Exception{
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
		default:
			beforeThisTime = beforeThisTime.withHour(OFFSET_ZERO).withMinute(OFFSET_ZERO).withSecond(OFFSET_ZERO)
							.withNano(OFFSET_ZERO).plusDays(OFFSET_ONE);
		}
		return beforeThisTime;
	}
}
