package controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.pbis.PBI;
import model.pbis.PBIEditor;
import model.projects.Project;
import model.releases.Release;
import model.releases.ReleaseItem;
import model.users.ScrumzuUser;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import services.PBIService;
import services.ProjectService;
import services.ReleaseService;

/**Controller handling CRUD operations on releases. Also, it provides methods for initializing release burndown chart.
 * @author TM
 * @see {@link Release}
 */
@Controller
@RequestMapping(value = "/{projectAlias}/")
public class ReleasesController extends AbstractInProjectController{

	@Autowired
	ProjectService projectService;

	@Autowired
	PBIService pbiService;

	@Autowired
	ReleaseService releaseService;

	/**
	 * Gets releases list for chosen project
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @return list of releases for given project
	 */
	@RequestMapping(value = "releases", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getReleasesListForProject(
			@PathVariable String projectAlias, Model model) {

		List<Release> releases = releaseService
				.getReleasesForProject(projectService.getProject(projectAlias));
		model.addAttribute("releases", releases);
		model.addAttribute("op", "list");
		return new ModelAndView("releaseList");
	}

	/**
	 * Method redirecting to new release form
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @return new release form view
	 */
	@RequestMapping(value = "releases/new", method = RequestMethod.GET)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView getNewReleaseForm(@PathVariable String projectAlias,
			Model model) {

		Release release = new Release();
		release.setReleaseItems(new ArrayList<ReleaseItem>());
		Project project = projectService.getProject(projectAlias);
		release.setProject(project);
		model.addAttribute("release", release);
		model.addAttribute("pbis", pbiService.getPBIsForProject(project));
		model.addAttribute("op", "add");
		return new ModelAndView("releaseAddForm");
	}

	/**
	 * Method handling release saving. Depending on url release is saved as new or updated.
	 * 
	 * @param projectAlias - chosen project alias
	 * @param release - command object, sent from form
	 * @param result - data binding and validtaion for command object
	 * @param model - model in MVC
	 * @param request - request object, needed to extract url
	 * @param principal - currently logged user
	 * @return - redirect view dto proper add/edit form if errors occured, otherwise redirect to release list
	 */
	@RequestMapping(value = { "releases/new", "releases/*/edit" }, method = RequestMethod.POST)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView saveRelease(@PathVariable String projectAlias,
			@Valid @ModelAttribute("release") Release release,
			BindingResult result, Model model, HttpServletRequest request,
			Principal principal) {

		String op = request.getRequestURI().contains("new") ? "Add" : "Edit";

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		ScrumzuUser user = (ScrumzuUser) auth.getPrincipal();

		if (release.getPbis() != null) {
			for (PBI pbi : release.getPbis()) {
				release.addReleaseItem(pbi, user);
			}
		}

		if (result.hasErrors()) {
			Project project = projectService.getProject(projectAlias);
			model.addAttribute("pbis", pbiService.getPBIsForProject(project));
			model.addAttribute("op", op.toLowerCase());
			return new ModelAndView("release" + op + "Form");
		}

		release.setProject(projectService.getProject(projectAlias));
		releaseService.saveRelease(release);

		return new ModelAndView(new RedirectView("/"
				+ projectAlias.toUpperCase() + "/releases", true, true, false));
	}

	/**
	 * Method generating and preparing edit release form view
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @param idRelease - chosen release id
	 * @return release edit form view
	 */
	@RequestMapping(value = "releases/{idRelease}/edit", method = RequestMethod.GET)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView getEditReleaseForm(@PathVariable String projectAlias,
			Model model, @PathVariable Long idRelease) {

		Release release = releaseService.getReleaseWithDetails(idRelease);
		List<PBI> pbis = new ArrayList<PBI>();

		for (ReleaseItem ri : release.getReleaseItems()) {
			pbis.add(ri.getPbi());
		}

		release.setPbis(pbis);

		Project project = projectService.getProject(projectAlias);
		model.addAttribute("pbis", pbiService.getPBIsForProject(project));
		model.addAttribute("release", release);
		model.addAttribute("op", "edit");
		return new ModelAndView("releaseEditForm");
	}

	/**
	 * Method for getting release detail view
	 * 
	 * @param projectAlias - chosen project alias
	 * @param model - model in MVC
	 * @param idRelease - chosen release id
	 * @return release details view
	 */
	@RequestMapping(value = "releases/{idRelease}", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView getReleaseById(@PathVariable String projectAlias,
			Model model, @PathVariable Long idRelease) {

		Release release = releaseService.getReleaseWithDetails(idRelease);
		model.addAttribute("release", release);
		model.addAttribute("op", "details");
		return new ModelAndView("releaseDetails");
	}

	/**
	 * Method for deleting single release, fired from release details form
	 * 
	 * @param idRelease - chosen release id
	 * @param model - model in mvc
	 * @return true if delete operation succeded
	 */
	@RequestMapping(value = "releases/{idRelease}", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deleteSingleRelease(@PathVariable long idRelease, Model model) {
		releaseService.deleteReleases(new long[] { idRelease });
		return true;
	}

	/**
	 * Method collecting data for release burndown chart, called by AJAX in release details page
	 * 
	 * @param idRelease - chosen release id
	 * @return data containing sprint list with remaining story points values, as JSON object
	 */
	@RequestMapping(value = "releases/ajax/burndown/{idRelease}", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody
	Map<String, Long> getBurndownChartData(@PathVariable Long idRelease) {
		Release release = releaseService.getReleaseWithDetails(idRelease);
		return releaseService.getBurndownForRealease(release);
	}

	/**
	 * Method handling multiple releases delete operation
	 * 
	 * @param ids - array of release ids
	 * @param model - model in MVC
	 * @return true if delete operation succeded
	 */
	@RequestMapping(value = "releases", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deleteReleases(@RequestBody long[] ids, Model model) {
		releaseService.deleteReleases(ids);
		return true;
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
		binder.registerCustomEditor(PBI.class, new PBIEditor(pbiService));
	}

}
