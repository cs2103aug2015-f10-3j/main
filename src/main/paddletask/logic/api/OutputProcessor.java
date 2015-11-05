//@@author A0125528E
package main.paddletask.logic.api;

import java.util.ArrayList;
import java.util.Observer;

import main.paddletask.background.Reminder;
import main.paddletask.common.util.DateTimeHelper;
import main.paddletask.task.entity.DeadlineTask;
import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.TimedTask;

public class OutputProcessor {
	/*** Variables ***/
	private static OutputProcessor instance = null;
	private static LogicController executor;
	private static Observer observer;
	private static Reminder reminder;

	private static final String FORMAT = "%1$-5s  %2$-10s  %3$-50s  %4$-11s  %5$-8s  %6$-11s  %7$-5s ";
	private static final String TAGS_FORMAT = "%s ";
	private static final String TAGS_PADDING = "\t\t %s";
	private static final String PRIORITY_INDICATOR = "*";
	private static final char BOLD_INDICATOR = '@';
	private static final String VIEW_HEADER = BOLD_INDICATOR + String.format(FORMAT, "ID", "Type", "Description", "Start Date","","Deadline", "");
	private static final int OFFSET_ONE = 1;
	private static final int OFFSET_ZERO = 0;
	private static final int DOUBLE = 2;
	private static final String NEXT_LINE = "\n";
	private static final int SINGLE_TASK = 1;
	private static final int PRIORITY_ONE = 1;
	private static final int PRIORITY_TWO = 2;
	private static final int PRIORITY_THREE = 3;
	private static final int MIN_SIZE = 0;
	private static final int MAX_LENGTH = 50;
	private static final int REPLACE_LENGTH = 47;
	private static final String EMPTY_STRING = "";
	private static final String REPLACEMENT_STRING = "...";
	private static final String DESC_STRING = "Description: ";
	private static final String TYPE_STRING = "Task Type: ";
	private static final String PRIORITY_STRING = "Priority: ";
	private static final String SPACE_STRING = " ";
	private static final String START_STRING = "Start: ";
	private static final String DEADLINE_STRING = "Deadline: ";
	private static final String REMINDER_STRING = "Reminder: ";
	private static final String TAG_STRING = "Tags: ";
	private static final String RECURRING_STRING = "Recurring: ";

	/*** Constructor ***/
	private OutputProcessor(){
		executor = LogicController.getInstance(observer);
		executor.addObserver(observer);
		reminder = Reminder.getInstance(observer);
		reminder.addObserver(observer);
	}

	/*** Methods ***/
	/**
	 * This method returns an instance of the OutputProcessor. 
	 * This is an implementation of Singleton Class.
	 * 
	 * @return instance	
	 * 				OutputProcessor's singleton object
	 */
	public static OutputProcessor getInstance(Observer observer){
		if(instance == null){
			OutputProcessor.observer = observer;
			instance = new OutputProcessor();
		}

		return instance;
	}

	/**
	 * This method returns an array of output from LogicController class.
	 * 
	 * @param input 
	 *            String input string of the user
	 * @return output
	 * 			  String array for output
	 */
	public String[] processUserInput(String input){
		String[] output = null;
		ArrayList<Task> taskList = executor.processCommand(input);

		if(taskList != null){
			if(taskList.size() == SINGLE_TASK){
				output = formatOutput(taskList.get(OFFSET_ZERO));
			}
			else{
				output = formatOutput(taskList);
			}
		} 
		return output;
	}

