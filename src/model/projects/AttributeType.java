package model.projects;

public enum AttributeType {
	DOUBLE("Double"),
	STRING("String");

	private String value="";

	AttributeType(String value){
		this.value= value;
	}

	public void setValue(){}

	public String getValue(){
		return value;
	}

	public String toString(){
		return value;
	}
}
