package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.projects.Project;
import model.sprint.Sprint;
import model.sprint.SprintStatus;
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

import services.PBIService;
import services.ProjectService;
import services.SprintService;
import services.TeamService;

@RunWith(MockitoJUnitRunner.class)
public class SprintsControllerTest {
	@Mock
	private SprintService mockSprintService;
	@Mock
	private ProjectService mockProjectService;
	@Mock
	private TeamService mockTeamService;
	@Mock
	private PBIService mockPbiService;
	private SprintsController controller;
	Model model;
	@Mock BindingResult bindingResult;
	String projectAlias;
	List<Project> projects;
	List<Team> teams;

	@Before
	public void setUp() throws Exception {
		controller = new SprintsController();
		controller.sprintService = mockSprintService;
		model = new ExtendedModelMap();
		MockitoAnnotations.initMocks(this);
		projectAlias = "PA";
		projects = new ArrayList<Project>();
		teams = new ArrayList<Team>();
		model.addAttribute("teams", teams);
		model.addAttribute("projects", projects);
		model.addAttribute("chosenProjectAlias", projectAlias);
	}

	@Test
	public void testGetSprintsList() {
		List<Sprint> sprints = new ArrayList<Sprint>();
		sprints.add(new Sprint(1L));
		sprints.add(new Sprint(2L));

		when(mockTeamService.countTeamsForSprint((Sprint) anyObject())).thenReturn(2L);
		ReflectionTestUtils.setField(controller, "teamService",
				mockTeamService);
		when(mockPbiService.countPBIsForSprint((Sprint) anyObject())).thenReturn(2L);
		ReflectionTestUtils.setField(controller, "pbiService",
				mockPbiService);
		when(mockSprintService.getSprintsForProject((Project) anyObject())).thenReturn(sprints);
		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);

