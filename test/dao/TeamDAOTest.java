package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;

import model.pbis.PBI;
import model.pbis.Type;
import model.projects.Project;
import model.sprint.Sprint;
import model.sprint.SprintStatus;
import model.teams.Team;
import model.users.ScrumzuUser;
import model.workItems.Status;
import model.workItems.WorkItem;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import dao.PBIDAO;
import dao.ProjectDAO;
import dao.ScrumzuUserDAO;
import dao.SprintDAO;
import dao.TeamDAO;
import dao.WorkItemDAO;

@ContextConfiguration("DaoTest-context.xml")
public class TeamDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private TeamDAO teamDao;
	@Autowired
	private ProjectDAO projectDao;
	@Autowired
	private SprintDAO sprintDao;
	@Autowired
	private PBIDAO pbiDao;
	@Autowired
	private WorkItemDAO workItemDao;
	@Autowired
	private ScrumzuUserDAO userDao;

	private static Team insertedTeam;
	private static Project insertedProject;
	private static Sprint insertedSprint;
	private static PBI insertedPBI;
	private static WorkItem insertedWorkItem;
	private static ScrumzuUser insertedUser;

	@BeforeClass
	public static void insertTeam() {
		ScrumzuUser user = new ScrumzuUser("TestUser", "Hash password", true);
		insertedUser = user;


		Calendar calendar = Calendar.getInstance();
		calendar.set(2012, 01, 01);

		Project project = new Project();
		project.setAlias("XX");
		project.setDescription("To jest test do JUnit");
		project.setName("Projekt Testowy");
		project.setOwner("Kowalski Jarosław");
		project.setUrl("http://com.pl");
		project.setVersion("1.23");
		insertedProject = project;

		Team team = new Team();
		team.setAlias("ZZ");
		team.setDescription("To jest test do JUnit");
		team.setName("Test Testowy");
		team.setProject(insertedProject);
		team.setUser(insertedUser);
		insertedTeam = team;

		Sprint sprint = new Sprint();
		sprint.setDateFrom(Calendar.getInstance().getTime());
		sprint.setDateTo(calendar.getTime());
		sprint.setSprintStatus(SprintStatus.CREATED);
		sprint.setName("pierwszy");
		sprint.setProject(insertedProject);
		insertedSprint = sprint;

		PBI pbi = new PBI();
		pbi.setDateCreation(Calendar.getInstance().getTime());
		pbi.setDescription("To jest PBI testowe JUnit");
		pbi.setPriority(1);
		pbi.setProject(insertedProject);
		pbi.setTitle("Testowe PBI");
		pbi.setType(Type.PERFORMANCE);
		insertedPBI = pbi;

		WorkItem workItem = new WorkItem();
		workItem.setStatus(Status.DONE);
		workItem.setPbi(insertedPBI);
		workItem.setSprint(insertedSprint);
		workItem.setTeam(insertedTeam);
		workItem.setUser(insertedUser);
		workItem.setDateForNow();
		insertedWorkItem = workItem;
	}

	@Test
	public void saveTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		int rowsBeforeSave = super.countRowsInTable("Teams");
		teamDao.save(insertedTeam);
		teamDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Teams");
		assertEquals("Number of rows has not increased", rowsBeforeSave+1, rowsAfterSave);
		long insertedTeamId = insertedTeam.getIdTeam();
		assertEquals("Teams are not equal", teamDao.getTeam(insertedTeamId),insertedTeam);
	}

	@Test
	public void getTeamsTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		teamDao.save(insertedTeam);
		teamDao.flushSession();
		List<Team> teams = teamDao.getTeams();
		if (teams.isEmpty()) {
			fail("List of teams cannot be empty after insert");
		}
		assertEquals("JDBC query must show the same number of teams", super.countRowsInTable("Teams"), teams.size());
		Team team = teams.get(teams.size()-1);
		assertEquals("Selected team is not equal", team.getAlias(),"ZZ");
		assertEquals("Selected team is not equal", team.getName(),"Test Testowy");
	}

	@Test
	public void getTeamId() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		teamDao.save(insertedTeam);
		teamDao.flushSession();
		long teamId = teamDao.getTeamId(insertedTeam.getAlias());
		assertEquals("Team's selected id is not equal to expected one", insertedTeam.getIdTeam(),new Long(teamId));
	}

	@Test
	public void getTeamByIdTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		teamDao.save(insertedTeam);
		teamDao.flushSession();
		long insertedTeamId = insertedTeam.getIdTeam();
		Team selectedTeam = teamDao.getTeam(insertedTeamId);
		assertEquals("Selected teams are not equal", selectedTeam, insertedTeam);
	}

	@Test
	public void deleteTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		int rowsBeforeInsert = super.countRowsInTable("Teams");
		teamDao.save(insertedTeam);
		teamDao.flushSession();
		long teamId = insertedTeam.getIdTeam();
		teamDao.delete(insertedTeam);
		teamDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("Teams");
		assertEquals("JDBC query must return the same number of teams", rowsBeforeInsert, rowsAfterDelete);
		Team selectedTeam = teamDao.getTeam(teamId);
		assertNull("Selected team after delete should be null", selectedTeam);
	}

	@Test
	public void deleteByIdTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		int rowsBeforeInsert = super.countRowsInTable("Teams");
		teamDao.save(insertedTeam);
		teamDao.flushSession();
		long teamId = insertedTeam.getIdTeam();
		teamDao.delete(teamId);
		teamDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("Teams");
		assertEquals("JDBC query must return the same number of teams", rowsBeforeInsert, rowsAfterDelete);
		Team selectedTeam = teamDao.getTeam(teamId);
		assertNull("Selected team after delete should be null", selectedTeam);
	}


	@Test
	public void deleteWithWorkItemsTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		insertedTeam.addWorkItem(insertedWorkItem);
		insertedWorkItem.setTeam(insertedTeam);
		teamDao.flushSession();
		long idTeam = insertedTeam.getIdTeam();
		teamDao.delete(insertedTeam);
		teamDao.flushSession();
		int counter = super.simpleJdbcTemplate.queryForInt("select count(*) from Teams where idTeam=?",new Object[]{idTeam});
		assertEquals("JDBC query must return no teams with this id", 0, counter);
		counter = super.simpleJdbcTemplate.queryForInt("select count(*) from WorkItems where idTeam=?",new Object[]{idTeam});
		assertEquals("JDBC query must return no workItems with this id", 0, counter);
	}

	@Test
	public void loadTeamByIdTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		teamDao.save(insertedTeam);
		teamDao.flushSession();
		long teamId = insertedTeam.getIdTeam();
		Team loadedTeam = teamDao.loadTeam(teamId);
		assertEquals("Loaded object is not equal", loadedTeam.getAlias(), insertedTeam.getAlias());
		assertEquals("Loaded object is not equal", loadedTeam.getName(), insertedTeam.getName());
	}

	@Test
	public void getTeamsForUser() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		teamDao.save(insertedTeam);
		List<Team> teams = teamDao.getTeamsForUser(insertedUser);
		assertEquals("List of teams for particular user should not be empty", 1, teams.size());
		Team selectedTeam = teamDao.getTeamsForUser(insertedUser).get(0);
		assertEquals("Selected team is not equal to expected one", insertedTeam, selectedTeam);
	}

	@Test
	public void getTeamsByProject() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		teamDao.save(insertedTeam);
		teamDao.flushSession();
		List<Team> teams = teamDao.getTeamsForProject(insertedProject);
		if (teams.size() != 1) {
			fail("List should have one related team");
		}
		assertEquals("Selected team is not equal", teams.get(0), insertedTeam);


	}

	@Test
	public void getTeamsBySprint() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		teamDao.flushSession();
		List<Team> teams = teamDao.getTeamsForSprint(insertedSprint);
		if (teams.size() != 1) {
			fail("List should have one related team");
		}
		assertEquals("Selected team is not equal", teams.get(0), insertedTeam);
	}

	@Test
	public void countTeamsForSprintTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		teamDao.flushSession();
		assertEquals("Count number is not valid", 1,teamDao.countTeamsForSprint(insertedSprint));
		workItemDao.delete(insertedWorkItem);
		teamDao.delete(insertedTeam);
		teamDao.flushSession();
		assertEquals("Count number is not valid", 0,teamDao.countTeamsForSprint(insertedSprint));
	}

}
