package controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.filters.Filter;
import model.pbis.PBI;
import model.pbis.Type;
import model.projects.Attribute;
import model.projects.AttributeEditor;
import model.projects.Project;
import model.sprint.Sprint;
import model.sprint.SprintEditor;
import model.sprint.SprintStatus;
import model.teams.Team;
import model.teams.TeamEditor;
import model.users.ScrumzuUser;
import model.workItems.Status;
import model.workItems.WorkItem;
import model.workItems.WorkItemValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import services.FilterService;
import services.PBIService;
import services.ProjectService;
import services.SprintService;
import services.TeamService;
import services.WorkItemService;

/** Controller handling operations on PBIs. Contains not only crud operations, but also handle ajax requests.
 * @author TM
 * @see {@link PBI}, {@link WorkItem}, {@link Status}
 */
@Controller
@RequestMapping(value = "/{projectAlias}/")
@SessionAttributes("pbi")
public class PbisController extends AbstractInProjectController {

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

	@Autowired
	FilterService filterService;

	@Autowired
	WorkItemValidator workItemValidator;

	@ModelAttribute("attributes")
	@PreAuthorize("isAuthenticated()")
	public Set<Attribute> getProjectAttributes(@PathVariable String projectAlias) {
		return projectService.getProjectAttributes(projectAlias);

	}

