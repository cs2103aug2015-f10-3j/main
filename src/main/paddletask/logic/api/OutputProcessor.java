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

	private static final String FORMAT = "%1$-5s  %2$-50s  %3$-11s  %4$-8s  %5$-11s  %6$-5s ";
	private static final String CLI_FORMAT = "%1$-3s  %2$-25s  %3$-11s  %4$-5s  %5$-11s  %6$-5s ";
	private static final String TAGS_FORMAT = "%s ";
	private static final String TAGS_PADDING = "       %s";
	private static final String PRIORITY_INDICATOR = "*";
	private static final char BOLD_INDICATOR = '@';
	private static final String VIEW_HEADER = BOLD_INDICATOR + String.format(FORMAT, "ID", "Description", "Start Date","","Deadline", "");
	private static final String CLI_VIEW_HEADER = BOLD_INDICATOR + String.format(CLI_FORMAT, "ID", "Description", "Start Date","","Deadline", "");
	private static final int OFFSET_ONE = 1;
	private static final int OFFSET_ZERO = 0;
	private static final int DOUBLE = 2;
	private static final String NEXT_LINE = "\n";
	private static final int SINGLE_TASK = 1;
	private static final int PRIORITY_ONE = 1;
	private static final int PRIORITY_TWO = 2;
	private static final int MAX_TAGS_SHOWN_LIMIT = 3;
	private static final int MIN_SIZE = 0;
	private static final int MAX_LENGTH = 50;
	private static final int CLI_MAX_LENGTH = 25;
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
	private static final String RECURRING_STRING = "Recurring: Every ";
	private static boolean ui_Mode = false;

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
		String format = FORMAT;
		if (!ui_Mode) {
			format = CLI_FORMAT;
			output[rowNumber++] = CLI_VIEW_HEADER;
		} else {
			output[rowNumber++] = VIEW_HEADER;
		}
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
			output[rowNumber++] = priorityIndicator + String.format(format, i+OFFSET_ONE,
								  taskDetails[++detailsPointer].length() > (ui_Mode ? MAX_LENGTH : CLI_MAX_LENGTH) ? 
								  taskDetails[detailsPointer].substring(OFFSET_ZERO, 
										  (ui_Mode ? REPLACE_LENGTH : CLI_MAX_LENGTH - MAX_TAGS_SHOWN_LIMIT)) + 
								  		   REPLACEMENT_STRING : taskDetails[detailsPointer],
								  taskDetails[++detailsPointer],
								  taskDetails[++detailsPointer],
								  taskDetails[++detailsPointer],
								  taskDetails[++detailsPointer]);
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
							if(j == MAX_TAGS_SHOWN_LIMIT){
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
	 * This method formats the variable needed from the selected task to string
	 * and store them into a string array.
	 * 
	 * @param task
	 * 				selected task to display
	 * @return String array
	 */
	public String[] formatOutput(Task task){
		ArrayList<String> output = new ArrayList<String>();
		formatDescription(task, output);
		formatType(task, output);
		formatPriority(task, output);
		formatBaseOnTaskType(task, output);
		formatTags(output, task);
		return output.toArray(new String[output.size()]);
	}
	
	/**
	 * This method formats the descriptions of the given task
	 * and insert into the output array list.
	 * 
	 * @param task
	 * 				selected Task
	 * 		  output
	 *            	Array list of outputs
	 */
	public void formatDescription(Task task, ArrayList<String> output) {
		String line =  DESC_STRING + task.getDescription();
		output.add(line);
	}

	/**
	 * This method formats the task types of the given task
	 * and insert into the output array list.
	 * 
	 * @param task
	 * 				selected Task
	 * 		  output
	 *            	Array list of outputs
	 */
	public void formatType(Task task, ArrayList<String> output) {
		String line;
		line =  TYPE_STRING + task.getType().toString().toLowerCase();
		output.add(line);
	}

	/**
	 * This method formats the priority of the given task
	 * and insert into the output array list.
	 * 
	 * @param task
	 * 				selected Task
	 * 		  output
	 *            	Array list of outputs
	 */
	public void formatPriority(Task task, ArrayList<String> output) {
		String line;
		line =  PRIORITY_STRING + task.getPriority();
		output.add(line);
	}
	
	/**
	 * This method formats the tags of the given task
	 * and insert into the output array list.
	 * 
	 * @param task
	 * 				selected Task
	 * 		  output
	 *            	Array list of outputs
	 */
	public void formatTags(ArrayList<String> output, Task task) {
		ArrayList<String> tags = task.getTags();
		String line;
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
	}

	/**
	 * This method formats the task based on the task type to extract
	 * necessary attributes uniquely to the task types.
	 * 
	 * @param task
	 * 				selected Task
	 * 		  output
	 *            	Array list of outputs
	 */
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
				line = RECURRING_STRING + t.getRecurPeriod().toString().toLowerCase();
				output.add(line);
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
				line = RECURRING_STRING + t.getRecurPeriod().toString().toLowerCase();
				output.add(line);
			}
		} else {
		}
	}

	public void setUIModeEnabled(boolean ui_Mode) {
		OutputProcessor.ui_Mode = ui_Mode;
	}


}
