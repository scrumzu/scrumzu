package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import model.filters.Filter;
import model.filters.FilterItem;
import model.pbis.PBI;
import model.pbis.Type;
import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;
import model.workItems.Status;
import model.workItems.WorkItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import services.PBIService;
import services.WorkItemService;

import dao.PBIDAO;

public class PBIServiceTest {

	@Mock
	PBIDAO mockPbiDao;
	@Mock
	WorkItemService mockWorkItemService;

	PBIService pbiService;

	@Before
	public void setUp() throws Exception {
		pbiService = new PBIService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetPBIById() {
		long id = 123;

		PBI pbi = new PBI(id);
		when(mockPbiDao.getPBI(anyLong())).thenReturn(pbi);
		ReflectionTestUtils.setField(pbiService, "pbiDao", mockPbiDao);

		PBI result = pbiService.getPBI(anyLong());

		assertEquals(result, pbi);
	}

	@Test
	public void testGetPBIsForTeam() {
		long id = 123;
		Team team = new Team(id);
		List<PBI> mockedList = new ArrayList<PBI>();

		when(mockPbiDao.getPBIsForTeam(team)).thenReturn(mockedList);
		ReflectionTestUtils.setField(pbiService, "pbiDao", mockPbiDao);
		List<PBI> result = pbiService.getPBIsForTeam(team);

		assertEquals(result, mockedList);
	}

	@Test
	public void testGetPBIsForSprint() {
		long id = 123;
		Sprint sprint = new Sprint(id);


		List<PBI> mockedList = new ArrayList<PBI>();

		when(mockPbiDao.getPBIsForSprint(sprint)).thenReturn(mockedList);
		ReflectionTestUtils.setField(pbiService, "pbiDao", mockPbiDao);
		List<PBI> result = pbiService.getPBIsForSprint(sprint);

		assertEquals(result, mockedList);
	}

	@Test
	public void testGetPBIsForProject() {
		long id = 123;
		Project project = new Project(id);

		List<PBI> mockedList = new ArrayList<PBI>();

		when(mockPbiDao.getPBIsForProject(project)).thenReturn(mockedList);
		ReflectionTestUtils.setField(pbiService, "pbiDao", mockPbiDao);
		List<PBI> result = pbiService.getPBIsForProject(project);

		assertEquals(result, mockedList);
	}

	@Test
	public void testSavePBI() {

		final String title2= "AFTERSAVE";
		final List<PBI> pbis = new ArrayList<PBI>();
		PBI pbi = new PBI(123);
		pbi.setTitle("BEFORESAVE");
		pbis.add(pbi);
		PBI pbiNew = new PBI();
		pbiNew.setTitle("UNSAVE");

		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				PBI pbi2 = pbis.get(pbis.indexOf((invocation.getArguments()[0])));
				pbi2.setTitle(title2);
				return pbis.contains(pbi2);
			}
		}).when(mockPbiDao).update((PBI) anyObject());

		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				PBI pbi2 = (PBI) (invocation.getArguments()[0]);
				pbi2.setTitle(title2);
				pbis.add(pbi2);
				return pbis.contains(pbi2);
			}
		}).when(mockPbiDao).save((PBI) anyObject());

		ReflectionTestUtils.setField(pbiService, "pbiDao", mockPbiDao);

		pbiService.savePBI(pbi);

		assertEquals(pbi.getTitle(), title2);

		pbiService.savePBI(pbiNew);

		assertTrue(pbis.contains(pbiNew));
		assertEquals(pbiNew.getTitle(), title2);

	}

	@Test
	public void testDeletePBI() {


		final List<PBI> pbis = new ArrayList<PBI>();
		for (int i = 1; i < 6; i++) {
			pbis.add(new PBI(i));
		}

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				PBI pbiToRemove = (PBI) invocation.getArguments()[0];
				pbis.remove(pbiToRemove);
				return pbis.toString();
			}
		}).when(mockPbiDao).delete((PBI) anyObject());



		ReflectionTestUtils.setField(pbiService, "pbiDao", mockPbiDao);
		PBI pbi3 = new PBI(3);
		pbiService.deletePBI(pbi3);

		assertFalse(pbis.contains(pbi3));

	}

	@Test
	public void testDeletePBIs() {


		final List<PBI> pbis = new ArrayList<PBI>();
		for (int i = 1; i < 6; i++) {
			pbis.add(new PBI(i));
		}
		int size  = pbis.size();
		long[] ids = { 1, 2, 3 };

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long idToRemove = (Long) invocation.getArguments()[0];
				pbis.remove(new PBI(idToRemove));
				return "Removed " + idToRemove;
			}
		}).when(mockPbiDao).delete(anyLong());

		ReflectionTestUtils.setField(pbiService, "pbiDao", mockPbiDao);
		pbiService.deletePBIs(ids);

		assertEquals(pbis.size(), (size-ids.length));


	}

	@Test
	public void testCountPBIsForSprint() {
		Sprint sprint = new Sprint(123);

		when(mockPbiDao.countPBIsForSprint(sprint)).thenReturn(1L);
		ReflectionTestUtils.setField(pbiService, "pbiDao", mockPbiDao);
		long answer = pbiService.countPBIsForSprint(sprint);
		assertEquals(1L,answer);
	}

	@Test
	public void testGetPBIWithAttributes() {
		long id = 123;
		PBI mockedPBI = new PBI(id);
		when(mockPbiDao.getPBIWithAttributes(id)).thenReturn(mockedPBI);
		ReflectionTestUtils.setField(pbiService,"pbiDao",mockPbiDao);
		assertEquals(mockedPBI, pbiService.getPBIWithAttributes(id));

	}

	@Test
	public void testCompareData() throws InterruptedException
	{
		long time1 = System.currentTimeMillis();
		assertEquals(pbiService.compareData(1L, 1L), true);
		assertEquals(pbiService.compareData(0L, 1L), false);
		assertEquals(pbiService.compareData(2L, 1L), false);
		assertEquals(pbiService.compareData("String", "String"), true);
		assertEquals(pbiService.compareData("A", "B"), false);
		assertEquals(pbiService.compareData("C", "B"), false);
		long time2 = time1+20;
		assertEquals(pbiService.compareData(time1, time1), true);
		assertEquals(pbiService.compareData(time1, time2), false);
		Date date1 = new Date(time1);
		assertEquals(pbiService.compareData(date1, ""+time1), true);
		assertEquals(pbiService.compareData(date1,  ""+time2), false);
	}

	@Test
	public void testGetPBIsByFilterAndProject()
	{
		Project project = new Project(1);
		project.setName("Projekt testowy");
		project.setAlias("TOM");
		Sprint sprint1 = new Sprint(1);
		Sprint sprint2 = new Sprint(2);

		Team team1= new Team(1);
		Team team2= new Team(2);
		Team team3= new Team(3);

		List<PBI> pbis = new ArrayList<PBI>();

		PBI pbi1 = new PBI(1);
		pbi1.setTitle("Stworzenie designu");
		pbi1.setPriority(100);
		pbi1.setProject(project);
		pbi1.setType(Type.DESIGN);
		WorkItem wi1 = new WorkItem(sprint1, Status.COMMITTED, team1, pbi1);
		wi1.setStoryPoints(100);
		List<WorkItem> wis5  = new ArrayList<WorkItem>();
		wis5.add(wi1);
		pbi1.setWorkItems(wis5);




		PBI pbi2 = new PBI(2);
		pbi2.setTitle("Stworzenie interface'u");
		pbi2.setPriority(100);
		pbi2.setProject(project);
		pbi2.setType(Type.DESIGN);
		WorkItem wi2 = new WorkItem(sprint1, Status.NEW, team2, pbi2);
		wi2.setStoryPoints(123);
		List<WorkItem> wis4  = new ArrayList<WorkItem>();
		wis4.add(wi2);
		pbi2.setWorkItems(wis4);


		PBI pbi3 = new PBI(3);
		pbi3.setTitle("Autoryzacja");
		pbi3.setDescription("Opis");
		pbi3.setPriority(20);
		pbi3.setProject(project);
		pbi3.setType(Type.IMPLEMENTATION);
		WorkItem wi3 = new WorkItem(sprint2, Status.NEW, team3, pbi3);
		wi3.setStoryPoints(200);
		List<WorkItem> wis = new ArrayList<WorkItem>();
		wis.add(wi3);
		pbi3.setWorkItems(wis);

		PBI pbi4 = new PBI(4);
		pbi4.setTitle("Wyszukiwarka PBIsów");
		pbi4.setPriority(250);
		pbi4.setProject(project);
		pbi4.setType(Type.IMPLEMENTATION);
		WorkItem wi4 = new WorkItem(sprint1, Status.COMMITTED, team2, pbi4);
		wi4.setStoryPoints(100);
		List<WorkItem> wis2  = new ArrayList<WorkItem>();

		wis2.add(wi4);
		pbi4.setWorkItems(wis2);

		PBI pbi5 = new PBI(4);
		pbi5.setTitle("Wyszukiwarka PBIsów");
		pbi5.setPriority(10);
		pbi5.setProject(project);
		pbi5.setType(Type.PERFORMANCE);
		WorkItem wi5 = new WorkItem(null, Status.NEW, null, pbi5);
		wi5.setStoryPoints(123);
		List<WorkItem> wis3  = new ArrayList<WorkItem>();
		wis3.add(wi5);
		pbi5.setWorkItems(wis3);

		pbis.add(pbi1);
		pbis.add(pbi2);
		pbis.add(pbi3);
		pbis.add(pbi4);
		pbis.add(pbi5);


		doAnswer(new Answer<WorkItem>() {
			public WorkItem answer(InvocationOnMock invocation) {
				PBI p = (PBI)invocation.getArguments()[0];
				return p.getWorkItems().get(0);
			}
		}).when(mockWorkItemService).getLastWorkItemForPBI((PBI) anyObject());

		ReflectionTestUtils.setField(pbiService,"workItemService",mockWorkItemService);


		when(mockPbiDao.getPBIsForProject((Project) anyObject())).thenReturn(pbis);
		ReflectionTestUtils.setField(pbiService,"pbiDao",mockPbiDao);

		Filter filter = new Filter(1L);
		FilterItem fi1 = new FilterItem(null,"storyPoints","eq", "100");
		filter.addFilterItem(fi1);

		Set<PBI> resPbi1 = pbiService.getPBIsByFilterAndProject(filter, (Project)anyObject());
		assertEquals(2, resPbi1.size());
		System.out.println(resPbi1);

		FilterItem fi2 = new FilterItem("or","priority","eq", "20");
		filter.addFilterItem(fi2);
		resPbi1 = pbiService.getPBIsByFilterAndProject(filter, (Project)anyObject());
		assertEquals(3, resPbi1.size());

		FilterItem fi3 = new FilterItem("and","team","nq", "3");
		filter.addFilterItem(fi3);
		resPbi1 = pbiService.getPBIsByFilterAndProject(filter, (Project)anyObject());
		assertEquals(2, resPbi1.size());

		Filter filter2 = new Filter(2L);
		FilterItem fi4 = new FilterItem(null,"team","eq", "2");
		filter2.addFilterItem(fi4);

		resPbi1 = pbiService.getPBIsByFilterAndProject(filter2, (Project)anyObject());
		assertEquals(2, resPbi1.size());

		Filter filter3 = new Filter(2L);
		FilterItem fi5 = new FilterItem(null,"sprint","eq", "1");
		filter3.addFilterItem(fi5);

		resPbi1 = pbiService.getPBIsByFilterAndProject(filter3, (Project)anyObject());
		assertEquals(3, resPbi1.size());

		Filter filter4 = new Filter(2L);
		FilterItem fi6 = new FilterItem(null,"status","eq", "COMMITED");
		filter4.addFilterItem(fi6);

		resPbi1 = pbiService.getPBIsByFilterAndProject(filter4, (Project)anyObject());
		assertEquals(2, resPbi1.size());


		filter4 = new Filter(2L);
		filter4.addFilterItem(new FilterItem(null,"title","eq", "sig"));

		resPbi1 = pbiService.getPBIsByFilterAndProject(filter4, (Project)anyObject());
		assertEquals(1, resPbi1.size());

		filter4 = new Filter(2L);
		filter4.addFilterItem(new FilterItem(null,"description","nq", "Opis"));

		resPbi1 = pbiService.getPBIsByFilterAndProject(filter4, (Project)anyObject());
		assertEquals(3, resPbi1.size());
	}



}
