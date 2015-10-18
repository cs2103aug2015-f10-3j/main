package task.entity;

import java.time.LocalDateTime;

import common.util.DateTimeHelper;

public class TimedTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "timed";
	private LocalDateTime start;
	private LocalDateTime end;
	private LocalDateTime reminder;
	
	/*** Constructors ***/
	public TimedTask() {
		super();
	}
	
	public TimedTask(String description, LocalDateTime start, LocalDateTime end, LocalDateTime reminder) {
		super(description, TASK_TYPE);
		this.start = start;
		this.end = end;
		this.reminder = reminder;
	}
	
	public TimedTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime start, LocalDateTime end, LocalDateTime reminder, boolean complete) {
        super(taskId, description, createdAt, complete, TASK_TYPE);
        this.start = start;
        this.end = end;
        this.reminder = reminder;
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
    public LocalDateTime getReminder() {
        return reminder;
    }
    public void setReminder(LocalDateTime reminder) {
        this.reminder = reminder;
    }
	
	/*** Method ***/
    public String[] toDetailsArray(){
    	String[] details = super.toDetailsArray();
    	details[3] = DateTimeHelper.getDate(start);
    	details[4] = DateTimeHelper.getTime(start);
    	details[5] = DateTimeHelper.getDate(end);
    	details[6] = DateTimeHelper.getTime(end); 
    	
    	return details;
    }
}
