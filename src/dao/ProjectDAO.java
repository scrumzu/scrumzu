package dao;

import java.util.List;
import java.util.Set;

import model.projects.Attribute;
import model.projects.Project;

public interface ProjectDAO {

	public List<Project> getProjects();
	public Project getProject(long projectId);
	public Project loadProject(long projectId);
	public Project getProject(String alias);
	public void delete(String alias);
	public void delete(Project project);
	public void delete(long id);
	public void save(Project project);
	public void update(Project project);
	public void flushSession();
	public Set<Attribute> getProjectAttributes(String projectAlias);
	public boolean isProjectPresent(String projectAlias);

}
