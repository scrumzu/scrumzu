package model.pbis;

public enum Type {
	TEST("Test"),
	IMPLEMENTATION("Implementation"),
	PERFORMANCE("Performance"),
	DESIGN("Design");


	private String value="";

	Type(String value){
		this.value= value;
	}

	public String getValue(){
		return value;
	}

	public void setValue(){}

	public String toString(){
		return value;
	}
}
