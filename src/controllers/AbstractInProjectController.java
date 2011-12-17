package controllers;

import java.util.List;

import model.projects.Project;
import model.teams.Team;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import services.PBIService;
import services.ProjectService;
import services.SprintService;
import services.TeamService;
import services.WorkItemService;

import exceptions.ProjectNotFoundException;

/**
 * Abstract controller for data initialisation on pages connected with chosen project
 * @author TM
 * @see {@link ChosenProjectController}, {@link AttributesController}, {@link PbisController},
 * {@link ReleasesControllerTest}, {@link SprintsController},
 * {@link TeamInProjectController},
 */
public abstract class AbstractInProjectController {
	@Autowired
	PBIService pbiService;

	@Autowired
	ProjectService projectService;

	@Autowired
	SprintService sprintService;

	@Autowired
	WorkItemService workItemService;

	@Autowired
	TeamService teamService;

	public Logger log = Logger.getRootLogger();

	/**
	 * Method adding list of teams in chosen project to model
	 * 
	 * @return list of teams in chosen project
	 */
	@ModelAttribute("teams")
	@PreAuthorize("isAuthenticated()")
	public List<Team> getTeamsInProjectList(@PathVariable String projectAlias) {
		return teamService.getTeamsForProject(projectService
				.getProject(projectAlias));
	}

	/**
	 * Method adding list of all projects to model. Neccesasry for main menu.
	 * 
	 * @return list of all projects
	 */
	@ModelAttribute("projects")
	@PreAuthorize("isAuthenticated()")
	public List<Project> getAllProjectsList() {
		return projectService.getProjects();
	}

	/**
	 * Method adding chosen project alias into model container
	 * 
	 * @param projectAlias - chosen project alias
	 * @return project alias in upper case
	 */
	@ModelAttribute("chosenProjectAlias")
	@PreAuthorize("isAuthenticated()")
	public String getChosenProjectAlias(@PathVariable String projectAlias) {
		if (!projectService.isProjectPresent(projectAlias, null)) {
			throw new ProjectNotFoundException(projectAlias);
		}
		return projectAlias.toUpperCase();
	}
}
