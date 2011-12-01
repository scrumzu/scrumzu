package controllers;

import javax.validation.Valid;

import model.projects.Attribute;
import model.projects.AttributeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

import services.AttributeService;
import services.ProjectService;

/** Controller handling crud operations on project specific attributes.
 * @see {@link Attribute}
 * @author TM
 */
@Controller
@RequestMapping(value = "/{projectAlias}/")
public class AttributesController extends AbstractInProjectController {

	@Autowired
	ProjectService projectService;

	@Autowired
	AttributeService attributeService;

	/** Method generating new attriubte form
	 * @param projectAlias - chosen project alias
	 * @param model - model object in MVC
	 * @return new attribute form
	 */
	@RequestMapping(value = "attributes/new", method = RequestMethod.GET)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView getNewAttributeForm(@PathVariable String projectAlias,
			Model model) {
		Attribute a = new Attribute();
		model.addAttribute("attribute", a);
		model.addAttribute("op", "add");
		model.addAttribute("types", AttributeType.values());
		return new ModelAndView("attributeAddForm");
	}

	/** Method saving newly added attribute, posted from new attribute form
	 * @param projectAlias - chosen project alias
	 * @param attribute - command object sent from form
	 * @param result - validation object
	 * @param model - model object in MVC
	 * @return redirect view - redirecting to list chosen project details page
	 */
	@RequestMapping(value = "attributes/new", method = RequestMethod.POST)
	@Secured("ROLE_PRODUCT_OWNER")
	public ModelAndView saveAttribute(@PathVariable String projectAlias,
			@Valid @ModelAttribute("attribute") Attribute attribute,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("op", "add");
			model.addAttribute("types", AttributeType.values());
			return new ModelAndView("attributeAddForm");
		}
		attribute.setProject(projectService.getProject(projectAlias));
		attributeService.save(attribute);
		return new ModelAndView(new RedirectView("/"
				+ projectAlias.toUpperCase(), true, true, false));
	}

	/** Metgod for deleting attributes, based on ids list
	 * @param ids - array of ids of attributes to delete
	 * @param model - model object in MVC
	 * @return true if operation succeded
	 */
	@RequestMapping(value = "attributes", method = RequestMethod.DELETE)
	@Secured("ROLE_PRODUCT_OWNER")
	public @ResponseBody
	boolean deleteAttributes(@RequestBody long[] ids, Model model) {
		attributeService.deleteAttributes(ids);
		return true;
	}
}
