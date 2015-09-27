package storage.data;

import java.time.LocalDateTime;

public class TimedTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "timed";
	private LocalDateTime end;
	private boolean complete;
	
	/*** Constructors ***/
	public TimedTask() {
		super();
	}
	
	public TimedTask(String description, LocalDateTime end) {
		super(description, TASK_TYPE);
		this.end = end;
		this.complete = false;
	}
	
	/*** Assessors ***/
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	/*** Methods ***/
	public boolean completeTask(int taskId) {
		// TODO
		return false;
	}
}
