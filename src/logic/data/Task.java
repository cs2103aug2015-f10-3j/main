package logic.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {
	/*** Variables ***/
	private static ArrayList<Task> taskList;
	private int taskId;
	private String description;
	private LocalDateTime createdAt;
	private TASK_TYPE type;
    private boolean complete;

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
        };;

    };
	
	/*** Constructors ***/
	public Task() {
		if (taskList == null) {
			taskList = new ArrayList<Task>();
		}
	}
	
	public Task(String description, String type) {
		this.description = description;
		this.createdAt = LocalDateTime.now();
		this.type = determineType(type);
		this.complete = false;
	}

    public Task(int taskId, String description, LocalDateTime createdAt, boolean complete, String type) {
	    this.taskId = taskId;
	    this.description = description;
	    this.createdAt = createdAt;
        this.complete = complete;
	    this.type = determineType(type);
	}
	
	/*** Assessors ***/
	public static ArrayList<Task> getTaskList() {
		return taskList;
	}
	public static void setTaskList(ArrayList<Task> taskList) {
		Task.taskList = taskList;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    public TASK_TYPE getType() {
        return type;
    }
    public void setType(TASK_TYPE type) {
        this.type = type;
    }
    public boolean isComplete() {
        return complete;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /*** Method ***/
    public static TASK_TYPE determineType(String typeString) {
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
    
    public String toString(){
    	String output = "";
    	output += taskId + " ";
    	output += type.toString() + " ";
    	output += description + " ";
    	return output;
    }
}
