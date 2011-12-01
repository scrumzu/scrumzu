package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import model.filters.Filter;
import model.teams.Team;
import model.users.ScrumzuAuthority;
import model.users.ScrumzuUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import services.ScrumzuUserDetailsService;

import dao.FilterDAO;
import dao.ScrumzuUserDAO;
import dao.TeamDAO;
public class ScrumzuUserDetailsServiceTest {

	@Mock
	ScrumzuUserDAO mockUserDao;
	
	@Mock
	TeamDAO mockTeamDao;
	
	@Mock
	FilterDAO mockFilterDao;
	
	ScrumzuUserDetailsService userService;
	@Before
	public void setUp() throws Exception {
		userService=new ScrumzuUserDetailsService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testLoadUserByUsername() {
		ScrumzuUser user = new ScrumzuUser(2L);
		when(mockUserDao.getUser(anyString())).thenReturn(user);
		ReflectionTestUtils.setField(userService, "userDao", mockUserDao);
		UserDetails res = userService.loadUserByUsername("asda");
		
		assertEquals(user, res);
		
		when(mockUserDao.getUser(anyString())).thenReturn(null);
		try{
			userService.loadUserByUsername("asda");
		}
		catch(Exception e){
			assertTrue(e instanceof UsernameNotFoundException);
		}
	}

	@Test
	public void testGetUser() {
		
		ScrumzuUser user = new ScrumzuUser(2L);
		when(mockUserDao.getUser(anyLong())).thenReturn(user);
		ReflectionTestUtils.setField(userService, "userDao", mockUserDao);
		ScrumzuUser res = userService.getUser(2L);
		
		assertEquals(user, res);
		
		when(mockUserDao.getUser(anyLong())).thenReturn(null);
		try{
			res = userService.getUser(2L);
		}
		catch(Exception e){
			assertTrue(e instanceof UsernameNotFoundException);
		}
	}

	@Test
	public void testGetScrumMasters() {
		List<ScrumzuUser> users = new ArrayList<ScrumzuUser>();
		ScrumzuUser user= new ScrumzuUser();
		List<ScrumzuAuthority> auths = new ArrayList<ScrumzuAuthority>();
		ScrumzuAuthority auth = new ScrumzuAuthority();
		auth.setAuthority("ROLE_PRODUCT_OWNER");
		auths.add(auth);
		user.setAuthorities(auths);
		users.add(user);
		when(mockUserDao.getScrumMasters()).thenReturn(users);
		ReflectionTestUtils.setField(userService, "userDao", mockUserDao);
		assertEquals(users, userService.getScrumMasters());
	}

	@Test
	public void testGetList() {
		List<ScrumzuUser> users = new ArrayList<ScrumzuUser>();
		ScrumzuUser user= new ScrumzuUser();
		List<ScrumzuAuthority> auths = new ArrayList<ScrumzuAuthority>();
		ScrumzuAuthority auth = new ScrumzuAuthority();
		auth.setAuthority("ROLE_PRODUCT_OWNER");
		auths.add(auth);
		user.setAuthorities(auths);
		users.add(user);
		when(mockUserDao.getUsers()).thenReturn(users);
		ReflectionTestUtils.setField(userService, "userDao", mockUserDao);
		assertEquals(users, userService.getList());
	}

	@Test
	public void testSave() {
		final String title2= "AFTERSAVE";
		final List<ScrumzuUser> users = new ArrayList<ScrumzuUser>();
		ScrumzuUser user = new ScrumzuUser(123);
		user.setUsername("BEFORESAVE");
		users.add(user);
		ScrumzuUser userNew = new ScrumzuUser();
		userNew.setUsername("UNSAVE");
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				ScrumzuUser user2 = users.get(users.indexOf((invocation.getArguments()[0])));
				user2.setUsername(title2);
				return users.contains(user2);
			}
		}).when(mockUserDao).update((ScrumzuUser) anyObject());
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				ScrumzuUser user2 = (ScrumzuUser) (invocation.getArguments()[0]);
				user2.setUsername(title2);
				users.add(user2);	
				return users.contains(user2);
			}
		}).when(mockUserDao).save((ScrumzuUser) anyObject());
		
		ReflectionTestUtils.setField(userService, "userDao", mockUserDao);
		
		userService.save(user);
		
		assertEquals(user.getUsername(), title2);
		
		userService.save(userNew);
		
		assertTrue(users.contains(userNew));
		assertEquals(userNew.getUsername(), title2);
	}

	@Test
	public void testDelete() {
		final List<ScrumzuUser> users = new ArrayList<ScrumzuUser>();
		for (int i = 1; i < 6; i++) {
			users.add(new ScrumzuUser(i));
		}
		int size  = users.size();
		long[] ids = { 1, 2, 3 };

		

		ScrumzuUser user = new ScrumzuUser();
		when(mockUserDao.getUser(anyLong())).thenReturn(user);
		

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long idToRemove = (Long) invocation.getArguments()[0];
					users.remove(new ScrumzuUser(idToRemove));
				return "Removed " + idToRemove;
			}
		}).when(mockUserDao).delete(anyLong());
		
		
		
		ReflectionTestUtils.setField(userService, "userDao", mockUserDao);
		userService.delete(ids);

		assertEquals((size-ids.length), users.size());
	}

	@Test
	public void testDisableUsers() {
		final List<ScrumzuUser> users = new ArrayList<ScrumzuUser>();
		for(long i=0;i<6; i++){
			ScrumzuUser sm = new ScrumzuUser(i);
			sm.setEnabled(true);
			users.add(sm);
		}
		
		final List<Team> teams = new ArrayList<Team>();
		for(long i=0;i<6; i++){
			Team team = new Team(i);
			team.setUser(users.get((int)i));
			teams.add(team);
		}
		
		
		final List<Filter> filters = new ArrayList<Filter>();
		for(long i=0;i<6; i++){
			Filter filter = new Filter(i);
			filter.setUser(users.get((int)i));
			filters.add(filter);
		}
		doAnswer(new Answer<ScrumzuUser>() {
			public ScrumzuUser answer(InvocationOnMock invocation) {
				long idToDisable = (Long) invocation.getArguments()[0];
				return users.get((int)idToDisable);
			}
		}).when(mockUserDao).getUser(anyLong());
		
		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				Team team = (Team) invocation.getArguments()[0];
				return team.toString();
			}
		}).when(mockTeamDao).update((Team) anyObject());
		
		doAnswer(new Answer<List<Team>>() {
			public List<Team> answer(InvocationOnMock invocation) {
				ScrumzuUser user = (ScrumzuUser) invocation.getArguments()[0];
				List<Team> res= new ArrayList<Team>();
				res.add(teams.get(Integer.parseInt(user.getIdUser().toString())));
				return res;
			}
		}).when(mockTeamDao).getTeamsForUser((ScrumzuUser) anyObject());
		
		doAnswer(new Answer<List<Filter>>() {
			public List<Filter> answer(InvocationOnMock invocation) {
				ScrumzuUser user = (ScrumzuUser) invocation.getArguments()[0];
				List<Filter> res= new ArrayList<Filter>();
				res.add(filters.get(Integer.parseInt(user.getIdUser().toString())));
				return res;
			}
		}).when(mockFilterDao).getFiltersCreatedByUser((ScrumzuUser) anyObject());
		
		
		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				Filter filter = (Filter) invocation.getArguments()[0];
				filters.set(Integer.parseInt(filter.getIdFilter().toString()), null);
				return filter.toString();
			}
		}).when(mockFilterDao).delete((Filter) anyObject());
		
		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				ScrumzuUser user = (ScrumzuUser) invocation.getArguments()[0];
				return user.toString();
			}
		}).when(mockUserDao).update((ScrumzuUser) anyObject());
		
		long[] ids = {0, 1, 2, 3, 4, 5};
		
		ReflectionTestUtils.setField(userService, "userDao", mockUserDao);
		ReflectionTestUtils.setField(userService, "teamDao", mockTeamDao);
		ReflectionTestUtils.setField(userService, "filterDao", mockFilterDao);
		userService.disableUsers(ids);
		
		for(ScrumzuUser u: users){
			assertEquals(false,u.isEnabled());
		}

		for(Filter f: filters){
			assertEquals(null,f);
		}

		for(Team t: teams){
			assertEquals(null,t.getUser());
		}
	}

	@Test
	public void testIsUsernameTaken() {
		ScrumzuUser user = new ScrumzuUser(2L);
		when(mockUserDao.getUser(anyString())).thenReturn(user);
		ReflectionTestUtils.setField(userService, "userDao", mockUserDao);
		
		assertEquals(true, userService.isUsernameTaken("ada", 2L));
		
	}

}
