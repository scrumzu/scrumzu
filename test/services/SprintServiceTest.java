package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import org.mockito.Mock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.projects.Project;
import model.releases.Release;
import model.sprint.Sprint;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import services.SprintService;

import dao.SprintDAO;

public class SprintServiceTest {
	
	@Mock
	SprintDAO mockSprintDao;
	
	SprintService sprintService;
	
	@Before
	public void setUp() throws Exception {
		sprintService = new SprintService();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAllSprints() {
		
		List<Sprint> sprints = new ArrayList<Sprint>();
		sprints.add(new Sprint());
		sprints.add(new Sprint());
		when(mockSprintDao.getSprints()).thenReturn(sprints);
		ReflectionTestUtils.setField(sprintService, "sprintDao", mockSprintDao);
		
		List<Sprint> result = sprintService.getAllSprints();
		
		assertEquals(result, sprints);
	}
	
	@Test
	public void testGetSprintsForProject() {
		Project project = new Project(new Long(123));
		List<Sprint> sprints = new ArrayList<Sprint>();
		
		when(mockSprintDao.getSprintsForProject(project)).thenReturn(sprints);
		ReflectionTestUtils.setField(sprintService, "sprintDao", mockSprintDao);
		
		List<Sprint> result = sprintService.getSprintsForProject(project);
		
		assertEquals(sprints, result);
	}

	@Test
		public void testGetSprintById() {
			long id = 123;
			
			Sprint sprint = new Sprint(id);
			when(mockSprintDao.getSprint(anyLong())).thenReturn(sprint);
			ReflectionTestUtils.setField(sprintService, "sprintDao", mockSprintDao);
			
			Sprint result = sprintService.getSprint(anyLong());
			
			assertEquals(result, sprint);
		}

	@Test
	public void testSaveSprint() {
		
		final List<Sprint> sprints = new ArrayList<Sprint>();
		Sprint sprint = new Sprint(123);
		Date date = new Date();
		sprint.setDateFrom(date);
		sprints.add(sprint);
		final Date date2 = new Date();
		Sprint sprintNew = new Sprint();
		sprintNew.setDateFrom(date2);
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Sprint sprint2 = sprints.get(sprints.indexOf((invocation.getArguments()[0])));
				sprint2.setDateFrom(date2);
				return sprints.contains(sprint2);
			}
		}).when(mockSprintDao).update((Sprint) anyObject());
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Sprint sprint2 = (Sprint) (invocation.getArguments()[0]);
				sprint2.setDateFrom(date2);
				sprints.add(sprint2);	
				return sprints.contains(sprint2);
			}
		}).when(mockSprintDao).save((Sprint) anyObject());
		
		ReflectionTestUtils.setField(sprintService, "sprintDao", mockSprintDao);
		
		sprintService.saveSprint(sprint);
		
		assertEquals(sprint.getDateFrom(), date2);
		
		sprintService.saveSprint(sprintNew);
		
		assertTrue(sprints.contains(sprintNew));
		assertEquals(sprintNew.getDateFrom(), date2);

	}

	@Test
	public void testDeleteSprint() {
		

		final List<Sprint> sprints = new ArrayList<Sprint>();
		for (int i = 1; i < 6; i++) {
			sprints.add(new Sprint(i));
		}

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				Sprint sprintToRemove = (Sprint) invocation.getArguments()[0];
				sprints.remove(sprintToRemove);
				return sprints.toString();
			}
		}).when(mockSprintDao).delete((Sprint) anyObject());
		
		ReflectionTestUtils.setField(sprintService, "sprintDao", mockSprintDao);
		Sprint sprint3 = new Sprint(3);
		sprintService.deleteSprint(sprint3);
		
		assertFalse(sprints.contains(sprint3));

	}

	@Test
	public void testDeleteSprints(){

		
		
		final List<Sprint> sprints = new ArrayList<Sprint>();
		for (int i = 1; i < 6; i++) {
			sprints.add(new Sprint(i));
		}
		int size  = sprints.size();
		long[] ids = { 1, 2, 3 };

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long idToRemove = (Long) invocation.getArguments()[0];
					sprints.remove(new Sprint(idToRemove));
				return "Removed " + idToRemove;
			}
		}).when(mockSprintDao).delete(anyLong());
		
		ReflectionTestUtils.setField(sprintService, "sprintDao", mockSprintDao);
		sprintService.deleteSprints(ids);
		
		assertEquals(sprints.size(), (size-ids.length));
	
	
	
	}
	@Test
	public void testGetSprintsForRelease()
	{
		
		List<Sprint> sprints = new ArrayList<Sprint>();
		sprints.add(new Sprint());
		sprints.add(new Sprint());
		when(mockSprintDao.getSprintsForRelease((Release) anyObject())).thenReturn(sprints);
		ReflectionTestUtils.setField(sprintService, "sprintDao", mockSprintDao);
		
		List<Sprint> res = sprintService.getSprintsForRelease(new Release());
		assertEquals(sprints, res);
		
	}

	@Test
	public void testGetStoryPointsForSprintAndRelease()
	{
		long points = 134L;
		when(mockSprintDao.getDoneStoryPoints((Sprint) anyObject(), (Release) anyObject(),  anyBoolean())).thenReturn(points);
		ReflectionTestUtils.setField(sprintService, "sprintDao", mockSprintDao);
		
		long res = sprintService.getStoryPointsForSprintAndRelease(new Sprint(), new Release(), false);
		assertEquals(points, res);
	}
	

}
