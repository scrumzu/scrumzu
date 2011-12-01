package model.users;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import services.ScrumzuAuthorityService;

public class ScrumzuAuthorityEditor extends PropertyEditorSupport {
	ScrumzuAuthorityService authorityService;
	Logger l = Logger.getLogger(getClass());

	public ScrumzuAuthorityEditor(ScrumzuAuthorityService authorityService){
		this.authorityService = authorityService;
	}


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		l.info("AUTHORITY setting as text: " + text);
		if(text == null  || "".equals(text) || "null".equals(text)){
			setValue(null);
		}
		else{
			setValue(authorityService.getAuthority(Long.parseLong(text)));
		}
	}

	@Override
	public String getAsText() {
		ScrumzuAuthority s = (ScrumzuAuthority) getValue();
		if (s == null) {
			return null;
		} else {
			l.info("AUTHORITY returning as text:" + s.getAuthority());
			return s.getIdAuthority()+"";
		}
	}


}
