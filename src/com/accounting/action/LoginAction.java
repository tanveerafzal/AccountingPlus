package com.accounting.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.taglibs.standard.lang.jstl.Logger;

import com.accounting.dao.ManagementDao;
import com.accounting.dao.ReferenceDao;
import com.accounting.data.RefDataInfo;
import com.accounting.data.UserDataInfo;
import com.accounting.form.LoginForm;
import com.accounting.utils.JspConstants;


public class LoginAction extends BaseAction {
	public LoginAction(){}
	private static Logger log = Logger.getLogger(LoginAction.class);
	

	public ActionForward submit(ActionMapping mapping, ActionForm frm, 
					HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		ActionErrors actionErrors = new ActionErrors();
		try {
			LoginForm form = getLoginForm(req);
			System.out.println("Login "+form.getLogin());
			log.debug("Login "+form.getLogin());
			if(isEmpty(form.getLogin())){
				actionErrors.add("getLogin", new ActionMessage("error.login.missing"));

			}
			if(isEmpty(form.getPassword())){
				actionErrors.add("getPassword", new ActionMessage("error.password.missing"));

			}
			
			saveErrors(req,actionErrors);
			System.out.println("erros saved  "+form.getPassword());
			
				if (actionErrors.isEmpty())
				{
					ManagementDao dao  = new ManagementDao();
					UserDataInfo user = dao.getLoginInfo(form.getLogin(),form.getPassword());
					
					if (user != null)
					{
						ReferenceDao refDao = new ReferenceDao();
						System.out.println("UserDataInfo getStoreId "+user.getStoreId());
						RefDataInfo refData = refDao.getRefDataInfo(user.getStoreId());
						Map userCodeToNameMapping = refDao.getUserCodeToNameMapping(user.getStoreId());
						System.out.println("userCodeToNameMapping loaded "+userCodeToNameMapping.size());
						user.setRefDataInfo(refData);
						req.getSession().setAttribute("UserDataInfo",user);
						req.getSession().setAttribute("UserCodeToNameMapping",userCodeToNameMapping);
						
						System.out.println("userCodeToNameMapping 303 = "+userCodeToNameMapping.get("303"));
						System.out.println("sending to success jsp ");
						return getForward(result, JspConstants.INDEX_JSP);
					}
					else
					{
						actionErrors.add("getLogin", new ActionMessage("error.login.invalid"));
						saveErrors(req,actionErrors);
						req.getSession().setAttribute("UserDataInfo",null);
						return getForward(result, JspConstants.LOGIN_JSP);
					}
				
				}
				else
				{
					System.out.println("sending to login jsp ");
					return getForward(result, JspConstants.LOGIN_JSP);
				}
			
				
		}
		catch (Exception e) {
			e.printStackTrace();
			actionErrors.add("getLogin", new ActionMessage("error.login.invalid"));
			saveErrors(req,actionErrors);
		}		
		
		return getForward(result, JspConstants.LOGIN_JSP);
	}
	


public ActionForward logout(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
try {
		req.getSession().invalidate();
}
catch (Exception e) {
	e.printStackTrace();
}		

return getForward(result, JspConstants.LOGIN_JSP);
}







}
