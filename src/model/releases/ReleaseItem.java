package model.releases;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.pbis.PBI;
import model.users.ScrumzuUser;

@Embeddable
public class ReleaseItem implements Serializable{

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="idUser", nullable=false, updatable=false)
	private ScrumzuUser user;

	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="idPBI", nullable=false, updatable=false)
	private PBI pbi;

	@Temporal(TemporalType.DATE)
	private Date date = Calendar.getInstance().getTime();

	public ReleaseItem(){}

	public ReleaseItem(PBI pbi, ScrumzuUser user) {
		this.pbi = pbi;
		this.user = user;
		date = Calendar.getInstance().getTime();
	}

	public PBI getPbi() {
		return pbi;
	}
	public void setPbi(PBI pbi) {
		this.pbi = pbi;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public ScrumzuUser getUser() {
		return user;
	}
	public void setUser(ScrumzuUser user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((pbi == null) ? 0 : pbi.hashCode());
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
		ReleaseItem other = (ReleaseItem) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (pbi == null) {
			if (other.pbi != null) {
				return false;
			}
		} else if (!pbi.equals(other.pbi)) {
			return false;
		}
		return true;
	}

	public String toString(){
		return user + " " +pbi + " " + date;
	}
}
