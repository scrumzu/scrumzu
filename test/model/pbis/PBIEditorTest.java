package model.pbis;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import services.PBIService;
@RunWith(MockitoJUnitRunner.class)
public class PBIEditorTest {


	private PBIEditor pbiEditor;
	@Mock
	PBIService mockPBIService;
	
	@Before
	public void setUp() throws Exception {
		pbiEditor = new PBIEditor(mockPBIService);
	}

	@Test
	public void testSetAsTextString() {
		long id = 23;
		PBI pbi = new PBI(id);
		when(mockPBIService.getPBI(anyLong())).thenReturn(pbi);
		
		String text = ""+id;
		pbiEditor.setAsText(text);
		assertEquals(pbi,pbiEditor.getValue());
		pbiEditor.setAsText(null);
		assertEquals(null,pbiEditor.getValue());
		pbiEditor.setAsText("");
		assertEquals(null,pbiEditor.getValue());
		pbiEditor.setAsText("null");
		assertEquals(null,pbiEditor.getValue());
	}
	
	@Test
	public void testGetAsText()
	{
		long id = 23;
		PBI pbi = new PBI(id);
		pbiEditor.setValue(pbi);
		
		String idPBI = pbiEditor.getAsText();
		assertEquals(idPBI,""+id);
		
		
		pbiEditor.setValue(null);

		idPBI = pbiEditor.getAsText();
		assertEquals(idPBI, null);
	}

}
