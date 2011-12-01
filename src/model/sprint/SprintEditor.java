package model.sprint;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import services.SprintService;

public class SprintEditor extends PropertyEditorSupport {
	SprintService sprintService;
	Logger l = Logger.getLogger(getClass());

	public SprintEditor(SprintService sprintService){
		this.sprintService = sprintService;
	}


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		l.info("SPRINT setting as text: " + text);
		if(text == null  || "".equals(text) || "null".equals(text)){
			setValue(null);
		}
		else{
			setValue(sprintService.getSprint(Long.parseLong(text)));
		}
	}

	@Override
	public String getAsText() {
		Sprint s = (Sprint) getValue();
		if (s == null) {
			return null;
		} else {
			l.info("SPRINT returning as text: " + s.getIdSprint());
			return s.getIdSprint()+"";
		}
	}


}
