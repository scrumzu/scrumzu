package model.teams;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import model.projects.Project;
import model.users.ScrumzuUser;
import model.workItems.WorkItem;

import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="Teams")
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTeam;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="idProject")
	@NotFound(action=NotFoundAction.IGNORE)
	private Project project;

	@ManyToOne
	@JoinColumn(name="idUser")
	private ScrumzuUser user;

	@Basic
	private String description;

	@Size(min = 3, max = 50, message = "Name must have between 3 to 50 characters.")
	@Basic
	private String name;

	@Size(min = 1, max = 10, message = "Team alias must have between 3 to 10 characters.")
	@Basic
	private String alias;

	@OneToMany(mappedBy="team")
	@org.hibernate.annotations.Cascade(CascadeType.DELETE)
	private List<WorkItem> workItems = new ArrayList<WorkItem>();

	public Team(){}

	public Team(String description, String name, String alias) {
		this.description = description;
		this.name = name;
		this.alias = alias;
	}

	public ScrumzuUser getUser() {
		return user;
	}

	public void setUser(ScrumzuUser user) {
		this.user = user;
	}

	public Team(long id) {
		idTeam=id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Long getIdTeam() {
		return idTeam;
	}

	public void setIdTeam(Long idTeam) {
		this.idTeam = idTeam;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

	public void setWorkItems(List<WorkItem> workItems) {
		this.workItems = workItems;
	}

	public void addWorkItem(WorkItem workItem) {
		workItems.add(workItem);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof Team)) {
			return false;
		}
		Team other = (Team) obj;
		if (idTeam == null) {
			if (other.idTeam != null) {
				return false;
			}
		} else if (!idTeam.equals(other.idTeam)) {
			return false;
		}
		if (alias == null) {
			if (other.alias != null) {
				return false;
			}
		} else if (!alias.equals(other.alias)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (project == null) {
			if (other.project != null) {
				return false;
			}
		} else if (!project.equals(other.project)) {
			return false;
		}
		return true;
	}

	public String getPublicName(){
		return alias + " - "+ name;
	}
	public String toString() {
		return "Team#"+idTeam+" [name=" + name + ", description=" + description+ ", alias=" + alias + "  project=" + project+"]";
	}



}
