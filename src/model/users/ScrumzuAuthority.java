package model.users;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="Authorities")
public class ScrumzuAuthority implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAuthority;

	@Basic(optional=false)
	private String authority;

	public ScrumzuAuthority(){}

	public Long getIdAuthority() {
		return idAuthority;
	}
	public void setIdAuthority(Long idAuthority) {
		this.idAuthority = idAuthority;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getSimpleName(){
		return authority.substring(5).replace("_", " ").toLowerCase();
	}

	public String toString(){
		return getSimpleName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authority == null) ? 0 : authority.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ScrumzuAuthority)) {
			return false;
		}
		ScrumzuAuthority other = (ScrumzuAuthority) obj;
		if (authority == null) {
			if (other.authority != null) {
				return false;
			}
		} else if (!authority.equals(other.authority)) {
			return false;
		}
		return true;
	}





}