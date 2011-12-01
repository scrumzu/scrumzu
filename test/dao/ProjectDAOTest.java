package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;

import model.projects.Attribute;
import model.projects.AttributeType;
import model.projects.Project;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import dao.ProjectDAO;

@ContextConfiguration("DaoTest-context.xml")
public class ProjectDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private ProjectDAO projectDao;
	
	private Project insertedProject;
	
	@Before
	public void insertTeam() {
		Project project = new Project();
		project.setAlias("XX");
		project.setDescription("To jest test do JUnit");
		project.setName("Projekt Testowy");
		project.setOwner("Kowalski Jarosław");
		project.setUrl("http://com.pl");
		project.setVersion("1.23");
		project.addAttribute("Length", AttributeType.DOUBLE);
		project.addAttribute("Day name", AttributeType.STRING);
		insertedProject = project;
	}
	
	@Test
	public void saveTest() {
		int rowsProjectsBeforeSave = super.countRowsInTable("Projects");
		int rowsAttributesBeforeSave = super.countRowsInTable("Attributes");
		projectDao.save(insertedProject);
		projectDao.flushSession();
		int rowsProjectsAfterSave = super.countRowsInTable("Projects");
		int rowsAttributesAfterSave = super.countRowsInTable("Attributes");
		assertEquals("Number of rows has not increased", rowsProjectsBeforeSave+1, rowsProjectsAfterSave);
		assertEquals("Number of rows has not increased", rowsAttributesBeforeSave+2, rowsAttributesAfterSave);
		long insertedProjectId = insertedProject.getIdProject();
		assertEquals("Projects are not equal", projectDao.getProject(insertedProjectId),insertedProject);
	}
	
	@Test
	public void getProjectsTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		List<Project> projects = projectDao.getProjects();
		if (projects.isEmpty()) fail("List of projects cannot be empty after insert");
		assertEquals("JDBC query must show the same number of projects", super.countRowsInTable("Projects"), projects.size());
		Project project = projects.get(projects.size()-1);
		assertEquals("Selected project is not equal", project.getAlias(),"XX");
		assertEquals("Selected project is not equal", project.getName(),"Projekt Testowy");
	}
	
	@Test
	public void getProjectByAliasTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		Project selectedProject = projectDao.getProject("XX");
		if (selectedProject == null) fail("Selected project by alias cannot be null");
		assertEquals("Selected project is not equal", selectedProject.getAlias(),"XX");
		assertEquals("Selected project is not equal", selectedProject.getName(),"Projekt Testowy");
	}
	
	@Test
	public void getProjectByAliasTestNull() {
		Project selectedProject = projectDao.getProject("XX");
		assertNull("Selected project by alias should be null", selectedProject);
	}
	
	@Test
	public void getProjectByIdTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		long insertedProjectId = insertedProject.getIdProject();
		Project selectedProject = projectDao.getProject(insertedProjectId);
		assertEquals("Selected projects are not equal", selectedProject, insertedProject);
	}
	
	@Test
	public void deleteTest() {
		int rowsBeforeInsert = super.countRowsInTable("Projects");
		int rowsAttributesBeforeSave = super.countRowsInTable("Attributes");
		projectDao.save(insertedProject);
		projectDao.flushSession();
		projectDao.delete(insertedProject);
		projectDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("Projects");
		int rowsAttributesAfterDelete = super.countRowsInTable("Attributes");
		assertEquals("JDBC query must return the same number of projects", rowsBeforeInsert, rowsAfterDelete);
		assertEquals("JDBC query must return the same number of projects", rowsAttributesBeforeSave, rowsAttributesAfterDelete);
		Project selectedProject = projectDao.getProject("XX");
		assertNull("Selected project after delete should be null", selectedProject);
	}
	
	@Test
	public void loadProjectByIdTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		long projectId = insertedProject.getIdProject();
		Project loadedProject = projectDao.loadProject(projectId);
		assertEquals("Loaded object is not equal", loadedProject.getAlias(), insertedProject.getAlias());
		assertEquals("Loaded object is not equal", loadedProject.getName(), insertedProject.getName());
	}
	
	@Test
	public void deleteByIdTest() {
		int rowsBeforeInsert = super.countRowsInTable("Projects");
		int rowsAttributesBeforeSave = super.countRowsInTable("Attributes");
		projectDao.save(insertedProject);
		projectDao.flushSession();
		long insertedProjectId = insertedProject.getIdProject();
		projectDao.delete(insertedProjectId);
		projectDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("Projects");
		int rowsAttributesAfterDelete = super.countRowsInTable("Attributes");
		assertEquals("JDBC query must return the same number of projects", rowsBeforeInsert, rowsAfterDelete);
		assertEquals("JDBC query must return the same number of projects", rowsAttributesBeforeSave, rowsAttributesAfterDelete);
		Project selectedProject = projectDao.getProject("XX");
		assertNull("Selected project after delete should be null", selectedProject);
	}
	
	@Test
	public void getProjectAttributesTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		Set<Attribute> actualAttributes = insertedProject.getAttributes();
		Set<Attribute> selectedAttributes = projectDao.getProjectAttributes(insertedProject.getAlias());
		assertEquals(actualAttributes, selectedAttributes);
	}
	
	@Test
	public void deleteByAliasTest() {
		int rowsBeforeInsert = super.countRowsInTable("Projects");
		int rowsAttributesBeforeSave = super.countRowsInTable("Attributes");
		projectDao.save(insertedProject);
		projectDao.flushSession();
		String insertedProjectAlias = insertedProject.getAlias();
		projectDao.delete(insertedProjectAlias);
		projectDao.flushSession();
		int rowsAfterDelete = super.countRowsInTable("Projects");
		int rowsAttributesAfterDelete = super.countRowsInTable("Attributes");
		assertEquals("JDBC query must return the same number of projects", rowsBeforeInsert, rowsAfterDelete);
		assertEquals("JDBC query must return the same number of projects", rowsAttributesBeforeSave, rowsAttributesAfterDelete);
		Project selectedProject = projectDao.getProject("XX");
		assertNull("Selected project after delete should be null", selectedProject);
	}
	
	@Test
	public void isProjectPresentTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		String projectAlias = insertedProject.getAlias();
		assertTrue("Project should be present", projectDao.isProjectPresent(projectAlias));
		projectDao.delete(insertedProject);
		projectDao.flushSession();
		assertFalse("Project should not be present", projectDao.isProjectPresent(projectAlias));
	}
	
	@Test
	public void updateTest() {
		super.simpleJdbcTemplate.update("insert into Projects (name, owner, version, alias) values (?, ?, ?, ?)",
				new Object[] {"Projekt testowy", "Kowalski Jarosław", "1.23", "XX"});
		long projectId = super.simpleJdbcTemplate.queryForLong("select idProject from Projects where name=?", new Object[] {"Projekt testowy"});
		insertedProject.setIdProject(projectId);
		insertedProject.setName("Projekt testowy2");
		projectDao.update(insertedProject);
		projectDao.flushSession();
		String name = super.simpleJdbcTemplate.queryForObject("select name from Projects where idProject=?", String.class, new Object[] {projectId});
		assertEquals("Username did not change properly", insertedProject.getName(), name);
	}
	
	
	


}
