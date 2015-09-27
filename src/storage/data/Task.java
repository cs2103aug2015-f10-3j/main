package storage.data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {
	/*** Variables ***/
	private static ArrayList<Task> taskList;
	private int taskId;
	private String description;
	private LocalDateTime createdAt;
	private String type;
	
	/*** Constructors ***/
	public Task() {
		if (taskList == null) {
			taskList = new ArrayList<Task>();
		}
	}
	
	public Task(String description, String type) {
		this.taskId = 0; //TODO: getAvailableTaskId()
		this.description = description;
		this.createdAt = LocalDateTime.now();
		this.type = type;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
