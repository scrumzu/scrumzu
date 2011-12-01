package controllers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.projects.Project;
import model.projects.ProjectValidator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import services.ProjectService;
import services.TeamService;

import exceptions.ProjectNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class ChosenProjectControllerTest {
	@Mock
	private ProjectService mockProjectService;
	Model model;
	@Mock
	private ProjectValidator projectValidator;

	@Mock 
	BindingResult bindingResult;
	@Mock
	private TeamService mockTeamService;
	private ChosenProjectController controller;


	@Before
	public void setUp() throws Exception {
		model = new ExtendedModelMap();
		controller = new ChosenProjectController();
		controller.projectService = mockProjectService;
		controller.projectValidator = projectValidator;
		controller.teamService = mockTeamService;
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetEditProjectForm() {
		Project project = new Project(1L);
		project.setAlias("asda");
		when(mockProjectService.getProject(anyString())).thenReturn(
				project);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		ModelAndView page = controller.getEditProjectForm(model, project.getAlias());
		
		Map<String, Object> modelMap = model.asMap();
		assertEquals("edit", modelMap.get("op"));
		assertEquals("projectEditForm", page.getViewName());
		
		
		when(mockProjectService.getProject(anyString())).thenReturn(
				null);
		try{
			controller.getEditProjectForm(model, project.getAlias());
		}
		catch(ProjectNotFoundException e){
			assertTrue(e instanceof ProjectNotFoundException);
		}
	}

	@Test
	public void testEditProject() {
		Project project = new Project();
		project.setName("BEFORETEST");

		final String title = "AFTERTEST";
		final List<Project> projects = new ArrayList<Project>();
		projects.add(project);

		int size = projects.size();

		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Project p = projects.get(projects.indexOf((invocation
						.getArguments()[0])));
				p.setName(title);
				return projects.contains(invocation.getArguments()[0]);
			}
		}).when(mockProjectService).saveProject((Project) anyObject());
		when(bindingResult.hasErrors()).thenReturn(false);

		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		ModelAndView page = controller.editProject(project, bindingResult,
				project.getAlias(), model);
		assertEquals(size, projects.size());
		assertEquals(project.getName(), title);
		assertEquals("/projects", ((RedirectView)page.getView()).getUrl());
	}
	
	@Test
	public void testEditProjectWithErrors() {
		Project project = mock(Project.class);
		project.setName("BEFORETEST");
		when(project.getAlias()).thenReturn("TST");
		when(project.getName()).thenReturn("AFTERTEST");

		final String title = "AFTERTEST";
		final List<Project> projects = new ArrayList<Project>();
		projects.add(project);

		int size = projects.size();

		when(bindingResult.hasErrors()).thenReturn(true);

		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		ModelAndView page = controller.editProject(project, bindingResult,
				project.getAlias(), model);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("projectEditForm", page.getViewName());
		assertEquals("edit", modelMap.get("op"));
		assertEquals(size, projects.size());
		assertEquals(project.getName(), title);
	}


	@Test
	public void testGetProjectByAlias() {

		Project project = mock(Project.class);

		when(mockProjectService.getProject(anyString())).thenReturn(
				project);
		when(project.getAlias()).thenReturn("TST");

		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		ModelAndView page = controller.getProjectByAlias(model, project.getAlias());

		Map<String, Object> modelMap = model.asMap();
		assertEquals("details", modelMap.get("op"));
		assertEquals(project, modelMap.get("project"));
		assertEquals("projectDetails", page.getViewName());
		
		
		
		when(mockProjectService.getProject(anyString())).thenReturn(
				null);
		try{
			controller.getProjectByAlias(model, project.getAlias());
		}
		catch(Exception e){
			assertTrue(e instanceof ProjectNotFoundException);
		}
		
	}

	@Test
	public void testDeleteSingleProject()
	{

		final List<Project> projects = new ArrayList<Project>();
		for (int i = 1; i < 6; i++) {
			projects.add(new Project(i));
		}

		int size = projects.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				projects.remove(1);
				return projects.toString();
			}
		}).when(mockProjectService).deleteProject(anyString());

		when(mockProjectService.getProject(anyString())).thenReturn(
				projects.get(1));

		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		boolean answer = controller.deleteSingleProject(model, "TST");

		assertTrue(answer);
		assertEquals(size-1, projects.size());
	}

}
