package model.workItems;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import model.pbis.PBI;
import model.sprint.Sprint;
import model.teams.Team;
import model.users.ScrumzuUser;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@JsonIgnoreProperties(value = { "pbi" })
@Entity
@Table(name = "WorkItems")
public class WorkItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idWorkItem;

	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private Status status;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTeam")
	@NotFound(action = NotFoundAction.IGNORE)
	private Team team;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPBI")
	private PBI pbi;

	@Column(name = "storyPoints", nullable = true)
	private Integer storyPoints;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idSprint")
	private Sprint sprint;

	@ManyToOne
	@JoinColumn(name="idUser")
	@NotNull
	private ScrumzuUser user;

	public WorkItem() {
	}

	public WorkItem(Sprint sprint, Status status, Team team, PBI pbi) {
		super();
		this.sprint = sprint;
		this.status = status;
		this.team = team;
		this.pbi = pbi;
	}

	public WorkItem(Status status){
		this.status = status;
	}

	public WorkItem(long idWorkItem) {
		this.idWorkItem = idWorkItem;
	}

	public ScrumzuUser getUser() {
		return user;
	}

	public void setUser(ScrumzuUser user) {
		this.user = user;
	}

	public void setIdWorkItem(Long idWorkItem) {
		this.idWorkItem = idWorkItem;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDateForNow() {
		date = Calendar.getInstance().getTime();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public PBI getPbi() {
		return pbi;
	}

	public void setPbi(PBI pbi) {
		this.pbi = pbi;
	}

	public Long getIdWorkItem() {
		return idWorkItem;
	}

	public Integer getStoryPoints() {
		return storyPoints;
	}

	public void setStoryPoints(Integer storyPoints) {
		this.storyPoints = storyPoints;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((idWorkItem == null) ? 0 : idWorkItem.hashCode());
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
		WorkItem other = (WorkItem) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (idWorkItem == null) {
			if (other.idWorkItem != null) {
				return false;
			}
		} else if (!idWorkItem.equals(other.idWorkItem)) {
			return false;
		}
		if (pbi == null) {
			if (other.pbi != null) {
				return false;
			}
		} else if (!pbi.equals(other.pbi)) {
			return false;
		}
		if (sprint == null) {
			if (other.sprint != null) {
				return false;
			}
		} else if (!sprint.equals(other.sprint)) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		if (team == null) {
			if (other.team != null) {
				return false;
			}
		} else if (!team.equals(other.team)) {
			return false;
		}
		return true;
	}

	public Object getValueByField(String fieldName) {
		Object result="";

		if(fieldName.equals("team")) {
			if(getTeam()!=null) {
				result= getTeam().getIdTeam().toString();
			}
		} else if(fieldName.equals("sprint")) {
			if(getSprint()!=null) {
				result= getSprint().getIdSprint().toString();
			}
		} else if(fieldName.equals("status")) {
			if(getStatus()!=null) {
				result= getStatus().name();
			}
		}
		else if(fieldName.equals("storyPoints")) {
			if(getStoryPoints()!=null) {
				result= getStoryPoints().toString();
			}
		}
		return result;
	}

	private boolean isProposed() {
		return sprint != null && status == Status.PROPOSED_FOR_SPRINT && team == null;
	}

	private boolean isNew(){
		return sprint == null && team == null && status == Status.NEW;
	}

	private boolean isSetForTeam() {
		return sprint != null && status != Status.PROPOSED_FOR_SPRINT &&  status != Status.NEW && status!=null && team != null;
	}

	public boolean isWriteable() {
		return isProposed() || isSetForTeam() || isNew();
	}

	public boolean isEmpty(){
		return sprint==null && team == null && status == null;
	}

	public String toString(){
		return idWorkItem + " " + sprint + " " + team + " " + status;
	}
}