	/**
	 * Method returning PBIs list for chosen project
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @param principal - currently logged user
	 * @return PBIs list view
	 */
	@RequestMapping(value = "pbis", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getPbisList(@PathVariable String projectAlias,
			Model model, Principal principal) {

		List<Filter> filters = null;

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		ScrumzuUser userDetails = (ScrumzuUser) auth.getPrincipal();
		filters = filterService.getFiltersForUser(userDetails);

		List<PBI> pbis = pbiService.getPBIsForProject(projectService
				.getProject(projectAlias));

		for (PBI p : pbis) {
			List<WorkItem> workItems = new ArrayList<WorkItem>();
			workItems.add(workItemService.getLastWorkItemForPBI(p));
			p.setWorkItems(workItems);
		}

		model.addAttribute("pbis", pbis);
		model.addAttribute("op", "list");
		model.addAttribute("filters", filters);
		return new ModelAndView("pbiList");
	}

	/**
	 * Method returning PBI details page, with neccesary data
	 * 
	 * @param model - model in MVC
	 * @param idPBI - chosen PBI id
	 * @param projectAlias - chosen project alias
	 * @return PBI details form
	 */
	@RequestMapping(value = "pbis/{idPBI}", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getPbiById(Model model, @PathVariable long idPBI,
			@PathVariable String projectAlias) {

		PBI pbi = pbiService.getPBI(idPBI);
		List<WorkItem> workItems = workItemService.getWorkItemsForPBI(pbi);
		pbi.setWorkItems(workItems);
		model.addAttribute("pbi", pbi);
		model.addAttribute("op", "details");
		return new ModelAndView("pbiDetails");
	}

	/**
	 * Method preparing and returning new PBI form
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @param principal - currently logged user
	 * @return new PBI form
	 */
	@RequestMapping(value = "pbis/new", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getNewPbiForm(@PathVariable String projectAlias,
			Model model, Principal principal) {

		PBI p = new PBI();
		p.setDateCreation(Calendar.getInstance().getTime());
		p.setProject(projectService.getProject(projectAlias));

		WorkItem wi = new WorkItem(Status.NEW);
		wi.setStoryPoints(0);
		p.getWorkItems().add(wi);

		model.addAttribute("pbi", p);
		model.addAttribute("op", "add");
		initFormData(model, projectAlias, principal);
		return new ModelAndView("pbiAddForm");
	}

	/**
	 * Method handling saving/updating PBI, depending on URL
	 * 
	 * @param projectAlias - chosen project alias
	 * @param pbi - command object, sent from form
	 * @param result - databinding and validation for command object
	 * @param model - model in MVC
	 * @param request - request used for getting URL details
	 * @param principal - currentyl logged user
	 * @return redirecting view to add/edit PBI form if errors occured, otherwise returning
	 *         to PBI list view
	 */
	@RequestMapping(value = { "pbis/new", "pbis/*/edit" }, method = RequestMethod.POST)
	@Secured({ "ROLE_PRODUCT_OWNER", "ROLE_SCRUM_MASTER" })
	public ModelAndView savePbi(@PathVariable String projectAlias,
			@Valid @ModelAttribute("pbi") PBI pbi, BindingResult result,
			Model model, HttpServletRequest request, Principal principal) {

		String op = request.getRequestURI().contains("new") ? "Add" : "Edit";

		WorkItem wi = pbi.getWorkItems().get(0);
		workItemValidator.validate(wi, result);

		if (result.hasErrors()) {
			model.addAttribute("op", op.toLowerCase());
			initFormData(model, projectAlias, principal);
			if(op.equals("Edit")){
				includeDisabledSprints(pbi, model);
			}
			return new ModelAndView("pbi" + op + "Form", model.asMap());
		}

		Map<String, Attribute> pa = new HashMap<String, Attribute>();
		for (Attribute a : getProjectAttributes(projectAlias)) {
			pa.put(a.getName(), a);
		}

		Double dblValue;
		for (String key : pbi.getFormDoubleAttributes().keySet()) {
			dblValue = pbi.getFormDoubleAttributes().get(key);
			if (dblValue != null) {
				pbi.setDoubleAttribute(pa.get(key), dblValue);
			}
		}

		String strValue;
		for (String key : pbi.getFormStringAttributes().keySet()) {
			strValue = pbi.getFormStringAttributes().get(key);
			if (strValue != null && !strValue.equals("")) {
				pbi.setStringAttribute(pa.get(key), strValue);
			}
		}
		pbi.setProject(projectService.getProject(projectAlias));
		pbiService.savePBI(pbi);

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		if (auth.getPrincipal() instanceof ScrumzuUser) {
			ScrumzuUser user = (ScrumzuUser) auth.getPrincipal();
			wi.setUser(user);
			wi.setDateForNow();
			wi.setPbi(pbi);
			workItemService.saveWorkItem(wi);
		}

		return new ModelAndView(new RedirectView("/"
				+ projectAlias.toUpperCase() + "/pbis", true, true, false));
	}

	/**
	 * Method preparing and returning edit PBI form
	 * 
	 * @param model - model in MVC
	 * @param idPBI - chosen PBI id
	 * @param projectAlias - chosen project alias
	 * @param principal - currently logged user
	 * @return edit PBI form view
	 */
	@RequestMapping(value = "pbis/{idPBI}/edit", method = RequestMethod.GET)
	@Secured({ "ROLE_PRODUCT_OWNER", "ROLE_SCRUM_MASTER" })
	public ModelAndView getEditPbiForm(Model model, @PathVariable long idPBI,
			@PathVariable String projectAlias, Principal principal) {
		PBI pbi = pbiService.getPBI(idPBI);

		List<WorkItem> workItems = new ArrayList<WorkItem>();
		workItems.add(0, workItemService.getLastWorkItemForPBI(pbi));

		pbi.setWorkItems(workItems);

		Map<String, String> sa = new HashMap<String, String>();
		for (Attribute attribute : pbi.getStringAttributes().keySet()) {
			sa.put(attribute.getName(),
					pbi.getStringAttribute(attribute.getName()));
		}

		Map<String, Double> da = new HashMap<String, Double>();
		for (Attribute attribute : pbi.getDoubleAttributes().keySet()) {
			da.put(attribute.getName(),
					pbi.getDoubleAttribute(attribute.getName()));
		}
		pbi.setFormStringAttributes(sa);
		pbi.setFormDoubleAttributes(da);

		model.addAttribute("pbi", pbi);
		includeDisabledSprints(pbi, model);
		model.addAttribute("op", "edit");
		initFormData(model, projectAlias, principal);
		return new ModelAndView("pbiEditForm");
	}

	/**
	 * Method handling delete operation on multiple PBIs
	 * 
	 * @param ids - array of chosen PBIs ids
	 * @param model - model in MVC
	 * @return true if operation succeded
	 */
	@RequestMapping(value = "pbis", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deletePbis(@RequestBody long[] ids, Model model) {
		pbiService.deletePBIs(ids);
		return true;
	}

	/**
	 * Method changing chosen PBIs statuses to DONE. PBI is ignored if it has state, that couldn't be changed
	 * to DONE, or is already done.
	 * 
	 * @param ids - array of chosen PBIs ids
	 * @param model - model in MVC
	 * @return JSON object containing changed PBIs ids
	 */
	@RequestMapping(value = "pbis/ajax/markasdone", method = RequestMethod.PUT)
	@Secured({ "ROLE_PRODUCT_OWNER", "ROLE_SCRUM_MASTER" })
	public @ResponseBody
	Map<String, Object> markAsDonePbis(@RequestBody long[] ids, Model model,
			Principal principal) {
		List<Long> saved = new ArrayList<Long>();
		ScrumzuUser user = null;
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		if (auth.getPrincipal() instanceof ScrumzuUser) {
			user = (ScrumzuUser) auth.getPrincipal();
		}

		for (long id : ids) {
			PBI pbi = pbiService.getPBI(id);
			WorkItem wi = workItemService.getWorkItemsForPBI(pbi).get(0);
			wi.setStatus(Status.DONE);
			wi.setUser(user);
			if (wi.isWriteable()) {
				wi.setDateForNow();
				wi.setPbi(pbi);
				workItemService.saveWorkItem(wi);
				saved.add(id);
			}
		}

		HashMap<String, Object> update = new HashMap<String, Object>();
		update.put("status", Status.DONE.toString());
		update.put("ids", saved.toArray(new Long[] {}));
		return update;
	}

	/**
	 * Method handling delete operation of single PBI, called from pbi details view
	 * delete button
	 * 
	 * @param model - model in MVC
	 * @param idPBI - chosen PBI id
	 * @return true if operation succeded
	 */
	@RequestMapping(value = "pbis/{idPBI}", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deleteSinglePbi(Model model, @PathVariable long idPBI) {
		pbiService.deletePBIs(new long[] { idPBI });
		return true;
	}

	/**
	 * Method initialising data collections required by filters js
	 * 
	 * @param model - model in MVC
	 * @param projectAlias - project alias
	 * @return JSON object containing teams, possible PBI statuses and srpints
	 */
	@RequestMapping(value = "pbis/ajax/init_filters", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody
	Map<String, Map<String, String>> initFilterData(Model model,
			@PathVariable String projectAlias) {
		Map<String, String> teams = new HashMap<String, String>();
		Map<String, String> sprints = new HashMap<String, String>();
		Map<String, String> statuses = new HashMap<String, String>();

		Project project = projectService.getProject(projectAlias);
		for (Team t : teamService.getTeamsForProject(project)) {
			teams.put(t.getIdTeam().toString(), t.getPublicName());
		}
		for (Sprint s : sprintService.getSprintsForProject(project)) {
			sprints.put(s.getIdSprint().toString(), s.getName());
		}
		for (Status status : Status.values()) {
			statuses.put(status.name(), status.getValue());
		}

		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		result.put("teams", teams);
		result.put("sprints", sprints);
		result.put("statuses", statuses);
		return result;
	}

	/**
	 * Method returning table containing PBIs filtered by given filter
	 * 
	 * @param filter - given filter, sent as JSON object from filters js
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @return table view containing filtered PBIs list
	 */
	@RequestMapping(value = "pbis/ajax/filters", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getPbisListByFilter(@RequestBody Filter filter,
			@PathVariable String projectAlias, Model model) {
		Set<PBI> pbis = pbiService.getPBIsByFilterAndProject(filter,
				projectService.getProject(projectAlias));
		model.addAttribute("pbis", pbis);
		model.addAttribute("op", "list");
		model.addAttribute("filtered", true);
		return new ModelAndView("pbiTable");
	}

	/**
	 * Method returning table containing all pbis in chosen project. Used to reset
	 * already filtered table in PBIs list to its initial state.
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @return pbis table containing all PBIs list
	 */
	@RequestMapping(value = "/pbis/ajax/all", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getPbisTable(@PathVariable String projectAlias,
			Model model) {

		List<PBI> pbis = pbiService.getPBIsForProject(projectService
				.getProject(projectAlias));
		for (PBI p : pbis) {
			List<WorkItem> workItems = new ArrayList<WorkItem>();
			workItems.add(workItemService.getLastWorkItemForPBI(p));
			p.setWorkItems(workItems);
		}
		model.addAttribute("pbis", pbis);
		return new ModelAndView("pbiTable");
	}

	/**
	 * Method initialising data collections required by add/edit PBI form
	 * 
	 * @param model - model in MVC
	 * @param projectAlias - chosen project alias
	 */
	private void initFormData(Model model, String projectAlias,
			Principal principal) {

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		if (auth.getPrincipal() instanceof ScrumzuUser) {
			ScrumzuUser user = (ScrumzuUser) auth.getPrincipal();
			model.addAttribute("availableStasuses",
					WorkItemService.getAvailableStatusesForUser(user));
		}

		model.addAttribute("types", Type.values());
		model.addAttribute("statuses", Status.values());
		model.addAttribute("teams", teamService
				.getTeamsForProject(projectService.getProject(projectAlias)));
		model.addAttribute("sprints", sprintService
				.getActiveSprintsForProject(projectService
						.getProject(projectAlias)));
	}

	/**Method initializing data collection of disabled sprints, needed in PBI edit form.
	 * @param pbi - PBI to be edited
	 * @param model - model in MVC
	 */
	private void includeDisabledSprints(PBI pbi, Model model){
		Sprint pbiLastSprint = pbi.getWorkItems().get(0).getSprint();
		if (pbiLastSprint != null
				&& pbiLastSprint.getSprintStatus().equals(SprintStatus.ENDED)) {
			List<Sprint> disabledSprints = new ArrayList<Sprint>();
			Long sid = pbi.getWorkItems().get(0).getSprint().getIdSprint();

			disabledSprints.add(sprintService.getSprint(sid));
			model.addAttribute("sprintsDisabled", disabledSprints);
		}
	}

	/**
	 * Method initializing databinders and editors used in releases forms
	 * 
	 * @param binder - spring framework data binder object
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
		binder.registerCustomEditor(Attribute.class, null,
				new AttributeEditor());
		binder.registerCustomEditor(Sprint.class, new SprintEditor(
				sprintService));
		binder.registerCustomEditor(Team.class, new TeamEditor(teamService));
	}

}
