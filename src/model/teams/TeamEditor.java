package model.teams;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import services.TeamService;

public class TeamEditor extends PropertyEditorSupport {
	TeamService teamService;
	Logger l = Logger.getLogger(getClass());

	public TeamEditor(TeamService teamService){
		this.teamService=teamService;
	}


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		l.info("TEAM setting as text: " + text);
		if(text == null  || "".equals(text) || "null".equals(text)){
			setValue(null);
		}
		else{
			setValue(teamService.getTeam(Long.parseLong(text)));
		}
	}

	@Override
	public String getAsText() {
		Team t = (Team) getValue();
		if (t == null) {
			return null;
		} else {
			l.info("TEAM returning as text:" + t.getIdTeam());
			return t.getIdTeam()+"";
		}
	}
}
