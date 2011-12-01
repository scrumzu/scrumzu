package controllers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author TM
 * Controller handling landing page ( / ) only.
 *
 */
public class HomeControllerTest {
	HomeController controller;
	Model model;


	@Before
	public void setUp() throws Exception {
		controller = new HomeController();
		model = new ExtendedModelMap();
	}

	@Test
	public void testGetHomePageNotLoggedIn()
	{
		ModelAndView page = controller.getHomePage(model, null);
		Map<String, Object> modelMap = page.getModelMap();
		assertEquals("home", page.getViewName());
		assertTrue(modelMap.isEmpty());
	}

	@Test
	public void testGetHomePageLoggedIn(){
		ModelAndView page = controller.getHomePage(model, null);
		Map<String, Object> modelMap = page.getModelMap();
		assertEquals("home", page.getViewName());
		assertTrue(modelMap.isEmpty());
		
		
	}



}
