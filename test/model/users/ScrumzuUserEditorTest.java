package model.users;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import services.ScrumzuUserDetailsService;
@RunWith(MockitoJUnitRunner.class)
public class ScrumzuUserEditorTest {


	private ScrumzuUserEditor ScrumzuUserEditor;
	@Mock
	ScrumzuUserDetailsService mockUserService;
	
	@Before
	public void setUp() throws Exception {
		ScrumzuUserEditor = new ScrumzuUserEditor(mockUserService);
	}

	@Test
	public void testSetAsTextString() {
		long id = 23;
		ScrumzuUser ScrumzuUser = new ScrumzuUser(id);
		when(mockUserService.getUser(anyLong())).thenReturn(ScrumzuUser);
		
		String text = ""+id;
		ScrumzuUserEditor.setAsText(text);
		assertEquals(ScrumzuUser,ScrumzuUserEditor.getValue());
		ScrumzuUserEditor.setAsText(null);
		assertEquals(null,ScrumzuUserEditor.getValue());
		ScrumzuUserEditor.setAsText("");
		assertEquals(null,ScrumzuUserEditor.getValue());
		ScrumzuUserEditor.setAsText("null");
		assertEquals(null,ScrumzuUserEditor.getValue());
	}
	
	@Test
	public void testGetAsText()
	{
		long id = 23;
		ScrumzuUser ScrumzuUser = new ScrumzuUser(id);
		ScrumzuUserEditor.setValue(ScrumzuUser);
		
		String idScrumzuUser = ScrumzuUserEditor.getAsText();
		assertEquals(idScrumzuUser,""+id);
		
		
		ScrumzuUserEditor.setValue(null);

		idScrumzuUser = ScrumzuUserEditor.getAsText();
		assertEquals(idScrumzuUser, null);
	}

}
