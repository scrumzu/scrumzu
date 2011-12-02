package model.projects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**Class representing project specific attributes, added to customize PBIs representation.
 * Product Owner can add unlimited number of additional attributes, of type String or Double
 * @author TM
 * @see {@link Project}
 */
@Entity
@Table(name = "Attributes")
public class Attribute {

	/**
	 * Id of attribute
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAttribute;

	/**
	 * Name (title) of attribute, visible in forms and tables presenting PBIs
	 */
	@Basic
	@Column(unique = true, nullable = false)
	@Size(min = 3, max = 30, message = "Name must have between 3 to 30 characters.")
	private String name;

	/**
	 * Type of attribute. Can be either String or Double
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "type", updatable = false, nullable = false)
	private AttributeType attributeType;

	/**
	 * Project in which additional attribute is added
	 */
	@ManyToOne
	@JoinColumn(name = "idProject", updatable = false, nullable = false)
	private Project project;

	/**
	 * Public constructor
	 */
	public Attribute() {
	}

	/**Public constructor for attribute with given name
	 * @param name - new attribute name
	 */
	public Attribute(String name) {
		this.name = name;
	}

	/**Public constructor for attribute with given name and type
	 * @param name - new attribute name
	 * @param attributeType - new attribute type
	 */
	public Attribute(String name, AttributeType attributeType) {
		super();
		this.name = name;
		this.attributeType = attributeType;
	}

	/**Public constructor to add attribute wtih given name and type to chosen project
	 * @param name - new attribute name
	 * @param attributeType - new attribute type
	 * @param project - chosen project to add attribute into
	 */
	public Attribute(String name, AttributeType attributeType, Project project) {
		super();
		this.name = name;
		this.attributeType = attributeType;
		this.project = project;
	}

	/**Public constructor with given attribute id
	 * @param id - new attribute id
	 */
	public Attribute(long id) {
		idAttribute = id;
	}

	/**Getter of attribute id
	 * @return attribute id
	 */
	public Long getIdAttribute() {
		return idAttribute;
	}

	/**Setter of attribute id
	 * @param idAttribute - attribute id to be set
	 */
	public void setIdAttribute(Long idAttribute) {
		this.idAttribute = idAttribute;
	}

	/**Getter of attribute name
	 * @return attribute name
	 */
	public String getName() {
		return name;
	}

	/**Setter of attribute name
	 * @param name - name to be set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**Getter of attribute type
	 * @return attribute type
	 * @see {@link AttributeType}
	 */
	public AttributeType getType() {
		return attributeType;
	}

	/**Setter of project in which attribute is added
	 * @param project - project to be set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**Getter of project in which attribute is added
	 * @return
	 */
	public Project getProject() {
		return project;
	}

	/**Setter of attribute type
	 * @param attributeType
	 * @see {@link AttributeType}
	 */
	public void setType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((attributeType == null) ? 0 : attributeType.hashCode());
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
		if (!(obj instanceof Attribute)) {
			return false;
		}
		Attribute other = (Attribute) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (attributeType != other.attributeType) {
			return false;
		}
		return true;
	}


	/**Returns camel name of attribute, used in css ids in views
	 * @return attribute name in camelCase
	 */
	public String getCamelName() {
		StringBuffer sb = new StringBuffer();
		String[] words = name.split(" ");
		sb.append(words[0].toLowerCase());

		for (int i = 1; i < words.length; i++) {
			sb.append(words[i].substring(0, 1).toUpperCase());
			sb.append(words[i].substring(1).toLowerCase());
		}
		return sb.toString();
	}

	public String toString() {
		return "Attribute:" + name + " type: " + attributeType + " id: "
				+ idAttribute;
	}

}
