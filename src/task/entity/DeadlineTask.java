package task.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import common.util.DateTimeHelper;

public class DeadlineTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "deadline";
	private LocalDateTime end;
	private LocalDateTime reminder;
	
	/*** Constructors ***/
	public DeadlineTask() {
		super();
	}
	
	public DeadlineTask(String description, LocalDateTime end, LocalDateTime reminder) {
		super(description, TASK_TYPE);
		this.end = end;
		this.reminder = reminder;
	}
	
	public DeadlineTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime end, LocalDateTime reminder, boolean complete, int priority, ArrayList<String> tags) {
        super(taskId, description, createdAt, complete, priority, tags, TASK_TYPE);
        this.end = end;
        this.reminder = reminder;
    }
	
	/*** Assessors ***/
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
    public LocalDateTime getReminder() {
        return reminder;
    }
    public void setReminder(LocalDateTime reminder) {
        this.reminder = reminder;
    }
	
	/*** Methods ***/
	public boolean completeTask(int taskId) {
		// TODO
		return false;
	}
	
    public String[] toDetailsArray(){
    	String[] details = super.toDetailsArray();
    	details[5] = DateTimeHelper.getDate(end);
    	details[6] = DateTimeHelper.getTime(end);
    	return details;
    }
}
