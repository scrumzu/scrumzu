package exceptions;

import org.springframework.validation.FieldError;

public class SprintNotStartedException extends FieldError {
	private static final long serialVersionUID = 1L;
	private final static String objectName = "pbi";
	private final static String field = "workItems";
	private final static String message = "Chosen sprint is has not started yet.";

	public SprintNotStartedException(String objectName, String field,
			String defaultMessage) {
		super(objectName, field, message);
	}

	public SprintNotStartedException(){
		super(objectName, field, message);
	}
}
