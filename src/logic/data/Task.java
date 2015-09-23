package logic.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {
	private static ArrayList<Task> taskList;
	private int taskId;
	private String description;
	private LocalDateTime createdAt;
	private String type;
	
	public static ArrayList getTaskList() {
		return taskList;
	}
	public static void setTaskList(ArrayList taskList) {
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
