package dao;

import java.util.List;

import model.projects.Project;
import model.releases.Release;
import model.sprint.Sprint;

public interface SprintDAO {

	public List<Sprint> getSprints();
	public Sprint getSprint(long sprintId);
	public Sprint loadSprint(long sprintId);
	public void delete(Sprint sprint);
	public void save(Sprint sprint);
	public void update(Sprint sprint);
	public void delete(long sprintId);
	public List<Sprint> getSprintsForProject(Project project);
	public void flushSession();
	public List<Sprint> getSprintsForRelease(Release release);
	public long getDoneStoryPoints(Sprint sprint, Release release, boolean dateTo);
	public long getMaxStoryPoints(Sprint sprint, Release release, boolean dateTo);
	public List<Sprint> getActiveSprintsForProject(Project project);
}
