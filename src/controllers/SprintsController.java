package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import model.sprint.Sprint;
import model.sprint.SprintStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import services.PBIService;
import services.ProjectService;
import services.SprintService;
import services.TeamService;

import exceptions.InvalidSprintDateException;

/** Controller handling CRUD operations on sprints, connected with already chosen project. Also handles ajax requests connected to sprint.
 * @author TM
 * @see {@link Sprint}, {@link SprintStatus}
 */
@Controller
@RequestMapping(value = "/{projectAlias}/")
public class SprintsController extends AbstractInProjectController{

	@Autowired
	SprintService sprintService;

	@Autowired
	ProjectService projectService;

	@Autowired
	PBIService pbiService;

	@Autowired
	TeamService teamService;

	/**
	 * Method returning all sprints list view
	 * 
	 * @param model - model in MVC
	 * @return 'sprintList' view
	 */
	@RequestMapping(value = "sprints", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getSprintsList(@PathVariable String projectAlias,
			Model model) {

		List<Sprint> sprints = sprintService
				.getSprintsForProject(projectService.getProject(projectAlias));
		Map<Long, Long> teamsCount = new HashMap<Long, Long>();
		Map<Long, Long> pbisCount = new HashMap<Long, Long>();

		for (Sprint s : sprints) {
			teamsCount.put(s.getIdSprint(), teamService.countTeamsForSprint(s));
			pbisCount.put(s.getIdSprint(), pbiService.countPBIsForSprint(s));
		}

		model.addAttribute("sprints", sprints);
		model.addAttribute("teamsCount", teamsCount);
		model.addAttribute("pbisCount", pbisCount);
		model.addAttribute("op", "list");
		return new ModelAndView("sprintList");
	}

	/**
	 * Method generationg form for adding sprint
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @return new sprint form view
	 */
	@RequestMapping(value = "sprints/new", method = RequestMethod.GET)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView getNewSprintForm(@PathVariable String projectAlias,
			Model model) {

		Sprint s = new Sprint();
		model.addAttribute("sprint", s);
		model.addAttribute("op", "add");
		model.addAttribute("statuses", SprintStatus.values());
		return new ModelAndView("sprintAddForm");
	}

	/**
	 * Method adding new sprint, sent from new sprint form
	 * 
	 * @param projectAlias - chosen project alias
	 * @param sprint - command object sent from form
	 * @param result - databinding and validation form sprint
	 * @param model - model in MBC
	 * @return redirect view to new sprint form if errors occured, otherwise redirect to sprints list view
	 */
	@RequestMapping(value = "sprints/new", method = RequestMethod.POST)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView addSprint(@Valid @PathVariable String projectAlias,
			@ModelAttribute("sprint") Sprint sprint, BindingResult result,
			Model model) {
		if (sprint.getDateFrom().after(sprint.getDateTo())) {
			result.addError(new InvalidSprintDateException());
		}
		if (result.hasErrors()) {
			model.addAttribute("op", "add");
			model.addAttribute("statuses", SprintStatus.values());
			return new ModelAndView("sprintAddForm");
		}
		sprint.setProject(projectService.getProject(projectAlias));
		sprintService.saveSprint(sprint);
		return new ModelAndView(new RedirectView("/"
				+ projectAlias.toUpperCase() + "/sprints", true, true, false));
	}

	/**
	 * Method generating edit sprint form
	 * 
	 * @param projectAlias - chosen project alaias
	 * @param model - model in MVC
	 * @param idSprint - chosen sprint id
	 * @return edit sprint form view
	 */
	@RequestMapping(value = "sprints/{idSprint}/edit", method = RequestMethod.GET)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView getEditSprintForm(@PathVariable String projectAlias,
			Model model, @PathVariable int idSprint) {

		Sprint sprint = sprintService.getSprint((long) idSprint);
		model.addAttribute("sprint", sprint);
		model.addAttribute("op", "edit");
		model.addAttribute("statuses", SprintStatus.values());
		return new ModelAndView("sprintEditForm");
	}

	/**
	 * Method handling edited sprint save
	 * 
	 * @param sprint - command object, sent from edit sprint form
	 * @param result - databinding and validation for given sprint
	 * @param projectAlias - chosen project alias
	 * @param model - model in MBC
	 * @return redirecting view to sprint edit form if errors occured, otherwise redirecting to sprints list
	 */
	@RequestMapping(value = "sprints/{idSprint}/edit", method = RequestMethod.POST)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView editSprint(
			@Valid @ModelAttribute("sprint") Sprint sprint,
			BindingResult result, @PathVariable String projectAlias, Model model) {
		if (sprint.getDateFrom().after(sprint.getDateTo())) {
			result.addError(new InvalidSprintDateException());
		}
		if (result.hasErrors()) {
			model.addAttribute("op", "edit");
			model.addAttribute("statuses", SprintStatus.values());
			return new ModelAndView("sprintEditForm");
		}
		sprint.setProject(projectService.getProject(projectAlias));
		sprintService.saveSprint(sprint);

		return new ModelAndView(new RedirectView("/"
				+ projectAlias.toUpperCase() + "/sprints", true, true, false));
	}

	/**
	 * Method returning sprint details view for chosen sprint
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @param idSprint - chosen sprint id
	 * @return sprint details view
	 */
	@RequestMapping(value = "sprints/{idSprint}", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getSprintById(@PathVariable String projectAlias,
			Model model, @PathVariable long idSprint) {

		Sprint sprint = sprintService.getSprint(idSprint);
		model.addAttribute("sprint", sprint);
		model.addAttribute("op", "details");
		return new ModelAndView("sprintDetails");
	}

	/**
	 * Method for deleting sprints chosen by ids
	 * @param ids - array of sprint ids
	 * @param model - model in MVC
	 * @return true if delete operation succeded
	 */
	@RequestMapping(value = "sprints", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deleteSprints(@RequestBody long[] ids, Model model) {
		sprintService.deleteSprints(ids);
		return true;
	}

	/**
	 * Method for changing sprint status from start to end, or other way around.
	 * Skips database operations if status is already set.
	 * @param idSprint - chosen sprint id
	 * @param action - start or end
	 * @param model - model in MVC
	 * @return JSON array containing sprint id and its new status
	 */
	@RequestMapping(value = "sprints/{idSprint}", method = RequestMethod.PUT)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	String[] startOrEndSprint(@PathVariable long idSprint,
			@RequestBody String action, Model model) {
		Sprint sprint = sprintService.getSprint(idSprint);
		if ("start".equals(action)) {
			sprint.setSprintStatus(SprintStatus.STARTED);
			sprintService.saveSprint(sprint);
		} else if ("end".equals(action)) {
			sprint.setSprintStatus(SprintStatus.ENDED);
			sprintService.saveSprint(sprint);
		}
		return new String[] { sprint.getIdSprint().toString(),
				sprint.getSprintStatus().toString() };
	}


	/**
	 * Method for deleting sprint based on its id
	 * @param idSprint - chosen sprint id
	 * @param model - model in MVC
	 * @return true if operation succedes
	 */
	@RequestMapping(value = "sprints/{idSprint}", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deleteSingleSprint(@PathVariable long idSprint, Model model) {
		sprintService.deleteSprints(new long[] { idSprint });
		return true;
	}

	/**
	 * Binding custom editors and binders used in sprint forms
	 * @param binder - spring framework object for data binding
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}
}
