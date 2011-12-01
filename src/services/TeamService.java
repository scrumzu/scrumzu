package services;

import java.util.List;

import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;
import model.users.ScrumzuUser;
import model.workItems.WorkItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.TeamDAO;


@Service("teamService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class TeamService {

	@Autowired
	private TeamDAO teamDao;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Team> getAllTeams() {
		return teamDao.getTeams();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Team getTeam(Long id) {
		return teamDao.getTeam(id);
	}

	public void saveTeam(Team team) {
		if (team.getIdTeam() == null) {
			teamDao.save(team);
		}
		else {
			teamDao.update(team);
		}
	}


	public void deleteTeams(long[] ids) {
		for (int i =0; i < ids.length; i++) {
			nullAssignedWorkItems(ids[i]);
			teamDao.delete(ids[i]);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Team> getTeamsForProject(Project project) {
		return teamDao.getTeamsForProject(project);
	}

	public void nullAssignedWorkItems(long idTeam) {
		Team team = teamDao.getTeam(idTeam);
		List<WorkItem> workItems = team.getWorkItems();
		for (WorkItem w: workItems ) {
			w.setTeam(null);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Team> getTeamsForSprint(Sprint sprint) {
		return teamDao.getTeamsForSprint(sprint);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long countTeamsForSprint(Sprint sprint) {
		return teamDao.countTeamsForSprint(sprint);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Team> getTeamsForUser(ScrumzuUser user) {
		return teamDao.getTeamsForUser(user);
	}

	public boolean isTeamPresent(String alias, Long id) {
		Long idDb = teamDao.getTeamId(alias);
		return idDb == null ? false : !idDb.equals(id);
	}
}
