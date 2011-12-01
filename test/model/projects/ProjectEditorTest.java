package model.projects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import services.ProjectService;

@RunWith(MockitoJUnitRunner.class)
public class ProjectEditorTest {


	private ProjectEditor projectEditor;
	@Mock
	ProjectService mockProjectService;
	
	@Before
	public void setUp() throws Exception {
		projectEditor = new ProjectEditor(mockProjectService);

	}

	@Test
	public void testSetAsTextString() {
		long id = 23;
		Project project = new Project(id);
		when(mockProjectService.getProject(anyLong())).thenReturn(project);
		
		String text = ""+id;
		projectEditor.setAsText(text);
		assertEquals(project,projectEditor.getValue());
		projectEditor.setAsText(null);
		assertEquals(null,projectEditor.getValue());
		projectEditor.setAsText("");
		assertEquals(null,projectEditor.getValue());
		projectEditor.setAsText("null");
		assertEquals(null,projectEditor.getValue());
	}
	@Test
	public void testGetAsText()
	{
		long id = 23;
		Project project = new Project(id);
		projectEditor.setValue(project);
		
		String idProject = projectEditor.getAsText();
		assertEquals(idProject,""+id);
		
		
		projectEditor.setValue(null);

		idProject = projectEditor.getAsText();
		assertEquals(idProject, null);
	}

}
