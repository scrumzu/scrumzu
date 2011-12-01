package daoImpl;

import java.util.List;
import java.util.Set;


import model.projects.Attribute;
import model.projects.Project;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import dao.ProjectDAO;


@Repository("projectDao")
public class ProjectDAOImpl extends DAOImpl implements ProjectDAO {

	@SuppressWarnings("unchecked")
	public List<Project> getProjects() {
		return currentSession().createCriteria(Project.class).list();
	}

	public Project getProject(long projectId) {
		return (Project) currentSession().get(Project.class, projectId);
	}

	public Project loadProject(long projectId) {
		return (Project) currentSession().load(Project.class, projectId);
	}

	public void delete(Project project) {
		currentSession().delete(project);
	}

	public void save(Project project) {
		currentSession().save(project);

	}

	public Project getProject(String alias) {
		Criterion criterion = Restrictions.eq("alias", alias);
		return (Project) currentSession().createCriteria(Project.class).add(criterion).uniqueResult();
	}
	
	public Set<Attribute> getProjectAttributes(String alias) {
		Criteria criteria = currentSession().createCriteria(Project.class)
				.add(Restrictions.eq("alias", alias))
				.setFetchMode("attributes", FetchMode.JOIN);
		return ((Project) criteria.uniqueResult()).getAttributes();
	}

	public void update(Project project) {
		currentSession().update(project);
	}

	public void delete(long id) {
		currentSession().delete(getProject(id));
	}

	public void delete(String alias){
		currentSession().delete(getProject(alias));
	}

	public boolean isProjectPresent(String projectAlias) {
		Criteria criteria = currentSession().createCriteria(Project.class)
				.add(Restrictions.eq("alias", projectAlias));
		return criteria.uniqueResult() != null;
	}


}
