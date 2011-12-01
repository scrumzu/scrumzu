package model.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import model.filters.Filter;
import model.teams.Team;
import model.workItems.WorkItem;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "Users")
@JsonIgnoreProperties({ "serialVersionUID", "authorities", "enabled",
	"filters", "idUser", "password", "salt", "teams", "workItems",
	"accountNonExpired", "accountNonLocked", "credentialsNonExpired",
"authoritiesList" })
public class ScrumzuUser implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUser;

	@Basic(optional = false)
	@Size(min = 2, max = 20, message = "Username must have between 2 to 30 characters.")
	private String username;

	@Basic(optional = false)
	private String password;

	@Basic(optional = false)
	private boolean enabled;

	@Basic(optional = false)
	private Long salt = (System.currentTimeMillis() * 1301077L) % 1000000;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "UserPrivileges", joinColumns = { @JoinColumn(name = "idUser", referencedColumnName = "idUser") }, inverseJoinColumns = { @JoinColumn(name = "idAuthority", referencedColumnName = "idAuthority") })
	private Collection<ScrumzuAuthority> authorities;

	@OneToMany(mappedBy = "user")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private final List<Filter> filters = new ArrayList<Filter>();

	@OneToMany(mappedBy = "user")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private final List<WorkItem> workItems = new ArrayList<WorkItem>();

	@OneToMany(mappedBy = "user")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private final List<Team> teams = new ArrayList<Team>();

	public ScrumzuUser() {
	}

	public ScrumzuUser(String username, String password, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public ScrumzuUser(long idUser) {
		this.idUser = idUser;
	}

	public ScrumzuUser(String username) {
		this.username = username;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		for (ScrumzuAuthority role : authorities) {
			list.add(new GrantedAuthorityImpl(role.getAuthority()));
		}
		return list;
	}

	public Collection<ScrumzuAuthority> getAuthoritiesList() {
		return authorities;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAccountNonExpired() {
		return enabled;
	}

	public boolean isAccountNonLocked() {
		return enabled;
	}

	public boolean isCredentialsNonExpired() {
		return enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean hasRole(String role) {
		for (ScrumzuAuthority a : authorities) {
			if (role.equals(a.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	public void setAuthorities(List<String> roles) {
		for (String role : roles) {
			ScrumzuAuthority a = new ScrumzuAuthority();
			a.setAuthority(role);
			authorities.add(a);
		}
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

	public List<Team> geTeams() {
		return teams;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(Collection<ScrumzuAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setAuthoritiesList(Collection<ScrumzuAuthority> authorities) {
		this.authorities = authorities;
	}

	public Long getSalt() {
		return salt;
	}

	public void setSalt(Long salt) {
		this.salt = salt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		if (!(obj instanceof ScrumzuUser)) {
			return false;
		}
		ScrumzuUser other = (ScrumzuUser) obj;
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ScrumzuUser [username=" + username + "]";
	}

}
