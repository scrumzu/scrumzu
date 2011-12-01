package controllers;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.projects.Project;
import model.projects.ProjectEditor;
import model.teams.Team;
import model.users.ScrumzuUser;
import model.users.ScrumzuUserEditor;

import org.springframework.beans.factory.annotation.Autowired;
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

import services.ProjectService;
import services.ScrumzuUserDetailsService;
import services.TeamService;

import exceptions.AliasIsAlreadyTakenError;

/**Controller handling CRUD operations on teams connected with already chosen project.
 * @author TM
 * @see {@link Team}, {@link TeamsController}
 */
@Controller
@RequestMapping(value = "/{projectAlias}/")
@SessionAttributes("team")
public class TeamInProjectController extends AbstractInProjectController{

	@Autowired
	TeamService teamService;
	@Autowired
	ProjectService projectService;
	@Autowired
	ScrumzuUserDetailsService userService;

	/**
	 * Method returning list view for teams in chosen project
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @return team list view
	 */
	@RequestMapping(value = "teams", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getTeamsList(@PathVariable String projectAlias,
			Model model) {
		model.addAttribute("op", "listByProject");
		return new ModelAndView("teamList");
	}

	/**
	 * Method generating form for adding new team for given project
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @param principal - currently logged user
	 * @return new team form view
	 */
	@RequestMapping(value = "teams/new", method = RequestMethod.GET)
	@Secured("ROLE_SCRUM_MASTER")
	public ModelAndView getNewTeamForm(@PathVariable String projectAlias,
			Model model, Principal principal) {
		Project p = projectService.getProject(projectAlias);
		Team team = new Team();
		team.setProject(p);

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		if (auth.getPrincipal() instanceof ScrumzuUser) {
			ScrumzuUser user = (ScrumzuUser) auth.getPrincipal();
			team.setUser(user);
		}

		List<ScrumzuUser> users = userService.getScrumMasters();
		model.addAttribute("users", users);
		model.addAttribute("team", team);
		model.addAttribute("op", "add");
		return new ModelAndView("teamAddForm");
	}

	/**
	 * Method handling saving new or edited team, sent from form
	 * 
	 * @param projectAlias - chosen project alias
	 * @param team - command object sent from form
	 * @param result - databinding and validation for given team
	 * @param model - model in MVC
	 * @param request - http request used for recognizing operation type (new or edit in request URL)
	 * @return redirecting view to form if errors occured, otherwise redirecting to list of teams
	 *         in given project
	 */
	@RequestMapping(value = { "teams/new", "teams/*/edit" }, method = RequestMethod.POST)
	@Secured("ROLE_SCRUM_MASTER")
	public ModelAndView saveTeam(@PathVariable String projectAlias,
			@Valid @ModelAttribute("team") Team team, BindingResult result,
			Model model, HttpServletRequest request) {
		String op = request.getRequestURI().contains("new") ? "Add" : "Edit";

		if (teamService.isTeamPresent(team.getAlias(), team.getIdTeam())) {
			result.addError(new AliasIsAlreadyTakenError("team", team
					.getAlias()));
		}

		if (result.hasErrors()) {
			List<ScrumzuUser> users = userService.getScrumMasters();
			model.addAttribute("users", users);
			model.addAttribute("op", op.toLowerCase());
			return new ModelAndView("team" + op + "Form");
		}

		teamService.saveTeam(team);

		return new ModelAndView(new RedirectView("/"
				+ projectAlias.toUpperCase() + "/teams", true, true, false));
	}

	/**
	 * Method generating edit form for chosen team
	 * 
	 * @param model - model in MVC
	 * @param idTeam - chosen team id
	 * @param projectAlias - chosen project alias
	 * @return edit team form view
	 */
	@RequestMapping(value = "teams/{idTeam}/edit", method = RequestMethod.GET)
	@Secured("ROLE_SCRUM_MASTER")
	public ModelAndView getEditTeamForm(Model model, @PathVariable long idTeam,
			@PathVariable String projectAlias) {

		Team team = teamService.getTeam(idTeam);
		model.addAttribute("team", team);
		List<ScrumzuUser> users = userService.getScrumMasters();
		model.addAttribute("users", users);
		model.addAttribute("op", "edit");
		return new ModelAndView("teamEditForm");
	}

	/**
	 * Method for getting team details view, based on team id and chosen project
	 * 
	 * @param projectAlias - chosen project alias
	 * @param idTeam - chosen team id
	 * @param model - model in MVX
	 * @return team details view
	 */
	@RequestMapping(value = "teams/{idTeam}", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getTeamById(@PathVariable String projectAlias,
			@PathVariable long idTeam, Model model) {
		List<ScrumzuUser> users = userService.getScrumMasters();
		model.addAttribute("users", users);
		Team team = teamService.getTeam(idTeam);
		model.addAttribute("team", team);
		model.addAttribute("op", "details");
		return new ModelAndView("teamDetails");
	}

	/**
	 * Method handling multiple teams delete operation
	 * 
	 * @param ids - array of teams ids
	 * @param model - model in MVC
	 * @return true if operation succeded
	 */
	@RequestMapping(value = "teams", method = RequestMethod.DELETE)
	@Secured("ROLE_SCRUM_MASTER")
	public @ResponseBody
	boolean deleteTeams(@RequestBody long[] ids, Model model) {
		teamService.deleteTeams(ids);
		return true;
	}

	/**
	 * Method handling single team delete operaion fired from form js
	 * 
	 * @param idTeam - chosen team id
	 * @param model - model in MVC
	 * @return true if delete operation succeded
	 */
	@RequestMapping(value = "teams/{idTeam}", method = RequestMethod.DELETE)
	@Secured("ROLE_SCRUM_MASTER")
	public @ResponseBody
	boolean deleteTeam(@PathVariable long idTeam, Model model) {
		teamService.deleteTeams(new long[] { idTeam });
		return true;
	}

	/**
	 * Method initializing databinders and editors used in releases forms
	 * 
	 * @param binder - spring framework data binder object
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Project.class, new ProjectEditor(
				projectService));
		binder.registerCustomEditor(ScrumzuUser.class, new ScrumzuUserEditor(
				userService));
	}
}
