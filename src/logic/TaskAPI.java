package logic;

import java.util.ArrayList;

import logic.data.Task;
import logic.data.Task.TASK_TYPE;


public class TaskAPI {
    /*** Variables ***/
    protected TaskController tController;
    
    /*** Constructor ***/
    public TaskAPI() {
        tController = new TaskController();
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
	     return tController.addTask(task);
	 }
	 
	 /**
  	  * This method returns a list of all tasks
	  * 
	  * @return       an ArrayList of Tasks
      */
	 public ArrayList<Task> getTask() {
	     return tController.getTask();
	 }
	 
	 /**
      * This method returns a list of the specified tasks
      * 
      * @param  TASK_TYPE  type of the Tasks to be selected 
      * @return            an ArrayList of Tasks
      */
	 public ArrayList<Task> getTask(TASK_TYPE type) {
	     return tController.getTask(type);
	 }
	 
     /**
      * This method returns a Task object
      * 
      * @param  taskId  the unique identifier of the Task object
      * @return         the specified Task object
      */
	 public Task getTask(int taskId) {
	     return tController.getTask(taskId);
	 }
	 
     /**
      * This method updates the Task object to file
      * 
      * @param  task  Task entry to be updated 
      * @return       <code>true</code> if the task is successfully updated; 
      *               <code>false</code> otherwise.
      */
	 public boolean updateTask(Task task) {
		 return tController.updateTask(task);
	 }
	 
     /**
      * This method delete the Task from file
      * 
      * @param  taskId  the unique identifier of the Task object 
      * @return         <code>true</code> if the task is successfully updated; 
      *                 <code>false</code> otherwise.
      */
	 public boolean deleteTask (int taskId) {
		 return tController.deleteTask(taskId);
	 }
	 
     /**
      * This method marks a Deadline Task as completed
      * 
      * @param  taskId  the unique identifier of the DeadlineTask object 
      * @return         <code>true</code> if the task is successfully marked as completed; 
      *                 <code>false</code> otherwise.
      */
	 public boolean completeTask(int taskId) {
		 return tController.completeTask(taskId);
	 }

     /**
      * This method overwrites the entire file with an new list of Tasks
      * 
      * @param  taskList  list of the new Task objects 
      * @return           <code>true</code> if the task is successfully marked as completed; 
      *                   <code>false</code> otherwise.
      */
	 public boolean writeAllToFile(ArrayList<Task> taskList) {
		 return tController.writeAllToFile(taskList);
	 }
}
