package services;

import java.util.List;

import model.filters.Filter;
import model.teams.Team;
import model.users.ScrumzuUser;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.FilterDAO;
import dao.ScrumzuUserDAO;
import dao.TeamDAO;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service("userDetailsService")
public class ScrumzuUserDetailsService implements UserDetailsService {

	@Autowired
	private ScrumzuUserDAO userDao;

	@Autowired
	private TeamDAO teamDao;

	@Autowired
	private FilterDAO filterDao;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

		ScrumzuUser user = userDao.getUser(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return user;
	}

	private List<ScrumzuUser> initializeAuthorities(List<ScrumzuUser> users) {
		for(ScrumzuUser user : users){
			Hibernate.initialize(user.getAuthorities());
		}
		return users;
	}

	public ScrumzuUser getUser(Long id)
			throws UsernameNotFoundException, DataAccessException {
		ScrumzuUser user = userDao.getUser(id);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return user;
	}

	public List<ScrumzuUser> getScrumMasters() {
		return initializeAuthorities(userDao.getScrumMasters());
	}

	public List<ScrumzuUser> getList(){
		return initializeAuthorities(userDao.getUsers());
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(ScrumzuUser user) {
		if (user.getIdUser() == null) {
			userDao.save(user);
		}
		else {
			userDao.update(user);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(long[] ids) {
		for (int i =0; i < ids.length; i++) {
			userDao.delete(ids[i]);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void disableUsers(long[] ids) {
		for (int i =0; i < ids.length; i++) {
			ScrumzuUser user =	userDao.getUser(ids[i]);
			user.setEnabled(false);
			List<Team> teams = teamDao.getTeamsForUser(user);
			for (Team t:teams) {
				t.setUser(null);
				teamDao.update(t);
			}
			List<Filter> filters = filterDao.getFiltersCreatedByUser(user);
			for (Filter f:filters) {
				filterDao.delete(f);
			}
			userDao.update(user);
		}
	}

	public boolean isUsernameTaken(String username, Long idUser) {
		Long idDb = null;
		ScrumzuUser user = userDao.getUser(username);
		if(user != null){
			idDb = user.getIdUser();
		}
		return idDb ==  null? false :  !idDb.equals(idUser);
	}
}

