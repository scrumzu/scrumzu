package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;

import model.pbis.PBI;
import model.pbis.Type;
import model.projects.AttributeType;
import model.projects.Project;
import model.sprint.Sprint;
import model.sprint.SprintStatus;
import model.teams.Team;
import model.users.ScrumzuUser;
import model.workItems.Status;
import model.workItems.WorkItem;

import org.junit.Before;
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
public class PBIDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private PBIDAO pbiDao;

	@Autowired
	private ProjectDAO projectDao;
	@Autowired
	private SprintDAO sprintDao;
	@Autowired
	private TeamDAO teamDao;
	@Autowired
	private WorkItemDAO workItemDao;
	@Autowired
	private ScrumzuUserDAO userDao;

	private PBI insertedPBI;
	private Project insertedProject;
	private Sprint insertedSprint;
	private Team insertedTeam;
	private WorkItem insertedWorkItem;
	private Calendar calendar;
	private ScrumzuUser insertedUser;



	@Before
	public void insertPBI() {
		calendar = Calendar.getInstance();
		calendar.set(2012, 01, 01);

		Project project = new Project();
		project.setAlias("XX");
		project.setDescription("To jest test do JUnit");
		project.setName("Projekt Testowy");
		project.setOwner("Kowalski Jarosław");
		project.setUrl("http://com.pl");
		project.setVersion("1.23");
		project.addAttribute("Length", AttributeType.DOUBLE);
		project.addAttribute("Day Name", AttributeType.STRING);
		insertedProject = project;

		PBI pbi = new PBI();
		pbi.setDateCreation(Calendar.getInstance().getTime());
		pbi.setDescription("To jest PBI testowe JUnit");
		pbi.setPriority(1);
		pbi.setProject(insertedProject);
		pbi.setTitle("Testowe PBI");
		pbi.setType(Type.PERFORMANCE);
		pbi.setDoubleAttribute(insertedProject.getAttribute("Length"), 21.23);
		pbi.setStringAttribute(insertedProject.getAttribute("Day Name"), "Monday");
		insertedPBI = pbi;
	}


	@Test
	public void saveTest() {
		int rowsBeforeSave = super.countRowsInTable("PBIs");
		int beforeDoubleAttributes = super.countRowsInTable("PBIs_DoubleAttributes");
		int beforeStringAttributes = super.countRowsInTable("PBIs_StringAttributes");
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("PBIs");
		int afterDoubleAttributes = super.countRowsInTable("PBIs_DoubleAttributes");
		int afterStringAttributes = super.countRowsInTable("PBIs_StringAttributes");
		assertEquals("JDBC query must return the proper number of double attributes", beforeDoubleAttributes+1, afterDoubleAttributes);
		assertEquals("JDBC query must return the proper number of string attributes", beforeStringAttributes+1, afterStringAttributes);
		assertEquals("Number of rows has not increased", rowsBeforeSave+1, rowsAfterSave);
		long insertedPBIId = insertedPBI.getIdPBI();
		assertEquals("PBIs are not equal", pbiDao.getPBI(insertedPBIId),insertedPBI);
	}

	@Test
	public void getPBIByIdTest() {
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		long insertedPBIId = insertedPBI.getIdPBI();
		PBI selectedPBI = pbiDao.getPBI(insertedPBIId);
		assertEquals("Selected pbis are not equal", insertedPBI, selectedPBI);
	}

	@Test
	public void getPBIWithAttributesTest() {
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		long insertedPBIId = insertedPBI.getIdPBI();
		PBI selectedPBI = pbiDao.getPBIWithAttributes(insertedPBIId);
		assertEquals("Selected pbis are not equal", insertedPBI, selectedPBI);
		assertEquals(21.23,selectedPBI.getDoubleAttribute("Length"), 0.00);
		assertEquals("Selected string attribute in pbis are not equal", "Monday", selectedPBI.getStringAttribute("Day Name"));
	}

	@Test
	public void deleteTest() {
		int rowsBeforeInsert = super.countRowsInTable("PBIs");
		int beforeDoubleAttributes = super.countRowsInTable("PBIs_DoubleAttributes");
		int beforeStringAttributes = super.countRowsInTable("PBIs_StringAttributes");
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		long pbiId = insertedPBI.getIdPBI();
		pbiDao.delete(insertedPBI);
		pbiDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("PBIs");
		int afterDoubleAttributes = super.countRowsInTable("PBIs_DoubleAttributes");
		int afterStringAttributes = super.countRowsInTable("PBIs_StringAttributes");
		assertEquals("JDBC query must return the same number of pbis", rowsBeforeInsert, rowsAfterDelete);
		assertEquals("JDBC query must return the same number of double attributes", beforeDoubleAttributes, afterDoubleAttributes);
		assertEquals("JDBC query must return the same number of string attributes", beforeStringAttributes, afterStringAttributes);
		PBI selectedPBI = pbiDao.getPBI(pbiId);
		assertNull("Selected pbi after delete should be null", selectedPBI);
	}

	@Test
	public void deleteByIdTest() {
		int rowsBeforeInsert = super.countRowsInTable("PBIs");
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		long insertedPBIId = insertedPBI.getIdPBI();
		pbiDao.delete(insertedPBIId);
		pbiDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("PBIs");
		assertEquals("JDBC query must return the same number of pbis", rowsBeforeInsert, rowsAfterDelete);
		PBI selectedPBI = pbiDao.getPBI(insertedPBIId);
		assertNull("Selected pbi after delete should be null", selectedPBI);
	}

	@Test
	public void getPBIsForProjectTest() {
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		List<PBI> pbis = pbiDao.getPBIsForProject(insertedProject);
		if (pbis.size() != 1) {
			fail("List size should be 1 after insert");
		}
		assertEquals("Selected pbi are not equal", pbis.get(0), insertedPBI);

	}

	@Test
	public void getPBIsForTeamTest() {
		prepareObjects();
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		pbiDao.flushSession();
		List<PBI> pbis = pbiDao.getPBIsForTeam(insertedTeam);
		if (pbis.size() != 1) {
			fail("List size should be 1 after insert");
		}
		assertEquals("Selected pbi are not equal", pbis.get(0), insertedPBI);
	}

	@Test
	public void getPBIsForSprintTest() {
		prepareObjects();
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		pbiDao.flushSession();
		List<PBI> pbis = pbiDao.getPBIsForSprint(insertedSprint);
		if (pbis.size() != 1) {
			fail("List size should be 1 after insert");
		}
		assertEquals("Selected pbi are not equal", pbis.get(0), insertedPBI);
	}

	@Test
	public void countPBIsForSprintTest() {
		prepareObjects();
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		pbiDao.flushSession();
		assertEquals("Count number is not valid", 1,pbiDao.countPBIsForSprint(insertedSprint));
		workItemDao.delete(insertedWorkItem);
		teamDao.delete(insertedTeam);
		pbiDao.delete(insertedPBI);
		pbiDao.flushSession();
		assertEquals("Count number is not valid", 0,pbiDao.countPBIsForSprint(insertedSprint));
	}

	@Test
	public void updateTest() {
		projectDao.save(insertedProject);
		long projectId = insertedProject.getIdProject();
		super.simpleJdbcTemplate.update("insert into PBIs (title, idProject, dateCreation, type) values (?, ?, ?, ?, ?)",
				new Object[] {"To jest PBI testowe JUnit", projectId, Calendar.getInstance().getTime(), new Integer(1) });
		long pbiId = super.simpleJdbcTemplate.queryForLong("select idPBI from PBIs where title=?", new Object[] {"To jest PBI testowe JUnit"});
		insertedPBI.setIdPBI(pbiId);
		insertedPBI.setTitle("To jest PBI testowe JUnit2");
		pbiDao.update(insertedPBI);
		pbiDao.flushSession();
		String title = super.simpleJdbcTemplate.queryForObject("select title from PBIs where idPBI=?", String.class, new Object[] {pbiId});
		assertEquals("Username did not change properly", insertedPBI.getTitle(), title);
	}

	@Test
	public void getStoryPointsTest() {
		prepareObjects();
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		sprintDao.save(insertedSprint);
		teamDao.save(insertedTeam);
		workItemDao.save(insertedWorkItem);
		pbiDao.flushSession();
		assertEquals("Story points for pbi are not valid", 120, pbiDao.getStoryPointsForPBI(insertedPBI));
		workItemDao.delete(insertedWorkItem);
		teamDao.delete(insertedTeam);
		pbiDao.delete(insertedPBI);
		pbiDao.flushSession();
		assertEquals("Story points for pbi are not valid", 0, pbiDao.getStoryPointsForPBI(insertedPBI));
	}

	private void prepareObjects() {
		ScrumzuUser user = new ScrumzuUser("TestUser", "Hash password", true);
		insertedUser = user;

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
		workItem.setStoryPoints(120);
		insertedWorkItem = workItem;
	}

}
