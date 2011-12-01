package daoImpl;


import model.projects.Attribute;
import model.projects.Project;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import dao.AttributeDAO;

@Repository("attributeDao")
public class AttributeDAOImpl extends DAOImpl implements AttributeDAO {

	@Override
	public Attribute getAttribute(long attributeId) {
		return (Attribute) currentSession().get(Attribute.class, attributeId);
	}

	@Override
	public void saveAttribute(Attribute attribute) {
		currentSession().save(attribute);
	}
	
	@Override
	public void deleteAttribute(long attributeId) {
		currentSession().delete(getAttribute(attributeId));

	}
	
	@Override
	public void deleteAttribute(Attribute attribute) {
		currentSession().delete(attribute);

	}

	@Override
	public boolean isAttributePresentForProject(String projectAlias, long attributeId) {
		Criteria criteria = currentSession().createCriteria(Project.class)
				.add(Restrictions.eq("alias", projectAlias))
				.createCriteria("attributes")
				.add(Restrictions.eq("idAttribute", attributeId));
		return criteria.uniqueResult() != null;

	}

	@Override
	public void update(Attribute attribute) {
		currentSession().update(attribute);
		
	}

}
