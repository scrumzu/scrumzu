package dao;

import java.util.List;

import model.pbis.PBI;
import model.workItems.WorkItem;

public interface WorkItemDAO {
	
	public List<WorkItem> getWorkItems();
	public WorkItem getWorkItem(long idWorkItem);
	public void save(WorkItem workItem);
	public void update(WorkItem workItem);
	public void delete(WorkItem workItem);
	public WorkItem loadWorkItem(long idWorkItem);
	public List<WorkItem> getWorkItemsForPBI(PBI pbi);
	public void flushSession();
	public List<WorkItem> getWorkItemsByField(String field, Object value, String operator);
	

	
	

}
