package exceptions;

@SuppressWarnings("serial")
public class UserHasBeenBannedException extends Exception{
	public UserHasBeenBannedException() {
		super("This User has been Banned, he can't have more shift");
	}
}
