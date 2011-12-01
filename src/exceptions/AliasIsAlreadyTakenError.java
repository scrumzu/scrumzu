package exceptions;

import org.springframework.validation.FieldError;

public class AliasIsAlreadyTakenError extends FieldError {
	private static final long serialVersionUID = 1L;

	public AliasIsAlreadyTakenError(String commandObject, String alias){
		super(commandObject , "alias", "Alias '" + alias + "' is already taken");
	}

}
