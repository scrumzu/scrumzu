package exceptions;

import model.workItems.Status;

import org.springframework.validation.FieldError;

public class InvalidWorkItemFieldError extends FieldError {
	private static final long serialVersionUID = 1L;
	private final static String objectName = "pbi";
	private final static String field = "workItems";

	public InvalidWorkItemFieldError(String objectName, String field,
			String defaultMessage) {
		super(objectName, field, defaultMessage);
	}

	public InvalidWorkItemFieldError(Status status) {
		super(objectName, field, getCustomMessageForStatus(status));
	}

	private static String getCustomMessageForStatus(Status status) {
		String msg = "Invalid settings for "
				+ (status == null ? "empty" : status.toString()) + " status. ";
		switch (status) {
		case COMMITTED:
		case DONE:
		case DROPPED:
		case WORK_IN_PROGRESS:
		case PREASSIGNED:
			msg += "Team and sprint must be set.";
			break;
		case NEW:
			msg += "Team and sprint should be left blank.";
			break;
		case PROPOSED_FOR_SPRINT:
			msg += "Team should be left blank.";
		}
		return msg;
	}
}
