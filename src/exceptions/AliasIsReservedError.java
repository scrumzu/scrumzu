package exceptions;

import org.springframework.validation.FieldError;

public class AliasIsReservedError extends FieldError {
	private static final long serialVersionUID = 1L;

	public AliasIsReservedError(String alias){
		super("project", "alias", "Alias '" + alias +"' is a reserved word.");
	}

}
