package daoImpl;

import java.math.BigInteger;
import java.util.List;

import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;
import model.users.ScrumzuUser;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import dao.TeamDAO;

@Repository("TeamDAO")
public class TeamDAOImpl  extends DAOImpl implements TeamDAO{


	@SuppressWarnings("unchecked")
	public List<Team> getTeams() {
		return currentSession().createCriteria(Team.class).list();
	}

	public Team getTeam(long teamId) {
		return (Team) currentSession().get(Team.class, teamId);
	}

	public Team loadTeam(long teamId) {
		return (Team) currentSession().load(Team.class, teamId);
	}

	public void save(Team team) {
		currentSession().save(team);
	}

	public void delete(Team team) {
		currentSession().delete(team);
	}

	public void update(Team team) {
		currentSession().update(team);
	}

	public void delete(long teamId) {
		currentSession().delete(loadTeam(teamId));

	}

	@SuppressWarnings("unchecked")
	public List<Team> getTeamsForProject(Project project) {
		Criteria criteria = currentSession().createCriteria(Team.class)
				.add( Restrictions.eq("project",project));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Team> getTeamsForSprint(Sprint sprint) {
		Criteria criteria = currentSession().createCriteria(Team.class)
				.createCriteria("workItems")
				.add(Restrictions.eq("sprint", sprint));
		return criteria.list();
	}


	@Override
	public long countTeamsForSprint(Sprint sprint) {
		Query query = currentSession().createSQLQuery("SELECT COUNT(DISTINCT workItems.idTeam) FROM PBIs pbis " +
				"INNER JOIN (SELECT  idPBI, MAX(date) MaxDate FROM WorkItems GROUP BY idPBI) MaxDates ON pbis.idPBI = MaxDates.idPBI " +
				"INNER JOIN WorkItems workItems ON MaxDates.idPBI = workItems.idPBI AND MaxDates.MaxDate = workItems.date " +
				"WHERE idSprint = :id").setParameter("id", sprint.getIdSprint()) ;
		return ((BigInteger)query.uniqueResult()).longValue();	
	}

	public Long getTeamId(String teamAlias) {
		Criteria criteria = currentSession().createCriteria(Team.class)
				.add(Restrictions.eq("alias", teamAlias));
		return criteria.uniqueResult() != null ? ((Team)criteria.uniqueResult()).getIdTeam() : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getTeamsForUser(ScrumzuUser user) {
		Criteria criteria = currentSession().createCriteria(Team.class)
				.add(Restrictions.eq("user", user));
		return criteria.list();
	}


}
