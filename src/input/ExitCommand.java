package input;

public class ExitCommand implements Command {

	@Override
	public void execute() {
		exitProgram();
	}
	
	private void exitProgram() {
		System.exit(0);
	}
}
