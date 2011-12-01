package dao;

import java.util.List;

import model.projects.Project;
import model.releases.Release;


public interface ReleaseDAO {
	public List<Release> getReleasesForProject(Project project);
	public Release getRelease(long releaseId);
	public Release getReleaseWithDetails(long releaseId);
	public Release loadRelease(long releaseId);
	public void save(Release release);
	public void delete(long releaseId);
	public void delete(Release release);
	public void update(Release release);
	public void flushSession();

}
