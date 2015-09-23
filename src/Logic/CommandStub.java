package Logic;

public class CommandStub {
	enum CommandType {
		ADD,EDIT,VIEW,DELETE,INVALID,STUB
	}
	
	public static CommandType getCommandType() {
		return CommandType.STUB;
	}
}
