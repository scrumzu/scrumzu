package exceptions;

import org.springframework.validation.FieldError;

public class EmptyAuthoritiesListError extends FieldError {
	private static final long serialVersionUID = 1L;

	public EmptyAuthoritiesListError(){
		super("user", "authoritiesList", "User must have at least one role set.");
	}

}
