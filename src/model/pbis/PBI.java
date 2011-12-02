package model.pbis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import model.projects.Attribute;
import model.projects.AttributeType;
import model.projects.Project;
import model.workItems.WorkItem;

/**Class representing PBI - Product Backlog Item.
 * PBIs are used to provide precise description of user stories, time and effort estimations and current work status.
 * @author TM
 * 
 */

@Entity
@Table(name = "PBIs")
public class PBI {

	/**
	 * PBI id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPBI;

	/**
	 * PBI title
	 */
	@Size(min = 3, max = 100, message = "Title must have between 3 to 100 characters.")
	@Basic
	private String title;

	/**
	 * PBI description - full text of user story
	 */
	@Basic
	private String description;

	/**
	 * Priority value set by Product Owner
	 */
	@Min(value = 0, message = "Priority must be a positive number")
	@Column(name = "priority", nullable = true)
	private int priority;

	/**
	 * Date when PBI was created
	 */
	@NotNull(message = "Creation date must not be blank")
	@Temporal(TemporalType.DATE)
	private Date dateCreation;

	/**
	 * Represents basic type of PBI
	 * @see {@link Type}
	 */
	@NotNull(message = "Type must not be blank")
	@Enumerated(EnumType.ORDINAL)
	private Type type;

	/**
	 * Project in which PBI is implemented.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idProject")
	private Project project;

	/**
	 * List of WorkItems - holds history of PBI implementation.
	 * @see {@link WorkItem}
	 */
	@OneToMany(mappedBy = "pbi")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private List<WorkItem> workItems = new ArrayList<WorkItem>();

	/**
	 * Transient container for double attributes, specific for project. Used only in PBI add/edit form
	 */
	@Transient
	private Map<String, Double> formDoubleAttributes = new HashMap<String, Double>();

	/**
	 * Transient container for string attributes, specific for project. Used only in PBI add/edit form
	 */
	@Transient
	private Map<String, String> formStringAttributes = new HashMap<String, String>();

	/**
	 * Container holding project specific attributes, type: String
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PBIs_StringAttributes", joinColumns = @JoinColumn(name = "idPBI"))
	@MapKeyJoinColumn(name = "idAttribute")
	@Column(name = "value")
	private Map<Attribute, String> stringAttributes = new HashMap<Attribute, String>();

	/**
	 * Container holding project specific attributes, type: Double
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PBIs_DoubleAttributes", joinColumns = @JoinColumn(name = "idPBI"))
	@MapKeyJoinColumn(name = "idAttribute")
	@Column(name = "value")
	private Map<Attribute, Double> doubleAttributes = new HashMap<Attribute, Double>();

	/**
	 * Basic, public contructor, used by SpringFramework
	 */
	public PBI() {
	}

	/** Public constructor
	 * @param title - PBI title
	 * @param description - PBI description, eg. user story full text
	 * @param priority - priority value set by Product Owner
	 * @param dateCreation - date of creation
	 * @param type - PBI type
	 * @param project - project in which PBI is implemented
	 */
	public PBI(String title, String description, int priority,
			Date dateCreation, Type type, Project project) {
		super();
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.dateCreation = dateCreation;
		this.type = type;
		this.project = project;
	}

	/**Public constructor for creation of PBI with given id
	 * @param idPBI - id of PBI
	 */
	public PBI(long idPBI) {
		this.idPBI = idPBI;
	}

	/**Getter of PBI type
	 * @return {@link Type}
	 */
	public Type getType() {
		return type;
	}

	/**Setter of PBI type
	 * @param type - enum value - {@link Type}
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**Getter of project in which PBI is implemented
	 * @return project {@link Project}
	 */
	public Project getProject() {
		return project;
	}

	/**Setter of project in which PBI is implemented
	 * @param project - project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**Setter of PBI id number
	 * @param idPBI - unique id number
	 */
	public void setIdPBI(Long idPBI) {
		this.idPBI = idPBI;
	}


	/**Getter of PBI id number
	 * @return - id number
	 */
	public Long getIdPBI() {
		return idPBI;
	}

	/**Getter of PBI title
	 * @return PBI title
	 */
	public String getTitle() {
		return title;
	}

	/**Setter of PBI title
	 * @param title - title to be set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**Getter of PBI description
	 * @return PBI description
	 */
	public String getDescription() {
		return description;
	}

	/**Setter of PBI description
	 * @param description - text to be set as description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**Getter of PBI priority
	 * @return priority - value set by Product Owner
	 */
	public int getPriority() {
		return priority;
	}

	/**Setter of PBI priority
	 * @param priority - number to be set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**Getter of date of creation
	 * @return date of creation
	 */
	public Date getDateCreation() {
		return dateCreation;
	}

	/**Setter for date of creation
	 * @param dateCreation - date to be set
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**Getter for list of work items (PBI history records)
	 * @return WorkItem list {@link WorkItem}
	 */
	public List<WorkItem> getWorkItems() {
		return workItems;
	}

