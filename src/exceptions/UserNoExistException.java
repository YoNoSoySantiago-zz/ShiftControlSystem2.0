package exceptions;

@SuppressWarnings("serial")
public class UserNoExistException extends Exception {
	public UserNoExistException() {
		super("This user don't exist ");
	}
}
