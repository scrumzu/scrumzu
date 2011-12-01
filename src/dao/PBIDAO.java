package dao;

import java.util.List;

import model.pbis.PBI;
import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;

public interface PBIDAO {
	
	public PBI getPBI(long PBIId);
	public void save(PBI pbi);
	public void update(PBI pbi);
	public void delete(PBI pbi);
	public void delete(long PBIId);
	public PBI loadPBI(long PBIId);
	public List<PBI> getPBIsForTeam(Team team);
	public void flushSession();
	public List<PBI> getPBIsForSprint(Sprint sprint);
	public List<PBI> getPBIsForProject(Project project);
	public long countPBIsForSprint(Sprint sprint);
	public PBI getPBIWithAttributes(long PBIId);
	public int getStoryPointsForPBI(PBI pbi);

 
}
