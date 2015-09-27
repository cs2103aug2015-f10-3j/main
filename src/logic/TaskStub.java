package logic;

import java.time.LocalDateTime;
import logic.data.*;

public class TaskStub extends Task {
	private int _taskId;
	private String _description;
	private String _type;
	private LocalDateTime _createdAt;
	private LocalDateTime _start;
	private LocalDateTime _end;
	
	public TaskStub() {
		// Define default value to indicate that it is an EDIT/VIEW
	}
	
	public TaskStub(int taskId, String description, String type, LocalDateTime start, LocalDateTime end) {
		_taskId = taskId;
		_description = description;
		_type = type;
		_createdAt = null; // Supposed to get current time
		_start = start;
		_end = end;
	}

	// Used by EditTaskCommand to copy the original createdAt to the new created Task
	public TaskStub(int taskId, String description, String type, LocalDateTime createdAt, LocalDateTime start, LocalDateTime end) {
		_taskId = taskId;
		_description = description;
		_type = type;
		_createdAt = createdAt;
		_start = start;
		_end = end;
	}
	
	public int get_taskId() {
		return _taskId;
	}

	public void set_taskId(int _taskId) {
		this._taskId = _taskId;
	}

	public String get_description() {
		return _description;
	}

	public void set_description(String _description) {
		this._description = _description;
	}

	public String get_type() {
		return _type;
	}

	public void set_type(String _type) {
		this._type = _type;
	}

	public LocalDateTime get_createdAt() {
		return _createdAt;
	}

	public void set_createdAt(LocalDateTime _createdAt) {
		this._createdAt = _createdAt;
	}

	public LocalDateTime get_start() {
		return _start;
	}

	public void set_start(LocalDateTime _start) {
		this._start = _start;
	}

	public LocalDateTime get_end() {
		return _end;
	}

	public void set_end(LocalDateTime _end) {
		this._end = _end;
	}
}
