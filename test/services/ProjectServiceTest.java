package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.projects.Attribute;
import model.projects.Project;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import services.ProjectService;

import dao.ProjectDAO;

public class ProjectServiceTest {

	@Mock
	ProjectDAO mockProjectDao;

	ProjectService projectService;

	@Before
	public void setUp() throws Exception {
		projectService = new ProjectService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllProjects() {

		List<Project> projects = new ArrayList<Project>();
		projects.add(new Project());
		projects.add(new Project());
		when(mockProjectDao.getProjects()).thenReturn(projects);
		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);

		List<Project> result = projectService.getProjects();

		assertEquals(result, projects);
	}

	@Test
	public void testGetProjectById() {
		long id = 123;

		Project project = new Project(id);
		when(mockProjectDao.getProject(anyLong())).thenReturn(project);
		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);

		Project result = projectService.getProject(anyLong());

		assertEquals(result, project);
	}

	@Test
	public void testSaveProject() {

		final String title2= "AFTERSAVE";
		final List<Project> projects = new ArrayList<Project>();
		Project project = new Project(123);
		project.setName("BEFORESAVE");
		projects.add(project);
		Project projectNew = new Project();
		projectNew.setName("UNSAVE");

		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Project project2 = projects.get(projects.indexOf((invocation.getArguments()[0])));
				project2.setName(title2);
				return projects.contains(project2);
			}
		}).when(mockProjectDao).update((Project) anyObject());

		doAnswer(new Answer<Boolean>() {
			public Boolean answer(InvocationOnMock invocation) {
				Project project2 = (Project) (invocation.getArguments()[0]);
				project2.setName(title2);
				projects.add(project2);
				return projects.contains(project2);
			}
		}).when(mockProjectDao).save((Project) anyObject());

		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);

		projectService.saveProject(project);

		assertEquals(project.getName(), title2);

		projectService.saveProject(projectNew);

		assertTrue(projects.contains(projectNew));
		assertEquals(projectNew.getName(), title2);

	}


	@Test
	public void testDeleteProjects() {


		final List<Project> projects = new ArrayList<Project>();
		for (int i = 1; i < 6; i++) {
			projects.add(new Project(i));
		}
		int size  = projects.size();
		long[] ids = { 1, 2, 3 };

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				long idToRemove = (Long) invocation.getArguments()[0];
				projects.remove(new Project(idToRemove));
				return "Removed " + idToRemove;
			}
		}).when(mockProjectDao).delete(anyLong());

		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);
		projectService.deleteProjects(ids);

		assertEquals(projects.size(), (size-ids.length));


	}
	@Test
	public void testLoadProjectById()
	{
		long id = 3;
		Project project = new Project(id);
		when(mockProjectDao.loadProject(anyLong())).thenReturn(project);
		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);

		Project project2 = projectService.loadProject(id);
		assertEquals(project, project2);
	}
	@Test
	public void testGetProjectByAlias()
	{
		String alias = "TEST";
		long id = 3;
		Project project = new Project(id);
		project.setAlias(alias);
		when(mockProjectDao.getProject(anyString())).thenReturn(project);

		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);

		Project project2 = projectService.getProject(alias);
		assertEquals(project, project2);
	}
	
	@Test
	public void testGetProjectAttributes() {
		String alias = "TEST";
		Set<Attribute> mockedSet = new HashSet<Attribute>();
		when(mockProjectDao.getProjectAttributes(alias)).thenReturn(mockedSet);
		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);
		assertEquals(mockedSet, projectService.getProjectAttributes(alias));
	}
	@Test
	public void testIsProjectPresent()
	{
		when(mockProjectDao.isProjectPresent(anyString())).thenReturn(false);
		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);
		assertFalse(projectService.isProjectPresent(""));
	}
	@Test
	public void testDeleteProject()
	{
	

		final List<Project> projects = new ArrayList<Project>();
		for (int i = 1; i < 6; i++) {
			projects.add(new Project(i));
		}
		int size  = projects.size();

		doAnswer(new Answer<String>() {
			public String answer(InvocationOnMock invocation) {
				String aliasToRemove = (String) invocation.getArguments()[0];
				projects.remove(new Project(4));
				return "Removed " + aliasToRemove;
			}
		}).when(mockProjectDao).delete(anyString());

		ReflectionTestUtils.setField(projectService, "projectDao", mockProjectDao);
		projectService.deleteProject("asd");

		assertEquals(projects.size(), (size-1));
	}

}
