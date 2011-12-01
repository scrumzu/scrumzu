package services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import model.users.ScrumzuAuthority;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import services.ScrumzuAuthorityService;

import dao.ScrumzuAuthorityDAO;
public class ScrumzuAuthorityServiceTest {
	@Mock
	ScrumzuAuthorityDAO mockAuthorityDao;
	
	ScrumzuAuthorityService scrumzuAuthorityService;
	
	@Before
	public void setUp() throws Exception {
		scrumzuAuthorityService= new ScrumzuAuthorityService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAuthorities() {
		List<ScrumzuAuthority> auths= new ArrayList<ScrumzuAuthority>();
		when(mockAuthorityDao.getAuthorities()).thenReturn(auths);
		ReflectionTestUtils.setField(scrumzuAuthorityService, "authorityDao", mockAuthorityDao);
		List<ScrumzuAuthority> res = scrumzuAuthorityService.getAuthorities();
		assertEquals(auths,res);
	}

	@Test
	public void testGetAuthority() {
		ScrumzuAuthority auth= new ScrumzuAuthority();
		auth.setAuthority("ROLE_PRODUCT_OWNER");
		when(mockAuthorityDao.getAuthority(anyLong())).thenReturn(auth);
		ReflectionTestUtils.setField(scrumzuAuthorityService, "authorityDao", mockAuthorityDao);
		ScrumzuAuthority res = scrumzuAuthorityService.getAuthority(1L);
		assertEquals(auth, res);
	}

}