	/**Setter for project specific attributes, type: Double. Used in add/edit pbi form only
	 * @param formDoubleAttributes - map containing attribute name and its numeric value
	 */
	public void setFormDoubleAttributes(Map<String, Double> formDoubleAttributes) {
		this.formDoubleAttributes = formDoubleAttributes;
	}

	/**Setter for project specific attributes, type: String. Used in add/edit pbi form only
	 * @param formDoubleAttributes - map containing attribute name and its text value
	 */
	public void setFormStringAttributes(Map<String, String> formStringAttributes) {
		this.formStringAttributes = formStringAttributes;
	}

	/**Method for adding new Dobule atribute with its value to attributes collection
	 * @param attribute - attribute to be added
	 * @param value - value for chosen attribute
	 */
	public void setDoubleAttribute(Attribute attribute, double value) {
		if (attribute == null) {
			throw new NullPointerException();
		}
		doubleAttributes.put(attribute, value);
	}


	/**Method for adding new Dobule atribute with its value to attributes collection
	 * @param attribute - attribute to be added
	 * @param value - value for chosen attribute
	 */
	public void setStringAttribute(Attribute attribute, String value) {
		if (attribute == null || value == null) {
			throw new NullPointerException();
		}
		stringAttributes.put(attribute, value);
	}

	/**Setter of String attributes in given PBI. Allows you to entirely replace current map.
	 * @param stringAttributes - new attribute-value map to be set
	 */
	public void setStringAttributes(Map<Attribute, String> stringAttributes) {
		this.stringAttributes = stringAttributes;
	}

	/**Setter of Double attributes in given PBI. Allows you to entirely replace current map.
	 * @param stringAttributes - new attribute-value map to be set
	 */
	public void setDoubleAttributes(Map<Attribute, Double> doubleAttributes) {
		this.doubleAttributes = doubleAttributes;
	}

	/**Getter of chosen double attribute value
	 * @param name - name of attribute
	 * @return value of given attribute
	 */
	public double getDoubleAttribute(String name) {
		return doubleAttributes.get(new Attribute(name, AttributeType.DOUBLE));
	}

	/**Getter of chosen string attribute value
	 * @param name - name of attribute
	 * @return value of given attribute
	 */
	public String getStringAttribute(String name) {
		return stringAttributes.get(new Attribute(name, AttributeType.STRING));
	}


	/**Getter for map of all string attributes, specific for connected project.
	 * @return map of string attributes and values
	 */
	public Map<Attribute, String> getStringAttributes() {
		return stringAttributes;
	}

	/**Getter for map of all double attributes, specific for connected project.
	 * @return map of string attributes and values
	 */
	public Map<Attribute, Double> getDoubleAttributes() {
		return doubleAttributes;
	}


	/** Method to delete chosen attribute from either double attributes or string attributes map.
	 * @param attribute - attribute to be deleted
	 */
	public void deleteAttribute(Attribute attribute) {
		if (attribute.getType() == AttributeType.DOUBLE) {
			doubleAttributes.remove(attribute);
		} else {
			stringAttributes.remove(attribute);
		}
	}

	/**Setter of workItems list (PBI history records)
	 * @param workItems - new list to be set
	 */
	public void setWorkItems(List<WorkItem> workItems) {
		this.workItems = workItems;
	}

	/**Method used for proper PBI filtering in Backlog view
	 * @param fieldName - considered field name of PBI or its WorkItem
	 * @return value of chosen field
	 */
	public Object getValueByField(String fieldName) {
		Object result="";

		if(fieldName.equals("title")) {
			result= getTitle();
		} else if(fieldName.equals("description")) {
			result= getDescription();
		} else if(fieldName.equals("priority")) {
			result= getPriority();
		} else if(fieldName.equals("dateCreation")) {
			result= getDateCreation().toString();
		} else if(fieldName.equals("type")) {
			result= getType().name();
		} else  {
			result = workItems.get(0).getValueByField(fieldName);
		}

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((idPBI == null) ? 0 : idPBI.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		PBI other = (PBI) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (idPBI == null) {
			if (other.idPBI != null) {
				return false;
			}
		} else if (!idPBI.equals(other.idPBI)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		return true;
	}

	public String toString() {
		return "PBI [title=" + title + ", project=" + project
				+ " dateCreation=" + dateCreation + ", type=" + type + ", priority=" + priority + "]";
	}

	/**Getter of string attributes map, used in add/edit PBI form only
	 * @return map of string attributes names and values
	 */
	public Map<String, String> getFormStringAttributes() {
		return formStringAttributes;
	}

	/**Getter of double attributes map, used in add/edit PBI form only
	 * @return map of double attributes names and values
	 */
	public Map<String, Double> getFormDoubleAttributes() {
		return formDoubleAttributes;
	}

}
