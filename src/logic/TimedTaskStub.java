package logic;

import java.time.LocalDateTime;

public class TimedTaskStub extends TaskStub {
	/*** Variables ***/
	private static final String TASK_TYPE = "timed";
	private LocalDateTime start;
	private LocalDateTime end;
	
	/*** Constructors ***/
	public TimedTaskStub() {
		super();
	}
	
	public TimedTaskStub(String description, LocalDateTime start, LocalDateTime end) {
		super(description, TASK_TYPE);
		this.start = start;
		this.end = end;
	}
	
	public TimedTaskStub(int taskId, String description, LocalDateTime createdAt, LocalDateTime start, LocalDateTime end) {
        super(taskId, description, createdAt, TASK_TYPE);
        this.start = start;
        this.end = end;
    }
	
	/*** Assessors ***/
	public LocalDateTime getStart() {
		return start;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
}
