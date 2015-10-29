package command.api;

import java.util.ArrayList;

import common.data.HelpManual;
import task.entity.Task;

public class HelpCommand extends Command{
	private static final String KEYWORD_HELP = "help";

	private String commandType = "";
	private String helpComments = "";

	@Override
	public ArrayList<Task> execute() {
		// TODO Auto-generated method stub
		HelpManual manual = HelpManual.getInstance();
		if(hasOption(KEYWORD_HELP)){
			if(getOption(KEYWORD_HELP)==null){
				commandType = "";
			} else{
				commandType = getOption(KEYWORD_HELP).getStringValue();
			}
		} else{
			assert false;
		}
		System.out.println("commandtype = " + commandType);
		helpComments = manual.getHelp(commandType.trim());
		System.out.println("help comments = " + helpComments);
		setChanged();
		notifyObservers(helpComments);
		return null;
	}

	public String getHelpComments(){
		return helpComments;
	}
	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
