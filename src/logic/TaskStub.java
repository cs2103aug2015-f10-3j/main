package logic;

import java.time.LocalDateTime;
import java.util.ArrayList;

import logic.data.*;

public class TaskStub extends Task{
	/*** Variables ***/
	private static ArrayList<TaskStub> TaskStubList;
	private int TaskStubId;
	private String description;
	private LocalDateTime createdAt;
	private String type;
	private boolean complete;
	
	/*** Constructors ***/
	public TaskStub() {
		if (TaskStubList == null) {
			TaskStubList = new ArrayList<TaskStub>();
		}
	}
	
	public TaskStub(String description, String type) {
		this.description = description;
		this.createdAt = LocalDateTime.now();
		this.type = type;
	}
	
	public TaskStub(int taskId, String description, LocalDateTime start, String type) {
		
	}
	
	public TaskStub(int TaskStubId, String description, LocalDateTime createdAt, String type, boolean complete) {
	    this.TaskStubId = TaskStubId;
	    this.description = description;
	    this.createdAt = createdAt;
	    this.type = type;
	    this.complete = complete;
	}
	
	/*** Assessors ***/
	public static ArrayList<TaskStub> getTaskStubList() {
		return TaskStubList;
	}
	public static void setTaskStubList(ArrayList<TaskStub> TaskStubList) {
		TaskStub.TaskStubList = TaskStubList;
	}
	public int getTaskStubId() {
		return TaskStubId;
	}
	public void setTaskStubId(int TaskStubId) {
		this.TaskStubId = TaskStubId;
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

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
