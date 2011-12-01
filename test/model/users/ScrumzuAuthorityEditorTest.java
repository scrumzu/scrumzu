package model.users;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import model.users.ScrumzuAuthority;
import model.users.ScrumzuAuthorityEditor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import services.ScrumzuAuthorityService;
@RunWith(MockitoJUnitRunner.class)
public class ScrumzuAuthorityEditorTest {


	private ScrumzuAuthorityEditor scrumzuAuthorityEditor;
	@Mock
	ScrumzuAuthorityService mockAuthorityService;
	
	@Before
	public void setUp() throws Exception {
		scrumzuAuthorityEditor = new ScrumzuAuthorityEditor(mockAuthorityService);

	}

	@Test
	public void testSetAsTextString() {
		ScrumzuAuthority sa= new ScrumzuAuthority();
		sa.setAuthority("AUTHORITY");
		when(mockAuthorityService.getAuthority(anyLong())).thenReturn(sa);
		long id = 23;
		String text = ""+id;
		scrumzuAuthorityEditor.setAsText(text);

		assertEquals(sa,scrumzuAuthorityEditor.getValue());

		scrumzuAuthorityEditor.setAsText("");
		assertEquals(null,scrumzuAuthorityEditor.getValue());
	}

}
