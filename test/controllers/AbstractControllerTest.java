package controllers;

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

import junit.framework.TestCase;

public class AbstractControllerTest extends TestCase {
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

	AbstractController controller;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = Mockito.mock(AbstractController.class, Mockito.CALLS_REAL_METHODS);
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

	public void testGetTeamsInProjectList()
	{
		List<Team> teams = new ArrayList<Team>();
		teams.add(new Team());


		when(mockTeamService.getAllTeams()).thenReturn(teams);
		ReflectionTestUtils.setField(controller, "teamService",
				mockTeamService);
		List<Team> result = controller.getTeamsInProjectList();
		assertEquals(teams, result);
	}
}
