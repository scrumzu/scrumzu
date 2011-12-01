package dao;

import java.util.List;

import model.users.ScrumzuAuthority;

public interface ScrumzuAuthorityDAO {

	public List<ScrumzuAuthority> getAuthorities();
	public ScrumzuAuthority getAuthority(Long id);
	public ScrumzuAuthority loadAuthority(Long authorityId);
	public void delete(ScrumzuAuthority authority);
	public void save(ScrumzuAuthority authority);
	public void update(ScrumzuAuthority authority);
	public void delete(Long authorityId);
	public void flushSession();
}
