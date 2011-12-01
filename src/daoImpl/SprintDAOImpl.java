package daoImpl;

import java.math.BigDecimal;
import java.util.List;

import model.projects.Project;
import model.releases.Release;
import model.sprint.Sprint;
import model.sprint.SprintStatus;
import model.workItems.Status;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import dao.SprintDAO;

@Component("sprintDao")
public class SprintDAOImpl extends DAOImpl implements SprintDAO  {


	@SuppressWarnings("unchecked")
	public List<Sprint> getSprints() {
		return currentSession().createCriteria(Sprint.class).list();
	}

	public Sprint getSprint(long sprintId) {
		return (Sprint) currentSession().get(Sprint.class, sprintId);
	}


	public Sprint loadSprint(long sprintId) {
		return (Sprint) currentSession().load(Sprint.class, sprintId);
	}

	public void delete(Sprint sprint) {
		currentSession().delete(sprint);

	}

	public void save(Sprint sprint) {
		currentSession().save(sprint);

	}

	public void update(Sprint sprint) {
		currentSession().update(sprint);

	}

	public void delete(long idSprint) {
		currentSession().delete(getSprint(idSprint));

	}

	@SuppressWarnings("unchecked")
	public List<Sprint> getSprintsForProject(Project project) {
		Criteria criteria = currentSession()
				.createCriteria(Sprint.class)
				.add(Restrictions.eq("project", project));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sprint> getSprintsForRelease(Release release) {
		Criteria criteria = currentSession()
				.createCriteria(Sprint.class)
				.add(Restrictions.eq("project", release.getProject()))
				.add(Restrictions.ge("dateFrom",release.getDateFrom()))
				.add(Restrictions.le("dateTo", release.getDateTo()))
				.addOrder(Order.asc("dateFrom"));
		return criteria.list();
	}

	@Override
	public long getDoneStoryPoints(Sprint sprint, Release release, boolean dateTo) {
		Query query = currentSession().createSQLQuery("SELECT SUM(workItems.storyPoints) FROM PBIs pbis " +
				" INNER JOIN (SELECT  idPBI, MAX(date) MaxDate FROM WorkItems WHERE date < :sprintDate GROUP BY idPBI ) MaxDates ON pbis.idPBI = MaxDates.idPBI " +
				" INNER JOIN WorkItems workItems ON MaxDates.idPBI = workItems.idPBI AND MaxDates.MaxDate = workItems.date " +
				" INNER JOIN ReleaseItems relItems ON pbis.idPBI= relItems.idPBI"+
				" WHERE workItems.status = :nrStatus AND idRelease= :releaseId").
				setParameter("sprintDate", dateTo?sprint.getDateTo():sprint.getDateFrom()).
				setParameter("releaseId", release.getIdRelease()).
				setParameter("nrStatus", Status.DONE.ordinal());
		BigDecimal result = (BigDecimal) query.uniqueResult();
		return result != null ?  result.longValue(): 0;
	}

	@Override
	public long getMaxStoryPoints(Sprint sprint, Release release, boolean dateTo) {
		Query query = currentSession().createSQLQuery("SELECT SUM(workItems.storyPoints) FROM PBIs pbis " +
				" INNER JOIN (SELECT  idPBI, MAX(date) MaxDate FROM WorkItems WHERE date < :sprintDate GROUP BY idPBI ) MaxDates ON pbis.idPBI = MaxDates.idPBI " +
				" INNER JOIN WorkItems workItems ON MaxDates.idPBI = workItems.idPBI AND MaxDates.MaxDate = workItems.date " +
				" INNER JOIN ReleaseItems relItems ON pbis.idPBI= relItems.idPBI"+
				" WHERE relItems.idRelease= :releaseId").
				setParameter("sprintDate", dateTo?sprint.getDateTo():sprint.getDateFrom()).
				setParameter("releaseId", release.getIdRelease());
		BigDecimal result = (BigDecimal) query.uniqueResult();
		return result != null ?  result.longValue(): 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sprint> getActiveSprintsForProject(Project project) {
		Criteria criteria = currentSession()
				.createCriteria(Sprint.class)
				.add(Restrictions.eq("project", project)).add(Restrictions.ne("sprintStatus", SprintStatus.ENDED));
		return criteria.list();
	}


}
