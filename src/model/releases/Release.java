package model.releases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import model.pbis.PBI;
import model.projects.Project;
import model.users.ScrumzuUser;

@Entity
@Table(name = "Releases")
public class Release implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRelease;

	@NotNull(message = "Date from must not be blank")
	@Temporal(TemporalType.DATE)
	private Date dateFrom;

	@NotNull(message = "Date to must not be blank")
	@Temporal(TemporalType.DATE)
	private Date dateTo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idProject")
	private Project project;

	@Size(min = 1, max = 100, message = "Name must hava between 1 to 100 characters")
	@Basic
	private String name;

	@ElementCollection(targetClass = ReleaseItem.class)
	@CollectionTable(name = "ReleaseItems", joinColumns = @JoinColumn(name = "idRelease"))
	private List<ReleaseItem> releaseItems = new ArrayList<ReleaseItem>();

	@Transient
	private List<PBI> pbis = new ArrayList<PBI>();

	public void setReleaseItems(List<ReleaseItem> releaseItems) {
		this.releaseItems = releaseItems;
	}

	public List<ReleaseItem> getReleaseItems() {
		return releaseItems;
	}

	public Release() {
	}

	public Release(long idRelease) {
		this.idRelease = idRelease;
	}

	public Long getIdRelease() {
		return idRelease;
	}

	public void setIdRelease(Long idRelease) {
		this.idRelease = idRelease;
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

	public List<PBI> getPbis() {
		return pbis;
	}

	public void setPbis(List<PBI> pbis) {
		this.pbis = pbis;
	}

	public void addReleaseItem(PBI pbi, ScrumzuUser user) {
		if(! releaseItems.contains(pbi)){
			releaseItems.add(new ReleaseItem(pbi,user));
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idRelease == null) ? 0 : idRelease.hashCode());
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
		if (!(obj instanceof Release)) {
			return false;
		}
		Release other = (Release) obj;
		if (idRelease == null) {
			if (other.idRelease != null) {
				return false;
			}
		} else if (!idRelease.equals(other.idRelease)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Release [dateFrom=" + dateFrom + ", dateTo=" + dateTo
				+ ", name=" + name + "]";
	}


}
