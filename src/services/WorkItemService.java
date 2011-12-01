package services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.pbis.PBI;
import model.projects.Project;
import model.users.ScrumzuUser;
import model.workItems.Status;
import model.workItems.WorkItem;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.WorkItemDAO;

@Service("workItemService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class WorkItemService {

	@Autowired
	private WorkItemDAO workItemDAO;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<WorkItem> getWorkItemsForPBI(PBI pbi) {
		return workItemDAO.getWorkItemsForPBI(pbi);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ArrayList<String> getIdPBIsFromWorkItemsByProjectAndField(Project project, String field, Object value, String operator) {

		List<WorkItem> wis = workItemDAO.getWorkItemsByField(field, value, operator);
		ArrayList<String> result = new ArrayList<String>();
		for(WorkItem wi: wis){
			result.add(""+wi.getPbi().getIdPBI());
		}
		return result;
	}


	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public WorkItem getLastWorkItemForPBI(PBI pbi) {
		List<WorkItem> workItemsForPBI = workItemDAO.getWorkItemsForPBI(pbi);
		if(! workItemsForPBI.isEmpty()){
			WorkItem workItem = workItemsForPBI.get(0);
			Hibernate.initialize(workItem.getTeam());
			Hibernate.initialize(workItem.getSprint());
			return workItem;
		}
		return null;
	}


	public void saveWorkItem(WorkItem workItem) {
		workItem.setIdWorkItem(null);
		workItemDAO.save(workItem);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public static Set<Status> getAvailableStatusesForUser(ScrumzuUser user) {
		Set<Status> statuses = new HashSet<Status>();
		if (user.hasRole("ROLE_PRODUCT_OWNER")) {
			statuses.add(Status.PROPOSED_FOR_SPRINT);
			statuses.add(Status.PREASSIGNED);
			statuses.add(Status.NEW);
			statuses.add(Status.DONE);
			statuses.add(Status.DROPPED);
		}
		else if (user.hasRole("ROLE_SCRUM_MASTER")) {
			statuses.add(Status.DONE);
			statuses.add(Status.COMMITTED);
			statuses.add(Status.DROPPED);
			statuses.add(Status.WORK_IN_PROGRESS);
		}
		return statuses;
	}


}
