package model.teams;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import services.TeamService;
@RunWith(MockitoJUnitRunner.class)
public class TeamEditorTest {


	private TeamEditor teamEditor;
	@Mock
	TeamService mockTeamService;
	
	@Before
	public void setUp() throws Exception {
		teamEditor = new TeamEditor(mockTeamService);
	}

	@Test
	public void testSetAsTextString() {
		long id = 23;
		Team team = new Team(id);
		when(mockTeamService.getTeam(anyLong())).thenReturn(team);
		
		String text = ""+id;
		teamEditor.setAsText(text);
		assertEquals(team,teamEditor.getValue());
		teamEditor.setAsText(null);
		assertEquals(null,teamEditor.getValue());
		teamEditor.setAsText("");
		assertEquals(null,teamEditor.getValue());
		teamEditor.setAsText("null");
		assertEquals(null,teamEditor.getValue());
	}
	
	@Test
	public void testGetAsText()
	{
		long id = 23;
		Team team = new Team(id);
		teamEditor.setValue(team);
		
		String idTeam = teamEditor.getAsText();
		assertEquals(idTeam,""+id);
		
		
		teamEditor.setValue(null);

		idTeam = teamEditor.getAsText();
		assertEquals(idTeam, null);
	}

}
