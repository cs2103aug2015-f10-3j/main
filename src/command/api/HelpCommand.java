package command.api;

import java.util.ArrayList;

import common.data.HelpManual;
import task.entity.Task;

public class HelpCommand extends Command{
	private static final String KEYWORD_HELP = "help";
	
	private String commandType = "";
	
	@Override
	public ArrayList<Task> execute() {
		// TODO Auto-generated method stub
		HelpManual manual = HelpManual.getInstance();
		if(hasOption(KEYWORD_HELP)){
			commandType = getOption(KEYWORD_HELP).getStringValue();
			
		} else{
			commandType = null;
		}
		String helpComments = manual.getHelp(commandType);
		setChanged();
		notifyObservers(helpComments);
		return null;
	}

	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
