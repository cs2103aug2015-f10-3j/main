package common.exception;

@SuppressWarnings("serial")
public class InvalidCommandFormatException extends PaddleTaskException {
	
	public InvalidCommandFormatException(String message) {
		super(message);
	}
	
}
