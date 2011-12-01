package daoImpl;

import java.util.List;


import model.users.ScrumzuAuthority;

import org.springframework.stereotype.Component;

import dao.ScrumzuAuthorityDAO;

@Component("authorityDao")
public class ScrumzuAuthorityDAOImpl extends DAOImpl implements
ScrumzuAuthorityDAO {

	@SuppressWarnings("unchecked")

	public List<ScrumzuAuthority> getAuthorities() {
		return currentSession().createCriteria(ScrumzuAuthority.class).list();
	}

	public ScrumzuAuthority getAuthority(Long authorityId) {
		return (ScrumzuAuthority) currentSession().get(ScrumzuAuthority.class,
				authorityId);
	}

	public ScrumzuAuthority loadAuthority(Long authorityId) {
		return (ScrumzuAuthority) currentSession().load(ScrumzuAuthority.class,
				authorityId);
	}

	public void delete(ScrumzuAuthority authority) {
		currentSession().delete(authority);
	}

	public void save(ScrumzuAuthority authority) {
		currentSession().save(authority);
	}

	public void update(ScrumzuAuthority authority) {
		currentSession().update(authority);
	}

	public void delete(Long authorityId) {
		currentSession().delete(getAuthority(authorityId));
	}

}
