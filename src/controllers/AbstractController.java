package controllers;

import java.util.List;

import model.projects.Project;
import model.teams.Team;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;

import services.ProjectService;
import services.TeamService;

/**
 * Abstract controller class for data initialisation on pages without chosen project set.
 * @author TM
 * @see {@link TeamsController}, {@link ProjectsController}
 */
public abstract class AbstractController {

	@Autowired
	TeamService teamService;

	@Autowired
	ProjectService projectService;

	public Logger log = Logger.getRootLogger();

	/**
	 * Method adding list of all teams in chosen project to model in every method in controller
	 * 
	 * @param projectAlias - chosen project alias
	 * @return list of teams in project
	 */
	@ModelAttribute("teams")
	@PreAuthorize("isAuthenticated()")
	public List<Team> getTeamsInProjectList() {
		return teamService.getAllTeams();
	}

	/**
	 * Method adding list of all projects to model in every method in controller
	 * 
	 * @return ist of all projects
	 */
	@ModelAttribute("projects")
	@PreAuthorize("isAuthenticated()")
	public List<Project> getAllProjectsList() {
		return projectService.getProjects();
	}
}
