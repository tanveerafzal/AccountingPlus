package com.ultrasoft.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

public class AlternateTag extends BaseTag {
	private ArrayList<String> strings;
	
	public AlternateTag(){
		strings = new ArrayList<String>();
	}
	
	public void setItems(String items){
		strings.clear();
		
		if(items != null){
			StringTokenizer tokenizer = new StringTokenizer(items, ",");
			
			while(tokenizer.hasMoreTokens()){
				strings.add(tokenizer.nextToken());
			}
		} // if items is not null
	}

	@Override
	public int doStartTag() throws JspException {
		// get the parent index, if found, determine the value and next
		// increment the value
		AlternateParentTag parent = getParentTag();
		
		try {
			if (parent != null) {
				int val = parent.getValue();
				parent.increment();

				int size = strings.size();

				if (size > 0) {
					int adjusted = val % size;
					String toPrint = strings.get(adjusted);

					JspWriter out = pageContext.getOut();

					out.print(toPrint);
				}
			}
		} 
		catch (Exception e) {
			throw new JspException(e);
		}		
		
		return SKIP_BODY;
	}

	private AlternateParentTag getParentTag() {
		AlternateParentTag result = null;
		Tag current = this;
		
		while(result == null){
			current = current.getParent();
			
			if(current == null){
				break;
			}
			else {
				if(current instanceof AlternateParentTag){
					result = (AlternateParentTag) current;
				}
			}
		}
		
		return result;
	}
	
	
}
