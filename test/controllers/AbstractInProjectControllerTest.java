package controllers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import model.projects.Project;
import model.teams.Team;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import services.FilterService;
import services.PBIService;
import services.ProjectService;
import services.SprintService;
import services.TeamService;
import services.WorkItemService;

import exceptions.ProjectNotFoundException;


public class AbstractInProjectControllerTest {

	@Mock
	BindingResult bindingResult;
	@Mock
	PBIService mockPbiService;
	@Mock
	TeamService mockTeamService;
	@Mock
	SprintService mockSprintService;
	@Mock
	WorkItemService mockWorkItemService;
	@Mock
	ProjectService mockProjectService;
	@Mock
	FilterService mockFilterService;

	AbstractInProjectController controller;

	@Before
	public void setUp() throws Exception {
	
		controller = Mockito.mock(AbstractInProjectController.class,
				Mockito.CALLS_REAL_METHODS);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetTeamsInProjectList() {
		List<Team> teams = new ArrayList<Team>();
		teams.add(new Team());

		String projectAlias = "PA";
		Project project = new Project();

		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);
		when(mockProjectService.getProject(projectAlias)).thenReturn(project);
		when(mockTeamService.getTeamsForProject(project)).thenReturn(teams);
		List<Team> result = controller.getTeamsInProjectList(projectAlias);
		assertEquals(teams, result);
	}

	@Test
	public void testGetAllProjectsList() {
		List<Project> projects = new ArrayList<Project>();
		projects.add(new Project());
		projects.add(new Project());

		when(mockProjectService.getProjects()).thenReturn(projects);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		List<Project> result = controller.getAllProjectsList();
		assertEquals(projects, result);
	}

	@Test
	public void testGetChosenProjectAlias() {

		String projectAliasLowerCase = "xyz";
		String projectAliasUpperCase = "XYZ";

		when(mockProjectService.isProjectPresent(anyString())).thenReturn(true);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		assertEquals(controller.getChosenProjectAlias(projectAliasLowerCase),
				projectAliasLowerCase.toUpperCase());
		assertEquals(controller.getChosenProjectAlias(projectAliasUpperCase),
				projectAliasUpperCase);
	}

	@Test(expected = ProjectNotFoundException.class)
	public void testGetChosenProjectAliasShouldThrowProjectNotFound(){
		String projectAlias = "xyz";
		
		when(mockProjectService.isProjectPresent(anyString())).thenReturn(true);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		String res = controller.getChosenProjectAlias(projectAlias);
		assertEquals(projectAlias, res);


		when(mockProjectService.isProjectPresent(anyString())).thenReturn(false);
		try{
			controller.getChosenProjectAlias(projectAlias);
		}catch(Exception e){
			assertTrue(e instanceof ProjectNotFoundException);
		}
	}
}
