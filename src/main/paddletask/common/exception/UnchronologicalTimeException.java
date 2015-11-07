package main.paddletask.common.exception;

@SuppressWarnings("serial")
public class UnchronologicalTimeException extends PaddleTaskException {

    public UnchronologicalTimeException(String message) {
        super(message);
    }
}
