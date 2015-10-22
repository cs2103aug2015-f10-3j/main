package common.exception;

@SuppressWarnings("serial")
public class NoTaskStateException extends PaddleTaskException {

	public NoTaskStateException(String message) {
		super(message);
	}
}
