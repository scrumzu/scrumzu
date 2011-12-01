package daoImpl;

import java.util.List;

import model.filters.Filter;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import dao.FilterDAO;

@Component("filterDao")
public class FilterDAOImpl extends DAOImpl implements FilterDAO {


	@SuppressWarnings("unchecked")
	@Override
	public List<Filter> getFiltersForUser(UserDetails user) {
		LogicalExpression restriction = Restrictions.or(
				Restrictions.and(Restrictions.eq("isPublic", Boolean.FALSE), Restrictions.eq("user", user)),
				Restrictions.eq("isPublic",Boolean.TRUE));
		Criteria criteria = currentSession()
				.createCriteria(Filter.class)
				.add(restriction);	
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Filter> getFiltersCreatedByUser(UserDetails user) {
		return currentSession().createCriteria(Filter.class).add(Restrictions.eq("user", user)).list();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Filter> getFilters() {
		return currentSession().createCriteria(Filter.class).list();
	}
	
	@Override
	public void save(Filter filter) {
		currentSession().save(filter);

	}

	@Override
	public void update(Filter filter) {
		currentSession().update(filter);

	}

	@Override
	public void delete(Filter filter) {
		currentSession().delete(filter);

	}

	@Override
	public void delete(long idFilter) {
		currentSession().delete(getFilter(idFilter));

	}

	@Override
	public Filter loadFilter(long idFilter) {
		return (Filter) currentSession().load(Filter.class, idFilter);
	}

	@Override
	public Filter getFilter(long idFilter) {
		Criteria criteria = currentSession()
				.createCriteria(Filter.class)
				.add(Restrictions.eq("idFilter", idFilter))
				.setFetchMode("filterItems", FetchMode.JOIN);
		return (Filter) criteria.uniqueResult();
	}

}
