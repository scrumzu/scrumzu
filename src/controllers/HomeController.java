package controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/** Basic controller managing homepage display and redirecting already logged users.
 * @author TM
 */
@Controller
public class HomeController {

	/** Method returning home page or index page depending on user authentication status
	 * @param model - model in MVC
	 * @param principal - already logged user, null if not authenticated
	 * @return home page view or redirecting to index page view
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getHomePage(Model model, Principal principal) {
		return principal == null? new ModelAndView("home") : new ModelAndView(new RedirectView("/projects", true, true, false));
	}
}
