package services;

import java.util.List;

import model.pbis.PBI;
import model.projects.Attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.AttributeDAO;


@Service("attributeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AttributeService {

	@Autowired
	private AttributeDAO attributeDao;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(Attribute attribute) {
		if (attribute.getIdAttribute() == null) {
			attributeDao.saveAttribute(attribute);
		}
		else {
			attributeDao.update(attribute);
		}
	}

	public boolean isAttributePresentForProject(String projectAlias, long attributeId) {
		return attributeDao.isAttributePresentForProject(projectAlias, attributeId);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteAttributes(long[] ids) {
		for (int i =0; i < ids.length; i++) {
			Attribute attribute = attributeDao.getAttribute(ids[i]);
			List<PBI> pbis = attribute.getProject().getPBIs();
			for (PBI p:pbis) {
				p.deleteAttribute(attribute);
			}
			attributeDao.deleteAttribute(ids[i]);
		}
	}



}
