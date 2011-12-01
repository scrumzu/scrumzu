package model.projects;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import services.ProjectService;

public class ProjectEditor extends PropertyEditorSupport {
	ProjectService projectService;
	Logger l = Logger.getLogger(getClass());

	public ProjectEditor(ProjectService projectService){
		this.projectService = projectService;
	}


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		l.info("PROJECT setting as text: " + text);
		if(text == null  || "".equals(text) || "null".equals(text)){
			setValue(null);
		}
		else{
			setValue(projectService.getProject(Long.parseLong(text)));
		}
	}

	@Override
	public String getAsText() {
		Project s = (Project) getValue();
		if (s == null) {
			return null;
		} else {
			l.info("PROJECT returning as text:" + s.getIdProject());
			return s.getIdProject()+"";
		}
	}


}
