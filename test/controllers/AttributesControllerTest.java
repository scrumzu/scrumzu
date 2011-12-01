package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.projects.Attribute;
import model.projects.AttributeType;
import model.projects.Project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import services.AttributeService;
import services.ProjectService;

@RunWith(MockitoJUnitRunner.class)
public class AttributesControllerTest {
	@Mock
	private AttributeService mockAttributeService;
	Model model;
	@Mock
	private ProjectService mockProjectService;
	private AttributesController controller;
	@Mock
	BindingResult bindingResult;

	@Before
	public void setUp() throws Exception {
		controller = new AttributesController();
		controller.attributeService = mockAttributeService;
		controller.projectService = mockProjectService;
		model = new ExtendedModelMap();
	}


	@Test
	public void testGetNewAttributeForm() {
		model = new ExtendedModelMap();
		controller.getNewAttributeForm("TD", model);
		Map<String, Object> modelMap = model.asMap();
		Attribute a = new Attribute();
		assertEquals(a, modelMap.get("attribute"));
		assertEquals(modelMap.get("op"), "add");
		assertEquals(((AttributeType[])modelMap.get("types")).length, AttributeType.values().length);
	}

	@Test
		public void testSaveAttribute() {
			
			Attribute a = new Attribute();
	
			
			final List<Attribute> attribs = new ArrayList<Attribute>();
			
			doAnswer(new Answer<String>() {
				public String answer(InvocationOnMock invocation) {
					Attribute att = (Attribute) invocation.getArguments()[0];
					attribs.add(att);
					return attribs.toString();
				}
			}).when(mockAttributeService).save((Attribute) anyObject());
		
			ReflectionTestUtils.setField(controller, "projectService",
					mockProjectService);
	
			Project project = new Project(1);
			project.setAlias("TD");
			when(mockProjectService.getProject(anyString())).thenReturn(project);
			ReflectionTestUtils.setField(controller, "attributeService",
					mockAttributeService);
			
			ModelAndView page = controller.saveAttribute("TD", a, bindingResult, model);
			
			assertEquals(1,  attribs.size());
			assertEquals(attribs.get(0).getProject(), project);
			
			when(bindingResult.hasErrors()).thenReturn(true);
			page = controller.saveAttribute("TD", a, bindingResult, model);
			Map<String, Object> modelMap = model.asMap();	
			assertEquals("add", modelMap.get("op"));
			assertEquals("attributeAddForm", page.getViewName());
		}

	@Test
	public void testDeleteAttributes() {

		final List<Attribute> attribute = new ArrayList<Attribute>();
		for (int i = 1; i < 6; i++) {
			attribute.add(new Attribute(i));
		}

		long[] ids = { 1, 2, 3 };
		int size = attribute.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long[] deletedIndexes = (long[]) invocation.getArguments()[0];
				for (int i = 0; i < deletedIndexes.length; i++) {
					attribute.remove(deletedIndexes[i]);
				}

				return attribute.toString();
			}
		}).when(mockAttributeService).deleteAttributes((long[]) anyObject());


		ReflectionTestUtils
		.setField(controller, "attributeService", mockAttributeService);
		boolean answer = controller.deleteAttributes(ids, model);
		assertTrue(answer);
		assertEquals(size, attribute.size());
	}

}
