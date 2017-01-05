package com.accounting.utils;

import javax.servlet.http.HttpServletRequest;


public class AttributeHelper {
    public static Object getObjectForm(HttpServletRequest request, String key, Class crtClass, boolean inRequest)  {
		Object result = null;
		if(inRequest){
			result = request.getAttribute(key);
		}
		else {
			result = request.getSession().getAttribute(key);
		}
		if(result == null){
            try {
                result = crtClass.newInstance();
            }
            catch (Exception e) {
            	throw new RuntimeException(Constants.ERROR_CREATING_FORM_OBJECT);
            }
			
			if(inRequest){
				request.setAttribute(key, result);
			}
			else {
				request.getSession().setAttribute(key, result);
			}
		}
		return result;
	}
}
