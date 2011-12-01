package services;

import java.util.List;

import model.projects.Project;
import model.releases.Release;
import model.sprint.Sprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.SprintDAO;

@Service("sprintService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class SprintService {

	@Autowired
	private SprintDAO sprintDao;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Sprint> getAllSprints() {
		List<Sprint> sprints = sprintDao.getSprints();
		return sprints;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Sprint getSprint(Long id) {
		Sprint sprint = sprintDao.getSprint(id);
		return sprint;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Sprint> getSprintsForProject(Project project) {
		return sprintDao.getSprintsForProject(project);
	}

	public void saveSprint(Sprint sprint) {
		if (sprint.getIdSprint() == null) {
			sprintDao.save(sprint);
		}
		else {
			sprintDao.update(sprint);
		}
	}

	public void deleteSprint(Sprint Sprint) {
		sprintDao.delete(Sprint);
	}

	public void deleteSprints(long[] ids) {
		for(int i=0; i<ids.length; i++){
			sprintDao.delete(ids[i]);
		}

	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Sprint> getSprintsForRelease(Release release){
		return sprintDao.getSprintsForRelease(release);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long getStoryPointsForSprintAndRelease(Sprint sprint, Release release, boolean dateTo) {
		return sprintDao.getDoneStoryPoints(sprint, release,  dateTo);
	}

	public long getMaxStoryPointsForReleaseAndSprint(Sprint sprint,
			Release release, boolean dateTo) {
		return sprintDao.getMaxStoryPoints(sprint, release,  dateTo);
	}

	public Object getActiveSprintsForProject(Project project) {
		return sprintDao.getActiveSprintsForProject(project);
	}



}
