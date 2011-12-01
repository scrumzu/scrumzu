package daoImpl;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;

/** 
 * Class representing general DAO Implementation common for all DAO classes
 * @author TW
 *
 */
public class DAOImpl {

	/** Session Factory 
	 * @see SessionFactory
	 */
	@Autowired
	private SessionFactory sessionFactory;

	/** Function returning current session
	 * 
	 * @return Current Session
	 * @see Session
	 */
	public Session currentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * Function which flushes current session
	 */
	public void flushSession() {
		currentSession().flush();
	}

}
