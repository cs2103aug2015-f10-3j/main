package storage.data;

import java.time.LocalDateTime;

public class DeadlineTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "deadline";
	private LocalDateTime end;
	private boolean complete;
	
	/*** Constructors ***/
	public DeadlineTask() {
		super();
	}
	
	public DeadlineTask(String description, LocalDateTime end) {
		super(description, TASK_TYPE);
		this.end = end;
		this.complete = false;
	}
	
	public DeadlineTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime end, boolean complete) {
        super(taskId, description, createdAt, TASK_TYPE);
        this.end = end;
        this.complete = complete;
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
