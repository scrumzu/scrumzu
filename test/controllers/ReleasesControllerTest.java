package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.pbis.PBI;
import model.projects.Project;
import model.releases.Release;
import model.releases.ReleaseItem;
import model.teams.Team;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import services.PBIService;
import services.ProjectService;
import services.ReleaseService;
import services.SprintService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReleasesControllerTest {
	@Mock
	private SprintService sprintService;
	@Mock
	private ProjectService projectService;

	@Mock
	private ReleaseService releaseService;
	@Mock
	private PBIService pbiService;
	private ReleasesController controller;
	Model model;
	@Mock
	BindingResult bindingResult;
	String projectAlias;
	List<Project> projects;
	List<Team> teams;

	@Before
	public void setUp() throws Exception {
		controller = new ReleasesController();
		controller.sprintService = sprintService;
		model = new ExtendedModelMap();
		MockitoAnnotations.initMocks(this);
		projectAlias = "PA";
		projects = new ArrayList<Project>();
		teams = new ArrayList<Team>();
		model.addAttribute("teams", teams);
		model.addAttribute("projects", projects);
		model.addAttribute("chosenProjectAlias", projectAlias);
	}

	@Test
	public void testGetReleasesListForProject() {
		List<Release> releases = new ArrayList<Release>();
		releases.add(new Release());

		ReflectionTestUtils.setField(controller, "releaseService",
				releaseService);
		ReflectionTestUtils.setField(controller, "projectService",
				projectService);

		when(releaseService.getReleasesForProject((Project) anyObject())).thenReturn(releases);
		controller.getReleasesListForProject("TEST", model);
	}

	@Test
	public void testGetNewReleaseForm() {
		Release release = new Release();
		Project project = mock(Project.class);
		List<PBI> pbis = new ArrayList<PBI>();

		when(projectService.getProject(anyString())).thenReturn(project);
		ReflectionTestUtils.setField(controller, "projectService",
				projectService);

		when(releaseService.getRelease(anyLong())).thenReturn(release);
		ReflectionTestUtils.setField(controller, "releaseService",
				releaseService);

		when(pbiService.getPBIsForProject((Project)anyObject())).thenReturn(pbis);
		ReflectionTestUtils.setField(controller, "pbiService",
				pbiService);

		ModelAndView page = controller.getNewReleaseForm(projectAlias, model);

		Map<String, Object> modelMap = model.asMap();
		assertEquals("add", modelMap.get("op"));
		assertEquals(release, modelMap.get("release"));
		assertEquals("releaseAddForm", page.getViewName());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));

	}

	@Test
	public void testGetEditReleaseForm()
	{
		List<ReleaseItem> ris = new ArrayList<ReleaseItem>();
		PBI pbi = new PBI();
		List<PBI> pbis = new ArrayList<PBI>();
		pbis.add(pbi);

		Project project = mock(Project.class);

		ReleaseItem ri = mock(ReleaseItem.class);
		ris.add(ri);

		Release release = mock(Release.class);

		when(projectService.getProject(anyString())).thenReturn(project);
		ReflectionTestUtils.setField(controller, "projectService",
				projectService);

		when(releaseService.getReleaseWithDetails(anyLong())).thenReturn(release);
		ReflectionTestUtils.setField(controller, "releaseService",
				releaseService);

		when(pbiService.getPBIsForProject((Project)anyObject())).thenReturn(pbis);
		ReflectionTestUtils.setField(controller, "pbiService",
				pbiService);

		release.setReleaseItems(ris);
		release.setPbis(pbis);
		when(release.getPbis()).thenReturn(pbis);
		when(ri.getPbi()).thenReturn(pbi);

		ModelAndView page = controller.getEditReleaseForm(projectAlias, model, anyLong());
		Map<String, Object> modelMap = model.asMap();
		assertEquals("edit", modelMap.get("op"));
		assertEquals(release, modelMap.get("release"));
		assertEquals(release.getPbis(), pbis);
		assertEquals("releaseEditForm", page.getViewName());
		assertEquals(projectAlias, modelMap.get("chosenProjectAlias"));

	}
}
