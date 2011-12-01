package model.pbis;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import services.PBIService;

public class PBIEditor extends PropertyEditorSupport {
	PBIService pbiService;
	Logger l = Logger.getLogger(getClass());

	public PBIEditor(PBIService pbiService){
		this.pbiService = pbiService;
	}


	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		l.info("PBI setting as text: " + text);
		if(text == null  || "".equals(text) || "null".equals(text)){
			setValue(null);
		}
		else{
			setValue(pbiService.getPBI(Long.parseLong(text)));
		}
	}

	@Override
	public String getAsText() {
		PBI pbi = (PBI) getValue();
		if (pbi == null) {
			return null;
		} else {
			l.info("PBI returning as text:" + pbi.getIdPBI());
			return pbi.getIdPBI()+"";
		}
	}


}
