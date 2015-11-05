package task.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import common.util.DateTimeHelper;

public class DeadlineTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "deadline";
	private LocalDateTime _end;
	private LocalDateTime _reminder;
    private boolean _isRecurring;
    private RECUR_TYPE _recurPeriod;
	
	/*** Constructors ***/
	public DeadlineTask() {
		super();
	}
	
	public DeadlineTask(String description, LocalDateTime end, LocalDateTime reminder, int priority, boolean isRecurring, RECUR_TYPE recurPeriod) {
		super(description, priority, TASK_TYPE);
		this._end = end;
		this._reminder = reminder;
        this._isRecurring = isRecurring;
        this._recurPeriod = recurPeriod;
	}
	
	public DeadlineTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime end, LocalDateTime reminder, boolean isComplete, int priority, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(taskId, description, createdAt, isComplete, priority, new ArrayList<String>(), TASK_TYPE);
        this._end = end;
        this._reminder = reminder;
        this._isRecurring = isRecurring;
        this._recurPeriod = recurPeriod;
    }
	
	public DeadlineTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime end, LocalDateTime reminder, boolean isComplete, int priority, ArrayList<String> tags, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(taskId, description, createdAt, isComplete, priority, tags, TASK_TYPE);
        this._end = end;
        this._reminder = reminder;
        this._isRecurring = isRecurring;
        this._recurPeriod = recurPeriod;
    }
	
	/*** Assessors ***/
	public LocalDateTime getEnd() {
		return _end;
	}
	public void setEnd(LocalDateTime end) {
		this._end = end;
	}
    public LocalDateTime getReminder() {
        return _reminder;
    }
    public void setReminder(LocalDateTime reminder) {
        this._reminder = reminder;
    }
    public boolean isRecurring() {
        return _isRecurring;
    }
    public void setRecurring(boolean isRecurring) {
        this._isRecurring = isRecurring;
    }
    public RECUR_TYPE getRecurPeriod() {
        return _recurPeriod;
    }
    public void setRecurPeriod(RECUR_TYPE recurPeriod) {
        this._recurPeriod = recurPeriod;
    }
	
	/*** Methods ***/
	/**
     * This method gets the end date
     * in a primitive array
     * 
     * @return       array of information
     */
    public String[] toDetailsArray(){
    	String[] details = super.toDetailsArray();
    	details[5] = DateTimeHelper.getDate(_end);
    	details[6] = DateTimeHelper.getTime(_end);
    	return details;
    }
}
