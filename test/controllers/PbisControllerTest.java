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

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import model.filters.Filter;
import model.pbis.PBI;
import model.projects.Attribute;
import model.projects.AttributeType;
import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;
import model.users.ScrumzuAuthority;
import model.users.ScrumzuUser;
import model.workItems.Status;
import model.workItems.WorkItem;
import model.workItems.WorkItemValidator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import services.FilterService;
import services.PBIService;
import services.ProjectService;
import services.SprintService;
import services.TeamService;
import services.WorkItemService;

public class PbisControllerTest {

	PbisController controller;
	Model model;
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
	@Mock
	WebDataBinder binder;
	String projectAlias;
	List<Project> projects;
	List<Team> teams;

	@Mock
	HttpServletRequest mockRequest;

	Principal principal = new UsernamePasswordAuthenticationToken(new ScrumzuUser(1L), "AUTHENTICATED");

	@Before
	public void setUp() throws Exception {
		controller = new PbisController();
		model = new ExtendedModelMap();

		projectAlias = "TSP";
		projects = new ArrayList<Project>();
		teams = new ArrayList<Team>();
		controller.workItemValidator=new WorkItemValidator();
		model.addAttribute("teams", teams);
		model.addAttribute("projects", projects);
		model.addAttribute("chosenProjectAlias", projectAlias);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetPbisList() {
		PBI pbi = new PBI(1);
		List<PBI> pbis = new ArrayList<PBI>();
		pbis.add(pbi);

		List<Filter> filters = new ArrayList<Filter>();
		when(mockFilterService.getFiltersForUser((ScrumzuUser) anyObject())).thenReturn(filters);
		ReflectionTestUtils.setField(controller, "filterService", mockFilterService);

		when(mockPbiService.getPBIsForProject((Project) anyObject())).thenReturn(pbis);
		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);

		WorkItem wi = new WorkItem(1);
		when(mockWorkItemService.getLastWorkItemForPBI(pbi)).thenReturn(wi);
		ReflectionTestUtils.setField(controller, "workItemService",
				mockWorkItemService);

		Project p = mock(Project.class);
		when(mockProjectService.getProject(anyString())).thenReturn(p);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		ModelAndView page = controller.getPbisList("TEST", model, principal);

		Map<String, Object> modelMap = model.asMap();
		assertEquals(pbi.getWorkItems().get(0), wi);
		assertEquals("list", modelMap.get("op"));
		assertEquals(pbis, modelMap.get("pbis"));
		assertEquals("pbiList", page.getViewName());
	}

	@Test
	public void testGetPbiById() {
		PBI pbi = mock(PBI.class);
		WorkItem wi = mock(WorkItem.class);
		when(mockPbiService.getPBI(anyLong())).thenReturn(pbi);
		when(mockWorkItemService.getLastWorkItemForPBI(pbi)).thenReturn(wi);

		ReflectionTestUtils.setField(controller, "workItemService",
				mockWorkItemService);
		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);

