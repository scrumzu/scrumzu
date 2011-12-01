package daoImpl;

import java.util.List;


import model.users.ScrumzuUser;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import dao.ScrumzuUserDAO;

@Component("userDao")
public class ScrumzuUserDAOImpl extends DAOImpl implements ScrumzuUserDAO {

	@SuppressWarnings("unchecked")
	public List<ScrumzuUser> getScrumMasters() {
		Criteria criteria = currentSession().createCriteria(ScrumzuUser.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq("enabled", true))
				.createCriteria("authorities")
				.add(Restrictions.eq("authority", "ROLE_SCRUM_MASTER"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<ScrumzuUser> getUsers() {
		return currentSession()
				.createCriteria(ScrumzuUser.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq("enabled", true))
				.list();
	}

	public ScrumzuUser getUser(String username) {
		Criterion criterion = Restrictions.eq("username", username);
		return (ScrumzuUser) currentSession().createCriteria(ScrumzuUser.class).setFetchMode("authorities", FetchMode.JOIN)
				.add(criterion).uniqueResult();

	}

	public void delete(ScrumzuUser user) {
		currentSession().delete(user);

	}

	public void save(ScrumzuUser user) {
		currentSession().save(user);

	}

	public void update(ScrumzuUser user) {
		currentSession().update(user);

	}

	public ScrumzuUser getUser(Long idUser) {
		Criteria criteria = currentSession().createCriteria(ScrumzuUser.class)
				.add(Restrictions.eq("idUser", idUser))
				.setFetchMode("authorities", FetchMode.JOIN);
		return (ScrumzuUser) criteria.uniqueResult();
	}

	public ScrumzuUser loadUser(Long idUser) {
		return (ScrumzuUser) currentSession().load(ScrumzuUser.class, idUser);
	}

	public void delete(Long idUser) {
		currentSession().delete(getUser(idUser));

	}

	public void delete(String username) {
		currentSession().delete(getUser(username));
	}



}