		when(mockProjectService.getProject(anyString())).thenReturn((Project) anyObject());
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		ModelAndView page = controller.getSprintsList(projectAlias, model);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("list", modelMap.get("op"));
		assertEquals(sprints, modelMap.get("sprints"));
		assertEquals("sprintList", page.getViewName());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
	}

	@Test
	public void testGetNewSprintForm() {
		Sprint sprint = new Sprint();
		ModelAndView page = controller.getNewSprintForm(projectAlias, model);

		Map<String, Object> modelMap = model.asMap();

		assertEquals("add", modelMap.get("op"));
		assertEquals(sprint, modelMap.get("sprint"));
		assertEquals("sprintAddForm", page.getViewName());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
	}

	@Test
	public void testAddSprint() {
		final List<Sprint> sprints = new ArrayList<Sprint>();
		int size = sprints.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				sprints.add((Sprint) invocation.getArguments()[0]);
				return sprints.toString();
			}
		}).when(mockSprintService).saveSprint((Sprint) anyObject());
		when(bindingResult.hasErrors()).thenReturn(false);

		Project project = new Project(1);
		when(mockProjectService.getProject(anyString())).thenReturn(project);


		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);

		Map<String, Object> modelMap = model.asMap();
		Sprint sprint = new Sprint(1);
		sprint.setDateFrom(new Date(System.currentTimeMillis()));
		sprint.setDateTo(new Date(System.currentTimeMillis()+10000));
		ModelAndView page = controller.addSprint(projectAlias, sprint, bindingResult,
				model);
		assertEquals(sprint.getProject(), project);
		assertEquals("/"+projectAlias+"/sprints",  ((RedirectView)page.getView()).getUrl());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
		assertEquals(size + 1, sprints.size());
	}

	@Test
	public void testAddSprintWithErrors(){
		final List<Sprint> sprints = new ArrayList<Sprint>();
		int size = sprints.size();

		
		when(bindingResult.hasErrors()).thenReturn(true);

		Map<String, Object> modelMap = model.asMap();

		SprintStatus[] sprintValues = SprintStatus.values();
		Sprint sprint = new Sprint(1L);
		sprint.setDateFrom(new Date(System.currentTimeMillis()+10000));
		sprint.setDateTo(new Date(System.currentTimeMillis()));
		
		ModelAndView page = controller.addSprint(projectAlias, sprint, bindingResult,
				model);
		assertEquals(sprintValues.length, ((SprintStatus[]) modelMap.get("statuses")).length);
		assertEquals("add",modelMap.get("op"));
		assertEquals("sprintAddForm", page.getViewName());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
		assertEquals(size, sprints.size());
	}

	@Test
	public void testGetEditSprintForm() {
		Sprint sprint = mock(Sprint.class);
		when(mockSprintService.getSprint(anyLong())).thenReturn(sprint);
		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);
		ModelAndView page = controller.getEditSprintForm(projectAlias, model, 1);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("edit", modelMap.get("op"));
		assertEquals(sprint, modelMap.get("sprint"));
		assertEquals("sprintEditForm", page.getViewName());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));

	}

	@Test
	public void testEditSprint() {
		Sprint sprint = new Sprint();
		sprint.setIdSprint(2L);
		sprint.setDateFrom(new Date(System.currentTimeMillis()));
		sprint.setDateTo(new Date(System.currentTimeMillis()+10000));
		final long id2 = 3L;
		final List<Sprint> sprints = new ArrayList<Sprint>();
		sprints.add(sprint);

		int size = sprints.size();

		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Sprint p = sprints.get(sprints.indexOf((invocation
						.getArguments()[0])));
				p.setIdSprint(id2);
				return sprints.contains(invocation.getArguments()[0]);
			}
		}).when(mockSprintService).saveSprint((Sprint) anyObject());

		when(bindingResult.hasErrors()).thenReturn(false);



		Project project = new Project(1);
		when(mockProjectService.getProject(anyString())).thenReturn(project);


		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);
		ModelAndView page = controller.editSprint(sprint, bindingResult, projectAlias, model);
		assertEquals(size, sprints.size());
		assertEquals(sprint.getIdSprint(), (Long) id2);
		assertEquals("/"+projectAlias+"/sprints",  ((RedirectView)page.getView()).getUrl());
		Map<String, Object> modelMap = model.asMap();
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
	}


	@Test
	public void testEditSprintWithErrors() {
		Sprint sprint = new Sprint(1L);
		sprint.setDateFrom(new Date(System.currentTimeMillis()));
		sprint.setDateTo(new Date(System.currentTimeMillis()+10000));
		sprint.setIdSprint(2L);
		Map<String, Object> modelMap = model.asMap();

		final List<Sprint> sprints = new ArrayList<Sprint>();
		sprints.add(sprint);

		int size = sprints.size();

		when(bindingResult.hasErrors()).thenReturn(true);


		ModelAndView page = controller.editSprint(sprint, bindingResult, projectAlias, model);

		SprintStatus[] sprintValues = SprintStatus.values();
		assertEquals(sprintValues.length, ((SprintStatus[]) modelMap.get("statuses")).length);
		assertEquals("edit",modelMap.get("op"));
		assertEquals("sprintEditForm", page.getViewName());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
		assertEquals(size, sprints.size());
	}

	@Test
	public void testGetSprintById() {

		Sprint sprint = mock(Sprint.class);

		when(mockSprintService.getSprint(anyLong())).thenReturn(sprint);

		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);

		ModelAndView page = controller.getSprintById(projectAlias, model,
				sprint.getIdSprint());

		Map<String, Object> modelMap = model.asMap();
		assertEquals("details", modelMap.get("op"));
		assertEquals(sprint, modelMap.get("sprint"));
		assertEquals("sprintDetails", page.getViewName());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
	}

	@Test
	public void testDeleteSprints() {

		final List<Sprint> sprint = new ArrayList<Sprint>();
		for (int i = 1; i < 6; i++) {
			sprint.add(new Sprint(i));
		}

		long[] ids = { 1, 2, 3 };
		int size = sprint.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long[] deletedIndexes = (long[]) invocation.getArguments()[0];
				for (int i = 0; i < deletedIndexes.length; i++) {
					sprint.remove(deletedIndexes[i]);
				}

				return sprint.toString();
			}
		}).when(mockSprintService).deleteSprints((long[]) anyObject());

		when(mockSprintService.getSprint(anyLong())).thenReturn(
				sprint.get(anyInt()));

		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);
		boolean answer = controller.deleteSprints(ids, model);

		assertTrue(answer);
		assertEquals(size, sprint.size());
	}

	@Test
	public void testStartOrEndSprint()
	{

		Sprint sprint = new Sprint(1L);
		when(mockSprintService.getSprint(anyLong())).thenReturn(sprint);


		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Sprint p = (Sprint) invocation.getArguments()[0];
				if (p!=null) {
					return true;
				} else {
					return false;
				}
			}
		}).when(mockSprintService).saveSprint((Sprint) anyObject());

		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);


		String[] res = controller.startOrEndSprint(1, "start", model);
		assertEquals(sprint.getIdSprint().toString(), res[0]);
		assertEquals(SprintStatus.STARTED.toString(), res[1]);

		res = controller.startOrEndSprint(1, "end", model);
		assertEquals(sprint.getIdSprint().toString(), res[0]);
		assertEquals(SprintStatus.ENDED.toString(), res[1]);
	}

}