	/**
	 * This method formats the variable needed from each task to string
	 * and store them into a string array.
	 * Max characters to be displayed for description is 50.
	 * Anything more will be substring(0,47). 
	 * 
	 * @param taskList 
	 *            Array list of tasks
	 * @return output
	 * 			String array of outputs
	 */
	public String[] formatOutput(ArrayList<Task> taskList){
		String[] output = new String[(taskList.size() * DOUBLE) + OFFSET_ONE];
		int rowNumber = OFFSET_ZERO;
		output[rowNumber++] = VIEW_HEADER;
		for(int i = 0; i < taskList.size();i++){
			Task selectedTask = taskList.get(i);
			String[] taskDetails = selectedTask.toDetailsArray();
			int detailsPointer = OFFSET_ZERO;
			String priorityIndicator = EMPTY_STRING;
			if(selectedTask.getPriority() == PRIORITY_TWO){
				priorityIndicator = PRIORITY_INDICATOR;
			}else if(selectedTask.getPriority() == PRIORITY_ONE){
				priorityIndicator = PRIORITY_INDICATOR + PRIORITY_INDICATOR;
			}
			output[rowNumber++] = priorityIndicator + String.format(FORMAT, i+OFFSET_ONE,taskDetails[++detailsPointer], 
								  taskDetails[++detailsPointer].length() > MAX_LENGTH ? 
								  taskDetails[detailsPointer].substring(OFFSET_ZERO, REPLACE_LENGTH) + REPLACEMENT_STRING : taskDetails[detailsPointer]
								  , taskDetails[++detailsPointer], taskDetails[++detailsPointer],
								  taskDetails[++detailsPointer],taskDetails[++detailsPointer]);
			ArrayList<String> tags = selectedTask.getTags();
			if(tags!=null){
				if(tags.size()>MIN_SIZE){
					String tag = EMPTY_STRING;
					if(!tags.get(OFFSET_ZERO).equals(EMPTY_STRING)){
						for(int j = 0; j < tags.size(); j++){
							String s = tags.get(j);
							if(!s.equals(EMPTY_STRING)){
								tag+=String.format(TAGS_FORMAT, tags.get(j));
							}
							if(j == PRIORITY_THREE){
								break;
							}
						}
						output[rowNumber++] = String.format(TAGS_PADDING, tag);
					}
				}
			}
		}
		return output;
	}

	/**
	 * This method formats the variable needed from each task to string
	 * and store them into a string array.
	 * 
	 * @param ArrayList 
	 *            Array list of task
	 * @return String array
	 */
	public String[] formatOutput(Task task){
		ArrayList<String> output = new ArrayList<String>();
		String line =  DESC_STRING + task.getDescription();
		output.add(line);
		line =  TYPE_STRING + task.getType().toString().toLowerCase();
		output.add(line);
		line =  PRIORITY_STRING + task.getPriority();
		output.add(line);
		formatBaseOnTaskType(task, output);
		ArrayList<String> tags = task.getTags();
		if(tags!= null){
			if(tags.size()>MIN_SIZE){
				line = TAG_STRING;
				if(!tags.get(OFFSET_ZERO).equals(EMPTY_STRING) || tags.size()>OFFSET_ONE){
					for(String s : tags){
						line += String.format(TAGS_FORMAT, s);
					}
					output.add(line);
				}
			}
		}
		output.add(NEXT_LINE);
		return output.toArray(new String[output.size()]);
	}

	public void formatBaseOnTaskType(Task task, ArrayList<String> output) {
		String line;
		if(task instanceof TimedTask){
			TimedTask t = (TimedTask)task;
			line =  START_STRING + DateTimeHelper.getDate(t.getStart()) + SPACE_STRING + DateTimeHelper.getTime(t.getStart());
			output.add(line);
			line =  DEADLINE_STRING + DateTimeHelper.getDate(t.getEnd()) + SPACE_STRING + DateTimeHelper.getTime(t.getEnd());
			output.add(line);
			if(t.getReminder()!=null){
				line = REMINDER_STRING + DateTimeHelper.getDate(t.getReminder()) + SPACE_STRING + DateTimeHelper.getTime(t.getReminder());
				output.add(line);
			}
			if(t.isRecurring()){
				line = RECURRING_STRING + t.getRecurPeriod().toString();
			}
		} else if(task instanceof DeadlineTask){
			DeadlineTask t = (DeadlineTask)task;
			line = DEADLINE_STRING  + DateTimeHelper.getDate(t.getEnd()) + SPACE_STRING + DateTimeHelper.getTime(t.getEnd());
			output.add(line);
			if(t.getReminder()!=null){
				line = REMINDER_STRING + DateTimeHelper.getDate(t.getReminder()) + SPACE_STRING + DateTimeHelper.getTime(t.getReminder());
				output.add(line);
			}
			if(t.isRecurring()){
				line = RECURRING_STRING + t.getRecurPeriod().toString();
			}
		} else {
		}
	}


}
