package exceptions;

@SuppressWarnings("serial")
public class ShiftTypeNotExist extends Exception{
	public ShiftTypeNotExist() {
		super("This tyoe of shift no exist yet");
	}

}
