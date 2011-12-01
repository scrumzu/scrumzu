package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.filters.Filter;
import model.filters.FilterItem;
import model.pbis.PBI;
import model.projects.Project;
import model.sprint.Sprint;
import model.teams.Team;
import model.workItems.WorkItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.PBIDAO;


@Service("pbiService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PBIService {

	@Autowired
	private PBIDAO pbiDao;

	@Autowired
	ProjectService projectService;

	@Autowired
	SprintService sprintService;

	@Autowired
	WorkItemService workItemService;

	@Autowired
	TeamService teamService;

	@Autowired
	FilterService filterService;

	public PBI getPBI(Long id) {
		PBI pbi = pbiDao.getPBI(id);
		return pbi;
	}

	public List<PBI> getPBIsForTeam(Team team) {
		return pbiDao.getPBIsForTeam(team);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void savePBI(PBI pbi) {
		if (pbi.getIdPBI() == null) {

			pbi.setDateCreation(Calendar.getInstance().getTime());
			pbiDao.save(pbi);
		} else {
			pbiDao.update(pbi);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deletePBI(PBI pbi) {
		pbiDao.delete(pbi);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deletePBIs(long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			pbiDao.delete(ids[i]);
		}
	}

	public long countPBIsForSprint(Sprint sprint) {
		return pbiDao.countPBIsForSprint(sprint);
	}

	public List<PBI> getPBIsForSprint(Sprint sprint) {
		return pbiDao.getPBIsForSprint(sprint);

	}

	public List<PBI> getPBIsForProject(Project project) {
		return pbiDao.getPBIsForProject(project);

	}

	public PBI getPBIWithAttributes(long PBIId) {
		return pbiDao.getPBIWithAttributes(PBIId);
	}

	public Set<PBI> getPBIsByFilterAndProject(Filter filter, Project project) {

		List<List<FilterItem>> listyANDow = new ArrayList<List<FilterItem>>();

		//splitting filter items by OR
		List<FilterItem> fisOrginal = filter.getFilterItems();
		for (int i = 0; i < fisOrginal.size();) {
			List<FilterItem> fiAnd = new ArrayList<FilterItem>();
			do {
				fiAnd.add(fisOrginal.get(i));
				i++;
			} while (i < fisOrginal.size()
					&& (fisOrginal.get(i).getAndOr() == null || fisOrginal
					.get(i).getAndOr().equals("and")));
			listyANDow.add(fiAnd);
		}
		Set<PBI> filteredPBI = new HashSet<PBI>();

		List<PBI> pbis = getPBIsForProject(project);

		for (List<FilterItem> fiAnd : listyANDow) {
			for (PBI p : pbis) {
				List<WorkItem> workItems = new ArrayList<WorkItem>();
				workItems.add(0, workItemService.getLastWorkItemForPBI(p));
				p.setWorkItems(workItems);
				if (isValid(fiAnd, p)) {
					filteredPBI.add(p);
				}
			}
		}

		return filteredPBI;
	}

	private boolean isValid(List<FilterItem> andfilterItems, PBI pbi) {
		boolean result = true;
		for (FilterItem fi : andfilterItems) {

			boolean expRes = false;
			if (fi.getOperator().equals("nq")) {
				expRes = false;
			} else if (fi.getOperator().equals("eq")) {
				expRes = true;
			}

			Object tmp = pbi.getValueByField(fi.getField());
			if(tmp!=null){
				result = (compareData(fi.getValue(), tmp)==expRes);
			}
			else if((tmp==null || tmp.equals("")) && !expRes) {
				result=true;
			} else {
				result = false;
			}

			if (result == false) {
				break;
			}
		}
		return result;
	}

	protected boolean compareData(Object first, Object second){
		boolean result =  false;


		if (first instanceof String && second instanceof String) {
			result = ((String)second).toLowerCase().indexOf(((String)first).toLowerCase())>-1;
		} else if (first instanceof Date && second instanceof String){
			long date1= ((Date)first).getTime();
			long date2= Long.parseLong((String)second);
			result = (date1==date2);
		} else {
			result = first.toString().equals(second.toString());
		}
		return result;
	}

}
