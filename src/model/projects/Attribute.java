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

@Entity
@Table(name = "Attributes")
public class Attribute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAttribute;

	@Basic
	@Column(unique = true, nullable = false)
	@Size(min = 3, max = 30, message = "Name must have between 3 to 30 characters.")
	private String name;

	@Enumerated(EnumType.ORDINAL)
	@Column(name="type", updatable = false, nullable = false)
	private AttributeType attributeType;

	@ManyToOne
	@JoinColumn(name="idProject", updatable = false, nullable = false)
	private Project project;

	public Attribute() {}

	public Attribute(String name){
		this.name = name;
	}

	public Attribute(String name, AttributeType attributeType) {
		super();
		this.name = name;
		this.attributeType = attributeType;
	}

	public Attribute(String name, AttributeType attributeType, Project project) {
		super();
		this.name = name;
		this.attributeType = attributeType;
		this.project = project;
	}

	public Attribute(long id) {
		idAttribute=id;
	}

	public Long getIdAttribute() {
		return idAttribute;
	}

	public void setIdAttribute(Long idAttribute) {
		this.idAttribute = idAttribute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttributeType getType() {
		return attributeType;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	public void setType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((attributeType == null) ? 0 : attributeType.hashCode());
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

	public String getCamelName(){
		StringBuffer sb = new StringBuffer();
		String[] words = name.split(" ");
		sb.append(words[0].toLowerCase());

		for(int i=1; i<words.length; i++){
			sb.append(words[i].substring(0,1).toUpperCase());
			sb.append(words[i].substring(1).toLowerCase());
		}
		return sb.toString();
	}

	public String toString(){
		return "Attribute:"+ name +" type: "+ attributeType +" id: " + idAttribute;
	}







}
