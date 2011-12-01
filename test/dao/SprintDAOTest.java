package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;

import model.pbis.PBI;
import model.pbis.Type;
import model.projects.Project;
import model.releases.Release;
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
import dao.ReleaseDAO;
import dao.ScrumzuUserDAO;
import dao.SprintDAO;
import dao.TeamDAO;
import dao.WorkItemDAO;

@ContextConfiguration("DaoTest-context.xml")
public class SprintDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SprintDAO sprintDao;

	@Autowired
	private ProjectDAO projectDao;

	@Autowired
	private ReleaseDAO releaseDao;

	@Autowired
	private PBIDAO pbiDao;

	@Autowired
	private ScrumzuUserDAO userDao;

	@Autowired
	private TeamDAO teamDao;

	@Autowired
	private WorkItemDAO workItemDao;

	private static Sprint insertedSprint;
	private static Project insertedProject;
	private static Release insertedRelease;
	private static Sprint insertedSprint2;

	@BeforeClass
	public static void insertSprint() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2012, 01, 01);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2013, 01, 01);

		Project project = new Project();
		project.setAlias("XX");
		project.setDescription("To jest test do JUnit");
		project.setName("Projekt Testowy");
		project.setOwner("Kowalski Jarosław");
		project.setUrl("http://com.pl");
		project.setVersion("1.23");
		insertedProject = project;

		Sprint sprint = new Sprint();
		sprint.setDateFrom(Calendar.getInstance().getTime());
		sprint.setDateTo(calendar.getTime());
		sprint.setSprintStatus(SprintStatus.CREATED);
		sprint.setName("pierwszy");
		sprint.setProject(insertedProject);
		insertedSprint = sprint;

		Sprint sprint2 = new Sprint();
		sprint2.setDateFrom(calendar.getTime());
		sprint2.setDateTo(calendar2.getTime());
		sprint2.setSprintStatus(SprintStatus.ENDED);
		sprint2.setName("drugi");
		sprint2.setProject(insertedProject);
		insertedSprint2 = sprint2;

		Release release = new Release();
		release.setDateFrom(Calendar.getInstance().getTime());
		release.setDateTo(calendar.getTime());
		release.setName("rilis1");
		release.setProject(insertedProject);
		insertedRelease = release;
	}

	@Test
	public void saveTest() {
		projectDao.save(insertedProject);
		int rowsBeforeSave = super.countRowsInTable("Sprints");
		sprintDao.save(insertedSprint);
		sprintDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Sprints");
		assertEquals("Number of rows has not increased", rowsBeforeSave+1, rowsAfterSave);
		long insertedSprintId = insertedSprint.getIdSprint();
		assertEquals("Sprints are not equal", sprintDao.getSprint(insertedSprintId),insertedSprint);
	}

	@Test
	public void getSprintsTest() {
		projectDao.save(insertedProject);
		sprintDao.save(insertedSprint);
		sprintDao.flushSession();
		long sprintId = insertedSprint.getIdSprint();
		List<Sprint> sprints = sprintDao.getSprints();
		if (sprints.isEmpty()) {
			fail("List of sprints cannot be empty after insert");
		}
		assertEquals("JDBC query must show the same number of sprints", super.countRowsInTable("Sprints"), sprints.size());
		Sprint sprint = sprints.get(sprints.size()-1);
		assertEquals("Selected sprint is not equal",sprint.getIdSprint(),(Long)sprintId);
	}

	@Test
	public void getSprintByIdTest() {
		projectDao.save(insertedProject);
		sprintDao.save(insertedSprint);
		sprintDao.flushSession();
		long insertedSprintId = insertedSprint.getIdSprint();
		Sprint selectedSprint = sprintDao.getSprint(insertedSprintId);
		assertEquals("Selected sprints are not equal", selectedSprint, insertedSprint);
	}

	@Test
	public void getSprintsForProjectTest() {
		projectDao.save(insertedProject);
		sprintDao.save(insertedSprint);
		sprintDao.flushSession();
		List<Sprint> sprints = sprintDao.getSprintsForProject(insertedProject);
		if (sprints.size() != 1) {
			fail ("Wrong size of list for project");
		}
		assertEquals("Selected sprints are not equal", sprints.get(0), insertedSprint);
	}

	@Test
	public void loadSprintByIdTest() {
		projectDao.save(insertedProject);
		sprintDao.save(insertedSprint);
		sprintDao.flushSession();
		long projectId = insertedSprint.getIdSprint();
		Sprint loadedSprint = sprintDao.loadSprint(projectId);
		assertEquals("Loaded object is not equal", loadedSprint.getSprintStatus(), insertedSprint.getSprintStatus());
		assertEquals("Loaded object is not equal", loadedSprint.getDateFrom(), insertedSprint.getDateFrom());
	}

	@Test
	public void deleteTest() {
		projectDao.save(insertedProject);
		int rowsBeforeInsert = super.countRowsInTable("Sprints");
		sprintDao.save(insertedSprint);
		sprintDao.flushSession();
		long sprintId = insertedSprint.getIdSprint();
		sprintDao.delete(insertedSprint);
		sprintDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("Sprints");
		assertEquals("JDBC query must return the same number of sprints", rowsBeforeInsert, rowsAfterDelete);
		Sprint selectedSprint = sprintDao.getSprint(sprintId);
		assertNull("Selected sprint after delete should be null", selectedSprint);
	}

	@Test
	public void deleteByIdTest() {
		projectDao.save(insertedProject);
		int rowsBeforeInsert = super.countRowsInTable("Sprints");
		sprintDao.save(insertedSprint);
		sprintDao.flushSession();
		long insertedSprintId = insertedSprint.getIdSprint();
		sprintDao.delete(insertedSprintId);
		sprintDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("Sprints");
		assertEquals("JDBC query must return the same number of sprints", rowsBeforeInsert, rowsAfterDelete);
		Sprint selectedSprint = sprintDao.getSprint(insertedSprintId);
		assertNull("Selected sprint after delete should be null", selectedSprint);
	}

	@Test
	public void getSprintsForRealese() {
		projectDao.save(insertedProject);
		sprintDao.save(insertedSprint);
		sprintDao.save(insertedSprint2);
		releaseDao.save(insertedRelease);
		sprintDao.flushSession();
		List<Sprint> sprints = sprintDao.getSprintsForRelease(insertedRelease);

		assertFalse("Selected sprints list should not be empty", sprints.isEmpty());
		assertTrue("Selected sprints list should contain inserted sprint", sprints.contains(insertedSprint));
		assertFalse("Selected sprints list should not contain inserted sprint", sprints.contains(insertedSprint2));

	}

	@Test
	public void updateTest() {
		projectDao.save(insertedProject);
		long projectId = insertedProject.getIdProject();
		super.simpleJdbcTemplate.update("insert into Sprints (dateFrom, dateTo, sprintStatus, idProject, name) values (?, ?, ?, ?, ?)",
				new Object[] {Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), new Integer(1), projectId, "SprintTestowy"});
		long sprintId = super.simpleJdbcTemplate.queryForLong("select idSprint from Sprints where name=?", new Object[] {"SprintTestowy"});
		insertedSprint.setIdSprint(sprintId);
		insertedSprint.setName("SprintTestowy2");
		sprintDao.update(insertedSprint);
		sprintDao.flushSession();
		String name = super.simpleJdbcTemplate.queryForObject("select name from Sprints where idSprint=?", String.class, new Object[] {sprintId});
		assertEquals("Name did not change properly", insertedSprint.getName(), name);
	}

	@Test
	public void getStoryPointsForSprint() {
		projectDao.save(insertedProject);
		sprintDao.save(insertedSprint);
		sprintDao.save(insertedSprint2);

		ScrumzuUser user = new ScrumzuUser("TestUser", "Hash password", true);
		userDao.save(user);

		PBI pbi = new PBI();
		pbi.setDateCreation(Calendar.getInstance().getTime());
		pbi.setDescription("To jest PBI testowe JUnit");
		pbi.setPriority(1);
		pbi.setProject(insertedProject);
		pbi.setTitle("Testowe PBI");
		pbi.setType(Type.PERFORMANCE);
		PBI pbi2 = new PBI();
		pbi2.setDateCreation(Calendar.getInstance().getTime());
		pbi2.setDescription("To jest PBI testowe 2 JUnit");
		pbi2.setPriority(1);
		pbi2.setProject(insertedProject);
		pbi2.setTitle("Testowe PBI2");
		pbi2.setType(Type.PERFORMANCE);
		pbiDao.save(pbi);
		pbiDao.save(pbi2);

		Team team = new Team();
		team.setAlias("ZZ");
		team.setDescription("To jest test do JUnit");
		team.setName("Test Testowy");
		team.setProject(insertedProject);
		team.setUser(user);
		teamDao.save(team);

		WorkItem workItem = new WorkItem();
		workItem.setStatus(Status.DONE);
		workItem.setPbi(pbi);
		workItem.setSprint(insertedSprint2);
		workItem.setTeam(team);
		workItem.setUser(user);
		workItem.setStoryPoints(120);
		workItem.setDateForNow();

		WorkItem workItem2 = new WorkItem();
		workItem2.setStatus(Status.DONE);
		workItem2.setPbi(pbi2);
		workItem2.setSprint(insertedSprint2);
		workItem2.setTeam(team);
		workItem2.setUser(user);
		workItem2.setStoryPoints(10);
		workItem2.setDateForNow();
		workItemDao.save(workItem);
		workItemDao.save(workItem2);

		sprintDao.flushSession();
		//		long result = sprintDao.getDoneStoryPoints(insertedSprint2);
		//		assertEquals("Wrong sum of story points for sprint", 130, result);
		//
		//		result = sprintDao.getDoneStoryPoints(insertedSprint);
		//		assertEquals("Wrong sum of story points for sprint", 0, result);
	}

}

