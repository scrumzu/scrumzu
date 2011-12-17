package controllers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.projects.Project;
import model.projects.ProjectValidator;
import model.teams.Team;
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

import exceptions.ProjectNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class ProjectsControllerTest {
	@Mock
	private ProjectService projectService;
	@Mock
	BindingResult bindingResult;
	@Mock
	ProjectValidator projectValidator;

	Model model;
	private ProjectsController controller;
	List<Project> projects;
	List<Team> teams;

	@Before
	public void setUp() throws Exception {
		controller = new ProjectsController();
		controller.projectService = projectService;
		model = new ExtendedModelMap();
		projects = new ArrayList<Project>();
		teams = new ArrayList<Team>();

		model.addAttribute("teams", teams);
		model.addAttribute("projects", projects);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetProjectsList() {
		List<Project> projects = new ArrayList<Project>();
		when(projectService.getProjects()).thenReturn(projects);
		ReflectionTestUtils.setField(controller, "projectService",projectService);

		ModelAndView page = controller.getProjectsList(model);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("listAll", modelMap.get("op"));
		assertEquals(projects, modelMap.get("projects"));
		assertEquals("projectList", page.getViewName());
	}

	@Test
	public void testGetNewProjectForm() {
		Project project = new Project();
		ModelAndView page = controller.getNewProjectForm(model);

		Map<String, Object> modelMap = model.asMap();

		assertEquals("add", modelMap.get("op"));
		assertEquals(project, modelMap.get("project"));
		assertEquals("projectAddForm", page.getViewName());
	}

	@Test
	public void testAddProject() {
		final List<Project> projects = new ArrayList<Project>();
		int size = projects.size();
		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				projects.add((Project) invocation.getArguments()[0]);
				return projects.toString();
			}
		}).when(projectService).saveProject((Project) anyObject());

		when(bindingResult.hasErrors()).thenReturn(false);
		ReflectionTestUtils.setField(controller, "projectValidator",
				projectValidator);

		ReflectionTestUtils.setField(controller, "projectService",
				projectService);
		Project project = mock(Project.class);

		ModelAndView page = controller
				.addProject(project, bindingResult, model);
		assertEquals("/projects", ((RedirectView) page.getView()).getUrl());
		assertEquals(size + 1, projects.size());
	}

	@Test
	public void testAddProjectWithErrors() {
		final List<Project> projects = new ArrayList<Project>();
		int size = projects.size();

		ReflectionTestUtils.setField(controller, "projectValidator",
				projectValidator);
		when(bindingResult.hasErrors()).thenReturn(true);
		when(projectService.isProjectPresent(anyString(), anyLong())).thenReturn(true);
		ReflectionTestUtils.setField(controller, "projectService",
				projectService);

		Project project = mock(Project.class);
		ModelAndView page = controller
				.addProject(project, bindingResult, model);

		Map<String, Object> modelMap = model.asMap();

		assertEquals("projectAddForm", page.getViewName());
		assertEquals("add", modelMap.get("op"));
		assertEquals(size, projects.size());
	}

	@Test
	public void testDeleteProjects() {

		final List<Project> projects = new ArrayList<Project>();
		for (int i = 1; i < 6; i++) {
			projects.add(new Project(i));
		}

		long[] ids = { 1, 2, 3 };
		int size = projects.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long[] deletedIndexes = (long[]) invocation.getArguments()[0];
				System.out.println("DELETING:");
				for (int i = 0; i < deletedIndexes.length; i++) {
					projects.remove((int) deletedIndexes[i] - 1);
				}

				return projects.toString();
			}
		}).when(projectService).deleteProjects((long[]) anyObject());

		ReflectionTestUtils.setField(controller, "projectService",
				projectService);
		boolean answer = controller.deleteProjects(ids, model);
		assertTrue(answer);
		assertEquals(size - ids.length, projects.size());
	}

	@Test
	public void testGetEditProjectFormById() {
		Project project = mock(Project.class);
		String alias = "TST";
		when(projectService.getProject(anyLong())).thenReturn(project);
		when(project.getAlias()).thenReturn(alias);
		when(project.getIdProject()).thenReturn(1L);
		ReflectionTestUtils.setField(controller, "projectService",
				projectService);
		ModelAndView page = controller.getEditProjectFormById(project
				.getIdProject());
		assertEquals("/" + project.getAlias() + "/edit",
				((RedirectView) page.getView()).getUrl());
	}

	@Test (expected = ProjectNotFoundException.class)
	public void testGetEditProjectFormByIdProjectNotFound()
	{
		when(projectService.getProject(anyString()))
		.thenReturn(null);
		controller.getEditProjectFormById(anyLong());
	}

}
