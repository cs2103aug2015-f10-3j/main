package storage;

import java.util.ArrayList;

import storage.data.Task;

public class StorageAPI {
    /*** Variables ***/
    protected StorageController sController;
    
    /*** Constructor ***/
    public StorageAPI() {
        sController = new StorageController();
    }
    
    /*** Methods ***/
	/**
	 * This method adds the Task object to file
	 * 
	 * @param  task  Task entry to be added 
	 * @return       <code>true</code> if the task is successfully added; 
     *               <code>false</code> otherwise.
	 */
	 public boolean addTask(Task task) {
	     return sController.addTask(task);
	 }
	 
	 /**
  	  * This method returns a list of all tasks for viewing
	  * 
	  * @return       an ArrayList of Tasks
      */
	 public ArrayList<Task> viewTask() {
	     return sController.viewTask();
	 }
	 
	 /**
      * This method returns a list of the specified tasks for viewing
      * 
      * @param  type  type of the Tasks to be selected 
      * @return       an ArrayList of Tasks
      */
	 public ArrayList<Task> viewTask(String type) {
	     return sController.viewTask(type);
	 }
	 
     /**
      * This method returns a Task object for viewing
      * 
      * @param  taskId  the unique identifier of the Task object
      * @return         the specified Task object
      */
	 public Task viewTask(int taskId) {
	     return sController.viewTask(taskId);
	 }
	 
     /**
      * This method updates the Task object to file
      * 
      * @param  task  Task entry to be updated 
      * @return       <code>true</code> if the task is successfully updated; 
      *               <code>false</code> otherwise.
      */
	 public boolean updateTask(Task task) {
		 return sController.updateTask(task);
	 }
	 
     /**
      * This method delete the Task from file
      * 
      * @param  taskId  the unique identifier of the Task object 
      * @return         <code>true</code> if the task is successfully updated; 
      *                 <code>false</code> otherwise.
      */
	 public boolean deleteTask (int taskId) {
		 return sController.deleteTask(taskId);
	 }
	 
     /**
      * This method marks a Deadline Task as completed
      * 
      * @param  taskId  the unique identifier of the DeadlineTask object 
      * @return         <code>true</code> if the task is successfully marked as completed; 
      *                 <code>false</code> otherwise.
      */
	 public boolean completeTask(int taskId) {
		 return sController.completeTask(taskId);
	 }

     /**
      * This method overwrites the entire file with an new list of Tasks
      * 
      * @param  taskList  list of the new Task objects 
      * @return           <code>true</code> if the task is successfully marked as completed; 
      *                   <code>false</code> otherwise.
      */
	 public boolean writeAllToFile(ArrayList<Task> taskList) {
		 return sController.writeAllToFile(taskList);
	 }
}
