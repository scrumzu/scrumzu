package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import model.pbis.PBI;
import model.pbis.Type;
import model.projects.AttributeType;
import model.projects.Project;
import model.releases.Release;
import model.users.ScrumzuUser;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import dao.PBIDAO;
import dao.ProjectDAO;
import dao.ReleaseDAO;
import dao.ScrumzuUserDAO;

@ContextConfiguration("DaoTest-context.xml")
public class ReleaseDAOTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ReleaseDAO releaseDao;

	@Autowired
	private ProjectDAO projectDao;

	@Autowired
	private PBIDAO pbiDao;

	@Autowired
	private ScrumzuUserDAO userDao;

	private Release insertedRelease;
	private Project insertedProject;
	private PBI insertedPBI;
	private Calendar calendar;
	private Calendar calendar2;
	private ScrumzuUser insertedUser;

	@Before
	public void insertObjects() {
		ScrumzuUser user = new ScrumzuUser("TestUser", "Hash password", true);
		insertedUser = user;

		calendar = Calendar.getInstance();
		calendar.set(2011, 11, 19, 0, 0, 0);

		calendar2 = Calendar.getInstance();
		calendar2.set(2012, 01, 01, 0, 0, 0);

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
		pbi.setDateCreation(calendar.getTime());
		pbi.setDescription("To jest PBI testowe JUnit");
		pbi.setPriority(1);
		pbi.setProject(insertedProject);
		pbi.setTitle("Testowe PBI");
		pbi.setType(Type.PERFORMANCE);
		pbi.setDoubleAttribute(insertedProject.getAttribute("Length"), 21.23);
		pbi.setStringAttribute(insertedProject.getAttribute("Day Name"), "Monday");
		insertedPBI = pbi;

		Release release = new Release();
		release.setDateFrom(calendar.getTime());
		release.setDateTo(calendar2.getTime());
		release.setName("Release Testowy");
		release.setProject(insertedProject);
		release.addReleaseItem(insertedPBI, insertedUser);
		insertedRelease = release;
	}


	@Test
	public void getReleaseTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		super.simpleJdbcTemplate.update("insert into Releases (dateFrom, dateTo, name, idProject) values (?, ?, ?, ?)",
				new Object[]{calendar.getTime(),calendar2.getTime(), "Release Testowy", insertedProject.getIdProject()});
		long idRelease = super.simpleJdbcTemplate.queryForLong("select idRelease from Releases where name =?", "Release Testowy");
		insertedRelease.setIdRelease(idRelease);
		Release selectedRelease = releaseDao.getRelease(idRelease);
		assertEquals("Selected release is not eqal to inserted one", insertedRelease, selectedRelease);
	}

	@Test
	public void loadReleaseTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		super.simpleJdbcTemplate.update("insert into Releases (dateFrom, dateTo, name, idProject) values (?, ?, ?, ?)",
				new Object[]{calendar.getTime(),calendar2.getTime(), "Release Testowy", insertedProject.getIdProject()});
		long idRelease = super.simpleJdbcTemplate.queryForLong("select idRelease from Releases where name =?", "Release Testowy");
		Release selectedRelease = releaseDao.loadRelease(idRelease);
		assertEquals("Selected release is not eqal to inserted one", insertedRelease.getName(), selectedRelease.getName());
	}

	@Test
	public void saveReleaseTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		int beforeInsert = super.countRowsInTable("Releases");
		releaseDao.save(insertedRelease);
		releaseDao.flushSession();
		int afterInsert = super.countRowsInTable("Releases");
		assertEquals("Number of rows in table Releases is not proper", beforeInsert+1, afterInsert);
	}

	@Test
	public void deleteTestById() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		int beforeInsert = super.countRowsInTable("Releases");
		super.simpleJdbcTemplate.update("insert into Releases (dateFrom, dateTo, name, idProject) values (?, ?, ?, ?)",
				new Object[]{calendar.getTime(),calendar2.getTime(), "Release Testowy", insertedProject.getIdProject()});
		long idRelease = super.simpleJdbcTemplate.queryForLong("select idRelease from Releases where name =?", "Release Testowy");
		releaseDao.delete(idRelease);
		releaseDao.flushSession();
		int afterInsert = super.countRowsInTable("Releases");
		assertEquals("Number of rows in table Releases is not proper", beforeInsert, afterInsert);
		long count = super.simpleJdbcTemplate.queryForLong("select count(*) from Releases where name =?", "Release Testowy");
		assertEquals("Release testowy shoul not be present in table", 0, count);
	}

	@Test
	public void deleteTest() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		int beforeInsert = super.countRowsInTable("Releases");
		super.simpleJdbcTemplate.update("insert into Releases (dateFrom, dateTo, name, idProject) values (?, ?, ?, ?)",
				new Object[]{calendar.getTime(),calendar2.getTime(), "Release Testowy", insertedProject.getIdProject()});
		long idRelease = super.simpleJdbcTemplate.queryForLong("select idRelease from Releases where name =?", "Release Testowy");
		Release release = new Release();
		release.setIdRelease(idRelease);
		release.setDateFrom(calendar.getTime());
		release.setDateTo(calendar2.getTime());
		release.setName("Release Testowy");
		release.setProject(insertedProject);
		releaseDao.delete(release);
		releaseDao.flushSession();
		int afterInsert = super.countRowsInTable("Releases");
		assertEquals("Number of rows in table Releases is not proper", beforeInsert, afterInsert);
		long count = super.simpleJdbcTemplate.queryForLong("select count(*) from Releases where name =?", "Release Testowy");
		assertEquals("Release testowy shoul not be present in table", 0, count);
	}

	@Test
	public void getRealsesForProject() {
		userDao.save(insertedUser);
		projectDao.save(insertedProject);
		pbiDao.save(insertedPBI);
		pbiDao.flushSession();
		super.simpleJdbcTemplate.update("insert into Releases (dateFrom, dateTo, name, idProject) values (?, ?, ?, ?)",
				new Object[]{calendar.getTime(),calendar2.getTime(), "Release Testowy", insertedProject.getIdProject()});
		long idRelease = super.simpleJdbcTemplate.queryForLong("select idRelease from Releases where name =?", "Release Testowy");
		List<Release> releases = releaseDao.getReleasesForProject(insertedProject);
		assertFalse("Selected list should not be empty", releases.isEmpty());
		Release release = new Release();
		release.setIdRelease(idRelease);
		release.setDateFrom(calendar.getTime());
		release.setDateTo(calendar2.getTime());
		release.setName("Release Testowy");
		release.setProject(insertedProject);
		assertTrue("List should contain inserted release", releases.contains(release));

	}

}
