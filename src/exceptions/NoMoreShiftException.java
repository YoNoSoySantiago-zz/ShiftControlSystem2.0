package exceptions;

@SuppressWarnings("serial")
public class NoMoreShiftException extends Exception{
	public NoMoreShiftException() {
		super("No more users witch shift");
	}
}
