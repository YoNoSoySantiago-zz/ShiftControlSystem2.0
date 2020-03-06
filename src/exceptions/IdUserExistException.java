package exceptions;

@SuppressWarnings("serial")
public class IdUserExistException extends Exception {
	public IdUserExistException() {
		super("This user already exist");
	}
}
