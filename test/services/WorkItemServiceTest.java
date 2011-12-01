package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.pbis.PBI;
import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;
import model.users.ScrumzuAuthority;
import model.users.ScrumzuUser;
import model.workItems.Status;
import model.workItems.WorkItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import services.WorkItemService;

import dao.WorkItemDAO;

public class WorkItemServiceTest {

	@Mock
	WorkItemDAO mockWorkItemDAO;

	WorkItemService workItemService;

	@Before
	public void setUp() throws Exception {
		workItemService = new WorkItemService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetWorkItemsForPBI() {
		List<WorkItem> wis = new ArrayList<WorkItem>();
		when(mockWorkItemDAO.getWorkItemsForPBI((PBI) anyObject())).thenReturn(wis);
		ReflectionTestUtils.setField(workItemService, "workItemDAO", mockWorkItemDAO);
		List<WorkItem> res = workItemService.getWorkItemsForPBI(mock(PBI.class));
		assertEquals(wis, res);
	}

	@Test
	public void testGetIdPBIsFromWorkItemsByProjectAndField() {
		List<WorkItem> wis = new ArrayList<WorkItem>();
		for(int i=0;i<6;i++){
			PBI pbi = new PBI(i);
			WorkItem wi = new WorkItem(i);
			wi.setPbi(pbi);
			wis.add(wi);
		}
		when(mockWorkItemDAO.getWorkItemsByField(anyString(),anyString(),anyString())).thenReturn(wis);
		ReflectionTestUtils.setField(workItemService, "workItemDAO", mockWorkItemDAO);
		List<String> res = workItemService.getIdPBIsFromWorkItemsByProjectAndField(mock(Project.class),"", "","");
		assertEquals(wis.size(), res.size());
	}

	@Test
	public void testGetLastWorkItemForPBI() {
		List<WorkItem> wis = new ArrayList<WorkItem>();

		when(mockWorkItemDAO.getWorkItemsForPBI((PBI)anyObject())).thenReturn(wis);
		ReflectionTestUtils.setField(workItemService, "workItemDAO", mockWorkItemDAO);
		WorkItem res = workItemService.getLastWorkItemForPBI(mock(PBI.class));
		assertEquals(null, res);

		WorkItem wi = new WorkItem(1l);
		wi.setTeam(new Team(1l));
		wi.setSprint(new Sprint(1l));
		wi.setStatus(Status.COMMITTED);
		wis.add(wi);
		when(mockWorkItemDAO.getWorkItemsForPBI((PBI)anyObject())).thenReturn(wis);
		res = workItemService.getLastWorkItemForPBI(mock(PBI.class));
		assertEquals(wi, res);
	}

	@Test
	public void testSaveWorkItem() {
		final List<WorkItem> wis = new ArrayList<WorkItem>();

		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				WorkItem wi = (WorkItem) (invocation.getArguments()[0]);
				wis.add(wi);
				return wis.contains(wi);
			}
		}).when(mockWorkItemDAO).save((WorkItem) anyObject());
		ReflectionTestUtils.setField(workItemService, "workItemDAO", mockWorkItemDAO);
		WorkItem wi2= new WorkItem(2L);
		workItemService.saveWorkItem(wi2);
		assertTrue(wis.contains(wi2));
		assertEquals(1,wis.size());
	}

	public void testGetAvailableStatusesForUser()
	{
		String poRole ="ROLE_PRODUCT_OWNER";
		String smRole ="ROLE_SCRUM_MASTER";
		ScrumzuUser sm = new ScrumzuUser(1L);
		List<ScrumzuAuthority> auths = new ArrayList<ScrumzuAuthority>();
		ScrumzuAuthority smAuth= new ScrumzuAuthority();
		smAuth.setAuthority(smRole);
		auths.add(smAuth);
		sm.setAuthorities(auths);

		Set<Status> res = WorkItemService.getAvailableStatusesForUser(sm);
		assertEquals(Status.DONE, res.toArray()[0]);


		ScrumzuUser po = new ScrumzuUser(1L);
		auths = new ArrayList<ScrumzuAuthority>();
		ScrumzuAuthority poAuth= new ScrumzuAuthority();
		poAuth.setAuthority(poRole);
		auths.add(poAuth);
		po.setAuthorities(auths);

		res = WorkItemService.getAvailableStatusesForUser(po);
		assertEquals(Status.DONE, res.toArray()[0]);


	}

}
