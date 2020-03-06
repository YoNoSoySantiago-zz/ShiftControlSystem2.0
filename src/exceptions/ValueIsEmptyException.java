package exceptions;

@SuppressWarnings("serial")
public class ValueIsEmptyException extends Exception {
	public ValueIsEmptyException() {
		super("The mandatory values are empty");
	}
}