		ModelAndView page = controller
				.getPbiById(model, pbi.getIdPBI(), projectAlias);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("details", modelMap.get("op"));
		assertEquals(pbi, modelMap.get("pbi"));
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
		assertEquals("pbiDetails", page.getViewName());

	}

	@Test
	public void testGetNewPbiForm() {
		PBI pbi = new PBI();

		Map<String, Object> modelMap = model.asMap();
		Project project = new Project(1);
		when(mockProjectService.getProject(anyString())).thenReturn(
				project);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		List<Team> teams = new ArrayList<Team>();
		when(mockTeamService.getAllTeams()).thenReturn(teams);
		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);

		List<Sprint> sprints = new ArrayList<Sprint>();
		when(mockSprintService.getAllSprints()).thenReturn(sprints);
		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);
		ScrumzuUser user = new ScrumzuUser(1L);
		user.setEnabled(true);
		user.setPassword("asdsa");
		user.setUsername("asd");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_PRODUCT_OWNER");
		List<ScrumzuAuthority> auths= new ArrayList<ScrumzuAuthority>();
		user.setAuthorities(auths);
		user.setAuthorities(roles);
		principal = new UsernamePasswordAuthenticationToken(user, "AUTHENTICATED");
		ModelAndView page = controller.getNewPbiForm(projectAlias, model, principal);
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
		assertEquals("add", modelMap.get("op"));
		assertEquals(pbi, modelMap.get("pbi"));
		assertEquals("pbiAddForm", page.getViewName());

	}


	@Test
	public void testGetEditPbiForm() {
		PBI pbi = new PBI(2L);
		Map<Attribute, String> sa = new HashMap<Attribute, String>();
		sa.put(new Attribute("asd", AttributeType.STRING), new String("adsad"));
		sa.put(new Attribute("ert", AttributeType.STRING), new String("adsaasdsdd"));
		pbi.setStringAttributes(sa);
		Map<Attribute, Double> da = new HashMap<Attribute, Double>();
		da.put(new Attribute("asdasd", AttributeType.DOUBLE), new Double(34));
		da.put(new Attribute("asasdsaddasd", AttributeType.DOUBLE), new Double(56));
		pbi.setDoubleAttributes(da);
		List<Project> projects = new ArrayList<Project>();
		when(mockPbiService.getPBI(anyLong())).thenReturn(pbi);
		when(mockProjectService.getProjects()).thenReturn(projects);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);
		WorkItem wi = new WorkItem(1);
		when(mockWorkItemService.getLastWorkItemForPBI(pbi)).thenReturn(wi);

		List<Sprint> sprints = new ArrayList<Sprint>();
		when(mockSprintService.getAllSprints()).thenReturn(sprints);
		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);

		List<Team> teams = new ArrayList<Team>();
		when(mockTeamService.getAllTeams()).thenReturn(teams);
		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);

		ReflectionTestUtils.setField(controller, "workItemService",
				mockWorkItemService);
		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);
		ScrumzuUser user = new ScrumzuUser(1L);
		user.setEnabled(true);
		user.setPassword("asdsa");
		user.setUsername("asd");
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_PRODUCT_OWNER");
		List<ScrumzuAuthority> auths= new ArrayList<ScrumzuAuthority>();
		user.setAuthorities(auths);
		user.setAuthorities(roles);

		principal = new UsernamePasswordAuthenticationToken(user, "AUTHENTICATED");
		ModelAndView page = controller.getEditPbiForm(model, 1337, projectAlias, principal);

		Map<String, Object> modelMap = model.asMap();
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));
		assertEquals(projects, modelMap.get("projects"));
		assertEquals("edit", modelMap.get("op"));
		assertEquals(pbi, modelMap.get("pbi"));
		assertEquals("pbiEditForm", page.getViewName());

	}

	@Test
	public void testDeletePbis() {
		final List<PBI> pbis = new ArrayList<PBI>();
		for (int i = 1; i < 6; i++) {
			pbis.add(new PBI(i));
		}

		long[] ids = { 1, 2, 3 };

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long[] idsToRemove = (long[]) invocation.getArguments()[0];
				for (int i = 1; i < idsToRemove.length; i++) {
					pbis.remove(idsToRemove[i]);
				}
				return pbis.toString();
			}
		}).when(mockPbiService).deletePBIs((long[]) anyObject());

		when(mockPbiService.getPBI(anyLong())).thenReturn(
				pbis.get(anyInt()));

		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);
		boolean answer = controller.deletePbis(ids, model);

		int size = pbis.size();
		assertTrue(answer);
		assertEquals(size, pbis.size());
	}

	@Test
	public void testInitBinder() {

		controller.initBinder(binder);
		assertTrue(true);
	}

	@Test
	public void testDeleteSinglePbi() {

		final List<PBI> pbi = new ArrayList<PBI>();
		for (int i = 1; i < 6; i++) {
			pbi.add(new PBI(i));
		}

		long id = 3;
		int size = pbi.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long[] deletedIndexes = (long[]) invocation.getArguments()[0];
				for (int i = 0; i < deletedIndexes.length; i++) {
					pbi.remove(deletedIndexes[i]);
				}

				return pbi.toString();
			}
		}).when(mockPbiService).deletePBIs((long[]) anyObject());

		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);

		boolean answer = controller.deleteSinglePbi(model, id);
		assertTrue(answer);
		assertEquals(size, pbi.size());

	}

	@Test
	public void testMarkAsDonePbis() {


		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);

		final ArrayList<PBI> pbis = new ArrayList<PBI>();
		for (int i = 0; i < 6; i++) {
			WorkItem wi = new WorkItem(i);
			wi.setStatus(Status.PROPOSED_FOR_SPRINT);
			wi.setTeam(mock(Team.class));
			wi.setSprint(mock(Sprint.class));
			PBI pbi = new PBI(i);
			List<WorkItem> wis = new ArrayList<WorkItem>();
			wis.add(wi);
			pbi.setWorkItems(wis);
			pbis.add(pbi);
		}
		doAnswer(new Answer<PBI>() {
			public PBI answer(InvocationOnMock invocation) {
				long index = (Long) invocation.getArguments()[0];
				return pbis.get((int)index);
			}
		}).when(mockPbiService).getPBI(anyLong());

		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);

		doAnswer(new Answer<List<WorkItem>>() {
			public List<WorkItem> answer(InvocationOnMock invocation) {
				PBI pbi = (PBI) invocation.getArguments()[0];
				return pbi.getWorkItems();
			}
		}).when(mockWorkItemService).getWorkItemsForPBI((PBI)anyObject());

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				WorkItem wi = (WorkItem) invocation.getArguments()[0];
				return wi.toString();
			}
		}).when(mockWorkItemService).saveWorkItem((WorkItem) anyObject());

		ReflectionTestUtils.setField(controller, "workItemService", mockWorkItemService);
		long[] ids = { 3, 4, 5 };

		Map<String, Object> page = controller.markAsDonePbis(ids, model, principal);

		assertEquals(page.get("status"), "Done");

		for (int i = 0; i < ids.length; i++) {
			assertEquals(Status.DONE, pbis.get((int) ids[i]).getWorkItems().get(0).getStatus());
		}


	}

	@Test
	public void testGetProjectAttributes()
	{
		Set<Attribute> attribs = new HashSet<Attribute>();
		when(mockProjectService.getProjectAttributes(anyString())).thenReturn(attribs);

		ReflectionTestUtils.setField(controller, "projectService", mockProjectService);

		Set<Attribute> result= controller.getProjectAttributes("as");
		assertEquals(attribs, result);

	}
	@Test
	public void testGetPbisListByFilter()
	{
		Project project = new Project(2L);
		project.setAlias("ertr");
		when(mockProjectService.getProject(anyString())).thenReturn(project);
		ReflectionTestUtils.setField(controller, "projectService", mockProjectService);

		Set<PBI> pbis = new HashSet<PBI>();
		when(mockPbiService.getPBIsByFilterAndProject((Filter) anyObject(), (Project) anyObject())).thenReturn(pbis);
		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);

		ModelAndView page = controller.getPbisListByFilter(new Filter(), "", model);
		Map<String, Object> modelMap = model.asMap();
		assertEquals(pbis, modelMap.get("pbis"));
		assertEquals("list", modelMap.get("op"));
		assertEquals(true, modelMap.get("filtered"));
		assertEquals(page.getViewName(), "pbiTable");
	}
	@Test
	public void testGetPbisTable()
	{
		Project project = new Project(2L);
		project.setAlias("ertr");
		when(mockProjectService.getProject(anyString())).thenReturn(project);
		ReflectionTestUtils.setField(controller, "projectService", mockProjectService);

		List<PBI> pbis = new ArrayList<PBI>();
		pbis.add(new PBI(234L));
		when(mockPbiService.getPBIsForProject((Project) anyObject())).thenReturn(pbis);
		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);

		WorkItem wi = new WorkItem(2L);
		when(mockWorkItemService.getLastWorkItemForPBI((PBI) anyObject())).thenReturn(wi);
		ReflectionTestUtils.setField(controller, "workItemService", mockWorkItemService);

		ModelAndView page = controller.getPbisTable("asd", model);
		Map<String, Object> modelMap = model.asMap();
		assertEquals(page.getViewName(), "pbiTable");
		assertEquals(pbis, modelMap.get("pbis"));
		@SuppressWarnings("unchecked")
		List<PBI> pbis2 = (List<PBI>) modelMap.get("pbis");
		for(PBI pbi: pbis2){
			assertEquals(pbi.getWorkItems().get(0), wi);
		}
	}

	@Test
	public void testSavePbi()
	{

		when(mockRequest.getRequestURI()).thenReturn("http://localhost:8080/scrumzu/RO/pbis/new");

		PBI pbi = new PBI(2L);
		WorkItem wi = new WorkItem(3L);
		wi.setTeam(new Team(2L));
		wi.setStatus(Status.PREASSIGNED);
		wi.setSprint(new Sprint(3));
		wi.setStoryPoints(123);
		List<WorkItem> wis = new ArrayList<WorkItem>();
		wis.add(wi);
		pbi.setWorkItems(wis);
		Map<String, String> sa = new HashMap<String, String>();
		sa.put("a","adsad");
		sa.put("b", "ondas");
		pbi.setFormStringAttributes(sa);
		Map<String, Double> da = new HashMap<String, Double>();
		da.put("c",  new Double(34));
		da.put("d", new Double(56));
		pbi.setFormDoubleAttributes(da);
		Project project = new Project(1L);
		when(mockProjectService.getProject(anyString())).thenReturn(project);
		Set<Attribute> pa = new HashSet<Attribute>();
		pa.add(new Attribute("a", AttributeType.STRING));
		pa.add(new Attribute("b", AttributeType.STRING));
		pa.add(new Attribute("c", AttributeType.DOUBLE));
		pa.add(new Attribute("d", AttributeType.DOUBLE));
		when(mockProjectService.getProjectAttributes(anyString())).thenReturn(pa);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				PBI pbi = (PBI) invocation.getArguments()[0];
				return pbi.toString();
			}
		}).when(mockPbiService).savePBI((PBI)anyObject());

		List<Sprint> sprints = new ArrayList<Sprint>();
		when(mockSprintService.getAllSprints()).thenReturn(sprints);
		ReflectionTestUtils.setField(controller, "sprintService",
				mockSprintService);

		List<Team> teams = new ArrayList<Team>();
		when(mockTeamService.getAllTeams()).thenReturn(teams);
		ReflectionTestUtils
		.setField(controller, "teamService", mockTeamService);

		ReflectionTestUtils.setField(controller, "workItemService",
				mockWorkItemService);
		ReflectionTestUtils.setField(controller, "pbiService", mockPbiService);

		ModelAndView page = controller.savePbi("asd", pbi, bindingResult, model, mockRequest, principal);


		assertEquals("/"+ "asd".toUpperCase() + "/pbis", ((RedirectView)page.getView()).getUrl());

		when(bindingResult.hasErrors()).thenReturn(true);


		page = controller.savePbi("asd", pbi, bindingResult, model, mockRequest, principal);


		assertEquals("pbiAddForm", page.getViewName());

		when(mockRequest.getRequestURI()).thenReturn("http://localhost:8080/scrumzu/RO/pbis/1");
		page = controller.savePbi("asd", pbi, bindingResult, model, mockRequest, principal);


		assertEquals("pbiEditForm", page.getViewName());

	}
	@Test
	public void testInitFilterData()
	{
		Project project= new Project(2L);
		project.setAlias("asd");
		when(mockProjectService.getProject(anyString())).thenReturn(project);
		ReflectionTestUtils.setField(controller, "projectService",
				mockProjectService);

		List<Team> teams = new ArrayList<Team>();
		Team team1= new Team(1L);
		team1.setAlias("1");
		team1.setName("pierwszy");
		teams.add(team1);
		Team team2= new Team(2L);
		team2.setAlias("2");
		team2.setName("drugi");
		teams.add(team2);
		when(mockTeamService.getTeamsForProject((Project) anyObject())).thenReturn(teams);

		List<Sprint> sprints = new ArrayList<Sprint>();
		Sprint sprint1 = new Sprint(1L);
		sprint1.setName("pierwszy");
		sprints.add(sprint1);
		Sprint sprint2 = new Sprint(2L);
		sprint2.setName("drugi");
		sprints.add(sprint2);
		ReflectionTestUtils.setField(controller, "teamService", mockTeamService);
		when(mockSprintService.getSprintsForProject((Project) anyObject())).thenReturn(sprints);
		ReflectionTestUtils.setField(controller, "sprintService", mockSprintService);


		Map<String, Map<String, String>> result = controller.initFilterData(model, "asd");

		assertEquals(2, result.get("teams").size());
		assertEquals(2, result.get("sprints").size());
		assertEquals(Status.values().length, result.get("statuses").size());


	}



}
