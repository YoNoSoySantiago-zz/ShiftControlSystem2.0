package exceptions;

@SuppressWarnings("serial")
public class NameShiftTypeAlreadyExist extends Exception{
	public NameShiftTypeAlreadyExist() {
		super("This shift type already exist");
	}
}
