//@@author A0126332R
package main.paddletask.task.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import main.paddletask.common.util.DateTimeHelper;

public class TimedTask extends Task {
    /*** Variables ***/
    private static final String TASK_TYPE = "timed";
    private LocalDateTime _start;
    private LocalDateTime _end;
    private LocalDateTime _reminder;
    private boolean _isRecurring;
    private RECUR_TYPE _recurPeriod;
    
    /*** Constructors ***/
    public TimedTask() {
        super();
    }
    
    public TimedTask(String description, LocalDateTime start, LocalDateTime end, LocalDateTime reminder, int priority, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(description, priority, TASK_TYPE);
        this._start = start;
        this._end = end;
        this._reminder = reminder;
        this._isRecurring = isRecurring;
        this._recurPeriod = recurPeriod;
    }
    
    public TimedTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime start, LocalDateTime end, LocalDateTime reminder, boolean isComplete, int priority, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(taskId, description, createdAt, isComplete, priority, new ArrayList<String>(), TASK_TYPE);
        this._start = start;
        this._end = end;
        this._reminder = reminder;
        this._isRecurring = isRecurring;
        this._recurPeriod = recurPeriod;
    }
    
    public TimedTask(int taskId, String description, LocalDateTime createdAt, LocalDateTime start, LocalDateTime end, LocalDateTime reminder, boolean isComplete, int priority, ArrayList<String> tags, boolean isRecurring, RECUR_TYPE recurPeriod) {
        super(taskId, description, createdAt, isComplete, priority, tags, TASK_TYPE);
        this._start = start;
        this._end = end;
        this._reminder = reminder;
        this._isRecurring = isRecurring;
        this._recurPeriod = recurPeriod;
    }
    
    /*** Assessors ***/
    public LocalDateTime getStart() {
        return _start;
    }
    public void setStart(LocalDateTime start) {
        this._start = start;
    }
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
    public void setRecurring(boolean recurring) {
        this._isRecurring = recurring;
    }
    public RECUR_TYPE getRecurPeriod() {
        return _recurPeriod;
    }
    public void setRecurPeriod(RECUR_TYPE recurPeriod) {
        this._recurPeriod = recurPeriod;
    }
    
    /*** Method ***/
    //@@author A0125528E
    /**
     * This method gets the start and end date
     * in a primitive array
     * 
     * @return      array of information
     */
    public String[] toDetailsArray(){
        String[] details = super.toDetailsArray();
        details[3] = DateTimeHelper.getDate(_start);
        details[4] = DateTimeHelper.getTime(_start);
        details[5] = DateTimeHelper.getDate(_end);
        details[6] = DateTimeHelper.getTime(_end); 
        
        return details;
    }
}
