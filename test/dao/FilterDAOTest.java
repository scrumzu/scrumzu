package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import model.filters.Filter;
import model.users.ScrumzuUser;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import dao.FilterDAO;
import dao.ScrumzuUserDAO;


@ContextConfiguration("DaoTest-context.xml")
public class FilterDAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	FilterDAO filterDao;
	
	@Autowired
	ScrumzuUserDAO userDao;
	
	private Filter insertedFilter;
	private ScrumzuUser insertedUser;
	
	@Before
	public void insertFilter() {
		ScrumzuUser user = new ScrumzuUser("TestUser", "Hash password", true);
		insertedUser = user;
		Filter filter = new Filter();
		filter.setName("TestFilter");
		filter.setUser(user);
		filter.setIsPublic(true);
		filter.addFilterItem("and", "status", "=", "1");
		filter.addFilterItem("or", "status", "=", "0");
		insertedFilter = filter;
	}
	
	@Test
	public void saveTest() {
		userDao.save(insertedUser);
		userDao.flushSession();
		int rowsBeforeSave = super.countRowsInTable("Filters");
		int rowsFilterItemsBeforeSave = super.countRowsInTable("FilterItems");
		filterDao.save(insertedFilter);
		filterDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Filters");
		int rowsFilterItemsAfterSave = super.countRowsInTable("FilterItems");
		assertEquals("Number of rows in table is not valid",rowsBeforeSave+1, rowsAfterSave);
		assertEquals("Number of rows in table is not valid",rowsFilterItemsBeforeSave+2, rowsFilterItemsAfterSave);
	}
	
	@Test
	public void getFilterTest() {
		userDao.save(insertedUser);
		userDao.flushSession();
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		super.simpleJdbcTemplate.update("insert into Filters (idUser, name) values (?, ?)", new Object[] {userId, "TestFilter"});
		long filterId = super.simpleJdbcTemplate.queryForLong("select idFilter from Filters where name=?", new Object[] {"TestFilter"});
		Filter selectedFilter = filterDao.getFilter(filterId);
		assertEquals("Selected filter is not equal to inserted one",insertedFilter.getName(), selectedFilter.getName());
	}
	
	@Test
	public void deleteTest() {
		userDao.save(insertedUser);
		userDao.flushSession();
		int rowsBeforeSave = super.countRowsInTable("Filters");
		int rowsFilterItemsBeforeSave = super.countRowsInTable("FilterItems");
		filterDao.save(insertedFilter);
		filterDao.flushSession();
		filterDao.delete(insertedFilter);
		filterDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Filters");
		int rowsFilterItemsAfterSave = super.countRowsInTable("FilterItems");
		assertEquals("Number of rows in table is not valid",rowsBeforeSave, rowsAfterSave);
		assertEquals("Number of rows in table is not valid",rowsFilterItemsBeforeSave, rowsFilterItemsAfterSave);
		
	}
	
	@Test
	public void deleteByIdTest() {
		userDao.save(insertedUser);
		userDao.flushSession();
		int rowsBeforeSave = super.countRowsInTable("Filters");
		int rowsFilterItemsBeforeSave = super.countRowsInTable("FilterItems");
		filterDao.save(insertedFilter);
		filterDao.flushSession();
		filterDao.delete(insertedFilter.getIdFilter());
		filterDao.flushSession();
		int rowsAfterSave = super.countRowsInTable("Filters");
		int rowsFilterItemsAfterSave = super.countRowsInTable("FilterItems");
		assertEquals("Number of rows in table is not valid",rowsBeforeSave, rowsAfterSave);
		assertEquals("Number of rows in table is not valid",rowsFilterItemsBeforeSave, rowsFilterItemsAfterSave);	
	}
	
	@Test
	public void loadFilterTest() {
		userDao.save(insertedUser);
		userDao.flushSession();
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		super.simpleJdbcTemplate.update("insert into Filters (idUser, name) values (?, ?)", new Object[] {userId, "TestFilter"});
		long filterId = super.simpleJdbcTemplate.queryForLong("select idFilter from Filters where name=?", new Object[] {"TestFilter"});
		Filter selectedFilter = filterDao.loadFilter(filterId);
		assertEquals("Selected filter is not equal to inserted one",insertedFilter.getName(), selectedFilter.getName());
	}
	
	@Test
	public void getFiltersForUserTest() {
		userDao.save(insertedUser);
		userDao.flushSession();
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		super.simpleJdbcTemplate.update("insert into Filters (idUser, name, isPublic) values (?, ?, ?)", new Object[] {userId, "TestFilter", Boolean.TRUE});
		super.simpleJdbcTemplate.update("insert into Filters (idUser, name, isPublic) values (?, ?, ?)", new Object[] {userId, "TestFilter2", Boolean.FALSE});
		List<Filter> filters = filterDao.getFiltersForUser(insertedUser);
		assertTrue("List size is not valid", filters.size() != 0);
		boolean containTestFilter = false;
		boolean containTestFilter2 = false;
		for (Filter f:filters) {
			if (f.getName().equals("TestFilter")) containTestFilter = true;
			else if (f.getName().equals("TestFilter2")) containTestFilter2 = true;
			
			if (!f.getIsPublic()) {
				assertEquals("User of private filter should be equal to specified user", f.getUser().getUsername(), insertedUser.getUsername());
			}
		}
		assertTrue("Selected list of filters should contain inserted filters", containTestFilter);
		assertTrue("Selected list of filters should contain inserted filters", containTestFilter2);
	}
	
	@Test
	public void getFiltersTest() {
		userDao.save(insertedUser);
		userDao.flushSession();
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		super.simpleJdbcTemplate.update("insert into Users (username, password, enabled, salt) values (?, ?, ?, ?)", new Object[] {"TestSuperUser", "Hash password", true, System.currentTimeMillis()});
		long superUserId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestSuperUser"});
		super.simpleJdbcTemplate.update("insert into Filters (idUser, name, isPublic) values (?, ?, ?)", new Object[] {userId, "TestFilter", Boolean.TRUE});
		super.simpleJdbcTemplate.update("insert into Filters (idUser, name, isPublic) values (?, ?, ?)", new Object[] {superUserId, "TestFilter2", Boolean.FALSE});		
		List<Filter> filters = filterDao.getFilters();
		assertTrue("List size is not valid", filters.size() != 0);
		boolean containTestFilter = false;
		boolean containTestFilter2 = false;
		for (Filter f:filters) {
			if (f.getName().equals("TestFilter")) containTestFilter = true;
			else if (f.getName().equals("TestFilter2")) containTestFilter2 = true;
		}
		assertTrue("Selected list of filters should contain inserted filters", containTestFilter);
		assertTrue("Selected list of filters should contain inserted filters", containTestFilter2);
	}
	
	@Test
	public void updateTest() {
		userDao.save(insertedUser);
		userDao.flushSession();
		long userId = super.simpleJdbcTemplate.queryForLong("select idUser from Users where username=?", new Object[] {"TestUser"});
		super.simpleJdbcTemplate.update("insert into Filters (idUser, name) values (?, ?)", new Object[] {userId, "TestFilter"});
		long filterId = super.simpleJdbcTemplate.queryForLong("select idFilter from Filters where name=?", new Object[] {"TestFilter"});
		insertedFilter.setIdFilter(filterId);
		insertedFilter.setName("TestFilter2");
		filterDao.update(insertedFilter);
		filterDao.flushSession();
		String name = super.simpleJdbcTemplate.queryForObject("select name from Filters where idFilter=?", String.class, new Object[] {filterId});
		assertEquals("Username did not change properly", insertedFilter.getName(), name);
	}
	

}
