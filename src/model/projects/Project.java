package model.projects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import model.pbis.PBI;
import model.sprint.Sprint;
import model.teams.Team;

@Entity
@Table(name="Projects")
public class Project {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProject;
	
	@Size(min = 3, max = 50, message = "Name must have between 3 to 50 characters.")
	@Basic
	private String name;
	
	@Size(min = 3, max = 100, message = "Product owner must have between 3 to 100 characters.")
	@Basic
	private String owner;
	
	@Basic
	private String url;
	
	@Basic
	private String description;
	
	@Size(min = 1, max = 20, message = "Version must have between 1 to 20 characters.")
	@Basic
	private String version;
	
	@Size(min = 1, max = 20, message = "Alias must have between 1 to 20 characters.")
	@Basic
	@Column(unique=true)
	private String alias;
	
	@OneToMany(mappedBy="project")
	private Set<Team> teams = new HashSet<Team>();
	
	@OneToMany(mappedBy="project")
	@org.hibernate.annotations.Cascade( org.hibernate.annotations.CascadeType.DELETE)
	private List<PBI> PBIs = new ArrayList<PBI>();
	
	@OneToMany(mappedBy="project")
	private List<Sprint> sprints = new ArrayList<Sprint>();
	
	@OneToMany(mappedBy="project")
	@org.hibernate.annotations.Cascade( org.hibernate.annotations.CascadeType.ALL)
	private Set<Attribute> attributes = new HashSet<Attribute>();
	
	
	public Project(){}

	public Project(String name, String owner, String url, String description, String version, String alias) {
		this.name = name;
		this.owner = owner;
		this.url = url;
		this.description = description;
		this.version = version;
		this.alias = alias;
	}

	public Project(long idProject) {
		this.idProject=idProject;
	}
	
	public Long getIdProject() {
		return idProject;
	}

	public void setIdProject(Long idProject) {
		this.idProject = idProject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Set<Team> getTeams() {
		return teams;
	}

	public List<PBI> getPBIs() {
		return PBIs;
	}

	public void setPBIs(List<PBI> PBIs) {
		this.PBIs=PBIs;
	}

	public List<Sprint> getSprints() {
		return sprints;
	}
	
	public Set<Attribute> getAttributes() {
		return attributes;
	}
	
	public void addAttribute(String name, AttributeType attributeType) {
		attributes.add(new Attribute(name,attributeType, this));
	}
	
	public Attribute getAttribute(String name) {
		Iterator<Attribute> iterator = attributes.iterator();
		Attribute attribute = null;
		boolean equalFound = false;
		while (iterator.hasNext() && !equalFound) {
			attribute = iterator.next();
			equalFound = attribute.getName().equals(name);
		}
		return attribute;
		
	}
	
	@Override
	public String toString() {
		return "Project [name=" + name + ", owner=" + owner + ", url=" + url
				+ ", version=" + version + ", alias=" + alias + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((idProject == null) ? 0 : idProject.hashCode());
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idProject == null) {
			if (other.idProject != null)
				return false;
		} else if (!idProject.equals(other.idProject))
			return false;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}



}
