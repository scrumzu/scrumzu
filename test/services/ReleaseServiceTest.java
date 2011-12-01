package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.pbis.PBI;
import model.projects.Project;
import model.releases.Release;
import model.releases.ReleaseItem;
import model.sprint.Sprint;
import model.sprint.SprintStatus;
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

import services.ReleaseService;
import services.SprintService;

import dao.PBIDAO;
import dao.ReleaseDAO;
@RunWith(MockitoJUnitRunner.class)
public class ReleaseServiceTest {
	
	@Mock
	ReleaseDAO mockReleaseDao;

	@Mock
	SprintService mockSprintService;
	@Mock
	PBIDAO mockPbiDao;
	
	ReleaseService releaseService;
	
	@Before
	public void setUp() throws Exception {
		releaseService = new ReleaseService();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetReleasesForProject() {
		
		List<Release> releases = new ArrayList<Release>();
		releases.add(new Release());
		releases.add(new Release());
		when(mockReleaseDao.getReleasesForProject((Project)anyObject())).thenReturn(releases);
		ReflectionTestUtils.setField(releaseService, "releaseDao", mockReleaseDao);
		
		List<Release> result = releaseService.getReleasesForProject(new Project());
		
		assertEquals(result, releases);
	}

	@Test
	public void testGetRelease() {
		long id = 123;
		
		Release release = new Release(id);
		when(mockReleaseDao.getRelease(anyLong())).thenReturn(release);
		ReflectionTestUtils.setField(releaseService, "releaseDao", mockReleaseDao);
		
		Release result = releaseService.getRelease(anyLong());
		
		assertEquals(result, release);
	}

	@Test
	public void testSaveRelease() {
		
		final String title2= "AFTERSAVE";
		final List<Release> releases = new ArrayList<Release>();
		Release release = new Release(123);
		release.setName("BEFORESAVE");
		releases.add(release);
		Release releaseNew = new Release();
		releaseNew.setName("UNSAVE");
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Release release2 = releases.get(releases.indexOf((invocation.getArguments()[0])));
				release2.setName(title2);
				return releases.contains(release2);
			}
		}).when(mockReleaseDao).update((Release) anyObject());
		
		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Release release2 = (Release) (invocation.getArguments()[0]);
				release2.setName(title2);
				releases.add(release2);	
				return releases.contains(release2);
			}
		}).when(mockReleaseDao).save((Release) anyObject());
		
		ReflectionTestUtils.setField(releaseService, "releaseDao", mockReleaseDao);
		
		releaseService.saveRelease(release);
		
		assertEquals(release.getName(), title2);
		
		releaseService.saveRelease(releaseNew);
		
		assertTrue(releases.contains(releaseNew));
		assertEquals(releaseNew.getName(), title2);

	}


	@Test
	public void testDeleteReleases() {

		final List<Release> releases = new ArrayList<Release>();
		for (int i = 1; i < 6; i++) {
			releases.add(new Release(i));
		}
		int size  = releases.size();
		long[] ids = { 1, 2, 3 };

		

		Release release = new Release();
		when(mockReleaseDao.getRelease(anyLong())).thenReturn(release);
		

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long idToRemove = (Long) invocation.getArguments()[0];
					releases.remove(new Release(idToRemove));
				return "Removed " + idToRemove;
			}
		}).when(mockReleaseDao).delete(anyLong());
		
		
		
		ReflectionTestUtils.setField(releaseService, "releaseDao", mockReleaseDao);
		releaseService.deleteReleases(ids);

		assertEquals(releases.size(), (size-ids.length));
	
	}
	@Test
	public void testGetReleaseWithDetails()
	{
		long id = 123;
		
		Release release = new Release(id);
		List<ReleaseItem> ris = new ArrayList<ReleaseItem>();
		ReleaseItem ri = new ReleaseItem(new PBI(1L), new ScrumzuUser(1L));
		ris.add(ri);
		release.setReleaseItems(ris);
		when(mockReleaseDao.getReleaseWithDetails(anyLong())).thenReturn(release);
		ReflectionTestUtils.setField(releaseService, "releaseDao", mockReleaseDao);
		
		Release result = releaseService.getReleaseWithDetails(anyLong());
		
		assertEquals(result, release);
	}

	@Test
	public void testLoadRelease()
	{
		long id = 123;
		
		Release release = new Release(id);
		when(mockReleaseDao.loadRelease(anyLong())).thenReturn(release);
		ReflectionTestUtils.setField(releaseService, "releaseDao", mockReleaseDao);
		
		Release result = releaseService.loadRelease(anyLong());
		
		assertEquals(result, release);
	}

	@Test
	public void testGetStoryPointsForRelease()
	{
		Release release = new Release(1L);
		List<ReleaseItem> ris = new ArrayList<ReleaseItem>();
		for(int i=0;i<6;i++){
			ReleaseItem ri = new ReleaseItem(new PBI(1L), new ScrumzuUser(1L));
			ris.add(ri);
		}
		release.setReleaseItems(ris);
		when(mockPbiDao.getStoryPointsForPBI((PBI)anyObject())).thenReturn(100);
		ReflectionTestUtils.setField(releaseService, "pbiDao", mockPbiDao);
		
		assertEquals(ris.size()*100, releaseService.getStoryPointsForRelease(release));
		
	}
	@Test
	public void testGetBurndownForRealease()
	{
		List<Sprint> sprints = new ArrayList<Sprint>();
		for(int i=0;i<6;i++){
			Sprint s= new Sprint(i);
			s.setName("s_"+i);
			s.setSprintStatus(SprintStatus.ENDED);
			s.setDateFrom(new Date(System.currentTimeMillis()));
			s.setDateTo(new Date(System.currentTimeMillis()));
			sprints.add(s);
		}
		
		when(mockSprintService.getSprintsForRelease((Release)anyObject())).thenReturn(sprints);
		when(mockSprintService.getStoryPointsForSprintAndRelease((Sprint)anyObject(), (Release)anyObject(), anyBoolean())).thenReturn(100L);
		ReflectionTestUtils.setField(releaseService, "sprintService", mockSprintService);
		
		Map<String, Long> res = releaseService.getBurndownForRealease(new Release());
		for(String key: res.keySet()){
			assertEquals(new Long(-100), res.get(key));
		}
		
	}


}
