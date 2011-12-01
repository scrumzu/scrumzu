package model.sprint;

public enum SprintStatus {
	
	CREATED("Created"),
	STARTED("Started"),
	ENDED("Ended");
	
	private String value="";

	SprintStatus(String value){
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
