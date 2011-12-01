package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.projects.Attribute;
import model.projects.AttributeType;
import model.projects.Project;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import dao.AttributeDAO;
import dao.ProjectDAO;

@ContextConfiguration("DaoTest-context.xml")
public class AttributeDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private AttributeDAO attributeDao;
	
	@Autowired
	private ProjectDAO projectDao;
	
	private Project insertedProject;
	private Attribute insertedAttribute;
	
	@Before
	public void insertAttribute() {
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
		
		Attribute attribute = new Attribute();
		attribute.setName("JunitTestAttribute");
		attribute.setProject(insertedProject);
		attribute.setType(AttributeType.STRING);
		insertedAttribute = attribute;
	}
	
	@Test
	public void getAttributeTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		long projectId = insertedProject.getIdProject();
		super.simpleJdbcTemplate.update("insert into Attributes (name, type, idProject) values (?, ?, ?)", new Object[] {"JunitTestAttribute", new Integer(1), projectId});
		long attributeId = super.simpleJdbcTemplate.queryForLong("select idAttribute from Attributes where name=?", new Object[] {"JunitTestAttribute"});
		Attribute selectedAttribute = attributeDao.getAttribute(attributeId);
		assertEquals("Selected attribute is not equal to inserted one", insertedAttribute, selectedAttribute);
	}
		
	@Test
	public void saveAttributeTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		int rowsAttributesBeforeSave = super.countRowsInTable("Attributes");
		attributeDao.saveAttribute(insertedAttribute);
		attributeDao.flushSession();
		int rowsAttributesAfterSave = super.countRowsInTable("Attributes");
		assertEquals("Number of rows in attribute table should increase ", rowsAttributesBeforeSave+1, rowsAttributesAfterSave);
		Attribute attribute = super.simpleJdbcTemplate.queryForObject("select idAttribute, name, type from Attributes where name=?",
				new RowMapper<Attribute>() {
					public Attribute mapRow(ResultSet rs, int rowNum) throws SQLException {
						Attribute attribute = new Attribute();
						attribute.setIdAttribute(rs.getLong("idAttribute"));
						attribute.setName(rs.getString("name"));
						attribute.setType(AttributeType.STRING);
						return attribute;
					}
				},
				new Object[] {"JunitTestAttribute"});
		assertEquals("Selected attribute is not equal to inserted one ", insertedAttribute, attribute);
	}
	
	@Test
	public void deleteAttributeTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		long projectId = insertedProject.getIdProject();
		int rowsAttributesBeforeSave = super.countRowsInTable("Attributes");
		super.simpleJdbcTemplate.update("insert into Attributes (name, type, idProject) values (?, ?, ?)", new Object[] {"JunitTestAttribute", new Integer(1), projectId});
		long attributeId = super.simpleJdbcTemplate.queryForLong("select idAttribute from Attributes where name=?", new Object[] {"JunitTestAttribute"});
		attributeDao.deleteAttribute(attributeId);
		attributeDao.flushSession();
		int rowsAttributesAfterSave = super.countRowsInTable("Attributes");
		assertEquals("Number of rows in attribute table should be equal", rowsAttributesBeforeSave, rowsAttributesAfterSave);
		Attribute selectedAttribute = attributeDao.getAttribute(attributeId);
		assertNull("Selected attribute should be null after delete",selectedAttribute);
	}
	
	@Test
	public void deleteAttributeByObjectTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		long projectId = insertedProject.getIdProject();
		int rowsAttributesBeforeSave = super.countRowsInTable("Attributes");
		super.simpleJdbcTemplate.update("insert into Attributes (name, type, idProject) values (?, ?, ?)", new Object[] {"JunitTestAttribute", new Integer(1), projectId});
		Attribute attribute = super.simpleJdbcTemplate.queryForObject("select idAttribute, name, type from Attributes where name=?",
				new RowMapper<Attribute>() {
					public Attribute mapRow(ResultSet rs, int rowNum) throws SQLException {
						Attribute attribute = new Attribute();
						attribute.setIdAttribute(rs.getLong("idAttribute"));
						attribute.setName(rs.getString("name"));
						attribute.setType(AttributeType.STRING);
						return attribute;
					}
				},
				new Object[] {"JunitTestAttribute"});
		attributeDao.deleteAttribute(attribute);
		attributeDao.flushSession();
		int rowsAttributesAfterSave = super.countRowsInTable("Attributes");
		assertEquals("Number of rows in attribute table should be equal", rowsAttributesBeforeSave, rowsAttributesAfterSave);
	}
	
	@Test
	public void isAttributePresentForProjectTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		long projectId = insertedProject.getIdProject();
		super.simpleJdbcTemplate.update("insert into Attributes (name, type, idProject) values (?, ?, ?)", new Object[] {"JunitTestAttribute", new Integer(1), projectId});
		long attributeId = super.simpleJdbcTemplate.queryForLong("select idAttribute from Attributes where name=?", new Object[] {"JunitTestAttribute"});
		assertTrue("Attribute should be present", attributeDao.isAttributePresentForProject(insertedProject.getAlias(), attributeId));
		super.simpleJdbcTemplate.update("delete from Attributes where idAttribute=?", new Object[] {attributeId});
		assertFalse("Attribute should not be present", attributeDao.isAttributePresentForProject(insertedProject.getAlias(), attributeId));
	}
	
	@Test
	public void updateAttributeTest() {
		projectDao.save(insertedProject);
		projectDao.flushSession();
		long projectId = insertedProject.getIdProject();
		super.simpleJdbcTemplate.update("insert into Attributes (name, type, idProject) values (?, ?, ?)", new Object[] {"JunitTestAttribute", new Integer(1), projectId});
		long attributeId = super.simpleJdbcTemplate.queryForLong("select idAttribute from Attributes where name=?", new Object[] {"JunitTestAttribute"});
		insertedAttribute.setIdAttribute(attributeId);
		insertedAttribute.setName("JunitTestAttribute2");
		attributeDao.update(insertedAttribute);
		attributeDao.flushSession();
		String attributeName = super.simpleJdbcTemplate.queryForObject("select name from Attributes where idAttribute=?", String.class, new Object[]{attributeId});
		assertEquals("Attribute name should be updated", "JunitTestAttribute2", attributeName);
	}
	
}
