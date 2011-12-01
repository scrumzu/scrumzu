package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.filters.Filter;
import model.users.ScrumzuAuthority;
import model.users.ScrumzuUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import dao.FilterDAO;

import services.FilterService;

public class FilterServiceTest {
	
	@Mock
	FilterDAO mockFilterDao;
	
	Collection<ScrumzuAuthority> authorities;
	
	FilterService filterService;
	
	@Before
	public void setUp() throws Exception {
		filterService = new FilterService();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAllFilters() {
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter());
		filters.add(new Filter());
		when(mockFilterDao.getFiltersForUser((ScrumzuUser) anyObject())).thenReturn(filters);
		ReflectionTestUtils.setField(filterService, "filterDao", mockFilterDao);
		
		ScrumzuUser user = mock(ScrumzuUser.class);
		List<Filter> result = filterService.getFiltersForUser(user);
		
		assertEquals(result, filters);
	}
	

	@Test
		public void testGetFilterById() {
			long id = 123;

			Filter filter = new Filter(id);
			
			ScrumzuUser su = new ScrumzuUser();
			authorities= new ArrayList<ScrumzuAuthority>();
			ReflectionTestUtils.setField(su, "authorities", authorities);
			List<String> auths = new ArrayList<String>();
			auths.add("ROLE_PRODUCT_OWNER");
			auths.add("ROLE_SCRUM_MASTER");
			su.setAuthorities(auths);
			filter.setUser(su);
			
			
			when(mockFilterDao.getFilter(anyLong())).thenReturn(filter);
			ReflectionTestUtils.setField(filterService, "filterDao", mockFilterDao);
			
			
			
			Filter result = filterService.getFilterById(anyLong());
			
			assertEquals(result, filter);
		}

	@Test
	public void testSaveFilter() {
		
		final List<Filter> filters = new ArrayList<Filter>();
		Filter filter = new Filter(123L);
		filter.setName("BEFORESAVE");
		filters.add(filter);
		Filter filterNew = new Filter();
		filter.setName("BEFORESAVE");
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Filter filter2 = filters.get(filters.indexOf((invocation.getArguments()[0])));
				filter2.setName("AFTERSAVE");
				return filters.contains(filter2);
			}
		}).when(mockFilterDao).update((Filter) anyObject());
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Filter filter2 = (Filter) (invocation.getArguments()[0]);
				filter2.setName("AFTERSAVE");
				filters.add(filter2);	
				return filters.contains(filter2);
			}
		}).when(mockFilterDao).save((Filter) anyObject());
		
		ReflectionTestUtils.setField(filterService, "filterDao", mockFilterDao);
		
		filterService.saveFilter(filter);
		
		assertEquals(filter.getName(), "AFTERSAVE");
		
		filterService.saveFilter(filterNew);
		
		assertTrue(filters.contains(filterNew));
		assertEquals(filterNew.getName(), "AFTERSAVE");

	}

	@Test
	public void testDeleteFilter() {
		

		final List<Filter> filters = new ArrayList<Filter>();
		for (long i = 0; i < 6; i++) {
			filters.add(new Filter(i));
		}

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				Filter filterToRemove = (Filter) invocation.getArguments()[0];
				filters.remove(filterToRemove);
				return filters.toString();
			}
		}).when(mockFilterDao).delete((Filter) anyObject());
		
		ReflectionTestUtils.setField(filterService, "filterDao", mockFilterDao);
		Filter filter3 = new Filter(3L);
		filterService.deleteFilter(filter3);
		
		
		assertFalse(filters.contains(filter3));

	}

	@Test
	public void testDeleteFilters(){

		
		
		final List<Filter> filters = new ArrayList<Filter>();
		for (long  i = 1; i < 6; i++) {
			filters.add(new Filter(i));
		}
		int size  = filters.size();
		long[] ids = { 1, 2, 3 };

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long idToRemove = (Long) invocation.getArguments()[0];
					filters.remove(new Filter(idToRemove));
				return "Removed " + idToRemove;
			}
		}).when(mockFilterDao).delete(anyLong());
		
		ReflectionTestUtils.setField(filterService, "filterDao", mockFilterDao);
		filterService.deleteFilters(ids);
		
		assertEquals(filters.size(), (size-ids.length));
	
	
	
	}
	@Test
	public void testGetFiltersCreatedByUser()
	{
		
		List<Filter> filters = new ArrayList<Filter>();
		when(mockFilterDao.getFiltersCreatedByUser((ScrumzuUser)anyObject())).thenReturn(filters);
		ReflectionTestUtils.setField(filterService, "filterDao", mockFilterDao);
		List<Filter> res = filterService.getFiltersCreatedByUser(new ScrumzuUser());
		assertEquals(filters,res);
	}
	@Test
	public void testGetFilters()
	{
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter(2L));
		when(mockFilterDao.getFilters()).thenReturn(filters);
		ReflectionTestUtils.setField(filterService, "filterDao", mockFilterDao);
		List<Filter> res = filterService.getFilters();
		assertEquals(filters,res);
		assertEquals(1,res.size());
	}
	

}
