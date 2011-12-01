package dao;

import model.projects.Attribute;

public interface AttributeDAO {

	public Attribute getAttribute(long attributeId);
	public void saveAttribute(Attribute attribute);
	public void deleteAttribute(long attributeId);
	public void update(Attribute attribute);
	public boolean isAttributePresentForProject(String projectAlias, long attributeId);
	public void deleteAttribute(Attribute attribute);
	public void flushSession();




}
