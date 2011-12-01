package dao;

import java.util.List;

import model.filters.Filter;

import org.springframework.security.core.userdetails.UserDetails;

public interface FilterDAO {
	
	/**
	 * The method returns list of filters for specified user. Filters for user are: all public filters
	 * and private filters, which author is equal to given user. 
	 *
	 * @param user - User for whom return filters
	 * @return List of filters
	 * @see UserDetails
	 */
	public List<Filter> getFiltersForUser(UserDetails user);
	/** Function which saves new filter to database
	 * 
	 * @param filter - Filter to save
	 * @see Filter
	 */
	public void save(Filter filter);
	/** 
	 * Function which updates an existing filter in databse
	 * @param filter - filter to update
	 * @see Filter
	 */
	public void update(Filter filter);
	/**
	 * Function which deletes filter from database if exists
	 * @param filter
	 * @see Filter
	 */
	public void delete(Filter filter);
	/**
	 * Function which deletes filter from database if exists
	 * @param idFilter - ID of filter to delete
	 */
	public void delete(long idFilter);
	/**
	 * Function which loads filter without filter items
	 * @param idFilter - Id of filter to load
	 * @return Loaded filter
	 */
	public Filter loadFilter(long idFilter);
	/**
	 * Function which flushes current session
	 */
	public void flushSession();
	/**
	 * Function which returns filter by id
	 * @param idFilter - filter id
	 * @return Filter
	 */
	public Filter getFilter(long idFilter);
	/** 
	 * The method returns list of all filters regardless whether filter is private or not 
	 * @return List of filters
	 */
	public List<Filter> getFilters();
	/**
	 * Function which returns list of filters created by user
	 * @param user - User, who created filters
	 * @return - List of filters created by user
	 * @see UserDetails
	 */
	public List<Filter> getFiltersCreatedByUser(UserDetails user);
 
}
