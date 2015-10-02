package logic;

import java.time.LocalDateTime;

public class DeadlineTaskStub extends TaskStub{
	/*** Variables ***/
	private static final String TASK_TYPE = "deadline";
	private LocalDateTime end;
	
	/*** Constructors ***/
	public DeadlineTaskStub() {
		super();
	}
	
	public DeadlineTaskStub(String description, LocalDateTime end) {
		super(description, TASK_TYPE);
		this.end = end;
	}
	
	public DeadlineTaskStub(int taskId, String description, LocalDateTime createdAt, LocalDateTime end, boolean complete) {
        super(taskId, description, createdAt, TASK_TYPE, complete);
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
	
	public static String getTaskType() {
		return TASK_TYPE;
	}
}
