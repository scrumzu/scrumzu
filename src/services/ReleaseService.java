package services;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.projects.Project;
import model.releases.Release;
import model.releases.ReleaseItem;
import model.sprint.Sprint;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.PBIDAO;
import dao.ReleaseDAO;

@Service("releaseService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReleaseService {

	@Autowired
	private ReleaseDAO releaseDao;

	@Autowired
	private SprintService sprintService;

	@Autowired
	private PBIDAO pbiDao;

	public Release getRelease(long releaseId) {
		return releaseDao.getRelease(releaseId);
	}
	public List<Release> getReleasesForProject(Project project) {
		return releaseDao.getReleasesForProject(project);
	}

	public Release getReleaseWithDetails(long releaseId) {
		Release release = releaseDao.getReleaseWithDetails(releaseId);
		for(ReleaseItem ri: release.getReleaseItems()){
			Hibernate.initialize(ri.getPbi().getWorkItems());
		}
		return releaseDao.getReleaseWithDetails(releaseId);
	}

	public Release loadRelease(long releaseId) {
		return releaseDao.loadRelease(releaseId);
	}


	public void saveRelease(Release release) {
		if (release.getIdRelease() == null) {
			releaseDao.save(release);
		}
		else {
			releaseDao.update(release);
		}
	}
	public long getStoryPointsForRelease(Release release) {
		long sum = 0;
		List<ReleaseItem> releaseItems = release.getReleaseItems();
		for (ReleaseItem ri:releaseItems) {
			sum += pbiDao.getStoryPointsForPBI(ri.getPbi());
		}
		return sum;
	}
	public long getStoryPointsForReleaseAndDate(Sprint sprint, Release release, boolean dateTo) {
		return sprintService.getMaxStoryPointsForReleaseAndSprint(sprint,  release,  dateTo);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Map<String, Long> getBurndownForRealease(Release release){
		List<Sprint> sprints = sprintService.getSprintsForRelease(release);
		Map<String, Long> data = new LinkedHashMap<String, Long>();
		Format formatter=new SimpleDateFormat("yyyy-MM-dd");

		Sprint lastSprint=null;
		for(Sprint sprint:sprints){
				lastSprint=sprint;
				long maxStoryPoints = getStoryPointsForReleaseAndDate(sprint, release, false);
				long doneStoryPoints = sprintService.getStoryPointsForSprintAndRelease(sprint, release, false);
				System.out.println(sprint.getName()+ " max: "+ maxStoryPoints+ "   done: "+ doneStoryPoints);
				if(sprint.getDateFrom().before(new Date(System.currentTimeMillis())))
					data.put("<div style=\"text-align: center;\">"+sprint.getName() +" <br/> " + formatter.format(sprint.getDateFrom())+"</div>", maxStoryPoints-doneStoryPoints);
				else
					data.put("<div style=\"text-align: center;\">"+sprint.getName() +" <br/> " + formatter.format(sprint.getDateFrom())+"</div>", null);	
		}
		if(lastSprint!=null ){
			long maxStoryPoints = getStoryPointsForReleaseAndDate(lastSprint, release, true);
			long doneStoryPoints = sprintService.getStoryPointsForSprintAndRelease(lastSprint, release, true);
			System.out.println(lastSprint.getName()+ " max: "+ maxStoryPoints+ "   done: "+ doneStoryPoints);
			if(lastSprint.getDateTo().before(new Date(System.currentTimeMillis())))
				data.put("<div style=\"text-align: center;\"><br/> " +formatter.format(lastSprint.getDateTo()) +"</div>",maxStoryPoints-doneStoryPoints);	
			else
				data.put("<div style=\"text-align: center;\"><br/> " +formatter.format(lastSprint.getDateTo()) +"</div>",null);	
		}
		return data;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteReleases(long[] ids) {
		for(int i=0; i<ids.length; i++){
			releaseDao.delete(ids[i]);
		}
	}

}
