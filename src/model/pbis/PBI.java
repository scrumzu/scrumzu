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

	/**Function for getting PBI type
	 * @return {@link Type}
	 */
	public Type getType() {
		return type;
	}

	/**Function which sets PBI type
	 * @param type - enum value - {@link Type}
	 */
	public void setType(Type type) {
		this.type = type;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setIdPBI(Long idPBI) {
		this.idPBI = idPBI;
	}

	public Long getIdPBI() {
		return idPBI;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

	public void setFormDoubleAttributes(Map<String, Double> formDoubleAttributes) {
		this.formDoubleAttributes = formDoubleAttributes;
	}

	public void setFormStringAttributes(Map<String, String> formStringAttributes) {
		this.formStringAttributes = formStringAttributes;
	}

	public void setDoubleAttribute(Attribute attribute, double value) {
		if (attribute == null) {
			throw new NullPointerException();
		}
		doubleAttributes.put(attribute, value);
	}

	public void setStringAttributes(Map<Attribute, String> stringAttributes) {
		this.stringAttributes = stringAttributes;
	}

	public void setDoubleAttributes(Map<Attribute, Double> doubleAttributes) {
		this.doubleAttributes = doubleAttributes;
	}

	public void setStringAttribute(Attribute attribute, String value) {
		if (attribute == null || value == null) {
			throw new NullPointerException();
		}
		stringAttributes.put(attribute, value);
	}

	public double getDoubleAttribute(String name) {
		return doubleAttributes.get(new Attribute(name, AttributeType.DOUBLE));
	}

	public String getStringAttribute(String name) {
		return stringAttributes.get(new Attribute(name, AttributeType.STRING));
	}

	public Map<Attribute, String> getStringAttributes() {
		return stringAttributes;
	}

	public Map<Attribute, Double> getDoubleAttributes() {
		return doubleAttributes;
	}

	public void deleteAttribute(Attribute attribute) {
		if (attribute.getType() == AttributeType.DOUBLE) {
			doubleAttributes.remove(attribute);
		} else {
			stringAttributes.remove(attribute);
		}
	}

	public void setWorkItems(List<WorkItem> workItems) {
		this.workItems = workItems;
	}

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

	public Map<String, String> getFormStringAttributes() {
		return formStringAttributes;
	}

	public Map<String, Double> getFormDoubleAttributes() {
		return formDoubleAttributes;
	}

}
