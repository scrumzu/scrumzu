package model.users;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import services.ScrumzuUserDetailsService;

public class ScrumzuUserEditor extends PropertyEditorSupport {
	ScrumzuUserDetailsService scrumzuUserService;
	Logger l = Logger.getLogger(getClass());

	public ScrumzuUserEditor(ScrumzuUserDetailsService scrumzuUserService){
		this.scrumzuUserService=scrumzuUserService;
	}


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		l.info("ScrumzuUser setting as text: " + text);
		if(text == null  || "".equals(text) || "null".equals(text)){
			setValue(null);
		}
		else{
			setValue(scrumzuUserService.getUser(Long.parseLong(text)));
		}
	}

	@Override
	public String getAsText() {
		ScrumzuUser user = (ScrumzuUser) getValue();
		if (user == null) {
			return null;
		} else {
			l.info("ScrumzuUser returning as text:" + user.getIdUser());
			return user.getIdUser()+"";
		}
	}
}
