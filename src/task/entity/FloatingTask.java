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
	
	public FloatingTask(String description) {
		super(description, TASK_TYPE);
	}
	
    public FloatingTask(int taskId, String description, LocalDateTime createdAt, boolean complete, int priority, ArrayList<String> tags) {
        super(taskId, description, createdAt, complete, priority, tags, TASK_TYPE);
    }
}
