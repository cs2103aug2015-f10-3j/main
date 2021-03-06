//@@author A0125528E
package main.paddletask.ui.controller;

import java.util.ArrayList;
import java.util.Observer;

import main.paddletask.logic.api.OutputProcessor;
import main.paddletask.task.entity.Task;

public class UIController {

	/*** Variables ***/
	private static UIController instance = null;
	private static Observer observer;
	private static OutputProcessor outputProcessor = null;

	/*** Constructor ***/
	private UIController(){
		outputProcessor = OutputProcessor.getInstance(observer);
	}

	/*** Methods ***/
	/**
	 * This method returns an instance of the UIController. 
	 * This is an implementation of Singleton Class.
	 * 
	 * @return String array
	 */
	public static UIController getInstance(Observer observer){
		if(instance == null){
			UIController.observer = observer;
			instance = new UIController();
		}
		
		return instance;
	}
	
	/**
	 * This method returns an array of output from Logic Component, OutputProcessor class.
	 * 
	 * @param String 
	 *            Input string of the user
	 * @return String array
	 */
	public String[] processUserInput(String input){
		String[] output = outputProcessor.processUserInput(input);
		return output;
	}
	/**
	 * This method returns an array of output from Logic Component, OutputProcessor class.
	 * 
	 * @param String 
	 *            Input string of the user
	 * @return String array
	 */
	public String[] format(ArrayList<Task> taskList){
		String[] output = outputProcessor.formatOutput(taskList);
		return output;
	}
	
	/**
	 * This method returns an array of output from Logic Component, OutputProcessor class.
	 * 
	 * @param new_Observer
	 *            Observer class to be used
	 */
	public static void setObserver(Observer new_Observer){
		observer = new_Observer;
	}
	
	/**
	 * This method sets the outputProcessor with the boolean of the ui mode.
	 * 
	 * @param ui_Mode
	 * 			boolean of the ui mode
	 */
	public static void setUIModeEnabled(boolean ui_Mode) {
		outputProcessor.setUIModeEnabled(ui_Mode);
	}
}
