//@@author A0126332R
package main.paddletask.task.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {
    /*** Variables ***/
    private static ArrayList<Task> _tasks;
    private int _taskId;
    private String _description;
    private LocalDateTime _createdAt;
    private TASK_TYPE _type;
    private boolean _isComplete;
    private int _priority;
    private ArrayList<String> _tags;
    private String[] _details = {"-","-","-","-","-","-","-"};

    public enum TASK_TYPE {
        FLOATING {
            @Override
            public String toString() {
                return "FLOATING";
            }
        },
        TIMED {
            @Override
            public String toString() {
                return "TIMED";
            }
        }, DEADLINE {
            @Override
            public String toString() {
                return "DEADLINE";
            }
        }, ANY {
            @Override
            public String toString() {
                return "ALL";
            }
        };

    };
    
    public enum RECUR_TYPE {
        DAY {
            @Override
            public String toString() {
                return "DAY";
            }
        },
        WEEK {
            @Override
            public String toString() {
                return "WEEK";
            }
        }, 
        MONTH {
            @Override
            public String toString() {
                return "MONTH";
            }
        },
        YEAR {
            @Override
            public String toString() {
                return "YEAR";
            }
        },
        NULL {
            @Override
            public String toString() {
                return "NULL";
            }
        };
    };
    
    /*** Constructors ***/
    public Task() {
        if (_tasks == null) {
            _tasks = new ArrayList<Task>();
        }
    }
    
    public Task(String description, int priority, String type) {
        this._description = description;
        this._createdAt = LocalDateTime.now();
        this._type = determineTaskType(type);
        this._priority = priority;
        this._isComplete = false;
        this._tags = new ArrayList<String>();
    }

    public Task(int taskId, String description, LocalDateTime createdAt, boolean isComplete, int priority, ArrayList<String> tags, String type) {
        this._taskId = taskId;
        this._description = description;
        this._createdAt = createdAt;
        this._isComplete = isComplete;
        this._priority = priority;
        this._tags = tags;
        this._type = determineTaskType(type);
    }
    
    /*** Assessors ***/
    public static ArrayList<Task> getTaskList() {
        return _tasks;
    }
    public static void setTaskList(ArrayList<Task> tasks) {
        Task._tasks = tasks;
    }
    public int getTaskId() {
        return _taskId;
    }
    public void setTaskId(int taskId) {
        this._taskId = taskId;
    }
    public String getDescription() {
        return _description;
    }
    public void setDescription(String description) {
        this._description = description;
    }
    public LocalDateTime getCreatedAt() {
        return _createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this._createdAt = createdAt;
    }
    public TASK_TYPE getType() {
        return _type;
    }
    public void setType(TASK_TYPE type) {
        this._type = type;
    }
    public boolean isComplete() {
        return _isComplete;
    }
    public void setComplete(boolean isComplete) {
        this._isComplete = isComplete;
    }
    public int getPriority() {
        return _priority;
    }
    public void setPriority(int priority) {
        this._priority = priority;
    }
    public ArrayList<String> getTags() {
        return _tags;
    }
    public void setTags(ArrayList<String> tags) {
        this._tags = tags;
    }

    /*** Method ***/
    /**
     * This method gets the task type from a string
     * 
     * @param  typeString  task type in string
     * @return             task type in TASK_TYPE
     */
    public static TASK_TYPE determineTaskType(String typeString) {
        TASK_TYPE type = null;

        if (typeString != null) {
            if (typeString.equalsIgnoreCase("floating")) {
                return TASK_TYPE.FLOATING;
            } else if (typeString.equalsIgnoreCase("timed")) {
                return TASK_TYPE.TIMED;
            } else if (typeString.equalsIgnoreCase("deadline")) {
                return TASK_TYPE.DEADLINE;
            } else if (typeString.equalsIgnoreCase("all")) {
                return TASK_TYPE.ANY;
            }
        }

        return type;
    }
    
    /**
     * This method gets the recurring type from a string
     * 
     * @param  typeString  recurring type in string
     * @return             recurring type in TASK_TYPE
     */
    public static RECUR_TYPE determineRecurType(String typeString) {
        RECUR_TYPE type = null;

        if (typeString != null) {
            if (typeString.equalsIgnoreCase("day")) {
                return RECUR_TYPE.DAY;
            } else if (typeString.equalsIgnoreCase("week")) {
                return RECUR_TYPE.WEEK;
            } else if (typeString.equalsIgnoreCase("month")) {
                return RECUR_TYPE.MONTH;
            } else if (typeString.equalsIgnoreCase("year")) {
                return RECUR_TYPE.YEAR;
            } else if (typeString.equalsIgnoreCase("null")) {
                return RECUR_TYPE.NULL;
            }
        }

        return type;
    }
    
    //@@author A0125528E
    /**
     * This method gets the taskId, type and description
     * in a primitive array
     * 
     * @return      array of information
     */
    public String[] toDetailsArray(){
        int counter = 0;
        _details[counter] = _taskId + "";
        _details[++counter] = _type.toString().toLowerCase();
        _details[++counter] = _description;
        return _details;
    }
}
