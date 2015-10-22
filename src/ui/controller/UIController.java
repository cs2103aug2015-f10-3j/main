package ui.controller;

import java.util.Observer;

import logic.api.OutputProcessor;

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

}
