package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.users.ScrumzuAuthority;
import model.users.ScrumzuUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import services.ProjectService;
import services.ScrumzuAuthorityService;
import services.ScrumzuUserDetailsService;
import services.TeamService;

@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest {
	@Mock
	private ScrumzuAuthorityService mockAuthorityService;
	@Mock
	HttpServletRequest mockRequest;

	@Mock
	private ProjectService mockProjectService;
	@Mock BindingResult bindingResult;
	@Mock
	private TeamService mockTeamService;
	Model model;
	@Mock
	private ScrumzuUserDetailsService mockUserService;
	private UsersController controller;


	@Before
	public void setUp() throws Exception {
		controller = new UsersController();
		controller.authorityService = mockAuthorityService;
		controller.projectService = mockProjectService;
		controller.teamService = mockTeamService;
		controller.userService = mockUserService;
		model = new ExtendedModelMap();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetUsersList() {
		List<ScrumzuUser> users  = new ArrayList<ScrumzuUser>();
		users.add(new ScrumzuUser(3L));
		when(mockUserService.getList()).thenReturn(users);
		ReflectionTestUtils.setField(controller, "userService", mockUserService);

		ModelAndView page = controller.getUsersList(model);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("list", modelMap.get("op"));
		assertEquals(users, modelMap.get("users"));
		assertEquals("userList", page.getViewName());
	}

	@Test
	public void testGetEditUserForm() {

		ScrumzuUser user  = new ScrumzuUser(3L);
		when(mockUserService.getUser(anyLong())).thenReturn(user);
		ReflectionTestUtils.setField(controller, "userService", mockUserService);

		List<ScrumzuAuthority> auths = new ArrayList<ScrumzuAuthority>();
		ScrumzuAuthority sa = new ScrumzuAuthority();
		sa.setAuthority("ROLE_SCRUM_MASTER");
		auths.add(sa);
		auths.add(new ScrumzuAuthority());
		when(mockAuthorityService.getAuthorities()).thenReturn(auths);
		ReflectionTestUtils.setField(controller, "userService", mockUserService);

		ModelAndView page = controller.getEditUserForm(model, 3L);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("edit", modelMap.get("op"));
		assertEquals(user, modelMap.get("user"));

		assertEquals("userEditForm", page.getViewName());


	}

	@Test
	public void testGetNewUserForm() {
		ScrumzuUser user  = new ScrumzuUser(3L);
		user.setAuthorities(new ArrayList<ScrumzuAuthority>());

		List<ScrumzuAuthority> auths = new ArrayList<ScrumzuAuthority>();
		auths.add(new ScrumzuAuthority());
		when(mockAuthorityService.getAuthorities()).thenReturn(auths);
		ReflectionTestUtils.setField(controller, "userService", mockUserService);

		ModelAndView page = controller.getNewUserForm(model);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("add", modelMap.get("op"));
		assertEquals(user, modelMap.get("user"));
		assertEquals("userAddForm", page.getViewName());

	}

	@Test
	public void testSaveUser() {


		final List<ScrumzuUser> users = new ArrayList<ScrumzuUser>();
		ScrumzuUser user = new ScrumzuUser("test", "test", false);
		List<ScrumzuAuthority> auths = new ArrayList<ScrumzuAuthority>();
		ScrumzuAuthority sa = new ScrumzuAuthority();
		sa.setAuthority("ROLE_SCRUM_MASTER");
		sa.setIdAuthority(1L);
		auths.add(sa);
		String newPassword ="haslo";
		when(mockRequest.getRequestURI()).thenReturn("http://localhost:8080/scrumzu/RO/pbis/new");

		when(mockUserService.isUsernameTaken(anyString(), anyLong())).thenReturn(false);
		ReflectionTestUtils.setField(controller, "userService",
				mockUserService);



		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				ScrumzuUser u = (ScrumzuUser) invocation.getArguments()[0];
				users.add(u);
				return u.toString();
			}
		}).when(mockUserService).save((ScrumzuUser) anyObject());


		when(bindingResult.hasErrors()).thenReturn(true);
		ModelAndView page = controller.saveUser(user, bindingResult, model, newPassword, "on", mockRequest);
		assertEquals(user.isEnabled(), false);
		assertFalse(users.contains(user));
		assertEquals("userAddForm",page.getViewName());

		when(bindingResult.hasErrors()).thenReturn(false);
		page = controller.saveUser(user, bindingResult, model, newPassword, "on", mockRequest);
		assertEquals(user.isEnabled(), true);
		assertTrue(users.contains(user));

		user.setEnabled(false);
		when(mockUserService.isUsernameTaken(anyString(), anyLong())).thenReturn(true);
		page = controller.saveUser(user, bindingResult, model, newPassword, "on", mockRequest);
		assertEquals(user.isEnabled(), true);
		assertEquals(2, users.size());


	}

	@Test
	public void testDeleteUsers() {
		final List<ScrumzuUser> users = new ArrayList<ScrumzuUser>();
		for(int i=0;i<6;i++){
			ScrumzuUser user = new ScrumzuUser(4L);
			user.setEnabled(true);
			users.add(user);
		}


		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				long[] idsy = (long[]) invocation.getArguments()[0];
				for(int i=0;i<idsy.length;i++){
					ScrumzuUser u = users.get((int) idsy[i]);
					u.setEnabled(false);
				}

				return true;
			}
		}).when(mockUserService).disableUsers((long[])anyObject());

		ReflectionTestUtils.setField(controller, "userService", mockUserService);

		long[]ids = {2,3,4};
		Boolean res= controller.deleteUsers(ids, model);
		assertTrue(res);
		for(int i=0;i<ids.length;i++){
			assertFalse(users.get((int) ids[i]).isEnabled());
		}
	}

}
