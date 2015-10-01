package storage;

import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.Document;

import storage.data.Task;

public class StorageController {
    /*** Variables ***/
    protected StorageDataParser sdParser;
    
    /*** Constructor ***/
    public StorageController() {
        sdParser = new StorageDataParser();
        Task.setTaskList(sdParser.readTask());
    }
    
    /*** Methods ***/
    /**
     * This method adds the Task object to file
     * 
     * @param  task  Task entry to be added 
     * @return       <code>true</code> if the task is successfully added; 
     *               <code>false</code> otherwise.
     */
     protected boolean addTask(Task task) {
         // Get and set the smallest available tasId
         int taskId = getAvailableTaskId();
         task.setTaskId(taskId);
         
         // Add this task to our arraylist
         ArrayList<Task> taskList = Task.getTaskList();
         taskList.add(task);
         Task.setTaskList(taskList);
         
         // Store to file
         Document doc = sdParser.parseTask(taskList);
         boolean result = sdParser.writeXml(doc);
         
         return result;
     }
     
     /**
      * This method returns a list of all tasks
      * 
      * @return       an ArrayList of Tasks
      */
     protected ArrayList<Task> getTask() {
         ArrayList<Task> taskList = Task.getTaskList();
         return taskList;
     }
     
     /**
      * This method returns a list of the specified tasks
      * 
      * @param  type  type of the Tasks to be selected 
      * @return       an ArrayList of Tasks
      */
     protected ArrayList<Task> getTask(String type) {
         ArrayList<Task> taskList = Task.getTaskList();
         ArrayList<Task> filteredTaskList = new ArrayList<Task>();
         
         for (Task task : taskList) {
             if (task.getType().equals(type)) {
                 filteredTaskList.add(task);
             }
         }
         
         return filteredTaskList;
     }
     
     /**
      * This method returns a Task object
      * 
      * @param  taskId  the unique identifier of the Task object
      * @return         the specified Task object
      */
     protected Task getTask(int taskId) {
         ArrayList<Task> taskList = Task.getTaskList();
         for (Task task : taskList) {
             if (task.getTaskId() == taskId) {
                 return task;
             }
         }
         return null;
     }
     
     /**
      * This method updates the Task object to file
      * 
      * @param  task  Task entry to be updated 
      * @return       <code>true</code> if the task is successfully updated; 
      *               <code>false</code> otherwise.
      */
     protected boolean updateTask(Task task) {
         ArrayList<Task> taskList = Task.getTaskList();
         boolean found = false;
         
         // Find task
         for (int i = 0; i < taskList.size(); i++) {
             Task existingTask = taskList.get(i);
             if (existingTask.getTaskId() == task.getTaskId()) {
                 taskList.set(i, task);
                 found = true;
                 break;
             }
         }
         
         // Return if task is not found
         if (!found) {
             return false;
         }
         
         // Store to file
         Task.setTaskList(taskList);
         Document doc = sdParser.parseTask(taskList);
         boolean result = sdParser.writeXml(doc);
         
         return result;
     }
     
     /**
      * This method delete the Task from file
      * 
      * @param  taskId  the unique identifier of the Task object 
      * @return         <code>true</code> if the task is successfully updated; 
      *                 <code>false</code> otherwise.
      */
     protected boolean deleteTask(int taskId) {
         ArrayList<Task> taskList = Task.getTaskList();
         boolean found = false;
         
         // Find task
         for (int i = 0; i < taskList.size(); i++) {
             Task task = taskList.get(i);
             if (task.getTaskId() == taskId) {
                 taskList.remove(i);
                 found = true;
                 break;
             }
         }
         
         // Return if task is not found
         if (!found) {
             return false;
         }
         
         // Store to file
         Task.setTaskList(taskList);
         Document doc = sdParser.parseTask(taskList);
         boolean result = sdParser.writeXml(doc);
         
         return result;
     }
     
     /**
      * This method marks a Deadline Task as completed
      * 
      * @param  taskId  the unique identifier of the DeadlineTask object 
      * @return         <code>true</code> if the task is successfully marked as completed; 
      *                 <code>false</code> otherwise.
      */
     protected boolean completeTask(int taskId) {
         ArrayList<Task> taskList = Task.getTaskList();
         Task task = null;
         boolean found = false;
         int index = -1;
         
         // Find task
         for (int i = 0; i < taskList.size(); i++) {
             task = taskList.get(i);
             if (task.getTaskId() == taskId) {
                 index = i;
                 found = true;
                 break;
             }
         }
         
         // Return if task is not found
         if (!found) {
             return false;
         }
         
         // Modify complete status
         task.setComplete(true);
         taskList.set(index, task);
         
         // Store to file
         Task.setTaskList(taskList);
         Document doc = sdParser.parseTask(taskList);
         boolean result = sdParser.writeXml(doc);
         
         return result;
     }

     /**
      * This method overwrites the entire file with an new list of Tasks
      * 
      * @param  taskList  list of the new Task objects 
      * @return           <code>true</code> if the task is successfully marked as completed; 
      *                   <code>false</code> otherwise.
      */
     protected boolean writeAllToFile(ArrayList<Task> taskList) {
         // Store to file
         Task.setTaskList(taskList);
         Document doc = sdParser.parseTask(taskList);
         boolean result = sdParser.writeXml(doc);
         
         return result;
     }
     
     /**
      * This method gets the next available taskId
      * 
      * @return       an int as the next taskId
      */
      protected int getAvailableTaskId() {
          ArrayList<Task> taskList = Task.getTaskList();
          int[] testArray = new int[taskList.size()];
          
          for (int i = 0; i < testArray.length; i++) {
              testArray[i] = taskList.get(i).getTaskId();
          }
          
          Arrays.sort(testArray);
          int smallest = testArray[0];
          int largest = testArray[testArray.length-1];
          int smallestUnused = largest + 1;
          //System.out.println("smallest: "+smallest);
          //System.out.println("largest: "+largest);
          if(smallest>1){
              smallestUnused = 1;
          }else{
              for(int i=2; i<largest; i++){
                  if(Arrays.binarySearch(testArray, i)<0){
                      smallestUnused = i;
                      break;
                  }
              }
          }
          //System.out.println("Smallest unused: "+smallestUnused);
          
          return smallestUnused;
      }
}
