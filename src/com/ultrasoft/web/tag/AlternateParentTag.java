package com.ultrasoft.web.tag;

import javax.servlet.jsp.JspException;

public class AlternateParentTag extends BaseTag {
	private int index; 
	
	public AlternateParentTag(){
	}
	
	public void increment(){
		index ++;
	}
	
	public int getValue(){
		return index;
	}

	@Override
	public int doStartTag() throws JspException {
		index = 0;
		
		return EVAL_BODY_INCLUDE;
	}
}
