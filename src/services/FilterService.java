package services;

import java.util.List;

import model.filters.Filter;
import model.users.ScrumzuUser;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.FilterDAO;

/** Class representing filter service managing in database operatorion between controler and DAO
 * 
 * @author TW
 *
 */
@Service("filterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class FilterService {

	/** Filter DAO
	 * @see FilterDAO
	 */
	@Autowired
	private FilterDAO filterDao;

	/**
	 * Function which returns all filters for users, own and public
	 * @param user 
	 * @return List of filters
	 * @see ScrumzuUser
	 * @see Filter
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Filter> getFiltersForUser(UserDetails user) {
		List<Filter> filters = filterDao.getFiltersForUser(user);
		for(Filter f: filters) {
			Hibernate.initialize(f.getFilterItems());
		}
		return filters;
	}

	/**
	 * The method returns list of filters for specified user. Filters for user are: all public filters
	 * and private filters, which author is equal to given user. 
	 * @param idFilter - filter id
	 * @return Filter
	 * @see Filter
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Filter getFilterById(Long idFilter) {
		Filter filter = filterDao.getFilter(idFilter);
		Hibernate.initialize(filter.getFilterItems());
		Hibernate.initialize(filter.getUser().getAuthorities());
		return filter;
	}
	
	/** 
	 * The method returns list of all filters regardless whether filter is private or not 
	 * @return List of all filters
	 * @see Filter
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Filter> getFilters() {
		List<Filter> filters = filterDao.getFilters();
		for(Filter f: filters) {
			Hibernate.initialize(f.getFilterItems());
		}
		return filters;
	}
	
	/** 
	 * Function which returns filters created by user
	 * @param user - Users who created filters
	 * @return List of user's filters
	 * @see ScrumzuUser
	 * @see Filter
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Filter> getFiltersCreatedByUser(UserDetails user) {
		return filterDao.getFiltersCreatedByUser(user);
	}

	/** 
	 * Function which manage saving and updating filter
	 * @param filter - filter to save or update
	 * @see Filter
	 */
	public void saveFilter(Filter filter) {
		if (filter.getIdFilter() == null) {
			filterDao.save(filter);
		}
		else {
			filterDao.update(filter);
		}
	}

	/**
	 * Function which manage deleting filter from database
	 * @param Filter - Filter to delete
	 * @see Filter
	 */
	public void deleteFilter(Filter Filter) {
		filterDao.delete(Filter);
	}

	/**
	 * Function which manage deleting filters from databse by id
	 * @param ids - Array of id of filters to delete from database
	 * @see Filter
	 */
	public void deleteFilters(long[] ids) {
		for(int i=0; i<ids.length; i++){
			filterDao.delete(ids[i]);
		}

	}

}
