package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.users.ScrumzuAuthority;
import model.users.ScrumzuAuthorityEditor;
import model.users.ScrumzuUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import services.ProjectService;
import services.ScrumzuAuthorityService;
import services.ScrumzuUserDetailsService;
import services.TeamService;

import exceptions.EmptyAuthoritiesListError;
import exceptions.UsernameIsAlreadyTakenError;

/** Controller handling CRUD operations on users.
 * @author TM
 * @see {@link ScrumzuUser}, {@link ScrumzuAuthority}
 */
@Controller
@Secured("ROLE_ADMIN")
public class UsersController extends AbstractController{

	@Autowired
	ScrumzuUserDetailsService userService;

	@Autowired
	ScrumzuAuthorityService authorityService;

	@Autowired
	TeamService teamService;

	@Autowired
	ProjectService projectService;

	/**
	 * Method returnining list view for all users
	 * 
	 * @param model - model in MVC
	 * @return users list view
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView getUsersList(Model model) {
		List<ScrumzuUser> users = userService.getList();
		model.addAttribute("users", users);
		model.addAttribute("op", "list");
		return new ModelAndView("userList");
	}

	/**
	 * 
	 * Method genrating and preparing edit user form view
	 * 
	 * @param model - model in MVC
	 * @param idUser - chosen user id
	 * @return user edit form view
	 */
	@RequestMapping(value = "/users/{idUser}/edit", method = RequestMethod.GET)
	public ModelAndView getEditUserForm(Model model, @PathVariable Long idUser) {
		ScrumzuUser user = userService.getUser(idUser);
		model.addAttribute("user", user);
		model.addAttribute("op", "edit");
		model.addAttribute("authorities", authorityService.getAuthorities());
		return new ModelAndView("userEditForm");
	}

	/**
	 * Merhod generating and preparing new user form view
	 * 
	 * @param model - model in MVC
	 * @return new user form view
	 */
	@RequestMapping(value = "/users/new", method = RequestMethod.GET)
	public ModelAndView getNewUserForm(Model model) {
		ScrumzuUser user = new ScrumzuUser();
		user.setAuthorities(new ArrayList<ScrumzuAuthority>());
		model.addAttribute("user", new ScrumzuUser());
		model.addAttribute("op", "add");
		model.addAttribute("authorities", authorityService.getAuthorities());
		return new ModelAndView("userAddForm");
	}

	/**
	 * Method handling save and update user info operations
	 * 
	 * @param user - chosen user, command object sent from fomr
	 * @param result - databinding and validation for command object
	 * @param model - model in MVC
	 * @param newPassword - new password, if was changed in form
	 * @param changePassword - flag indicating if password should be updated or not
	 * @param request - request representation, used for getting details about URL
	 * @return redirecting view to add/edit form if errors occured, otherwise redirecting
	 *         to users list view
	 */
	@RequestMapping(value = { "/users/new", "/users/*/edit" }, method = RequestMethod.POST)
	public ModelAndView saveUser(
			@Valid @ModelAttribute("user") ScrumzuUser user,
			BindingResult result,
			Model model,
			@RequestParam(value = "newPassword", defaultValue = "password") String newPassword,
			@RequestParam(value = "changePassword", defaultValue = "off") String changePassword,
			HttpServletRequest request) {
		PasswordEncoder encoder = new ShaPasswordEncoder();
		String op = request.getRequestURI().contains("new") ? "Add" : "Edit";

		if ("on".equals(changePassword) || "Add".equals(op)) {
			String hashedPassword = encoder.encodePassword(newPassword,
					user.getSalt());
			user.setPassword(hashedPassword);
		}

		if (user.getAuthoritiesList() == null) {
			result.addError(new EmptyAuthoritiesListError());
		}

		if (userService.isUsernameTaken(user.getUsername(), user.getIdUser())) {
			result.addError(new UsernameIsAlreadyTakenError(user.getUsername()));
		}

		if (result.hasErrors()) {
			model.addAttribute("op", op.toLowerCase());
			model.addAttribute("authorities", authorityService.getAuthorities());
			return new ModelAndView("user" + op + "Form");
		}

		user.setEnabled(true);
		userService.save(user);
		return new ModelAndView(new RedirectView("/users", true, true, false));
	}

	/**
	 * Method handling users delete operaiont
	 * 
	 * @param ids - chosen user ids
	 * @param model - model in MVC
	 * @return true if delete operation succeded
	 */
	@RequestMapping(value = "/users", method = RequestMethod.DELETE)
	public @ResponseBody
	boolean deleteUsers(@RequestBody long[] ids, Model model) {
		userService.disableUsers(ids);
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
		binder.registerCustomEditor(ScrumzuAuthority.class,
				new ScrumzuAuthorityEditor(authorityService));
	}
}
