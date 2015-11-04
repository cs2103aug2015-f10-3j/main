package task.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import common.util.DateTimeHelper;

public class TimedTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "timed";
	private LocalDateTime start;
	private LocalDateTime end;
	private LocalDateTime reminder;
    private boolean recurring;
    private RECUR_TYPE recurPeriod;
	
	/*** Constructors ***/
	public TimedTask() {
		super();
	}
	
	public TimedTask(String description, LocalDateTime start, LocalDateTime end, LocalDateTime reminder, int priority, boolean isRecurring, RECUR_TYPE recurPeriod) {
		super(description, priority, TASK_TYPE);
		this.start = start;
		this.end = end;
		this.reminder = reminder;
        this.recurring = isRecurring;
        this.recurPeriod = recurPeriod;
	}
	
	public TimedTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime start, LocalDateTime end, LocalDateTime reminder, boolean complete, int priority, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(taskId, description, createdAt, complete, priority, new ArrayList<String>(), TASK_TYPE);
        this.start = start;
        this.end = end;
        this.reminder = reminder;
        this.recurring = isRecurring;
        this.recurPeriod = recurPeriod;
    }
	
	public TimedTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime start, LocalDateTime end, LocalDateTime reminder, boolean complete, int priority, ArrayList<String> tags, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(taskId, description, createdAt, complete, priority, tags, TASK_TYPE);
        this.start = start;
        this.end = end;
        this.reminder = reminder;
        this.recurring = isRecurring;
        this.recurPeriod = recurPeriod;
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
    public boolean isRecurring() {
        return recurring;
    }
    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }
    public RECUR_TYPE getRecurPeriod() {
        return recurPeriod;
    }
    public void setRecurPeriod(RECUR_TYPE recurPeriod) {
        this.recurPeriod = recurPeriod;
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
