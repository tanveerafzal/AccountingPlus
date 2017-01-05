package com.accounting.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.accounting.dao.ManagementDao;
import com.accounting.data.Role;
import com.accounting.data.UserDataInfo;
import com.accounting.form.RegisterForm;
import com.accounting.utils.JspConstants;

public class RegisterAction extends BaseAction {
	public RegisterAction(){}

	
	
	
	public ActionForward gotoregister(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("gotoregister submitted ");
		RegisterForm form = getRegisterForm(req);
		try {
			ManagementDao dao  = new ManagementDao();
			ArrayList<Role> roles  = dao.getRoles(getStore_id(req));
			form.setRoleList(roles);
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("finalling sending to  REGISTER_JSP ");
	return getForward(result, JspConstants.REGISTER_JSP);
}
	public ActionForward submit(ActionMapping mapping, ActionForm frm, 
					HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		try {
			RegisterForm form = getRegisterForm(req);
			ActionErrors actionErrors = new ActionErrors();
			System.out.println("REGISTER  "+form.getLogin());
			
			if(isEmpty(form.getLogin())){
				actionErrors.add("getLogin", new ActionMessage("error.login.missing"));
			}
			if(isEmpty(form.getPassword())){
				actionErrors.add("getPassword", new ActionMessage("error.password.missing"));
			}
			if(isEmpty(form.getFirstName())){
				actionErrors.add("getFirstName", new ActionMessage("error.firstName.missing"));
			}
			if(isEmpty(form.getLastName())){
				actionErrors.add("getLastName", new ActionMessage("error.lastName.missing"));
			}
			if(form.getRoles().length==0){
				actionErrors.add("getLastName", new ActionMessage("error.lastName.missing"));
			}
		
			System.out.println("form.getRoles()   "+form.getRoles());
			saveErrors(req,actionErrors);
			form.setStoreId(getStore_id(req));
			System.out.println("erros saved  "+form.getPassword());
			if (actionErrors.isEmpty())
				{
					ManagementDao dao  = new ManagementDao();
					if(dao.checkUserExist(form.getLogin()))
					{
						actionErrors.add("getLastName", new ActionMessage("error.username.exist",form.getLogin()));
						saveErrors(req,actionErrors);
						System.out.println("saveErrors have errors: "+actionErrors.size());
						return getForward(result, JspConstants.REGISTER_JSP);
					}
					dao.registerUser(form);
					form.clear();
				}
				else
				{
					return getForward(result, JspConstants.REGISTER_JSP);
				}
				
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return listUsers(mapping, frm,req, resp);
	}
	
	public ActionForward updateUser(ActionMapping mapping, ActionForm frm, 
		HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			RegisterForm form = getRegisterForm(req);
			ActionErrors actionErrors = new ActionErrors();
			System.out.println("REGISTER  "+form.getLogin());
			
			if(isEmpty(form.getLogin())){
				actionErrors.add("getLogin", new ActionMessage("error.login.missing"));
			}
			if(isEmpty(form.getPassword())){
				actionErrors.add("getPassword", new ActionMessage("error.password.missing"));
			}
			if(isEmpty(form.getFirstName())){
				actionErrors.add("getFirstName", new ActionMessage("error.firstName.missing"));
			}
			if(isEmpty(form.getLastName())){
				actionErrors.add("getLastName", new ActionMessage("error.lastName.missing"));
			}
			saveErrors(req,actionErrors);
			form.setStoreId(getStore_id(req));
			System.out.println("erros saved  "+form.getPassword());
			
				if (actionErrors.isEmpty())
				{
					ManagementDao dao  = new ManagementDao();
					dao.updateUser(form);
					form.clear();
				}
				else
				{
					return getForward(result, JspConstants.REGISTER_JSP);
				}

		}
		catch (Exception e) {
				e.printStackTrace();
		}		
		return listUsers(mapping, frm,req, resp);
}
	
public ActionForward listUsers(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			try {
				RegisterForm form = getRegisterForm(req);
				int storeId = getStore_id(req);
				System.out.println("storeId  "+storeId);
				ManagementDao dao  = new ManagementDao();
				ArrayList<UserDataInfo>  users = dao.getRegisteredUsers(storeId);
				System.out.println("users list before  "+users.size());
				ArrayList<UserDataInfo>  newUsersList =  removeDuplicateUsers(users);
				form.setUsersList(newUsersList);
				System.out.println("users list after  "+newUsersList.size());
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
			return getForward(result, JspConstants.USER_MANAGEMENT_JSP);
			}

   private  ArrayList<UserDataInfo> removeDuplicateUsers(ArrayList<UserDataInfo> users) {
	   
	   ArrayList<UserDataInfo> newUsersList = new ArrayList<UserDataInfo>();
	   HashMap map = new HashMap();
	   if (users == null || users.size() == 0)
		   return newUsersList;
	   
	   for (int i=0; i< users.size(); i++ )
		{
		   UserDataInfo user = users.get(i);
		   System.out.println("Checking  "+user.getLogin());
		   if (map.containsKey(user.getLogin()))
		   {
			   System.out.println("Updateing "+user.getLogin());
			   UserDataInfo item =  (UserDataInfo) map.get(user.getLogin());
			   item.getRoles().add(""+user.getRole());
		   }else
		   {
			   System.out.println("inserting into map "+user.getLogin());
			   user.getRoles().add(""+user.getRole());
			   map.put(user.getLogin(), user);
		   }
		}
	   newUsersList.addAll(map.values());
	   return newUsersList;
}




public ActionForward editUser(ActionMapping mapping, ActionForm frm, 
		HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			
			
			
		
			 
			 
			RegisterForm form = getRegisterForm(req);
			String login = form.getEditId();
			System.out.println("editUser  "+login);
			UserDataInfo user= form.getUser(login);
			
			
			 
			 if ((!user.getRoles().contains("1"))  &&  (user.getRoles().contains("2")))  // PAYROLL ADMIN
			 {
				 
			 }
			 if (!user.getRoles().contains("1")  &&  user.getRoles().contains("3")) // CHARGE ADMIN
		     if (!user.getRoles().contains("1")  &&  user.getRoles().contains("4")) // YEARLY CHARGE ADMIN
		    	 if (!user.getRoles().contains("1")  && user.getRoles().contains("2"))  // PAYROLL ADMIN

			System.out.println("users found "+user.getFirstName());
			form.setLogin(user.getLogin());
			form.setPassword(user.getPassword());
			form.setFirstName(user.getFirstName());
			form.setLastName(user.getLastName());
			form.setRole(user.getRole());
			for (int i=0; i< user.getRoles().size(); i++)
			{
				form.getRoles()[i] = user.getRoles().get(i);
				System.out.println("currentRoles  "+user.getRoles().get(i));
			}
			System.out.println("currentRoles  "+form.getRoles());
			ManagementDao dao  = new ManagementDao();
			ArrayList<Role> roles  = dao.getRoles(getStore_id(req));
			System.out.println("roles  "+roles);
			form.setRoleList(roles);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.REGISTER_JSP);
		}
   public ActionForward deleteUser(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
			try {
				RegisterForm form = getRegisterForm(req);
				String login = form.getEditId();
				System.out.println("deleteUser  "+login);
				ManagementDao dao  = new ManagementDao();
				dao.deleteUser(login,getStore_id(req));
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
			return listUsers(mapping, frm,req, resp);
			}
}


