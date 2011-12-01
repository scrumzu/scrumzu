package controllers;

import javax.validation.Valid;

import model.projects.Project;
import model.projects.ProjectValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import services.ProjectService;
import services.TeamService;

import exceptions.ProjectNotFoundException;

/**Controller handling read, update and remove operation on project chosen by its alias.
 * @author TM
 * @see {@link Project}, {@link ProjectsController}
 */
@Controller
public class ChosenProjectController extends AbstractInProjectController {
	@Autowired
	ProjectService projectService;

	@Autowired
	TeamService teamService;

	@Autowired
	ProjectValidator projectValidator;

	/**
	 * Method generating edit form for project chosen by alias
	 * 
	 * @param model - model in MVC
	 * @param projectAlias - chosen project alias
	 * @return edit project form view
	 */
	@RequestMapping(value = "/{projectAlias}/edit", method = RequestMethod.GET)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView getEditProjectForm(Model model,
			@PathVariable String projectAlias) {

		Project project = projectService.getProject(projectAlias);

		if (project == null) {
			throw new ProjectNotFoundException(projectAlias);
		}

		model.addAttribute("project", project);
		model.addAttribute("op", "edit");
		model.addAttribute("chosenProjectAlias", projectAlias.toUpperCase());
		return new ModelAndView("projectEditForm");



	}

	/**
	 * Method handling edited project, sent from edit project form
	 * 
	 * @param project - edited project
	 * @param result - data binding and validation for given project
	 * @param projectAlias - chosen project alias
	 * @param model - model in MBC
	 * @return redirect to project edit form if errors occured, otherwise redirect to projects list
	 */
	@RequestMapping(value = "/{projectAlias}/edit", method = RequestMethod.POST)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView editProject(
			@Valid @ModelAttribute("project") Project project,
			BindingResult result, @PathVariable String projectAlias, Model model) {

		projectValidator.setProjectService(projectService);
		projectValidator.validate(project, result);

		if (result.hasErrors()) {
			model.addAttribute("op", "edit");
			model.addAttribute("chosenProjectAlias", projectAlias.toUpperCase());
			return new ModelAndView("projectEditForm");
		}

		projectService.saveProject(project);

		return new ModelAndView(
				new RedirectView("/projects", true, true, false));
	}

	/**
	 * Method for getting project detailspage
	 * 
	 * @param model - model in MVC
	 * @param projectAlias - chosen project alias
	 * @return projest details page based on it's alias
	 */
	@RequestMapping(value = "/{projectAlias}", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getProjectByAlias(Model model,
			@PathVariable String projectAlias) {

		Project project = projectService.getProject(projectAlias);

		if (project == null) {
			throw new ProjectNotFoundException(projectAlias);
		}

		model.addAttribute("attributes",
				projectService.getProjectAttributes(projectAlias));
		model.addAttribute("project", project);
		model.addAttribute("op", "details");
		model.addAttribute("chosenProjectAlias", projectAlias.toUpperCase());
		return new ModelAndView("projectDetails");
	}

	/**
	 * Method for deleting project by it's alias
	 * 
	 * @param model - model in MVC
	 * @param projectAlias - chosen project alias
	 * @return true if delete operation succedes
	 */
	@RequestMapping(value = "/{projectAlias}", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deleteSingleProject(Model model, @PathVariable String projectAlias) {
		projectService.deleteProject(projectAlias);
		return true;
	}

}
