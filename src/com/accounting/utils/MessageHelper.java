package com.accounting.utils;

import javax.servlet.http.HttpServletRequest;


public class MessageHelper {

    public static String getResourceString(String key, HttpServletRequest request, Object[] values){
        String result = null;
        
        ResourceBundle bundle =
            request == null
                ? ResourceBundle.getBundle(Constants.RESOURCE_BUNDLE)
                : ResourceBundle.getBundle(
                    Constants.RESOURCE_BUNDLE,
                    request.getLocale());
        
        result = bundle.getString(key);

        if(result != null && values != null){
            // replace the strings with the current values
            for(int count = 0; count < values.length; count ++){
                
                if(values[count] != null){
                    String replace = "\\{" + count + "\\}";
                    result = result.replaceAll(replace, values[count].toString());
                }
            } // for
        }

        return result;
    }    
}

