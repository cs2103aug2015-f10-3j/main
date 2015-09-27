package logic;

import java.time.LocalDateTime;
import logic.data.*;

public class TaskStub extends Task {
	private int taskId;
	private String description;
	private String type;
	private LocalDateTime createdAt;
	private LocalDateTime start;
	private LocalDateTime end;
	
	public TaskStub() {
		// Define default value to indicate that it is an EDIT/VIEW
	}
	
	public TaskStub(int _taskId, String _description, String _type, LocalDateTime _start, LocalDateTime _end) {
		taskId = _taskId;
		description = _description;
		type = _type;
		createdAt = null; // Supposed to get current time
		start = _start;
		end = _end;
	}

	// Used by EditTaskCommand to copy the original createdAt to the new created Task
	public TaskStub(int _taskId, String _description, String _type, LocalDateTime _createdAt, LocalDateTime _start, LocalDateTime _end) {
		taskId = _taskId;
		description = _description;
		type = _type;
		createdAt = _createdAt;
		start = _start;
		end = _end;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

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
}
