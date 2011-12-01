package model.filters;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

import services.FilterService;

/** Class representing custom editor for Filter. Class required by spring framework.
 * @see Filter
 * @see PropertyEditorSupport
 * @author TW
 *
 */
public class FilterEditor extends PropertyEditorSupport {
	
	
	private Long id;
	FilterService filterService;
	Logger l = Logger.getLogger(getClass());

	/** 
	 * Public constructor 
	 * @param filterService - filter service
	 * @see FilterService
	 */
	public FilterEditor(FilterService filterService){
		this.filterService = filterService;
	}

	public void setFormat(Long format) {
		id = format;
	}
	
	public Long getFormat() {
		return this.id;
	}
	
	@Override 
	public void setAsText(String text){
		l.info("FILTER setting as text: " + text);
		if(text == null  || "".equals(text) || "null".equals(text)){
			setValue(null);
		}
		else{
			setValue(filterService.getFilterById(Long.parseLong(text)));
		}
	}

	@Override
	public String getAsText() {
		Filter f = (Filter) getValue();
		if (f == null) {
			return null;
		} else {
			l.info("FILTER returning as text:" + f.getIdFilter());
			return f.getIdFilter()+"";
		}
	}
}
