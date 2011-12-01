package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.projects.Project;
import model.teams.Team;
import model.users.ScrumzuUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import services.ProjectService;
import services.ScrumzuUserDetailsService;
import services.TeamService;

@RunWith(MockitoJUnitRunner.class)
public class TeamsControllerTest {

	TeamsController controller;
	Model model;
	@Mock
	BindingResult bindingResult;
	@Mock
	TeamService mockTeamService;
	@Mock
	ProjectService mockProjectService;
	@Mock
	ScrumzuUserDetailsService mockUserService;
	String projectAlias;
	List<Team> teams;
	List<Project> projects = new ArrayList<Project>();
	Principal principal = new UsernamePasswordAuthenticationToken(
			new ScrumzuUser(1L), "AUTHENTICATED");
	@Mock
	HttpServletRequest mockRequest;

	@Before
	public void setUp() throws Exception {
		controller = new TeamsController();
		model = new ExtendedModelMap();
		MockitoAnnotations.initMocks(this);
		projectAlias = "TSP";
		projects = new ArrayList<Project>();
		teams = new ArrayList<Team>();
		model.addAttribute("teams", teams);
		model.addAttribute("projects", projects);
	}

	@Test
	public void testGetTeamsList() {

		when(mockTeamService.getAllTeams()).thenReturn(teams);
		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);

		ModelAndView page = controller.getTeamsList(model);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("listAll", modelMap.get("op"));
		assertEquals(teams, modelMap.get("teams"));
		assertEquals("teamList", page.getViewName());
	}

	@Test
	public void testGetNewTeamForm() {

		List<ScrumzuUser> sms = new ArrayList<ScrumzuUser>();

		when(mockUserService.getScrumMasters()).thenReturn(sms);
		ReflectionTestUtils
		.setField(controller, "userService", mockUserService);

		Team team = new Team();

		ArrayList<Project> projects = new ArrayList<Project>();
		Map<String, Object> modelMap = model.asMap();

		when(mockProjectService.getProjects()).thenReturn(projects);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		ModelAndView page = controller.getNewTeamForm(model, principal);
		assertEquals(projects, modelMap.get("projects"));
		assertEquals("add", modelMap.get("op"));
		assertEquals(team, modelMap.get("team"));
		assertEquals("teamAddForm", page.getViewName());
	}

	@Test
	public void testGetEditTeamForm() {
		List<ScrumzuUser> sms = new ArrayList<ScrumzuUser>();

		when(mockUserService.getScrumMasters()).thenReturn(sms);
		ReflectionTestUtils
		.setField(controller, "userService", mockUserService);

		Team team = mock(Team.class);
		when(team.getIdTeam()).thenReturn(1337L);
		when(mockTeamService.getTeam(anyLong())).thenReturn(team);
		ArrayList<Project> projects = new ArrayList<Project>();
		when(mockProjectService.getProjects()).thenReturn(projects);
		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		ModelAndView page = controller.getEditTeamForm(model, 1);

		Map<String, Object> modelMap = model.asMap();
		assertEquals(projects, modelMap.get("projects"));
		assertEquals("edit", modelMap.get("op"));
		assertEquals(team, modelMap.get("team"));
		assertEquals("teamEditForm", page.getViewName());

	}

	@Test
	public void testGetTeamById() {

		Team team = mock(Team.class);

		when(mockTeamService.getTeam(anyLong())).thenReturn(team);

		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);

		ModelAndView page = controller.getTeamById(model, team.getIdTeam());

		Map<String, Object> modelMap = model.asMap();
		assertEquals("details", modelMap.get("op"));
		assertEquals(team, modelMap.get("team"));
		assertEquals("teamDetails", page.getViewName());

	}

	@Test
	public void testDeleteTeams() {

		final List<Team> team = new ArrayList<Team>();
		for (int i = 1; i < 6; i++) {
			team.add(new Team(i));
		}

		long[] ids = { 1, 2, 3 };
		int size = team.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long[] deletedIndexes = (long[]) invocation.getArguments()[0];
				for (int i = 0; i < deletedIndexes.length; i++) {
					team.remove(deletedIndexes[i]);
				}

				return team.toString();
			}
		}).when(mockTeamService).deleteTeams((long[]) anyObject());

		when(mockTeamService.getTeam(anyLong())).thenReturn(team.get(anyInt()));

		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);
		boolean answer = controller.deleteTeams(ids, model);
		assertTrue(answer);
		assertEquals(size, team.size());
	}

	@Test
	public void testDeleteTeam() {

		final List<Team> team = new ArrayList<Team>();
		for (int i = 1; i < 6; i++) {
			team.add(new Team(i));
		}

		long id = 3;
		int size = team.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long[] deletedIndexes = (long[]) invocation.getArguments()[0];
				for (int i = 0; i < deletedIndexes.length; i++) {
					team.remove(deletedIndexes[i]);
				}

				return team.toString();
			}
		}).when(mockTeamService).deleteTeams((long[]) anyObject());

		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);
		boolean answer = controller.deleteTeam(id, model);
		assertTrue(answer);
		assertEquals(size, team.size());

	}

	@Test
	public void testSaveTeam() {
		Team team = new Team();
		when(mockRequest.getRequestURI()).thenReturn(
				"http://localhost:8080/scrumzu/RO/pbis/new");
		final List<Team> teams = new ArrayList<Team>();
		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				Team team = (Team) invocation.getArguments()[0];
				teams.add(team);
				return team.toString();
			}
		}).when(mockTeamService).saveTeam((Team) anyObject());

		when(mockTeamService.isTeamPresent(anyString(), anyLong())).thenReturn(
				false);

		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);

		when(bindingResult.hasErrors()).thenReturn(false);

		ModelAndView page = controller.saveTeam(team, bindingResult, model,
				mockRequest);
		assertEquals(1, teams.size());
		when(bindingResult.hasErrors()).thenReturn(true);
		
		List<ScrumzuUser> users  = new ArrayList<ScrumzuUser>();
		users.add(new ScrumzuUser(3L));
		when(mockUserService.getScrumMasters()).thenReturn(users);
		ReflectionTestUtils.setField(controller, "userService", mockUserService);
		
		page = controller.saveTeam(team, bindingResult, model, mockRequest);
		when(bindingResult.hasErrors()).thenReturn(true);
		Map<String, Object> modelMap = model.asMap();
		assertEquals("add", modelMap.get("op"));
		assertEquals(1, teams.size());
		assertEquals("teamAddForm", page.getViewName());

		when(mockTeamService.isTeamPresent(anyString(), anyLong())).thenReturn(
				true);
		page = controller.saveTeam(team, bindingResult, model, mockRequest);
		when(bindingResult.hasErrors()).thenReturn(false);
		assertEquals(1, teams.size());

	}

}
