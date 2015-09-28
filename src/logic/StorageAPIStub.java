package logic;

import java.util.ArrayList;

import logic.data.Task;
import storage.StorageController;

public class StorageAPIStub {

	public boolean addTask(TaskStub task) {
		return true;
	}
	
	public ArrayList<Task> viewTask() {
		return null;
	}
	
	public ArrayList<Task> viewTask(String type) {
		return null;
	}
	
	public TaskStub viewTask(int taskId) {
		return null;
	}
	
	public boolean updateTask(TaskStub task) {
		return true;
	}
	
	public boolean deleteTask(int taskId) {
		return true;
	}
	
	public boolean completeTask(int taskId) {
		return true;
	}
	
	public boolean writeAllToFile(ArrayList<Task> taskList) {
		return true;
	}
}	