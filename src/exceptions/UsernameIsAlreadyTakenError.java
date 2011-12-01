package exceptions;

import org.springframework.validation.FieldError;

public class UsernameIsAlreadyTakenError extends FieldError {
	private static final long serialVersionUID = 1L;

	public UsernameIsAlreadyTakenError(String username){
		super("user" , "username", "Username '" + username + "' is already taken.");
	}

}
