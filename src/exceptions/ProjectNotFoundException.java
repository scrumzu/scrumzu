package exceptions;

public class ProjectNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	private String alias;

	public ProjectNotFoundException(String alias){
		this.alias = alias;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

}
