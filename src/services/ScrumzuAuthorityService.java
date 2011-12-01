package services;

import java.util.List;

import model.users.ScrumzuAuthority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import dao.ScrumzuAuthorityDAO;

@Service("authorityService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ScrumzuAuthorityService {

	@Autowired
	private ScrumzuAuthorityDAO authorityDao;

	public List<ScrumzuAuthority> getAuthorities() {
		return  authorityDao.getAuthorities();
	}
	
	public ScrumzuAuthority getAuthority(Long id) {
		return authorityDao.getAuthority(id);
	}


}
