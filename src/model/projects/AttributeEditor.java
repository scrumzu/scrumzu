package model.projects;

import java.beans.PropertyEditorSupport;

public class AttributeEditor extends PropertyEditorSupport{
	public void setAsText(String text){
		if( text == "" || text == null ){
			setValue(new Attribute());
		}
	}
}
