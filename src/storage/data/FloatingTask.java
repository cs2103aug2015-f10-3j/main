package storage.data;

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
}
