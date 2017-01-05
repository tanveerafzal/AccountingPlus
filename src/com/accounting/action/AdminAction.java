package com.accounting.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.accounting.dao.ManagementDao;
import com.accounting.dao.ReferenceDao;
import com.accounting.data.ChargeFeeInfo;
import com.accounting.data.RefDataInfo;
import com.accounting.data.UserDataInfo;
import com.accounting.form.AdminForm;
import com.accounting.form.RegisterForm;
import com.accounting.utils.JspConstants;

public class AdminAction extends BaseAction {
	public AdminAction(){}

	public ActionForward showStoreRefInfo(ActionMapping mapping, ActionForm frm, 
					HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		try {
			AdminForm form = getAdminForm(req);
			ActionErrors actionErrors = new ActionErrors();
			UserDataInfo userData = (UserDataInfo)req.getSession().getAttribute("UserDataInfo");
			if (userData== null || userData.getRefDataInfo() == null){
				actionErrors.add("getLogin", new ActionMessage("error.login.invalid"));
				saveErrors(req,actionErrors);
				return getForward(result, JspConstants.LOGIN_JSP);	
			}
			
			int storeId= userData.getRefDataInfo().getStoreId();
			form.setStoreId(storeId);
			form.setStoreAddress(userData.getRefDataInfo().getStoreAddress());
			form.setStoreName(userData.getRefDataInfo().getStoreName());
			form.setGstRate(userData.getRefDataInfo().getGstRate());
			form.setNsfFee(userData.getRefDataInfo().getNsfFee());
			form.setChargeFeeList(userData.getRefDataInfo().getChargeFeeList());
			System.out.println("userData.getRefDataInfo().getChargeFee() "+userData.getRefDataInfo().getChargeFeeList().size());
			return getForward(result, JspConstants.STORE_INFO_JSP);
			
		//store_info
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.LOGIN_JSP);
	}
	
	
public ActionForward updateStoreRefInfo(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
try {
	AdminForm form = getAdminForm(req);
	ActionErrors actionErrors = new ActionErrors();
	UserDataInfo userData = (UserDataInfo)req.getSession().getAttribute("UserDataInfo");
	if (userData== null || userData.getRefDataInfo() == null){
		actionErrors.add("getLogin", new ActionMessage("error.login.invalid"));
		saveErrors(req,actionErrors);
		return getForward(result, JspConstants.LOGIN_JSP);	
	}
	
	System.out.println("New GST Rate: "+form.getGstRate()+" for storeid "+form.getStoreId());
	ReferenceDao dao = new ReferenceDao();
	userData.getRefDataInfo().setChargeFeeList(form.getChargeFeeList());
	userData.getRefDataInfo().setNsfFee(form.getNsfFee());
	userData.getRefDataInfo().setGstRate(form.getGstRate());
	userData.getRefDataInfo().setStoreAddress(form.getStoreAddress());
	userData.getRefDataInfo().setStoreLogo(form.getStoreLogo());
	userData.getRefDataInfo().setStoreName(form.getStoreName());

	
	dao.updateRefDataInfo(userData);
	ArrayList<ChargeFeeInfo > chargeFeeList = form.getChargeFeeList();
	for (ChargeFeeInfo chargeFeeInfo: chargeFeeList)
	{
		System.out.println("New GST Rate: "+chargeFeeInfo.getChargeFee());
	}
	
	return getForward(result, JspConstants.STORE_INFO_JSP);
	
//store_info
}
catch (Exception e) {
	e.printStackTrace();
}
return getForward(result, JspConstants.STORE_INFO_JSP);
}
public ActionForward addStoreRefInfo(ActionMapping mapping, ActionForm frm, 
		HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
try {
		AdminForm form = getAdminForm(req);
		System.out.println("addStoreRefInfo GST Rate: "+form.getGstRate()+" for storeid "+form.getStoreId());
		ActionErrors actionErrors = new ActionErrors();
		
		
		if(isEmpty(form.getStoreName())){
			actionErrors.add("getLogin", new ActionMessage("error.storename.missing"));
		}
		if(isEmpty(form.getStoreAddress())){
			actionErrors.add("getLogin", new ActionMessage("error.storeaddress.missing"));
		}
	
		RegisterForm 	registerForm = form.getRegisterForm();
		System.out.println("registerForm: "+registerForm.getFirstName()+" for storeid "+registerForm.getLastName());
		if(isEmpty(registerForm.getLogin())){
			actionErrors.add("getLogin", new ActionMessage("error.login.missing"));
		}
		if(isEmpty(registerForm.getPassword())){
			actionErrors.add("getPassword", new ActionMessage("error.password.missing"));
		}
		if(isEmpty(registerForm.getFirstName())){
			actionErrors.add("getFirstName", new ActionMessage("error.firstName.missing"));
		}
		if(isEmpty(registerForm.getLastName())){
			actionErrors.add("getLastName", new ActionMessage("error.lastName.missing"));
		}
		
		
		saveErrors(req,actionErrors);
		System.out.println("saveErrors have errors: "+actionErrors.size());
		if (actionErrors.size() > 0)
		{
			System.out.println("saveErrors have errors: "+form.getStoreId());
			return getForward(result, JspConstants.REGISTER_STORE_INFO_JSP);
		}

		UserDataInfo userData = new 		UserDataInfo();
		RefDataInfo refInfo = new RefDataInfo();
		
		System.out.println("New GST Rate: "+form.getGstRate()+" for storeid "+form.getStoreId());
		ReferenceDao dao = new ReferenceDao();
		//userData.getRefDataInfo().setChargeFeeList(form.getChargeFeeList());
		refInfo.setNsfFee(form.getNsfFee());
		refInfo.setGstRate(form.getGstRate());
		refInfo.setStoreAddress(form.getStoreAddress());
		refInfo.setStoreLogo(form.getStoreLogo());
		refInfo.setStoreName(form.getStoreName());
		refInfo.setEmail(form.getEmail());
		refInfo.setPhone(form.getPhone());
		refInfo.setContactName(form.getContactName());
		userData.setRefDataInfo(refInfo);
		
		if(dao.checkIfStoreExist(refInfo))
				{
					actionErrors.add("getLastName", new ActionMessage("error.storename.exist",form.getStoreName()));
					saveErrors(req,actionErrors);
					System.out.println("saveErrors have errors: "+actionErrors.size());
					return getForward(result, JspConstants.REGISTER_STORE_INFO_JSP);
				}

		ManagementDao mdao  = new ManagementDao();
		
		if(mdao.checkUserExist(registerForm.getLogin()))
		{
			actionErrors.add("getLastName", new ActionMessage("error.username.exist",registerForm.getLogin()));
			saveErrors(req,actionErrors);
			System.out.println("saveErrors have errors: "+actionErrors.size());
			return getForward(result, JspConstants.REGISTER_STORE_INFO_JSP);
		}

		dao.addRefDataInfo(userData);
//		ArrayList<ChargeFeeInfo > chargeFeeList = form.getChargeFeeList();
//		for (ChargeFeeInfo chargeFeeInfo: chargeFeeList)
//		{
//			System.out.println("New GST Rate: "+chargeFeeInfo.getChargeFee());
//		}
//		}
		
		if (actionErrors.isEmpty())
			{
			
				saveErrors(req,actionErrors);
				registerForm.setStoreId(userData.getStoreId());
				registerForm.setRole(1);
				String[] roles = new String[1];
				roles[0] = "1";
				registerForm.setRoles(roles);
				System.out.println("erros saved  "+registerForm.getPassword());
				
				mdao.registerUser(registerForm);
				form.clear();
			}
		
		return getForward(result, JspConstants.STORE_INFO_SUCCESS_JSP);
		
		//store_info
	}
	catch (Exception e) {
		e.printStackTrace();
	}		
	return getForward(result, JspConstants.LOGIN_JSP);
}
	
	
//changeBankChanges changeNsf
	
}
