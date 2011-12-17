package model.projects;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import services.ProjectService;

import exceptions.AliasIsAlreadyTakenError;
import exceptions.AliasIsReservedError;

@Component
public class ProjectValidator implements Validator {

	private ProjectService projectService;

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public boolean supports(Class<?> clazz) {
		return Project.class.equals(clazz);
	}

	public void validate(Object o, Errors errors) {
		Project project = (Project) o;
		if ("projects".equals(project.getAlias())
				|| "teams".equals(project.getAlias())
				|| "index".equals(project.getAlias())) {
			((BindingResult) errors).addError(new AliasIsReservedError(project
					.getAlias()));
		} else if (projectService.isProjectPresent(project.getAlias(), project.getIdProject())) {
			((BindingResult) errors).addError(new AliasIsAlreadyTakenError(
					"project", project.getAlias()));
		}
	}

}
