package model.filters;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import model.users.ScrumzuUser;

import org.hibernate.annotations.CascadeType;

/** Class representing Filter used by PBI controller in PBI Backlog
 * 
 * @author TW
 *
 */

@Entity
@Table(name="Filters")
public class Filter {

	/** Filter id  */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFilter;

	/** User, who created filter */
	@ManyToOne
	@JoinColumn(name="idUser")
	private ScrumzuUser user;

	/** Filter name */
	@Size(min=1, max=30, message="Name must have 3 to 70 characters")
	@Basic
	@Column(nullable = false)
	private String name;

	/** Defines if filter is public  */
	@Basic
	@Column(nullable = false)
	private Boolean isPublic;

	/** Collection of filter items which store filter rules  */
	@ElementCollection
	@CollectionTable(name="FilterItems",joinColumns=@JoinColumn(name="idFilter"))
	@org.hibernate.annotations.Cascade(CascadeType.ALL)
	private final List<FilterItem> filterItems = new ArrayList<FilterItem>();

	public Filter(){}

	/** Public constructor
	 * 
	 * @param user - User who created filter
	 * @param name - Filter name
	 * @param isPublic - Defines if filter is public
	 * 
	 * @see ScrumzuUser
	 */
	public Filter(ScrumzuUser user, String name, boolean isPublic) {
		this.user = user;
		this.name = name;
		this.isPublic = isPublic;
	}
	/** Public constructor
	 * 
	 * @param idFilter - Filter ID
	 */
	public Filter(Long idFilter) {
		this.idFilter = idFilter;
	}

	/** Function which returns filter ID
	 * @return Filter ID
	 */
	public Long getIdFilter() {
		return idFilter;
	}

	/**Function which sets filter ID
	 * @param  idFilter - Filter ID
	 */
	public void setIdFilter(Long idFilter) {
		this.idFilter = idFilter;
	}

	/** Function which returns a user, who created the filter
	 * @return User who created the filter
	 */
	public ScrumzuUser getUser() {
		return user;
	}

	/**Function which returns sets user, who created the filter
	 * @param user - User who created the filter
	 * @see ScrumzuUser
	 */
	public void setUser(ScrumzuUser user) {
		this.user = user;
	}
	/**Function which returns filter name
	 * @return Name of the filter
	 */
	public String getName() {
		return name;
	}
	/** Function which sets filter name
	 * @param name - Name of the filter
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**Function which returns filter items
	 * @return Filter items which store logical rules for filter
	 */
	public List<FilterItem> getFilterItems() {
		return filterItems;
	}

	/**Function which returns if filter is public - visible for all users
	 * @return Boolean variable defining if the filter is public - visible for all users.
	 */
	public Boolean getIsPublic() {
		return isPublic;
	}

	/**
	 * Function which defines if filter is public - visible for all users
	 * @param isPublic - A variable defining if the filter should be visible for all users
	 */
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	/**
	 * Adds filter items to filter
	 * @param andOr - logical rule for rule. Should be "and", "or" or null
	 * @param field - PBI field name. Should be one of the following values: 
	 * <ul>
	 * <li>title</li>
	 * <li>description</li>
	 * <li>priority</li>
	 * <li>team</li>
	 * <li>sprint</li>
	 * <li>status</li>
	 * <li>storyPoints</li>
	 * </ul>
	 * @param operator - logical operator: "=" or "!="
	 * @param value - text value to compare with a field value
	 */
	public void addFilterItem(String andOr,	String field, String operator, String value) {
		addFilterItem(new FilterItem(andOr,field, operator, value));
	}

	/**
	 * Adds filter items to filter
	 * @param filterItem - Filter item
	 * @see FilterItem
	 */
	public void addFilterItem(FilterItem filterItem) {
		filterItems.add(filterItem);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idFilter == null) ? 0 : idFilter.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		Filter other = (Filter) obj;
		if (idFilter == null) {
			if (other.idFilter != null) {
				return false;
			}
		} else if (!idFilter.equals(other.idFilter)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Filter [user=" + user + ", name=" + name + ", isPublic="
				+ isPublic + ", filterItems=" + filterItems + "]";
	}
}
