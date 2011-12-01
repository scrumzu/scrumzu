package daoImpl;

import java.util.List;

import model.projects.Project;
import model.releases.Release;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import dao.ReleaseDAO;


@Component("releaseDao")
public class ReleaseDAOImpl extends DAOImpl implements ReleaseDAO{

	@Override
	public Release getRelease(long releaseId) {
		return (Release) currentSession().get(Release.class, releaseId);
	}

	@Override
	public Release loadRelease(long releaseId) {
		return (Release) currentSession().load(Release.class, releaseId);
	}

	@Override
	public void save(Release release) {

		currentSession().save(release);
		currentSession().flush();

	}

	@Override
	public void delete(Release release) {
		currentSession().delete(release);
		currentSession().flush();
	}

	@Override
	public void delete(long releaseId) {
		currentSession().delete(getRelease(releaseId));
		currentSession().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Release> getReleasesForProject(Project project) {
		Criteria criteria = currentSession().createCriteria(Release.class)
				.add(Restrictions.eq("project", project));
		return criteria.list();
	}

	@Override
	public void update(Release release) {
		currentSession().update(release);
		currentSession().flush();
	}

	@Override
	public Release getReleaseWithDetails(long releaseId) {
		Criteria criteria = currentSession().createCriteria(Release.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq("idRelease", releaseId))
				.setFetchMode("releaseItems", FetchMode.JOIN);
		return (Release) criteria.uniqueResult();
	}



}
