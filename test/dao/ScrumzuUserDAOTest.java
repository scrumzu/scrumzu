package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import model.users.ScrumzuUser;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import dao.ScrumzuUserDAO;

@ContextConfiguration("DaoTest-context.xml")
public class ScrumzuUserDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private ScrumzuUserDAO userDao;
	
	private static ScrumzuUser insertedUser;
	private static Long salt =  (System.currentTimeMillis() * 1301077L) % 1000000;
	
	@BeforeClass
	public static void insertTeam() {
		ScrumzuUser user = new ScrumzuUser("TestUser", "Hash password", true);
		insertedUser = user;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2012, 01, 01);
		
	}
	
	@Test
	public void saveTest() {
		int rowsBeforeSave = super.countRowsInTable("Users");
		userDao.save(insertedUser);
		userDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Users");
		assertEquals("Number of rows has not increased", rowsBeforeSave+1, rowsAfterSave);
		long insertedUserId = insertedUser.getIdUser();
		ScrumzuUser selectedUser = super.simpleJdbcTemplate.queryForObject("select idUser, username, password, enabled, salt from Users where idUser=?",
				new RowMapper<ScrumzuUser>() {
			public ScrumzuUser mapRow(ResultSet rs,int rowNum) throws SQLException{
						ScrumzuUser user = new ScrumzuUser(rs.getString("username"), rs.getString("password"), rs.getBoolean("enabled"));
						user.setIdUser(rs.getLong("idUser"));
						user.setSalt(rs.getLong("salt"));
						return user;
			}
		}, new Object[] {insertedUserId});
		Logger l = Logger.getRootLogger();
		l.error(selectedUser);
		assertEquals("WorkItems are not equal",insertedUser, selectedUser);
	}
	
	@Test
	public void getUsersTest() {
		userDao.save(insertedUser);
		List<ScrumzuUser> users = userDao.getUsers();
		if (users.isEmpty()) fail("List of users cannot be empty after insert");
		ScrumzuUser user = users.get(users.size()-1);
		assertEquals("Selected users is not equal", user, userDao.loadUser(user.getIdUser()));
	}
	
	@Test
	public void getScrumMastersTest() {
		super.simpleJdbcTemplate.update("insert into Users (username, password, enabled, salt) values (?, ?, ?, ?)", new Object[] {"TestSuperUser", "Hash password", true, salt});
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestSuperUser"});
		super.simpleJdbcTemplate.update("insert into UserPrivileges (idAuthority, idUser) values (?, ?)", new Object[]{new Integer(1), userId});
		super.simpleJdbcTemplate.update("insert into Users (username, password, enabled, salt) values (?, ?, ?, ?)", new Object[] {"TestSuperUser2", "Hash password", true, salt});
		long userId2 = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestSuperUser2"});
		super.simpleJdbcTemplate.update("insert into UserPrivileges (idAuthority, idUser) values (?, ?)", new Object[]{new Integer(3), userId2});
		ScrumzuUser superUser = new ScrumzuUser();
		superUser.setUsername("TestSuperUser");
		ScrumzuUser superUser2 = new ScrumzuUser();
		superUser2.setUsername("TestSuperUser2");
		List<ScrumzuUser> users = userDao.getScrumMasters();
		Logger l = Logger.getRootLogger();
		l.error(users);
		assertFalse("Selected list should not contain different than scrum masters", users.contains(superUser));
		assertTrue("Selected list should contain scrum master", users.contains(superUser2));
	}
	
	@Test
	public void getUserByIdTest() {
		super.simpleJdbcTemplate.update("insert into Users (username, password, enabled, salt) values (?, ?, ?, ?)", new Object[] {"TestUser", "Hash password", true, salt});
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		ScrumzuUser user = userDao.getUser(userId);
		ScrumzuUser selectedUser = super.simpleJdbcTemplate.queryForObject("select idUser, username, password, enabled from Users where idUser=?",
				new RowMapper<ScrumzuUser>() {
			public ScrumzuUser mapRow(ResultSet rs,int rowNum) throws SQLException{
						ScrumzuUser user = new ScrumzuUser(rs.getString("username"), rs.getString("password"), rs.getBoolean("enabled"));
						user.setIdUser(rs.getLong("idUser"));
						return user;
			}
		}, new Object[] {userId});
		assertEquals("Selected user is not equal to inserted one",selectedUser, user);
	}
	
	@Test
	public void getUserByUserNameTest() {
		super.simpleJdbcTemplate.update("insert into Users (username, password, enabled, salt) values (?, ?, ?, ?)", new Object[] {"TestUser", "Hash password", true, salt});
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		ScrumzuUser user = userDao.getUser("TestUser");
		ScrumzuUser selectedUser = super.simpleJdbcTemplate.queryForObject("select idUser, username, password, enabled from Users where idUser=?",
				new RowMapper<ScrumzuUser>() {
			public ScrumzuUser mapRow(ResultSet rs,int rowNum) throws SQLException{
						ScrumzuUser user = new ScrumzuUser(rs.getString("username"), rs.getString("password"), rs.getBoolean("enabled"));
						user.setIdUser(rs.getLong("idUser"));
						return user;
			}
		}, new Object[] {userId});
		assertEquals("Selected user is not equal to inserted one",selectedUser, user);
	}
	
	@Test
	public void loadUserTest() {
		super.simpleJdbcTemplate.update("insert into Users (username, password, enabled, salt) values (?, ?, ?, ?)", new Object[] {"TestUser", "Hash password", true, salt});
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		ScrumzuUser user = userDao.loadUser(userId);
		ScrumzuUser selectedUser = super.simpleJdbcTemplate.queryForObject("select idUser, username, password, enabled from Users where idUser=?",
				new RowMapper<ScrumzuUser>() {
			public ScrumzuUser mapRow(ResultSet rs,int rowNum) throws SQLException{
						ScrumzuUser user = new ScrumzuUser(rs.getString("username"), rs.getString("password"), rs.getBoolean("enabled"));
						user.setIdUser(rs.getLong("idUser"));
						return user;
			}
		}, new Object[] {userId});
		assertEquals("Selected user is not equal to inserted one",selectedUser.getUsername(), user.getUsername());
	}
	
	@Test
	public void updateUserTest() {
		super.simpleJdbcTemplate.update("insert into Users (username, password, enabled, salt) values (?, ?, ?, ?)", new Object[] {"TestUser", "Hash password", true, salt});
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		ScrumzuUser user = userDao.getUser(userId);
		user.setUsername("TestUser2");
		userDao.update(user);
		userDao.flushSession();
		ScrumzuUser selectedUser = super.simpleJdbcTemplate.queryForObject("select idUser, username, password, enabled from Users where idUser=?",
				new RowMapper<ScrumzuUser>() {
			public ScrumzuUser mapRow(ResultSet rs,int rowNum) throws SQLException{
						ScrumzuUser user = new ScrumzuUser(rs.getString("username"), rs.getString("password"), rs.getBoolean("enabled"));
						user.setIdUser(rs.getLong("idUser"));
						return user;
			}
		}, new Object[] {userId});
		assertEquals("Selected user is not equal to inserted one",selectedUser, user);
	}
	
	@Test
	public void deleteUserTest() {
		int rowsBeforeSave = super.countRowsInTable("Users");
		userDao.save(insertedUser);
		userDao.flushSession();
		userDao.delete(insertedUser);
		userDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Users");
		assertEquals("Number of rows has not increased", rowsBeforeSave, rowsAfterSave);
		ScrumzuUser user = userDao.getUser(insertedUser.getUsername());
		assertNull("Selected user should be null after delete", user);
	}
	
	@Test
	public void deleteUserByIdTest() {
		int rowsBeforeSave = super.countRowsInTable("Users");
		userDao.save(insertedUser);
		userDao.flushSession();
		userDao.delete(insertedUser.getIdUser());
		userDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Users");
		assertEquals("Number of rows has not increased", rowsBeforeSave, rowsAfterSave);
		ScrumzuUser user = userDao.getUser(insertedUser.getUsername());
		assertNull("Selected user should be null after delete", user);
		
		
	}
	
	@Test
	public void deleteUserByUsernameTest() {
		int rowsBeforeSave = super.countRowsInTable("Users");
		userDao.save(insertedUser);
		userDao.flushSession();
		userDao.delete(insertedUser.getUsername());
		userDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Users");
		assertEquals("Number of rows has not increased", rowsBeforeSave, rowsAfterSave);
		ScrumzuUser user = userDao.getUser(insertedUser.getUsername());
		assertNull("Selected user should be null after delete", user);
		
		
	}
	
	

}
