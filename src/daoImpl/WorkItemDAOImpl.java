package daoImpl;

import java.util.List;

import model.pbis.PBI;
import model.workItems.WorkItem;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import dao.WorkItemDAO;

@Repository("workItemDAO")
public class WorkItemDAOImpl extends DAOImpl implements WorkItemDAO   {

	@SuppressWarnings("unchecked")
	public List<WorkItem> getWorkItems() {
		return currentSession().createCriteria(WorkItem.class).list();
	}

	public WorkItem getWorkItem(long idWorkItem	) {
		return (WorkItem) currentSession().get(WorkItem.class, idWorkItem);
	}

	public void save(WorkItem workItem) {
		currentSession().save(workItem);
		currentSession().flush();
	}

	public void update(WorkItem workItem) {
		currentSession().update(workItem);
	}

	public void delete(WorkItem workItem) {
		currentSession().delete(workItem);
	}

	public WorkItem loadWorkItem(long idWorkItem) {
		return (WorkItem) currentSession().load(WorkItem.class, idWorkItem);
	}

	@SuppressWarnings("unchecked")
	public List<WorkItem> getWorkItemsForPBI(PBI pbi) {
		Criteria criteria = currentSession().createCriteria(WorkItem.class)
				.add( Restrictions.eq("pbi",pbi))
				.setFetchMode("team", FetchMode.JOIN)
				.setFetchMode("sprint", FetchMode.JOIN)
				.addOrder(Order.desc("date"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkItem> getWorkItemsByField(String field, Object value, String operator) {

		Criteria criteria = currentSession().createCriteria(WorkItem.class);
		if(operator.compareTo("=")==0) {
			criteria.add( Restrictions.eq(field, value));
		} else if(operator.compareTo("<>")==0) {
			criteria.add( Restrictions.ne(field, value));
		}

		return criteria.list();
	}





}
