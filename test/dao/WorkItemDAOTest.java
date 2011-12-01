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
public class WorkItemDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private WorkItemDAO workItemDao;
	@Autowired
	private ProjectDAO projectDao;
	@Autowired
	private PBIDAO pbiDao;
	@Autowired
	private SprintDAO sprintDao;
	@Autowired
	private TeamDAO teamDao;
	@Autowired
	private ScrumzuUserDAO userDao;

	private static WorkItem insertedWorkItem;
	private static Project insertedProject;
	private static PBI insertedPBI;
	private static Sprint insertedSprint;
	private static Team insertedTeam;
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

		PBI pbi = new PBI();
		pbi.setDateCreation(Calendar.getInstance().getTime());
		pbi.setDescription("To jest PBI testowe JUnit");
		pbi.setPriority(1);
		pbi.setProject(insertedProject);
		pbi.setTitle("Testowe PBI");
		pbi.setType(Type.PERFORMANCE);
		insertedPBI = pbi;

		Sprint sprint = new Sprint();
		sprint.setDateFrom(Calendar.getInstance().getTime());
		sprint.setDateTo(calendar.getTime());
		sprint.setName("pierwszy");
		sprint.setSprintStatus(SprintStatus.CREATED);
		sprint.setProject(insertedProject);
		insertedSprint = sprint;

		Team team = new Team();
		team.setAlias("ZZ");
		team.setDescription("To jest test do JUnit");
		team.setName("Test Testowy");
		team.setProject(insertedProject);
		team.setUser(insertedUser);
		insertedTeam = team;


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
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		int rowsBeforeSave = super.countRowsInTable("WorkItems");
		workItemDao.save(insertedWorkItem);
		workItemDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("WorkItems");
		assertEquals("Number of rows has not increased", rowsBeforeSave+1, rowsAfterSave);
		long insertedWorkItemId = insertedWorkItem.getIdWorkItem();
		assertEquals("WorkItems are not equal", workItemDao.getWorkItem(insertedWorkItemId),insertedWorkItem);
	}

	@Test
	public void getWorkItemsTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		workItemDao.flushSession();
		List<WorkItem> workItems = workItemDao.getWorkItems();
		if (workItems.isEmpty()) {
			fail("List of workItems cannot be empty after insert");
		}
		assertEquals("JDBC query must show the same number of workItems", super.countRowsInTable("WorkItems"), workItems.size());
		WorkItem workItem = workItems.get(workItems.size()-1);
		assertEquals("Selected workItem is not equal", workItem, workItemDao.loadWorkItem(insertedWorkItem.getIdWorkItem()));
	}

	@Test
	public void getWorkItemByIdTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		workItemDao.flushSession();
		long insertedWorkItemId = insertedWorkItem.getIdWorkItem();
		WorkItem selectedWorkItem = workItemDao.getWorkItem(insertedWorkItemId);
		assertEquals("Selected workItems are not equal", selectedWorkItem, insertedWorkItem);
	}

	@Test
	public void deleteTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		int rowsBeforeInsert = super.countRowsInTable("WorkItems");
		workItemDao.save(insertedWorkItem);
		workItemDao.flushSession();
		workItemDao.delete(insertedWorkItem);
		workItemDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("WorkItems");
		assertEquals("JDBC query must return the same number of workItems", rowsBeforeInsert, rowsAfterDelete);
		long insertedWorkItemId = insertedWorkItem.getIdWorkItem();
		WorkItem selectedWorkItem = workItemDao.getWorkItem(insertedWorkItemId);
		assertNull("Selected workItem after delete should be null", selectedWorkItem);
	}

	@Test
	public void loadWorkItemByIdTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		workItemDao.flushSession();
		long workItemId = insertedWorkItem.getIdWorkItem();
		WorkItem loadedWorkItem = workItemDao.loadWorkItem(workItemId);
		assertEquals("Loaded object is not equal", loadedWorkItem, insertedWorkItem);
	}


	@Test
	public void getWorkItemsForPBITest(){
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		workItemDao.flushSession();
		List<WorkItem> workItems = workItemDao.getWorkItemsForPBI(insertedPBI);
		if (workItems.size()!= 1) {
			fail("Inserted PBI should have only one assigned work item");
		}
		WorkItem assignedWorkItem = workItems.get(0);
		assertEquals("Selected work item is not equal", assignedWorkItem, insertedWorkItem);
	}

	@Test
	public void getWorkItemsByFieldTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		workItemDao.flushSession();
		List<WorkItem> workItems = workItemDao.getWorkItemsByField("status", Status.DONE, "=");
		assertTrue("Size of selected list should be bigger than one", workItems.size() >= 1);
		workItems = workItemDao.getWorkItemsByField("user", insertedUser, "<>");
		assertFalse("List should not contain inserted work item", workItems.contains(insertedWorkItem));

	}


}



