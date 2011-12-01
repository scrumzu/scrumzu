package model.sprint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import model.projects.Project;
import model.workItems.WorkItem;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "Sprints")
@JsonIgnoreProperties({ "serialVersionUID", "project", "sprintStatus",
"workItems" })
public class Sprint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSprint;

	@NotNull(message = "Date from must not be blank")
	@Temporal(TemporalType.DATE)
	private Date dateFrom;

	@NotNull(message = "Date to must not be blank")
	@Temporal(TemporalType.DATE)
	private Date dateTo;

	@NotNull(message = "Sprint status must not be blank")
	@Enumerated(EnumType.ORDINAL)
	private SprintStatus sprintStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idProject")
	private Project project;

	@Size(min = 1, max = 100, message = "Name must hava between 1 to 100 characters")
	@Basic
	private String name;

	@OneToMany(mappedBy = "sprint")
	@org.hibernate.annotations.Cascade(CascadeType.DELETE)
	private final List<WorkItem> workItems = new ArrayList<WorkItem>();

	public Sprint() {
	}

	public Sprint(Date dateFrom, Date dateTo, Project project,
			SprintStatus sprintStatus, String name) {
		super();
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.project = project;
		this.sprintStatus = sprintStatus;
		this.name = name;
	}

	public Sprint(long idSprint) {
		this.idSprint = idSprint;
	}

	public Long getIdSprint() {
		return idSprint;
	}

	public void setIdSprint(Long idSprint) {
		this.idSprint = idSprint;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

	public SprintStatus getSprintStatus() {
		return sprintStatus;
	}

	public void setSprintStatus(SprintStatus sprintStatus) {
		this.sprintStatus = sprintStatus;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateFrom == null) ? 0 : dateFrom.hashCode());
		result = prime * result
				+ ((idSprint == null) ? 0 : idSprint.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((sprintStatus == null) ? 0 : sprintStatus.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		Sprint other = (Sprint) obj;
		if (idSprint == null) {
			if (other.idSprint != null) {
				return false;
			}
		} else if (!idSprint.equals(other.idSprint)) {
			return false;
		}
		if (dateFrom == null) {
			if (other.dateFrom != null) {
				return false;
			}
		} else if (!dateFrom.equals(other.dateFrom)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (sprintStatus != other.sprintStatus) {
			return false;
		}
		return true;
	}

	public String toString() {
		return "Sprint#" + idSprint + "[name: " + name + "]";
	}

}
