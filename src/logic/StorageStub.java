package logic;

import java.util.ArrayList;

import logic.data.Task;
import storage.StorageController;

public class StorageStub extends StorageController {
	public static TaskStub getTaskById(int taskId) {
		return null;
	}
	
	public static boolean editTask(Task task) {
		return true;
	}
	
	public ArrayList<Task> getAllTasks() {
		return null;
	}
	
	public ArrayList<Task> getAllTasks(String type) {
		return null;
	}
	
	public ArrayList<Task> getLatestTasks(int numberOfTasks) {
		return null;
	}
	
	public ArrayList<Task> getLatestTasks(int numberOfTasks, String type) {
		return null;
	}
	
	public boolean deleteTask(int taskId) {
		return true;
	}
	
	public boolean complete(int taskId) {
		return true;
	}
}