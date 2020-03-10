package exceptions;

@SuppressWarnings("serial")
public class TimeDateNoValid extends Exception{
	public TimeDateNoValid() {
		super("Your date no is valid, please type correcyly");
	}

}
