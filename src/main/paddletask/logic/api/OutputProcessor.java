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

	//private static final String ERROR_BAD_COMMAND = "Command fail to execute";
	//private static final String FORMAT = "| %1$-5s | %2$-10s | %3$-60s | %4$-11s | %5$-5s | %6$-11s | %7$-5s |";
	private static final String FORMAT = "%1$-5s  %2$-10s  %3$-50s  %4$-11s  %5$-8s  %6$-11s  %7$-5s ";
	private static final String TAGS_FORMAT = "%s ";
	private static final String TAGS_PADDING = "\t\t %s";
	private static final String PRIORITY_INDICATOR = "*";
	private static final char BOLD_INDICATOR = '@';
	private static final String VIEW_HEADER = BOLD_INDICATOR + String.format(FORMAT, "ID", "Type", "Description", "Start Date","","Deadline", "");
	private static final int OFFSET_ONE = 1;
	private static final String NEXT_LINE = "\n";

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
	 * @return OutputProcessor instance
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
	 * @param String 
	 *            Input string of the user
	 * @return String array
	 */
	public String[] processUserInput(String input){
		String[] output = null;
		ArrayList<Task> taskList = executor.processCommand(input);

		if(taskList != null){
			if(taskList.size() == 1){
				output = formatOutput(taskList.get(0));
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
	 * @param ArrayList 
	 *            Array list of task
	 * @return String array
	 */
	public String[] formatOutput(ArrayList<Task> taskList){
		String[] output = new String[(taskList.size() * 2) + OFFSET_ONE];
		int rowNumber = 0;
		output[rowNumber++] = VIEW_HEADER;
		for(int i = 0; i < taskList.size();i++){
			Task selectedTask = taskList.get(i);
			String[] taskDetails = selectedTask.toDetailsArray();
			int detailsPointer = 0;
			String priorityIndicator = "";
			if(selectedTask.getPriority() == 2){
				priorityIndicator = PRIORITY_INDICATOR;
			}else if(selectedTask.getPriority() == 1){
				priorityIndicator = PRIORITY_INDICATOR + PRIORITY_INDICATOR;
			}
			output[rowNumber++] = priorityIndicator + String.format(FORMAT, i+OFFSET_ONE,taskDetails[++detailsPointer], 
								  taskDetails[++detailsPointer].length() > 50 ? 
								  taskDetails[detailsPointer].substring(0, 47) + "..." : taskDetails[detailsPointer]
								  , taskDetails[++detailsPointer], taskDetails[++detailsPointer],
								  taskDetails[++detailsPointer],taskDetails[++detailsPointer]);
			ArrayList<String> tags = selectedTask.getTags();
			if(tags!=null){
				if(tags.size()>0){
					String tag = "";
					if(!tags.get(0).equals("")){
						for(int j = 0; j < tags.size(); j++){
							String s = tags.get(j);
							if(!s.equals("")){
								tag+=String.format(TAGS_FORMAT, tags.get(j));
							}
							if(j == 3){
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
		String line =  "Description: " + task.getDescription();
		output.add(line);
		line = "Task Type: " + task.getType().toString().toLowerCase();
		output.add(line);
		line = "Priority: " + task.getPriority();
		output.add(line);
		if(task instanceof TimedTask){
			TimedTask t = (TimedTask)task;
			line = "Start: " + DateTimeHelper.getDate(t.getStart()) + " " + DateTimeHelper.getTime(t.getStart());
			output.add(line);
			line = "Deadline: "  + DateTimeHelper.getDate(t.getEnd()) + " " + DateTimeHelper.getTime(t.getEnd());
			output.add(line);
			if(t.getReminder()!=null){
				line = "Reminder: " + DateTimeHelper.getDate(t.getReminder()) + " " + DateTimeHelper.getTime(t.getReminder());
				output.add(line);
			}
		} else if(task instanceof DeadlineTask){
			DeadlineTask t = (DeadlineTask)task;
			line = "Deadline: "  + DateTimeHelper.getDate(t.getEnd()) + " " + DateTimeHelper.getTime(t.getEnd());
			output.add(line);
			if(t.getReminder()!=null){
				line = "Reminder: " + DateTimeHelper.getDate(t.getReminder()) + " " + DateTimeHelper.getTime(t.getReminder());
				output.add(line);
			}
		} else {
		}
		ArrayList<String> tags = task.getTags();
		if(tags!= null){
			if(tags.size()>0){
				line = "Tags: ";
				if(!tags.get(0).equals("") || tags.size()>1){
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


}
