package model.filters;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/** Class representing filter item, containg one logical rule for a filter
 * 
 * @author TW
 *
 */
@Embeddable
@Table(name="FilterItems")
public class FilterItem {
	
	/**
	 * Logical connector. Should be "and", "or" or null
	 */
	@Size(min=1, max=5)
	@Basic
	private String andOr;
	
	/**
	 *  PBI field name. Should be one of the following values: 
	 * <ul>
	 * <li>title</li>
	 * <li>description</li>
	 * <li>priority</li>
	 * <li>team</li>
	 * <li>sprint</li>
	 * <li>status</li>
	 * <li>storyPoints</li>
	 * </ul>
	 * 
	 */

	@Size(min=1, max=45)
	@Basic
	private String field;
	
	/**
	 * Logical operator. Should be "=" or "!="
	 */
	@Size(min=1, max=5)
	@Basic
	private String operator;
	
	/**
	 * Value to compare with a value of the field
	 */
	@Size(min=0, max=60, message="Value must have 0 to 60 characters")
	@Basic
	private String value;

	/** Default constructor*/
	public FilterItem(){}
	
	/** Default constructor
	 * 
	 * @param andOr - Logical connector. Should be "and","or" or null
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
	 * @param operator - Logical operator. Should be "=" or "!="
	 * @param value - Value to compare with a value of the field
	 */
	public FilterItem(String andOr,	String field, String operator, String value) {
		this.andOr = andOr;
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
	/** Function which returns logical connector: "and","or" or null
	 * 
	 * @return "and","or" or null
	 */
	public String getAndOr() {
		return andOr;
	}
	/** 
	 * Function setting logical connector
	 * @param andOr - "and","or" or null
	 */
	public void setAndOr(String andOr) {
		this.andOr = andOr;
	}
	/** Function which returns field name
	 * 
	 * @return field name
	 */
	public String getField() {
		return field;
	}

	/**
	 * Function which sets field name
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * Function which returns logical operator of the filter item
	 * @return Logical operator of filter items
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * Function which sets logical operator to the filter item
	 * @param operator - Logical operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * Function which returns value to compare of the filter item
	 * @return value to compare
	 */
	public String getValue() {
		return value;
	}
	/** Function which sets value to the filter item
	 * 
	 * @param value - Value to compare
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((andOr == null) ? 0 : andOr.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		FilterItem other = (FilterItem) obj;
		if (andOr == null) {
			if (other.andOr != null)
				return false;
		} else if (!andOr.equals(other.andOr))
			return false;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FilterItem [andOr=" + andOr + ", field=" + field
				+ ", operator=" + operator + ", value=" + value + "]";
	}


	
	
}
