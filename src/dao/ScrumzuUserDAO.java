package dao;

import java.util.List;

import model.users.ScrumzuUser;

public interface ScrumzuUserDAO {

	public List<ScrumzuUser> getUsers();
	public void delete(ScrumzuUser user);
	public void save(ScrumzuUser user);
	public void update(ScrumzuUser user);
	public ScrumzuUser getUser(Long idUser);
	public ScrumzuUser loadUser(Long idUser);
	public void delete(Long idUser);
	public ScrumzuUser getUser(String username);
	public void delete(String username);
	public void flushSession();
	public List<ScrumzuUser> getScrumMasters();

}
