package task.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FloatingTask extends Task {
	/*** Variables ***/
	private static final String TASK_TYPE = "floating";
	
	/*** Constructors ***/
	public FloatingTask() {
		super();
	}
	
	public FloatingTask(String description, int priority) {
		super(description, priority, TASK_TYPE);
	}
	
	public FloatingTask(int taskId, String description, LocalDateTime createdAt, boolean isComplete, int priority) {
        super(taskId, description, createdAt, isComplete, priority, new ArrayList<String>(), TASK_TYPE);
    }
	
    public FloatingTask(int taskId, String description, LocalDateTime createdAt, boolean isComplete, int priority, ArrayList<String> tags) {
        super(taskId, description, createdAt, isComplete, priority, tags, TASK_TYPE);
    }
}
