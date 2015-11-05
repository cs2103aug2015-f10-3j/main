package task.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import common.util.DateTimeHelper;

public class DeadlineTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "deadline";
	private LocalDateTime end;
	private LocalDateTime reminder;
    private boolean recurring;
    private RECUR_TYPE recurPeriod;
	
	/*** Constructors ***/
	public DeadlineTask() {
		super();
	}
	
	public DeadlineTask(String description, LocalDateTime end, LocalDateTime reminder, int priority, boolean isRecurring, RECUR_TYPE recurPeriod) {
		super(description, priority, TASK_TYPE);
		this.end = end;
		this.reminder = reminder;
        this.recurring = isRecurring;
        this.recurPeriod = recurPeriod;
	}
	
	public DeadlineTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime end, LocalDateTime reminder, boolean complete, int priority, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(taskId, description, createdAt, complete, priority, new ArrayList<String>(), TASK_TYPE);
        this.end = end;
        this.reminder = reminder;
        this.recurring = isRecurring;
        this.recurPeriod = recurPeriod;
    }
	
	public DeadlineTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime end, LocalDateTime reminder, boolean complete, int priority, ArrayList<String> tags, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(taskId, description, createdAt, complete, priority, tags, TASK_TYPE);
        this.end = end;
        this.reminder = reminder;
        this.recurring = isRecurring;
        this.recurPeriod = recurPeriod;
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
