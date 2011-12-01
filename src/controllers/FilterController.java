package controllers;

import java.security.Principal;
import java.util.List;

import model.filters.Filter;
import model.users.ScrumzuUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import services.FilterService;

/** Controller handling operations on PBI filters created by users.
 * @author TW
 * @see {@link Filter}
 */
@Controller
public class FilterController {

	@Autowired
	FilterService filterService;

	/**
	 * Gets list of filters defined for currently logged user
	 * @param model - model object in MVC
	 * @param principal - logged user object
	 * @return list of Filter objects
	 */
	@RequestMapping(value = "/filters", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody List<Filter> getAllFilters( Model model, Principal principal) {
		List<Filter> filters = null;
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		if (auth.getPrincipal() instanceof ScrumzuUser) {
			ScrumzuUser userDetails = (ScrumzuUser) auth.getPrincipal();
			filters = filterService.getFiltersForUser(userDetails);
		}
		return filters;
	}

	/**
	 * Deletes filter with given id
	 * @param idFilter - id of filter to delete
	 * @param model - model object in MVC
	 * @return status of delete operation - true or false
	 */
	@RequestMapping(value = "/filters/{idFilter}", method = RequestMethod.DELETE)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody boolean deleteFilter(@PathVariable long idFilter, Model model) {
		filterService.deleteFilters(new long[]{idFilter});
		return true;
	}

	/**
	 * Updates filter with given id in database - filter edit operation
	 * @param filter - command object
	 * @param idFilter - filter id
	 * @param model - model object in MVC
	 * @return status of edit operation - true or false
	 */
	@RequestMapping(value = "/filters/{idFilter}", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ROLE_ADMIN') or (isAuthenticated() and #filter.user.username == principal.username)")
	public @ResponseBody boolean updateFilter(@RequestBody Filter filter, @PathVariable long idFilter, Model model, Principal principal) {

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		if (auth.getPrincipal() instanceof ScrumzuUser) {
			ScrumzuUser user = (ScrumzuUser) auth.getPrincipal();
			filter.setUser(user);
			filterService.saveFilter(filter);
		}
		return true;
	}

	/**
	 * Adds new filter to database, save filter operation
	 * @param filter - command object
	 * @param model - model object in MVC
	 * @return add status - true or false
	 */
	@RequestMapping(value = "/filters", method = RequestMethod.PUT)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody Long saveFilter(@RequestBody Filter filter, Model model, Principal principal) {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
		if (auth.getPrincipal() instanceof ScrumzuUser) {
			ScrumzuUser userDetails = (ScrumzuUser) auth.getPrincipal();
			filter.setUser(userDetails);

			filterService.saveFilter(filter);
		}
		return filter.getIdFilter();
	}

	/**
	 * Gets filter from database and returns as JSON object
	 * @param idFilter - filter id
	 * @param model - model object in MVC
	 * @return Filter represented as JSON object
	 */
	@RequestMapping(value = "/filters/{idFilter}", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody Filter getFilter(@PathVariable long idFilter, Model model) {
		Filter filter = filterService.getFilterById(idFilter);
		return filter;

	}




}
