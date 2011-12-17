package services;

import java.util.List;
import java.util.Set;


import model.projects.Attribute;
import model.projects.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.ProjectDAO;

import exceptions.ProjectNotFoundException;


@Service("projectService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ProjectService {

	@Autowired
	private ProjectDAO projectDao;

	public List<Project> getProjects() {
		return projectDao.getProjects();
	}

	public Project getProject(long projectId) {
		return projectDao.getProject(projectId);
	}

	public Project loadProject(long projectId) {
		return projectDao.loadProject(projectId);
	}


	public Project getProject(String alias) {
		Project project = projectDao.getProject(alias);
		if(project == null){
			throw new ProjectNotFoundException(alias);
		}
		return project;
	}

	public Set<Attribute> getProjectAttributes(String alias) {
		return projectDao.getProjectAttributes(alias);
	}

	public boolean isProjectPresent(String alias, Long idProject) {
		Long idDb = null;
		Project project = projectDao.getProject(alias);
		if(project != null){
			idDb = project.getIdProject();
		}
		return idDb ==  null? false :  !idDb.equals(idProject);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void saveProject(Project project) {
		if (project.getIdProject()==null) {
			projectDao.save(project);
		} else {
			projectDao.update(project);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteProjects(long[] ids) {
		for (int i =0; i < ids.length; i++) {
			projectDao.delete(ids[i]);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteProject(String alias){
		projectDao.delete(alias);
	}

}
