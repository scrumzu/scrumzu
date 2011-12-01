package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;
import model.users.ScrumzuUser;
import model.workItems.WorkItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import services.TeamService;

import dao.TeamDAO;
@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {
	
	@Mock
	TeamDAO mockTeamDao;

	TeamService teamService;
	
	@Before
	public void setUp() throws Exception {
		teamService = new TeamService();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAllTeams() {
		
		List<Team> teams = new ArrayList<Team>();
		teams.add(new Team());
		teams.add(new Team());
		when(mockTeamDao.getTeams()).thenReturn(teams);
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		
		List<Team> result = teamService.getAllTeams();
		
		assertEquals(result, teams);
	}

	@Test
		public void testGetTeamById() {
			long id = 123;
			
			Team team = new Team(id);
			when(mockTeamDao.getTeam(anyLong())).thenReturn(team);
			ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
			
			Team result = teamService.getTeam(anyLong());
			
			assertEquals(result, team);
		}

	@Test
	public void testSaveTeam() {
		
		final String title2= "AFTERSAVE";
		final List<Team> teams = new ArrayList<Team>();
		Team team = new Team(123);
		team.setName("BEFORESAVE");
		teams.add(team);
		Team teamNew = new Team();
		teamNew.setName("UNSAVE");
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Team team2 = teams.get(teams.indexOf((invocation.getArguments()[0])));
				team2.setName(title2);
				return teams.contains(team2);
			}
		}).when(mockTeamDao).update((Team) anyObject());
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Team team2 = (Team) (invocation.getArguments()[0]);
				team2.setName(title2);
				teams.add(team2);	
				return teams.contains(team2);
			}
		}).when(mockTeamDao).save((Team) anyObject());
		
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		
		teamService.saveTeam(team);
		
		assertEquals(team.getName(), title2);
		
		teamService.saveTeam(teamNew);
		
		assertTrue(teams.contains(teamNew));
		assertEquals(teamNew.getName(), title2);

	}


	@Test
	public void testDeleteTeams() {

		final List<Team> teams = new ArrayList<Team>();
		for (int i = 1; i < 6; i++) {
			teams.add(new Team(i));
		}
		int size  = teams.size();
		long[] ids = { 1, 2, 3 };

		

		Team team = new Team();
		when(mockTeamDao.getTeam(anyLong())).thenReturn(team);
		

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long idToRemove = (Long) invocation.getArguments()[0];
					teams.remove(new Team(idToRemove));
				return "Removed " + idToRemove;
			}
		}).when(mockTeamDao).delete(anyLong());
		
		
		
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		teamService.deleteTeams(ids);

		assertEquals(teams.size(), (size-ids.length));
	
	}
	@Test
	public void testNullAssignedWorkItems()
	{
		
		
		Team team = new Team(1);
		
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		WorkItem wi1 = new WorkItem(1L);
		wi1.setTeam(team);
		WorkItem wi2= new WorkItem(2L);
		wi2.setTeam(team);
		workItems.add(wi1);
		workItems.add(wi2);
		team.setWorkItems(workItems);
		
		when(mockTeamDao.getTeam(anyLong())).thenReturn(team);

		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		
		teamService.nullAssignedWorkItems(anyLong());
		
		assertEquals(wi1.getTeam(),null);
		assertEquals(wi2.getTeam(),null);
	}
	@Test
	public void testGetTeamsByProject()
	{
		List<Team> teams = new ArrayList<Team>();
		when(mockTeamDao.getTeamsForProject((Project) anyObject())).thenReturn(teams);
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		
		List<Team> result = teamService.getTeamsForProject((Project) anyObject());
		assertEquals(result, teams);
	}
	
	@Test
	public void testGetTeamsBySprint()
	{
		List<Team> teams = new ArrayList<Team>();
		when(mockTeamDao.getTeamsForSprint((Sprint) anyObject())).thenReturn(teams);
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		
		List<Team> result = teamService.getTeamsForProject((Project) anyObject());
		assertEquals(result, teams);
	}
	
	@Test
	public void testCountPBIsForSprint() {
		Sprint sprint = new Sprint(123);
		
		when(mockTeamDao.countTeamsForSprint(sprint)).thenReturn(1L);
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		long answer = teamService.countTeamsForSprint(sprint);
		assertEquals(1L,answer);
	}

	@Test
	public void testIsTeamPresent()
	{
		
		when(mockTeamDao.getTeamId(anyString())).thenReturn(new Long(1L));
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		assertEquals(true,teamService.isTeamPresent("asd", new Long(1L)));
		when(mockTeamDao.getTeamId(anyString())).thenReturn(null);
		assertEquals(false, teamService.isTeamPresent("asd", new Long(1L)));

	}
	@Test
	public void testGetTeamsForUser()
	{
		List<Team> teams = new ArrayList<Team>();
		when(mockTeamDao.getTeamsForUser((ScrumzuUser)anyObject())).thenReturn(teams);
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		assertEquals(teams, teamService.getTeamsForUser(mock(ScrumzuUser.class)));
	}
	@Test
	public void testGetTeamsForSprint()
	{
		List<Team> teams = new ArrayList<Team>();
		when(mockTeamDao.getTeamsForSprint((Sprint)anyObject())).thenReturn(teams);
		ReflectionTestUtils.setField(teamService, "teamDao", mockTeamDao);
		assertEquals(teams, teamService.getTeamsForSprint(mock(Sprint.class)));
	}


}
