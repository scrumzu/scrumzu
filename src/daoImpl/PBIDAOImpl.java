package daoImpl;

import java.math.BigInteger;
import java.util.List;

import model.pbis.PBI;
import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import services.WorkItemService;

import dao.PBIDAO;

@Repository("PBIDAO")
public class PBIDAOImpl extends DAOImpl implements PBIDAO{

	@Autowired
	WorkItemService workItemService;

	public PBI getPBI(long PBIId) {
		return (PBI) currentSession().get(PBI.class, PBIId);
	}

	public void save(PBI pbi) {
		currentSession().save(pbi);
	}

	public void delete(PBI pbi) {
		currentSession().delete(pbi);
	}

	public PBI loadPBI(long PBIId) {
		return (PBI) currentSession().load(PBI.class, PBIId);
	}

	public void update(PBI pbi) {
		currentSession().update(pbi);
	}

	public void delete(long PBIId) {
		currentSession().delete(loadPBI(PBIId));

	}

	@SuppressWarnings("unchecked")
	public List<PBI> getPBIsForTeam(Team team) {
		return currentSession().createCriteria(PBI.class).createCriteria("workItems").add(Restrictions.eq("team", team)).list();

	}

	@SuppressWarnings("unchecked")
	public List<PBI> getPBIsForSprint(Sprint sprint) {
		Criteria criteria = currentSession().createCriteria(PBI.class)
				.createCriteria("workItems")
				.add(Restrictions.eq("sprint", sprint));
		return criteria.list();
	}

	@Override
	public long countPBIsForSprint(Sprint sprint) {
		Query query = currentSession().createSQLQuery("SELECT count(*) FROM PBIs pbis " +
				"INNER JOIN (SELECT  idPBI, MAX(date) MaxDate FROM WorkItems GROUP BY idPBI) MaxDates ON pbis.idPBI = MaxDates.idPBI " +
				"INNER JOIN WorkItems workItems ON MaxDates.idPBI = workItems.idPBI AND MaxDates.MaxDate = workItems.date " +
				"WHERE idSprint = :id").setParameter("id", sprint.getIdSprint()) ;
		return ((BigInteger)query.uniqueResult()).longValue();	
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<PBI> getPBIsForProject(Project project) {
		Criteria criteria = currentSession().createCriteria(PBI.class)
				.add(Restrictions.eq("project", project))
				.setFetchMode("doubleAttributes", FetchMode.SELECT)
				.setFetchMode("stringAttributes", FetchMode.SELECT);
		return criteria.list();
	}

	public PBI getPBIWithAttributes(long PBIId) {
		Criteria criteria = currentSession().createCriteria(PBI.class)
				.add(Restrictions.eq("idPBI", PBIId))
				.setFetchMode("doubleAttributes", FetchMode.JOIN)
				.setFetchMode("stringAttributes", FetchMode.JOIN);
		return (PBI) criteria.uniqueResult();

	}
	
	@Override
	public int getStoryPointsForPBI(PBI pbi) {
		Query query = currentSession().createSQLQuery("SELECT workItems.storyPoints FROM PBIs pbis " +
				"INNER JOIN (SELECT  idPBI, MAX(date) MaxDate FROM WorkItems GROUP BY idPBI) MaxDates ON pbis.idPBI = MaxDates.idPBI " +
				"INNER JOIN WorkItems workItems ON MaxDates.idPBI = workItems.idPBI AND MaxDates.MaxDate = workItems.date " +
				"WHERE workItems.idPBI = :id").setParameter("id", pbi.getIdPBI()) ;
		Integer returnInt = (Integer) query.uniqueResult();
		return returnInt != null ?  returnInt: 0;	
	}


}
