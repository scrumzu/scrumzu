package controllers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import model.filters.Filter;
import model.projects.Project;
import model.users.ScrumzuUser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import services.FilterService;
import services.ProjectService;



public class FilterControllerTest {

	FilterController controller;
	Model model;
	@Mock BindingResult bindingResult;
	@Mock FilterService mockFilterService;
	@Mock ProjectService mockProjectService;
	String projectAlias;
	List<Filter> filters;
	List<Project>projects = new ArrayList<Project>();
	Principal principal = new UsernamePasswordAuthenticationToken(new ScrumzuUser(1L), "AUTHENTICATED");
	
	@Mock 
	Authentication mockAuth;
	@Mock
	SecurityContext mockSecurityContext;

	@Before
	public void setUp() throws Exception {
		controller = new FilterController();
		model = new ExtendedModelMap();
		MockitoAnnotations.initMocks(this);
		projectAlias = "TSP";
		projects = new ArrayList<Project>();
		filters = new ArrayList<Filter>();
		model.addAttribute("filters", filters);
	}
	
	@Test
	public void testGetAllFilters()
	{
		
		List<Filter> filters = new ArrayList<Filter>();
		when(mockFilterService.getFiltersForUser((ScrumzuUser) anyObject())).thenReturn(filters);
		ReflectionTestUtils.setField(controller, "filterService",
				mockFilterService);
		Principal principal = new UsernamePasswordAuthenticationToken(new ScrumzuUser(1L), "AUTHENTICATED");
		List<Filter> filterRes = controller.getAllFilters(model, principal);

		assertEquals(filterRes, filters);
	}

	@Test
	public void testDeleteFilter()
	{
		final List<Filter> filter = new ArrayList<Filter>();
		for (long i = 1; i < 6; i++) {
			filter.add(new Filter(i));
		}

		long id = 2;
		int size = filter.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long[] deletedIndexes = (long[]) invocation.getArguments()[0];
				for (int i = 0; i < deletedIndexes.length; i++) {
					filter.remove(deletedIndexes[i]);
				}

				return filter.toString();
			}
		}).when(mockFilterService).deleteFilters((long[]) anyObject());

		when(mockFilterService.getFilterById(anyLong())).thenReturn(
				filter.get(anyInt()));

		ReflectionTestUtils.setField(controller, "filterService",
				mockFilterService);
		boolean answer = controller.deleteFilter(id, model);

		assertTrue(answer);
		assertEquals(size, filter.size());
		
		
	
	}
	@Test
	public void testUpdateFilter()
	{
		Filter filter = new Filter();
		filter.setIdFilter(2L);

		final long id2 = 3L;
		final List<Filter> filters = new ArrayList<Filter>();
		filters.add(filter);

		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Filter p = filters.get(filters.indexOf((invocation
						.getArguments()[0])));
				p.setIdFilter(id2);
				return filters.contains(invocation.getArguments()[0]);
			}
		}).when(mockFilterService).saveFilter((Filter) anyObject());

		ReflectionTestUtils.setField(controller, "filterService",
				mockFilterService);
		
		
		boolean result= controller.updateFilter(filter, 2L, model, principal);
		assertEquals(result, true);
		assertEquals(filter.getIdFilter(), (Long) id2);

	}
	@Test
	public void testGetFilter()
	{
		Filter filter = new Filter(1L);
		when(mockFilterService.getFilterById(anyLong())).thenReturn(filter);
		ReflectionTestUtils.setField(controller, "filterService",
				mockFilterService);
		Filter result = controller.getFilter(anyLong(), model);
		assertEquals(filter, result);
	}
	@Test
	public void testSaveFilter()
	{
		
		Filter filter = new Filter();
		filter.setName("Filtr");
		final Long id2= new Long(2);
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Filter p = (Filter)invocation.getArguments()[0];
				p.setIdFilter(id2);
				return filters.contains(invocation.getArguments()[0]);
			}
		}).when(mockFilterService).saveFilter((Filter) anyObject());

		ReflectionTestUtils.setField(controller, "filterService", mockFilterService);

		controller.saveFilter(filter, model, principal);
		
		assertEquals(id2, filter.getIdFilter());
	}
	
	

}
