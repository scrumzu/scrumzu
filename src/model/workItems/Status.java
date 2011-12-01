package model.workItems;

public enum Status {
	NEW("New"),
	PROPOSED_FOR_SPRINT("Proposed for sprint"),
	PREASSIGNED("Preassigned"),
	COMMITTED("Committed"),
	WORK_IN_PROGRESS("Work in progress"),
	DONE("Done"),
	DROPPED("Dropped");

	private String value="";

	Status(String value){
		this.value= value;
	}

	public String getValue(){
		return value;
	}

	public String getValueUpperCase(){
		return value.toUpperCase().replace(" ", "_");
	}

	public void setValue(){}

	public String toString(){
		return value;
	}
}