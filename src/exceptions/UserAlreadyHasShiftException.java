package exceptions;

@SuppressWarnings("serial")
public class UserAlreadyHasShiftException extends Exception{
	public UserAlreadyHasShiftException() {
		super("The user Alrady has shift please wait");
	}
}