package model.sprint;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import services.SprintService;
@RunWith(MockitoJUnitRunner.class)
public class SprintEditorTest {


	private SprintEditor sprintEditor;
	@Mock
	SprintService mockSprintService;
	
	@Before
	public void setUp() throws Exception {
		sprintEditor = new SprintEditor(mockSprintService);
	}

	@Test
	public void testSetAsTextString() {
		long id = 23;
		Sprint sprint = new Sprint(id);
		when(mockSprintService.getSprint(anyLong())).thenReturn(sprint);
		
		String text = ""+id;
		sprintEditor.setAsText(text);
		assertEquals(sprint,sprintEditor.getValue());
		sprintEditor.setAsText(null);
		assertEquals(null,sprintEditor.getValue());
		sprintEditor.setAsText("");
		assertEquals(null,sprintEditor.getValue());
		sprintEditor.setAsText("null");
		assertEquals(null,sprintEditor.getValue());
	}
	
	@Test
	public void testGetAsText()
	{
		long id = 23;
		Sprint sprint = new Sprint(id);
		sprintEditor.setValue(sprint);
		
		String idSprint = sprintEditor.getAsText();
		assertEquals(idSprint,""+id);
		
		sprintEditor.setValue(null);

		idSprint = sprintEditor.getAsText();
		assertEquals(idSprint, null);
	}

}
