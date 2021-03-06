//@@author A0126332R
package main.paddletask.task.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import main.paddletask.common.util.DateTimeHelper;

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
    //@@author A0125528E
    /**
     * This method gets the end date
     * in a primitive array
     * 
     * @return       array of information
     */
    public String[] toDetailsArray(){
        String[] details = super.toDetailsArray();
        String endDay = DateTimeHelper.getDayOfWeek(DateTimeHelper.getDate(_end)).substring(0, 3);
        details[4] = endDay + " " + DateTimeHelper.getDate(_end);
        details[5] = DateTimeHelper.getTime(_end);
        return details;
    }
}
