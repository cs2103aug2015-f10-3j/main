package ui;

public class UIController {
	public UIController(){

	}

	public String[] processUserInput(String input){
		String[] output = null;

		//Call logicApi to process input
		output = new String[]{"testing :", input, "end"};
		return output;
	}
}
