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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import services.ProjectService;
import services.TeamService;

import exceptions.ProjectNotFoundException;
/**Controller handling CRUD operations on projects. Update and delete operations on project are based on its id number.
 * @author TM
 * @see {@link Project}, {@link ChosenProjectController}
 */
@Controller
public class ProjectsController extends AbstractController{

	@Autowired
	ProjectService projectService;

	@Autowired
	TeamService teamService;

	@Autowired
	ProjectValidator projectValidator;

	/**
	 * Method returning list view of all projects
	 * 
	 * @param model - model in MVC
	 * @return projectList view
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getProjectsList(Model model) {
		model.addAttribute("op", "listAll");
		return new ModelAndView("projectList");
	}

	/**
	 * Method generating edit form for project chosen by its id
	 * 
	 * @param projectId - chosen project id
	 * @return redirecting to method generating editt form based on alias
	 */
	@RequestMapping(value = "/projects/{projectId}/edit", method = RequestMethod.GET)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView getEditProjectFormById(@PathVariable long projectId) {
		Project p = projectService.getProject(projectId);

		if (p == null) {
			throw new ProjectNotFoundException("" + projectId);
		}

		return new ModelAndView(new RedirectView("/" + p.getAlias() + "/edit",
				true, true, false));
	}

	/**
	 * Method generatind form for adding new project
	 * 
	 * @param model - model in MVC
	 * @return projectAddForm view
	 */
	@RequestMapping(value = "/projects/new", method = RequestMethod.GET)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView getNewProjectForm(Model model) {

		model.addAttribute("project", new Project());
		model.addAttribute("op", "add");
		return new ModelAndView("projectAddForm");
	}

	/**
	 * Method adding new project, posted from projectAddForm
	 * 
	 * @param project - command object, sent from form
	 * @param result - data binding and validation
	 * @param model - model in mvc
	 * @return - redirects to form, when errors are present. Otherwise redirects to project list
	 */
	@RequestMapping(value = "/projects/new", method = RequestMethod.POST)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView addProject(
			@Valid @ModelAttribute("project") Project project,
			BindingResult result, Model model) {

		projectValidator.setProjectService(projectService);
		projectValidator.validate(project, result);

		if (result.hasErrors()) {
			model.addAttribute("op", "add");
			return new ModelAndView("projectAddForm");
		}

		projectService.saveProject(project);
		return new ModelAndView(
				new RedirectView("/projects", true, true, false));
	}


	/**
	 * Method for deleting projects by their id
	 * @param ids - array of project ids
	 * @param model - model in MVC
	 * @return true if delete operation succedes
	 */
	@RequestMapping(value = "/projects", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deleteProjects(@RequestBody long[] ids, Model model) {
		projectService.deleteProjects(ids);
		return true;
	}

}
