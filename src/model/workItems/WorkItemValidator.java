package model.workItems;

import model.sprint.SprintStatus;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import exceptions.InvalidWorkItemFieldError;
import exceptions.SprintNotStartedException;

@Component
public class WorkItemValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return WorkItem.class.equals(clazz);
	}

	public void validate(Object o, Errors errors) {
		WorkItem wi = (WorkItem) o;
		BindingResult result = (BindingResult) errors;

		if (wi.getStoryPoints() < 0) {
			errors.rejectValue("workItems[0].storyPoints",
					"workItem.negative.storyPoints");
		}
		if (!wi.isEmpty()) {
			if (!wi.isWriteable()) {
				result.addError(new InvalidWorkItemFieldError(wi.getStatus()));
			}
			else{
				if(wi.getStatus().equals(Status.COMMITTED)){
					if(wi.getSprint().getSprintStatus().equals(SprintStatus.CREATED)){
						result.addError(new SprintNotStartedException());
					}
				}
			}
		}
	}
}
