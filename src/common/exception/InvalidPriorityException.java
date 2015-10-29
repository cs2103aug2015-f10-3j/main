package common.exception;

@SuppressWarnings("serial")
public class InvalidPriorityException extends PaddleTaskException {
	
	public InvalidPriorityException (String message) {
		super(message);
	}
}
