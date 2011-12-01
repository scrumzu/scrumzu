package dao;

import java.util.List;

import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;
import model.users.ScrumzuUser;

public interface TeamDAO {

	public List<Team> getTeams();
	public Team getTeam(long teamId);
	public Team loadTeam(long teamId);
	public void save(Team team);
	public void update(Team team);
	public void delete(Team team);
	public void delete(long idTeam);
	public List<Team> getTeamsForProject(Project project);
	public void flushSession();
	public List<Team> getTeamsForSprint(Sprint sprint);
	public long countTeamsForSprint(Sprint sprint);
	public Long getTeamId(String alias);
	public List<Team> getTeamsForUser(ScrumzuUser user);


}
