package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.users.ScrumzuAuthority;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import dao.ScrumzuAuthorityDAO;

@ContextConfiguration("DaoTest-context.xml")
public class ScrumzuAuthorityDAOTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private ScrumzuAuthorityDAO authorityDao;
	
	private ScrumzuAuthority insertedAuthority;
	
	@Before
	public void insertAuthority() {
		insertedAuthority = new ScrumzuAuthority();
		insertedAuthority.setAuthority("TEST_AUTHORITY");
	}
	
	@Test
	public void saveAuthorityTest() {
		int rowsBeforeSave = super.countRowsInTable("Authorities");
		authorityDao.save(insertedAuthority);
		authorityDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Authorities");
		assertEquals("Number of rows in table should increase after insert", rowsBeforeSave +1, rowsAfterSave);
		ScrumzuAuthority selected = super.simpleJdbcTemplate.queryForObject("select idAuthority, authority from Authorities where authority=?",
				new RowMapper<ScrumzuAuthority>() {
						public ScrumzuAuthority mapRow(ResultSet rs, int arg1) throws SQLException {
							ScrumzuAuthority returnAuthority = new ScrumzuAuthority();
							returnAuthority.setIdAuthority(rs.getLong("idAuthority"));
							returnAuthority.setAuthority(rs.getString("authority"));
							return returnAuthority;
						}
				}, new Object[] {"TEST_AUTHORITY"});
		assertEquals("Selected and original objects are not equal", insertedAuthority, selected);
	}
	
	@Test
	public void getAuthorityTest() {
		super.simpleJdbcTemplate.update("insert into Authorities (authority) values (?)", new Object[]{"TEST_AUTHORITY"});
		long idAuthority = super.simpleJdbcTemplate.queryForLong("select idAuthority from Authorities where authority=?", new Object[]{"TEST_AUTHORITY"});
		ScrumzuAuthority selected = authorityDao.getAuthority(idAuthority);
		assertEquals("Selected and original objects are not equal", insertedAuthority, selected);
	}
	
	@Test
	public void loadAuthorityTest() {
		super.simpleJdbcTemplate.update("insert into Authorities (authority) values (?)", new Object[]{"TEST_AUTHORITY"});
		long idAuthority = super.simpleJdbcTemplate.queryForLong("select idAuthority from Authorities where authority=?", new Object[]{"TEST_AUTHORITY"});
		ScrumzuAuthority selected = authorityDao.loadAuthority(idAuthority);
		assertEquals("Selected and original objects are not equals", insertedAuthority.getAuthority(), selected.getAuthority());
	}
	
	@Test
	public void getAuthoritiesTest() {
		super.simpleJdbcTemplate.update("insert into Authorities (authority) values (?)", new Object[]{"TEST_AUTHORITY"});
		int listSize = super.countRowsInTable("Authorities");
		List<ScrumzuAuthority> authorities = authorityDao.getAuthorities();
		assertFalse("Selected list is empty", authorities.isEmpty());
		assertEquals("Selected list has wrong size", listSize, authorities.size());
	}
	
	@Test
	public void deleteAuthoritiesTest() {
		super.simpleJdbcTemplate.update("insert into Authorities (authority) values (?)", new Object[]{"TEST_AUTHORITY"});
		ScrumzuAuthority selected = super.simpleJdbcTemplate.queryForObject("select idAuthority, authority from Authorities where authority=?",
				new RowMapper<ScrumzuAuthority>() {
						public ScrumzuAuthority mapRow(ResultSet rs, int arg1) throws SQLException {
							ScrumzuAuthority returnAuthority = new ScrumzuAuthority();
							returnAuthority.setIdAuthority(rs.getLong("idAuthority"));
							returnAuthority.setAuthority(rs.getString("authority"));
							return returnAuthority;
						}
				}, new Object[]{"TEST_AUTHORITY"});
		authorityDao.delete(selected);
		authorityDao.flushSession();
		int countOfAuthorities = super.simpleJdbcTemplate.queryForInt("select count(*) from Authorities where authority=?", new Object[]{"TEST_AUTHORITY"});
		assertTrue("Selected id should be 0 (for null value)", countOfAuthorities == 0);
	}
	
	@Test
	public void deleteAuthoritiesByAliasTest() {
		super.simpleJdbcTemplate.update("insert into Authorities (authority) values (?)", new Object[]{"TEST_AUTHORITY"});
		Long idAuthority = super.simpleJdbcTemplate.queryForLong("select idAuthority from Authorities where authority=?", new Object[]{"TEST_AUTHORITY"});
		authorityDao.delete(idAuthority);
		authorityDao.flushSession();
		int countOfAuthorities = super.simpleJdbcTemplate.queryForInt("select count(*) from Authorities where authority=?", new Object[]{"TEST_AUTHORITY"});
		assertTrue("Selected id should be 0 (for null value)", countOfAuthorities == 0);
	}
	
	@Test
	public void updateAuthoritiesTest() {
		super.simpleJdbcTemplate.update("insert into Authorities (authority) values (?)", new Object[]{"TEST_AUTHORITY"});
		ScrumzuAuthority selected = super.simpleJdbcTemplate.queryForObject("select idAuthority, authority from Authorities where authority=?",
				new RowMapper<ScrumzuAuthority>() {
						public ScrumzuAuthority mapRow(ResultSet rs, int arg1) throws SQLException {
							ScrumzuAuthority returnAuthority = new ScrumzuAuthority();
							returnAuthority.setIdAuthority(rs.getLong("idAuthority"));
							returnAuthority.setAuthority(rs.getString("authority"));
							return returnAuthority;
						}
				}, new Object[] {"TEST_AUTHORITY"});
		selected.setAuthority("TEST_AUTHORITY2");
		authorityDao.update(selected);
		authorityDao.flushSession();
		int countOfAuthorities = super.simpleJdbcTemplate.queryForInt("select count(*) from Authorities where authority=?", new Object[]{"TEST_AUTHORITY"});
		assertTrue("Selected id should be 0 (for null value)", countOfAuthorities == 0);
		countOfAuthorities = super.simpleJdbcTemplate.queryForInt("select count(*) from Authorities where authority=?", new Object[]{"TEST_AUTHORITY2"});
		assertTrue("Selected id should be 0 (for null value)",countOfAuthorities != 0);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
