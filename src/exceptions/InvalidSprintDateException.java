package exceptions;

import org.springframework.validation.FieldError;

public class InvalidSprintDateException extends FieldError {
	private static final long serialVersionUID = 1L;

	public InvalidSprintDateException(){
		super("project", "dateTo", "Sprint end date  must be after start date ");
	}

}
