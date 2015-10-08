package logic.data;

import java.time.LocalDateTime;

import commons.DateTimeCommon;

public class DeadlineTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "deadline";
	private LocalDateTime end;
	
	/*** Constructors ***/
	public DeadlineTask() {
		super();
	}
	
	public DeadlineTask(String description, LocalDateTime end) {
		super(description, TASK_TYPE);
		this.end = end;
	}
	
	public DeadlineTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime end, boolean complete) {
        super(taskId, description, createdAt, complete, TASK_TYPE);
        this.end = end;
    }
	
	/*** Assessors ***/
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	
	/*** Methods ***/
	public boolean completeTask(int taskId) {
		// TODO
		return false;
	}
	
    public String toString(){
    	String output = super.toString();
    	output += DateTimeCommon.parseDateTimeToString(end) + " ";
    	return output;
    }
}
