package com.accounting.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.accounting.dao.ReferenceDao;
import com.accounting.form.ChargeForm;
import com.accounting.form.CompanyForm;
import com.accounting.form.MonthlyNoteForm;
import com.accounting.form.PayrollForm;
import com.accounting.form.YearlyClientForm;
import com.accounting.utils.Constants;
import com.accounting.utils.JspConstants;

/**
 * @author ADYAN
 *
 */
public class CompanyAction extends BaseAction {
	


	public CompanyAction(){}


	public ActionForward updatePayroll(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("************************************************updatePayroll started ************************************************ ");
		try {
			CompanyForm form =(CompanyForm)frm  ;
			req.getSession().setAttribute(Constants.MONTH,form.getMonth());			
			req.getSession().setAttribute(Constants.YEAR,form.getYear());	
			System.out.println("updatePayroll MONTH "+form.getMonth());
			System.out.println("updatePayroll getCompanyCode "+form.getCompanyCode());
			ReferenceDao dao  = new ReferenceDao();
			ArrayList<CompanyForm> selectedList = form.getCompanyList();
			ArrayList<CompanyForm> databaseSavedList = getPayrollList(form);
			ArrayList<CompanyForm> originalList = form.getOriginalList();
			boolean overwrite = true;
			System.out.println("updatePayroll YEAR "+selectedList);
			if (selectedList!= null)
			{
				for (CompanyForm companyForm: selectedList)
				{
					PayrollForm payroll = companyForm.getPayroll();
					double totalBankCharge = calculateBankCharges(payroll.getRemittanceAmount(),req);
					payroll.setBankCharge(totalBankCharge);
					payroll.setTotalAmount(payroll.getRemittanceAmount()+totalBankCharge);
					payroll.setTotalAmountDue(payroll.getTotalAmount()+payroll.getPreviousBalance());
					if (payroll.getCompanyCode().equals("111"))
						System.out.println(" *************************************************");
					if (payroll.getNsf() != null && payroll.getNsf().equalsIgnoreCase("Y"))
					{
						if (payroll.getComment()== null || payroll.getComment().length() == 0)
							payroll.setComment(payroll.getPayrollMonth()+" NSF Amount $25 added");
						payroll.setRevisedAmount(payroll.getTotalAmountDue()+getNsfCharge(req));
					}
					else if (payroll.getNsf() != null && payroll.getNsf().equalsIgnoreCase("N"))
					{
						if (payroll.getRevisedAmount() > 0)
						{
							payroll.setRevisedAmount(0.0);
							payroll.setComment(null);
						}
					}
					payroll.setStore_id(getStore_id(req));
					
					if ("Q".equals(payroll.getPayrollFrequency()))
					{
						System.out.println(" +++++++++++++++++++PROCESSING QUATERLY PaYARILLS "+form.getAllPayrollSentToCRA());
						if ("Y".equals(form.getAllPayrollSentToCRA())){	
						System.out.println("All Payments sent to CRA "+form.getAllPayrollSentToCRA());
						System.out.println("updating payroll for conpany "+payroll.getCompanyCode());
						payroll.setSentToCRA("Y");
						System.out.println("All Payments sent to CRA "+payroll.getSentToCRA());
						}
					}
					
					CompanyForm originalInfo = getCompanyInfo(originalList,payroll);
					CompanyForm savedInfo = getCompanyInfo(databaseSavedList,payroll);
						
					if (savedInfo.getPayroll().hashValue().equals(originalInfo.getPayroll().hashValue()) || overwrite)
					{
						dao.updatePayroll(payroll);
						payroll.setPayrollChanged(false);
					}
					else
					{
						payroll.setPayrollChanged(true);
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$HashValue for Payrolls is different .. "+savedInfo.getCompanyCode());
						System.out.println("originalInfo "+originalInfo.getPayroll().hashValue());
						System.out.println("savedInfo    "+savedInfo.getPayroll().hashValue());
						System.out.println("payroll      "+payroll.hashValue());
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$HashValue for Payrolls is different .. "+savedInfo.getCompanyCode());
					}
				}
			}
			
			/**
			 * MONTHLY PAYROLLS ENDS
			 * QUARTELY PAYROLLS START HERE
			 *
			 */			
/*			System.out.println("Processing quartely remiiters ");
			ArrayList<CompanyForm > quarterlyRemitterList = form.getQuarterlyRemitterList();
			if (quarterlyRemitterList != null)
			{
				for (CompanyForm companyForm: quarterlyRemitterList)
				{
					PayrollForm payroll = companyForm.getPayroll();
					System.out.println("**************************** PROCESSING QUATERLY PAYROLLS  "+payroll.getCompanyCode());
					System.out.println("Payments sent to CRA "+payroll.getSentToCRA());
					if ("Q".equals(payroll.getPayrollFrequency()))
					{
						if ("Y".equals(form.getAllPayrollSentToCRA())){	
						System.out.println("All Payments sent to CRA "+form.getAllPayrollSentToCRA());
						System.out.println("updating payroll for conpany "+payroll.getCompanyCode());
						payroll.setSentToCRA("Y");
						System.out.println("All Payments sent to CRA "+payroll.getSentToCRA());
						}
						else{	
//							payroll.setSentToCRA("N");
							System.out.println("All Payments sent to CRA "+payroll.getSentToCRA());
							}
							
						
					}
					System.out.println("updating payroll  getCompanyCode "+payroll.getCompanyCode()+" getPayrollMonth "+payroll.getPayrollMonth());
					System.out.println("updating payroll "+payroll.getRemittanceAmount()+" getBankCharge  "+payroll.getBankCharge());
					double totalBankCharge = calculateBankCharges(payroll.getRemittanceAmount(),req);
					payroll.setBankCharge(totalBankCharge);
					payroll.setTotalAmount(payroll.getRemittanceAmount()+totalBankCharge);
					payroll.setTotalAmountDue(payroll.getTotalAmount()+payroll.getPreviousBalance());
					if (payroll.getCompanyCode().equals("111"))
						System.out.println(" *************************************************");
					if (payroll.getNsf() != null && payroll.getNsf().equalsIgnoreCase("Y"))
					{
						if (payroll.getComment()== null || payroll.getComment().length() == 0)
							payroll.setComment(payroll.getPayrollMonth()+" NSF Amount $25 added");
						payroll.setRevisedAmount(payroll.getTotalAmountDue()+getNsfCharge(req));
					}
					else if (payroll.getNsf() != null && payroll.getNsf().equalsIgnoreCase("N"))
					{
						if (payroll.getRevisedAmount() > 0)
						{
							payroll.setRevisedAmount(0.0);
							payroll.setComment(null);
						}
					}
					payroll.setStore_id(getStore_id(req));
					System.out.println("All Payments sent to CRA "+payroll.getSentToCRA());
					dao.updatePayroll(payroll);
				}
			}*/
			
			
			System.out.println("************************************************updatePayroll ENDED ************************************************ ");
			
			
			if (form.getAction().equals("add"))
			{
				return getForward(result, JspConstants.LIST_COMPANY_JSP);
			}
			else
			{
				return getForward(result, JspConstants.UPDATE_PAYROLLS_JSP);
			}
	
		}
		
		catch (Exception e) {
			e.printStackTrace();
			ActionErrors errors = new ActionErrors();
			errors.add("getCompanyCode", new ActionMessage("error.payroll.cannotupdate",e.getMessage()));
			saveErrors(req,errors);
		}		
	return getForward(result, JspConstants.LIST_COMPANY_JSP);
}	
	
	private CompanyForm getCompanyInfo(ArrayList<CompanyForm> originalList, PayrollForm payroll) {
	
		if (originalList!= null)
		{
			for (CompanyForm companyForm: originalList)
			{
				if (companyForm.getCompanyCode()!= null && payroll.getCompanyCode()!= null
						&& companyForm.getCompanyCode().equals(payroll.getCompanyCode()))
						{
							return companyForm;
						}
			}
		}
		return null;
	}


	public ActionForward yearlyCharge(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("yearlyCharge called ");
		ActionForward result = null;
		CompanyForm form = getCompanyForm(req);
		String companyCode = form.getEditId();
		System.out.println("addPayroll submitted  getEditId  "+form.getEditId());
		CompanyForm companyForm = form.getCompany(companyCode);
		System.out.println("company found "+companyForm);
		ReferenceDao dao = new ReferenceDao();
		if (companyForm != null && companyForm.getCompanyCode() != null)
		{
			
			form.setCompanyCode(companyForm.getCompanyCode());
			form.setCompanyName(companyForm.getCompanyName());
			form.setBnNumber(companyForm.getBnNumber());
			
			form.setAddress1(companyForm.getAddress1());
			form.setAddress2(companyForm.getAddress2());
			form.setCity(companyForm.getCity());
			form.setProvince(companyForm.getProvince());
			form.setPostalCode(companyForm.getPostalCode());
			form.setComment(companyForm.getComment());
			form.setBankAccNumber(dao.getBankAccountNumber1(companyForm.getCompanyCode(),getStore_id(req)));
			req.getSession().setAttribute("companyForm",companyForm);
			req.setAttribute("companyCode",companyForm.getCompanyCode());
		}
		return getForward(result, JspConstants.COMPANY_JSP);
}
 	
	
	
	
	public ActionForward updateCharge(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("updateCharge submitted ");
		try {
			CompanyForm form =(CompanyForm)frm  ;
			req.getSession().setAttribute(Constants.MONTH,form.getMonth());			
			req.getSession().setAttribute(Constants.YEAR,form.getYear());	
			System.out.println("updateCharge MONTH "+form.getMonth());
			System.out.println("updateCharge getCompanyCode "+form.getCompanyCode());
			ReferenceDao dao  = new ReferenceDao();
			
			ArrayList<CompanyForm > selectedList = form.getCompanyList();
			System.out.println("updatePayroll YEAR "+selectedList);
			if (selectedList!= null)
			{
				for (CompanyForm companyForm: selectedList)
				{
					ChargeForm charge = companyForm.getCharge();
					System.out.println("Charge amount "+charge.getTotal());
					System.out.println("updateCharge getCompanyCode "+charge.getCompanyCode()+" getMonthYear "+charge.getMonthYear());
					charge.setGst(calculateGST(charge.getNetAmount(),req));
					charge.setTotal(charge.getNetAmount()+calculateGST(charge.getNetAmount(),req));
					charge.setPreviousAmountBalance(charge.getPreviousAmount() - charge.getPreviousAmountInstallment());
					if (charge.getPreviousAmountBalance() < 0)
						charge.setPreviousAmountBalance(0);
					System.out.println("companyCode "+charge.getCompanyCode()+" getMonthYear "+charge.getMonthYear()+" getPreviousAmountGstIncluded="+charge.getPreviousAmountGstIncluded());
					double nsfCharge = 0.0;	
					if (charge.getNsf() != null && charge.getNsf().equalsIgnoreCase("Y"))
					{
						
						if (charge.getComments()== null || charge.getComments().length() == 0){
							if (charge.getPreviousCharges() > 0)
								charge.setComments(charge.getMonthYear()+"  NSF Amount $25 added || Previous Month NSF Charges Included");
							else
								charge.setComments(charge.getMonthYear()+" NSF Amount $25 added");
						}
						nsfCharge = getNsfCharge(req);
					}
					else if (charge.getNsf() != null && charge.getNsf().equalsIgnoreCase("N"))
					{
						System.out.println("Charge getComments "+charge.getComments());
						nsfCharge = 0.0;
						if (charge.getPreviousCharges() > 0)
							charge.setComments(" Previous Month NSF Charges Included");
						else
							charge.setComments(null);
					}

					

					if (NO.equals(charge.getPreviousAmountGstIncluded())){
						charge.setTotalWithdrawAmount(charge.getTotal()+charge.getPreviousAmountInstallment()+calculateGST(charge.getPreviousAmountInstallment(),req)+charge.getPreviousCharges()+nsfCharge);
					}
					else{
						charge.setTotalWithdrawAmount(charge.getTotal()+charge.getPreviousAmountInstallment()+charge.getPreviousCharges()+nsfCharge);
					}
					charge.setStore_id(getStore_id(req));
					dao.updateCharge(charge);
				}
			}
				return getForward(result, JspConstants.LIST_CHARGE_JSP);
		
		}
		catch (Exception e) {
			e.printStackTrace();
			ActionErrors errors = new ActionErrors();
			errors.add("getCompanyCode", new ActionMessage("error.charge.cannotupdate",e.getMessage()));
			saveErrors(req,errors);
		}		
	return getForward(result, JspConstants.LIST_CHARGE_JSP);
}	
	
	
public ActionForward selectmonth(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("selectmonth submitted ");
		try {
			CompanyForm form = getCompanyForm(req);
			form.setCompanyCode(null);
			form.setCompanyName(null);
			req.getSession().setAttribute(Constants.MONTH,form.getMonth());			
			req.getSession().setAttribute(Constants.YEAR,form.getYear());	
			System.out.println("selectmonth MONTH "+form.getMonth());
			System.out.println("selectmonth YEAR "+form.getYear());
			if (form.getCompanyList()!= null ) form.getCompanyList().clear();
			System.out.println("flow Seleced "+form.getFlow() );
			form.setStore_id(getStore_id(req));
			
			if (JspConstants.CHANGE_PAYROLL_FLOW.equals(form.getFlow()))
			{
				return searchpayrolls(mapping, form, req, resp);
			}
			
			ReferenceDao dao  = new ReferenceDao();
			System.out.println("Action "+form.getAction() );
			ArrayList<CompanyForm >  list =  dao.getCompanies(form, form.getAction());
			ArrayList<CompanyForm > newList = removeDuplicates(list);
			form.setCompanyList(newList);
			return getForward(result, form.getNextPage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	return getForward(result, JspConstants.LIST_COMPANY_JSP);
}
	
 public ActionForward gotoselectmonth(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("gotoselectmonth submitted ");
		try {
			String operation = req.getParameter("operation");
			
			CompanyForm form = getCompanyForm(req);
			System.out.println("user action "+form.getAction());
			form.setFlow(operation);
			if (req.getSession().getAttribute(Constants.MONTH) != null)
				form.setMonth((String)req.getSession().getAttribute(Constants.MONTH));
			if (req.getSession().getAttribute(Constants.YEAR) != null)
				form.setYear((String)req.getSession().getAttribute(Constants.YEAR));
			if (form.getAction()!= null && form.getAction().equals("add"))
			{
				System.out.println("sending to  LIST_COMPANY_JSP ");
				return getForward(result, JspConstants.LIST_COMPANY_JSP);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		System.out.println("finalling sending to  SELECT_MONTH_JSP ");
	return getForward(result, JspConstants.SELECT_MONTH_FOR_CHARGE_UPDATE_JSP);
}
 public ActionForward back(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("gotoselectmonth submitted ");
		return changePayroll(mapping, frm, req, resp);
}

 
 public ActionForward editCompany(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("editCompany called ");
		ActionForward result = null;
		CompanyForm form = getCompanyForm(req);
		String companyCode = form.getEditId();
		System.out.println("editCompany submitted  getEditId  "+form.getEditId());
		CompanyForm companyForm = form.getCompany(companyCode);
		System.out.println("company found "+companyForm);
		ReferenceDao dao = new ReferenceDao();
		if (companyForm != null && companyForm.getCompanyCode() != null)
		{
			form.setCompanyCode(companyForm.getCompanyCode());
			form.setCompanyName(companyForm.getCompanyName()+"TESTER");
			form.setBnNumber(companyForm.getBnNumber());
			form.setAddress1(companyForm.getAddress1());
			form.setAddress2(companyForm.getAddress2());
			form.setCity(companyForm.getCity());
			form.setProvince(companyForm.getProvince());
			form.setPostalCode(companyForm.getPostalCode());
			form.setClientType(companyForm.getClientType());
			form.setClientStatusActive(companyForm.getClientStatusActive());
			System.out.println("companyForm.getClientStatusActive  "+companyForm.getClientStatusActive());
			System.out.println("form.getClientStatusActive  "+form.getClientStatusActive());
			
			YearlyClientForm yearlyClientInfo = new YearlyClientForm();
			yearlyClientInfo.setYear(MASTER);
			if (form.getClientType() != null && form.getClientType().equals(JspConstants.YEARLY) )
			{
				
				ArrayList<YearlyClientForm> yearlyClientInfos = dao.checkYearlyClientInfoExist(companyCode, getStore_id(req), null);
				
				if (yearlyClientInfos!= null && yearlyClientInfos.size() > 0)
				{
					yearlyClientInfo = yearlyClientInfos.get(0);	
				}
				
				if (yearlyClientInfos != null && yearlyClientInfos.size() > 0)
				{	
					form.setYearlyChargesExist(yearlyClientInfos);
					System.out.println("************* yearlyClientInfos not null "+yearlyClientInfos.size());
				}
				else
				{
					form.setYearlyChargesExist(null);
					System.out.println("*************  yearlyClientInfos is  null "+yearlyClientInfos);
				}
			}else
			{
				form.setYearlyChargesExist(null);
				System.out.println("*************  yearlyClientInfos is  null ");
			}
			form.setYearlyClientInfo(yearlyClientInfo);
			
			form.setComment(companyForm.getComment());
			form.setBankAccNumber(dao.getBankAccountNumber1(companyForm.getCompanyCode(),getStore_id(req)));
			//req.setAttribute("companyForm",companyForm);
			req.setAttribute("companyCode",companyForm.getCompanyCode());
		}
		return getForward(result, JspConstants.COMPANY_JSP);
}
 
 public ActionForward submit(ActionMapping mapping, ActionForm frm, 
					HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("CompanyAction submitted ");
		try {
			CompanyForm form = getCompanyForm(req);
			ActionErrors actionErrors = new ActionErrors();

			if(isEmpty(form.getCompanyCode())){
				actionErrors.add("getCompanyCode", new ActionMessage("error.companyCode.missing"));
				form.setCompanyCode(null);

			}
			if(isEmpty(form.getCompanyName())){
				actionErrors.add("getCompanyName", new ActionMessage("error.companyName.missing"));
				form.setCompanyName(null);

			}
			if(isEmpty(form.getBnNumber())){
				actionErrors.add("BnNumber", new ActionMessage("error.bnNumber.missing"));
			}
			if((form.getPostalCode() != null) && (form.getPostalCode().length() > 7)){
				actionErrors.add("postalCode", new ActionMessage("error.postalCode.missing"));
			}
			
			saveErrors(req,actionErrors);
			if (actionErrors.size()>0)
			{
				return getForward(result, JspConstants.COMPANY_JSP);
			}
			System.out.println("CompanyAction submitted "+form.getCompanyName());
			ReferenceDao dao  = new ReferenceDao();
			PayrollForm payrollForm =  new PayrollForm();

			ChargeForm chargeForm =  new ChargeForm();
			form.setOrderBy("COMPANY_NAME");
			CompanyForm formClone =new CompanyForm();
			formClone.setCompanyCode(form.getCompanyCode());
			formClone.setStore_id(getStore_id(req));
			ArrayList<CompanyForm>  masterCompany = dao.getCompanies(formClone,null);
			formClone = null;
			if (masterCompany != null && masterCompany.size() > 0)
			{
				System.out.println("master company already exist updating "+masterCompany);
				dao.updateMasterCompany(form);
				return submitCompanyAndAddPayroll(mapping, payrollForm, req, resp);
			}
			else  if (actionErrors.isEmpty())
			{
				form.setStore_id(getStore_id(req));
				form.setGstRate(getGstRate(req));
				dao.addCompany(form);
				addCompanyToMapping(req,form) ;
				payrollForm = addMasterPayroll(form,req);
				chargeForm = addMasterCharge(form,req);
				System.out.println("company  - charge - payroll added for "+form.getCompanyName());
				req.setAttribute("companyCode",form.getCompanyCode());
				return submitCompanyAndAddPayroll(mapping, payrollForm, req, resp);
			}
			else
			{
				return getForward(result, JspConstants.COMPANY_JSP);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.COMPANY_JSP);
	}

 
	
	
	
private PayrollForm  addMasterPayroll(CompanyForm form, HttpServletRequest req) throws SQLException
{
	ReferenceDao dao  = new ReferenceDao();
	PayrollForm payrollForm =  new PayrollForm();
	payrollForm.setCompanyCode(form.getCompanyCode());
	payrollForm.setPayrollMonth(JspConstants.MASTER_PAYROLL);
	payrollForm.setStore_id(getStore_id(req));
	payrollForm.setCompanyName(form.getCompanyName());
	payrollForm.setPayrollFrequency(MONTHLY);
	payrollForm.setActive(NO);
	payrollForm.setPayrollType(FIX);
	dao.addPayroll(payrollForm); 
	System.out.println("Master Payroll added for "+form.getCompanyCode());
	return payrollForm;
}


private ChargeForm addMasterCharge(CompanyForm form, HttpServletRequest req) throws SQLException
{
	ReferenceDao dao  = new ReferenceDao();
	ChargeForm chargeForm =  new ChargeForm();
	chargeForm.setCompanyCode(form.getCompanyCode());
	chargeForm.setAccountNumber(form.getBankAccNumber());
	chargeForm.setMonthYear(JspConstants.MASTER_CHARGE);
	chargeForm.setStore_id(getStore_id(req));
	chargeForm.setActive(NO);
	chargeForm.setNsf(NO);
	chargeForm.setPreviousAmountGstIncluded(NO);
	chargeForm.setLockAccount(NO);
	chargeForm.setWithdrawDate(ALL);
	dao.addCharge(chargeForm); 
	System.out.println("Master charge added for "+form.getCompanyCode());
	return chargeForm;
}



 public ActionForward updateCompany(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
	 ActionForward result = null;

	 System.out.println("updateCompany submitted ");
	 try {
			CompanyForm form = getCompanyForm(req);
			ActionErrors actionErrors = new ActionErrors();
			if(isEmpty(form.getCompanyCode())){
				actionErrors.add("getCompanyCode", new ActionMessage("error.companyCode.missing"));
				form.setCompanyCode(null);
			}
			if(isEmpty(form.getCompanyName())){
				actionErrors.add("getCompanyName", new ActionMessage("error.companyName.missing"));
				form.setCompanyName(null);
			}
			if(isEmpty(form.getBnNumber())){
				actionErrors.add("BnNumber", new ActionMessage("error.bnNumber.missing"));
			}
			if(JspConstants.YEARLY.equals(form.getClientType()) && isEmpty(form.getYearlyClientInfo().getYear())){
				actionErrors.add("BnNumber", new ActionMessage("error.year.missing"));
			}
			if(JspConstants.YEARLY.equals(form.getClientType()) && isEmpty(form.getYearlyClientInfo().getYearEnd())){
				actionErrors.add("BnNumber", new ActionMessage("error.yearEnd.missing"));
			}
			
			saveErrors(req,actionErrors);
		
			System.out.println("updateCompany submitted "+form.getCompanyCode());
			ReferenceDao dao  = new ReferenceDao();
			if (actionErrors.isEmpty())
			{
				form.setStore_id(getStore_id(req));
				form.setGstRate(getGstRate(req));
				if (dao.checkCompanyExist(form) != null)
				{
					System.out.println("updateCompany Updating existing company "+form.getCompanyCode());
					dao.updateMasterCompany(form);	
				}
				else
				{
					System.out.println("updateCompany Adding new company "+form.getCompanyCode());
					dao.addCompany(form);
					addCompanyToMapping(req,form) ;
					addMasterCharge(form,req);
					addMasterPayroll(form,req);
				}
				
				System.out.println("company  - charge - payroll added for "+form.getCompanyCode());
				req.setAttribute("companyCode",form.getCompanyCode());
				//return search(mapping,frm,req,resp);
			}
			else
			{
				return getForward(result, JspConstants.COMPANY_JSP);
				
			}
}
catch (Exception e) {
	e.printStackTrace();
}		
return getForward(result, JspConstants.COMPANY_JSP);
}

 
 
 
 
/* public ActionForward updateCompany(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
	 ActionForward result = null;

	 System.out.println("updateCompany submitted ");
	 try {
	CompanyForm form = getCompanyForm(req);
	ActionErrors actionErrors = new ActionErrors();

	if(isEmpty(form.getCompanyCode())){
		actionErrors.add("getCompanyCode", new ActionMessage("error.companyCode.missing"));
	}
	if(isEmpty(form.getCompanyName())){
		actionErrors.add("getCompanyName", new ActionMessage("error.companyName.missing"));

	}
	if(isEmpty(form.getBnNumber())){
		actionErrors.add("BnNumber", new ActionMessage("error.bnNumber.missing"));
	}
	
	saveErrors(req,actionErrors);

	System.out.println("updateCompany submitted "+form.getCompanyName());
	ReferenceDao dao  = new ReferenceDao();
	PayrollForm payrollForm =  new PayrollForm();
	payrollForm.setCompanyCode(form.getCompanyCode());

	ChargeForm chargeForm =  new ChargeForm();
	chargeForm.setCompanyCode(form.getCompanyCode());
	chargeForm.setAccountNumber(form.getBankAccNumber());
	if (actionErrors.isEmpty())
	{
		form.setStore_id(getStore_id(req));
		dao.addCompany(form);
		payrollForm.setPayrollMonth(JspConstants.MASTER_PAYROLL);
		payrollForm.setStore_id(getStore_id(req));
		dao.addPayroll(payrollForm);
		chargeForm.setMonthYear(JspConstants.MASTER_CHARGE);
		chargeForm.setStore_id(getStore_id(req));
		dao.addCharge(chargeForm);
		System.out.println("company  - charge - payroll added for "+form.getCompanyName());
		req.setAttribute("companyCode",form.getCompanyCode());
		//return search(mapping,frm,req,resp);
	}
	else
	{
		return getForward(result, JspConstants.COMPANY_JSP);
	}
}
catch (Exception e) {
	e.printStackTrace();
}		
return getForward(result, JspConstants.COMPANY_JSP);
}
*/ 
 
 
public ActionForward prepare(ActionMapping mapping, ActionForm frm, 
	HttpServletRequest req, HttpServletResponse resp)  {
	ActionForward result = null;
	System.out.println("CompanyAction prepare submoitted ");
	try {
		CompanyForm form = getCompanyForm(req);
		ActionErrors actionErrors = new ActionErrors();
	
		ReferenceDao dao  = new ReferenceDao();
		dao.prepare();
		req.setAttribute("prepare","System initilized successfull ");
		System.out.println("CompanyAction prepare completed .... ");
	}catch (Exception e) {
		req.setAttribute("prepare","System initilization failed");
		e.printStackTrace();
	}		
	return getForward(result, JspConstants.INDEX_JSP);
}
	
	public ActionForward addNewPayroll(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("addNewPayroll submitted ");
		try {
			CompanyForm form = getCompanyForm(req);
			form.clear();
			req.getSession().setAttribute(Constants.MONTH, null);
			req.getSession().setAttribute(Constants.YEAR,null);
			form.setAction("add");
			form.setNextPage(JspConstants.LIST_PAYROLL_JSP);
			System.out.println("gotoselectmonth YEAR "+form.getYear());
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	return getForward(result, JspConstants.LIST_COMPANY_JSP);
}

	
	
	public ActionForward changePayroll(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("gotoselectmonth submitted ");
		try {
			
			resetCompanyForm(req);
			CompanyForm form = getCompanyForm(req);
			req.getSession().setAttribute(Constants.MONTH, null);
			req.getSession().setAttribute(Constants.YEAR,null);
			form.setAction("update");
			System.out.println("gotoselectmonth YEAR "+form.getYear());
			form.setNextPage(JspConstants.UPDATE_PAYROLLS_JSP);
			gotoselectmonth(mapping, form,req,resp);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	return getForward(result, JspConstants.SELECT_MONTH_FOR_PAYROLL_UPDATE_JSP);
}

	public ActionForward changeCharges(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("gotoselectmonth submitted ");
		try {
			CompanyForm form = getCompanyForm(req);
			req.getSession().setAttribute(Constants.MONTH, null);
			req.getSession().setAttribute(Constants.YEAR,null);
			form.setAction("update");
			form.setNextPage(JspConstants.LIST_CHARGE_JSP);
			System.out.println("gotoselectmonth YEAR "+form.getYear());
			gotoselectmonth(mapping, form,req,resp);

			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	return getForward(result, JspConstants.SELECT_MONTH_FOR_CHARGE_UPDATE_JSP);
}
	

 public ActionForward search (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("CompanyAction submitted ");
		try {
			CompanyForm form = getCompanyForm(req);
			System.out.println("user action is "+form.getAction());
			System.out.println("CompanyAction submitted "+form.getCompanyName());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(getStore_id(req));
			ArrayList<CompanyForm >  list =  dao.getCompanies(form, form.getAction());
			ArrayList<CompanyForm > newList = removeDuplicates(list);
			System.out.println("form.getFlow() "+form.getFlow());
			
			if (form.getFlow() != null && form.getFlow().equalsIgnoreCase("CHARGE"))
			{
				newList = removeEmptyCharges(newList);
			}
			System.out.println("user action is "+form.getAction());
			form.setCompanyList(newList);
			
			if (form.getAction().equals(JspConstants.ACTION_YEARLY))
				return getForward(result, JspConstants.LIST_YEARLY_COMPANY);
			
			else if (form.getAction().equals("add"))
				return getForward(result, JspConstants.LIST_COMPANY_JSP);
			
			else if (form.getAction().equals("delete"))
			{
				list =  dao.getAllCompanies(form);
				newList = removeDuplicates(list);
				form.setCompanyList(newList);
				return getForward(result, JspConstants.DELETE_COMPANY_JSP);
			}
			else if (form.getAction().equals(JspConstants.ACTION_GOTO_SEARCH_HISTORY))
				return getForward(result, JspConstants.SEARCH_COMPANY_HISTORY_JSP);
			else
				return getForward(result, form.getNextPage());

		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.LIST_COMPANY_JSP);
	}
	
 
private ArrayList<CompanyForm>  getPayrollList(CompanyForm form) throws SQLException
	{
		ReferenceDao dao  = new ReferenceDao();
		ArrayList<CompanyForm>  list =  dao.searchPayrolls(form);
		ArrayList<CompanyForm> newList = removeDuplicates(list);
		ArrayList<CompanyForm> quarterlyRemitterList = getQuarterlyRemitters(newList);
		newList.addAll(quarterlyRemitterList);
		return newList;
	}
 
 public ActionForward searchpayrolls (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("searchpayrolls submitted ");
		try {
			CompanyForm form = getCompanyForm(req);
			System.out.println("user action is "+form.getAction());
			System.out.println("searchpayrolls submitted "+form.getCompanyName());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(getStore_id(req));
			
			ArrayList<CompanyForm> newList = getPayrollList(form);
			
			MonthlyNoteForm monthlyNoteForm  = getMonthlyNoteForm(req);
			monthlyNoteForm.setStore_id(getStore_id(req));
			monthlyNoteForm.setNotesType(PAYROLL);
			monthlyNoteForm.setStatus(form.getNotesStatus());
			monthlyNoteForm.setMonth(form.getMonth());
			monthlyNoteForm.setYear(form.getYear());
			MonthlyNotesAction action = new MonthlyNotesAction();
			action.getNotes(monthlyNoteForm);
			form.setNotes(monthlyNoteForm.getNotes());
			form.setCompanyList(newList);
			form.setOriginalList((ArrayList<CompanyForm>)newList.clone());
			//form.setQuarterlyRemitterList(quarterlyRemitterList);

		}
			catch (Exception e) {
				e.printStackTrace();
		}		
		return getForward(result, JspConstants.UPDATE_PAYROLLS_JSP);
	}
	public ArrayList<CompanyForm> getQuarterlyRemitters(ArrayList<CompanyForm> newList) {
		
		ArrayList<CompanyForm> monthlyRemiiterList = new ArrayList<CompanyForm>();
		ArrayList<CompanyForm> quarterlyRemitterList = new ArrayList<CompanyForm>();
		if (newList != null)
		{
			for (int i=0; i< newList.size(); i++)
			{
				CompanyForm c = (CompanyForm)newList.get(i);
				if ("Q".equals(c.getPayroll().getPayrollFrequency()))
				{
					quarterlyRemitterList.add(c);
					newList.remove(c);
				}
			
		}
	}
	return quarterlyRemitterList;
	
}


public ActionForward searchCharges (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("searchCharges submitted ");
		try {
			CompanyForm form = getCompanyForm(req);
			System.out.println("user action is "+form.getAction());
			System.out.println("searchCharges getMonth"+form.getMonth());
			System.out.println("searchCharges getYear "+form.getYear());
			System.out.println("searchCharges submitted "+form.getCompanyName());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(getStore_id(req));
			ArrayList<CompanyForm >  list =  dao.searchCharges(form);
			ArrayList<CompanyForm > newList = removeDuplicates(list);
			newList = removeEmptyCharges(newList);
			form.setCompanyList(newList);
		
			
			MonthlyNoteForm monthlyNoteForm  = getMonthlyNoteForm(req);
			monthlyNoteForm.setStore_id(getStore_id(req));
			monthlyNoteForm.setNotesType(CHARGE);
			monthlyNoteForm.setStatus(form.getNotesStatus());
			monthlyNoteForm.setMonth(form.getMonth());
			monthlyNoteForm.setYear(form.getYear());
			//monthlyNoteForm.setStatus(form.getNotesStatus());
			if (form.getWithdrawDate()!=null && form.getWithdrawDate().equals(ALL))
			{
				monthlyNoteForm.setNotesDate(0);
			}
			else if (form.getWithdrawDate()!=null)
			{
				monthlyNoteForm.setNotesDate(Integer.parseInt(form.getWithdrawDate()));
			}
			System.out.println("searchCharges monthlyNoteForm.setNotesDate "+monthlyNoteForm.getNotesDate());
			MonthlyNotesAction action = new MonthlyNotesAction();
			action.getNotes(monthlyNoteForm);
			form.setNotes(monthlyNoteForm.getNotes());
			
			
//			if (form.getAction().equals("add"))
//				return getForward(result, JspConstants.LIST_CHARGE_JSP);
//			
//			else
//				return getForward(result, form.getNextPage());

		}
			catch (Exception e) {
				e.printStackTrace();
		}		
		return getForward(result, JspConstants.LIST_CHARGE_JSP);
	}
	


public ActionForward generateYearlyCharges (ActionMapping mapping, ActionForm frm, 
		HttpServletRequest req, HttpServletResponse resp)  {
	ActionForward result = null;

	System.out.println("CompanyAction gotoGenerateYearlyCharges ");
	try {
	}
	catch (Exception e) {
	}		
	return getForward(result, JspConstants.SELECT_YEAR_FOR_CHARGE_COPY_JSP);
}

 
	public ActionForward gotosearchhistory (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("CompanyAction gotosearchhistory ");
		try {
			CompanyForm form = getCompanyForm(req);
			form.setAction(JspConstants.ACTION_GOTO_SEARCH_HISTORY);
			return search(mapping,frm,req,resp);
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.SEARCH_COMPANY_HISTORY_JSP);
	}

	public ActionForward searchhistory (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("CompanyAction searchhistory ");
		try {
			CompanyForm form = getCompanyForm(req);
			
			System.out.println("CompanyAction searchhistory "+form.getCompanyName());
			System.out.println("CompanyAction getEditId  "+form.getEditId());
			form = form.getCompany(form.getEditId());
			System.out.println("CompanyAction searchhistory "+form.getCompanyName());
			if (form != null)
			{
				System.out.println("searchhistory for "+form.getCompanyCode());
				ReferenceDao dao  = new ReferenceDao();
				form.setStore_id(getStore_id(req));
				form = dao.searchHistory(form);
			}
			System.out.println("searchhistory getPayrolllist "+form.getPayrolllist().size());
			System.out.println("searchhistory getChargelist"+form.getChargelist().size());
			
			
			double totalRemittanceAmount = 0.0;
			double totalBankCharge= 0.0;
			double totalAmount = 0.0;
			for (int i=0; i< form.getPayrolllist().size(); i++)
			{
				PayrollForm payroll = (PayrollForm)form.getPayrolllist().get(i);
				if (payroll.getPayrollMonth()!= null && payroll.getPayrollMonth().equals(MASTER_PAYROLL))
				{
					System.out.println("searchhistory removing Master Payroll From List ");
					form.getPayrolllist().remove(payroll);
					continue;
				}
				if (payroll != null)
					totalRemittanceAmount =totalRemittanceAmount+payroll.getRemittanceAmount();
					totalBankCharge =totalBankCharge+payroll.getBankCharge();
					totalAmount =totalAmount+payroll.getTotalAmount();
			}
			
			form.setTotalRemittanceAmount(totalRemittanceAmount);
			form.setTotalBankCharge(totalBankCharge);
			form.setTotalAmount(totalAmount);
			
			
			double totalNetAmount = 0.0;
			double totalGst= 0.0;
			double totalWithdrawAmount = 0.0;
			for (int i=0; i< form.getChargelist().size(); i++)
			{
				ChargeForm charge = (ChargeForm)form.getChargelist().get(i);
				if (charge != null && charge.getMonthYear() != null && charge.getMonthYear().equals(MASTER_CHARGE))
				{
					System.out.println("searchhistory removing Master Charge From List ");
					form.getChargelist().remove(charge);
					continue;
				}
				if (charge != null)
					totalNetAmount =totalNetAmount+charge.getNetAmount();
					totalGst =totalGst+charge.getGst();
					totalWithdrawAmount =totalWithdrawAmount+charge.getTotalWithdrawAmount();
			}
			
			form.setTotalNetAmount(totalNetAmount);
			form.setTotalGst(totalGst);
			form.setTotalWithdrawAmount(totalWithdrawAmount);
			
			
			req.getSession().setAttribute("companyForm",form);
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.SHOW_COMPANY_HISTORY_JSP);
	}

	public ActionForward gotodelete (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("CompanyAction delete ");
		try {
			CompanyForm form = getCompanyForm(req);
			System.out.println("CompanyAction delete "+form.getCompanyName());
			form.clear();
			
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.DELETE_COMPANY_JSP);
	}


	public ActionForward delete (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("CompanyAction delete ");
		try {
			CompanyForm form = getCompanyForm(req);
			
			System.out.println("CompanyAction delete "+form.getCompanyName());
			System.out.println("CompanyAction getEditId  "+form.getEditId());
			ReferenceDao dao  = new ReferenceDao();
			dao.deleteCompany(form.getEditId(),getStore_id(req));
			form.deleteFromList(form.getEditId());
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.DELETE_COMPANY_JSP);
	}
	
	public ActionForward deleteYearlyPaymentInfo (ActionMapping mapping, ActionForm frm,HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&CompanyAction deleteYearlyClient ");
		CompanyForm form = null;
		try {
			form = (CompanyForm)frm;
			System.out.println("CompanyAction getEditId  "+form.getCompanyCode());
			ReferenceDao dao  = new ReferenceDao();
			dao.deleteYearlyClientPaymentInfo(form.getYearlyClientInfo());
		}
		catch (Exception e) {
		}		
		return gotoEditYearlyClient( mapping,form,req,resp);
		//return getForward(result, JspConstants.LIST_YEARLY_COMPANY);
	}
	

	
	
	
	public ActionForward submitCompanyAndAddPayroll (ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		try {
			System.out.println("************************  submitCompanyAndAddPayroll submitted  ************************  ");
			PayrollForm form = getPayrollForm(req);
			CompanyForm company = getCompanyForm(req);
			String companyCode = company.getCompanyCode();
			System.out.println("submitCompanyAndAddPayroll submitted  getEditId  "+companyCode);
			ReferenceDao dao  = new ReferenceDao();
			PayrollForm  masterPayroll = dao.checkMasterPayrollExist(companyCode,getStore_id(req));
			PayrollForm payrollForm= null;
			if (masterPayroll != null )
			{
				System.out.println("submitCompanyAndAddPayroll masterPayroll FOUND: "+companyCode);
				payrollForm = masterPayroll;
				payrollForm.setCompanyName(getCompanyName(req, companyCode));
				form = payrollForm;
				
			}
			else
			{
				System.out.println("submitCompanyAndAddPayroll masterPayroll NOT FOUND: "+companyCode);
				payrollForm = new PayrollForm ();
				payrollForm.clear();
				payrollForm.setCompanyCode(companyCode);
				payrollForm.setCompanyName(getCompanyName(req, companyCode));
				payrollForm.setPayrollMonth(JspConstants.MASTER_PAYROLL);
				
			}
			payrollForm.setClientStatusActive(company.getClientStatusActive());
			req.getSession().setAttribute("payrollForm",payrollForm);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.PAYROLL_JSP);
	}
	public ActionForward addPayroll (ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		try {
			CompanyForm form = getCompanyForm(req);
			String companyCode = form.getEditId();
			System.out.println("addPayroll submitted  getEditId  "+form.getEditId());
			CompanyForm  CompanyDetails  = form.getCompany(form.getEditId());
			System.out.println("addPayroll . CompanyDetails finding company  "+CompanyDetails.getCompanyName());
			ReferenceDao dao  = new ReferenceDao();
			PayrollForm  masterPayroll = dao.checkMasterPayrollExist(companyCode,getStore_id(req));
			PayrollForm payrollForm = null;
			if (masterPayroll != null )
			{
				payrollForm = masterPayroll;
				payrollForm.setCompanyName(getCompanyName(req, companyCode));
				
			}
			else
			{
				payrollForm = new PayrollForm ();
				payrollForm.clear();
				payrollForm.setCompanyCode(form.getEditId());
				payrollForm.setCompanyName(getCompanyName(req, companyCode));
				payrollForm.setPayrollMonth(JspConstants.MASTER_PAYROLL);
				
			}
			if (CompanyDetails!= null)
				payrollForm.setClientStatusActive(CompanyDetails.getClientStatusActive());
			System.out.println("addPayroll submitted  getClientStatusActive  "+payrollForm.getClientStatusActive());
			req.getSession().setAttribute("payrollForm",payrollForm);
			req.setAttribute("companyCode",form.getEditId());
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.PAYROLL_JSP);
	}

	public ActionForward addCharge (ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		try {
			CompanyForm form = getCompanyForm(req);
			String companyCode = form.getEditId();
			System.out.println("CompanyAction getEditId  "+form.getEditId());
			CompanyForm  CompanyDetails  = form.getCompany(form.getEditId());
			System.out.println("CompanyDetails fond  "+CompanyDetails.getCompanyName());
			ReferenceDao dao = new ReferenceDao();
			ChargeForm masterCharge = dao.checkMasterChargeExist(companyCode,getStore_id(req));
			ChargeForm chargeForm = null;
			
			if (masterCharge != null )
			{
				chargeForm = masterCharge;
			}
			else
			{
				chargeForm = new ChargeForm ();
				chargeForm.clear();
				chargeForm.setCompanyCode(form.getEditId());
				chargeForm.setCompanyName(getCompanyName(req, companyCode));
				chargeForm.setMonthYear(JspConstants.MASTER_CHARGE);
			}
			
			if (CompanyDetails!= null)
				chargeForm.setClientStatusActive(CompanyDetails.getClientStatusActive());
			req.getSession().setAttribute("chargeForm",chargeForm);

			
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.CHARGE_JSP);
	}
	
	
	public ActionForward addNewYearlyCharge(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
	 ActionForward result = null;

	 System.out.println("addNewYearlyCharge submitted ");
	 try {
			CompanyForm form = getCompanyForm(req);
			ReferenceDao dao  = new ReferenceDao();
			YearlyClientForm yearlyClientInfo = new YearlyClientForm();
			//yearlyClientInfo.setYear(MASTER);
			yearlyClientInfo = dao.checkYearlyMasterClientInfoExist(form.getCompanyCode(),form.getStore_id());
			yearlyClientInfo.setYear(null);
			form.setYearlyClientInfo(yearlyClientInfo);
			System.out.println("addNewYearlyCharge Finished "+form.getCompanyName() + "BASE YEAR "+form.getYearlyClientInfo().getBaseYear());
	 }
	 catch (Exception e) {
		 e.printStackTrace();
	 }		
	 return getForward(result, JspConstants.COMPANY_JSP);
}

 public ActionForward getYearlyCharge(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
	 ActionForward result = null;
	 System.out.println("****************** getYearlyCharge submitted ********************");
	 try {
			CompanyForm form = getCompanyForm(req);
			ReferenceDao dao  = new ReferenceDao();
			String year = form.getEditId();
			System.out.println("getYearlyCharge submitted  getEditId  "+year);
			YearlyClientForm yearlyClientInfo = new YearlyClientForm();
			yearlyClientInfo.setYear(MASTER);
			form.setYearlyClientInfo(yearlyClientInfo);
			if (form.getYearlyChargesExist() != null)
				form.getYearlyChargesExist().clear();
			
			
			if (form.getClientType() != null && form.getClientType().equals(JspConstants.YEARLY) )
			{
					
				ArrayList<YearlyClientForm> yearlyClientInfos = dao.checkYearlyClientInfoExist(form.getCompanyCode(), getStore_id(req), null);
			
				if (yearlyClientInfos!= null && yearlyClientInfos.size() == 1)
				{
					yearlyClientInfo = yearlyClientInfos.get(0);
					System.out.println("yearlyClientInfo Checking   "+yearlyClientInfo.getYear());
					form.setYearlyClientInfo(yearlyClientInfo);
					form.setYearlyChargesExist(yearlyClientInfos);
					System.out.println("****************** getYearlyCharge ENDED********************");
				}
				else if (yearlyClientInfos!= null && yearlyClientInfos.size() > 1)
				{
					for (int x=0;x < yearlyClientInfos.size(); x++)
					{
						yearlyClientInfo = yearlyClientInfos.get(x);
						System.out.println("yearlyClientInfo Checking   "+yearlyClientInfo.getYear());
						if (year != null && year.equals(yearlyClientInfo.getYear()))
						{
							System.out.println("setting yearlyClientInfo  "+yearlyClientInfo.getBalanceDue());
							form.setYearlyClientInfo(yearlyClientInfo);
						}
					}

					form.setYearlyChargesExist(yearlyClientInfos);
					System.out.println("****************** getYearlyCharge ENDED********************");
				}
			}
	 }
	 catch (Exception e) {
		 e.printStackTrace();
	 }	
		 return getForward(result, JspConstants.COMPANY_JSP);
}
 
	public ActionForward addCompanyAndGotoCharge (ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			CompanyForm company = getCompanyForm(req);
			PayrollForm form = getPayrollForm(req);
			String companyCode = company.getCompanyCode();
			System.out.println("addCompanyAndGotoCharge companyCode  "+companyCode);
			System.out.println("addCompanyAndGotoCharge company.getCompanyCode()  "+company.getCompanyCode());
			company.setStore_id(getStore_id(req));
			form.setStore_id(getStore_id(req));
			ReferenceDao dao = new ReferenceDao();
			if (dao.checkCompanyExist(company) != null)
			{
				dao.updateMasterCompany(company);	
			}else{
				dao.addCompany(company);
				addCompanyToMapping(req, company);
			}
			
			ChargeForm masterCharge = dao.checkMasterChargeExist(companyCode,getStore_id(req));
			ChargeForm chargeForm = null;
			if (masterCharge != null )
			{
				chargeForm = masterCharge;
				chargeForm.setCompanyName(getCompanyName(req, companyCode));
				System.out.println(" chargeForm Found for "+chargeForm.getCompanyCode());
				
			}
			else
			{
				chargeForm = new ChargeForm ();
				chargeForm.clear();
				chargeForm.setCompanyCode(companyCode);
				chargeForm.setCompanyName(getCompanyName(req, companyCode));
				chargeForm.setMonthYear(JspConstants.MASTER_CHARGE);
				
			}
			chargeForm.setClientStatusActive(company.getClientStatusActive());
			req.getSession().setAttribute("chargeForm",chargeForm);
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.CHARGE_JSP);
	} 
	
	



	


	public ActionForward editTaxReturnInfo(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("editTaxReturnInfo called ");
		ActionForward result = null;
		CompanyForm form = getCompanyForm(req);
		String companyCode = form.getEditId();
		System.out.println("editTaxReturnInfo submitted  getEditId  "+form.getEditId());
		CompanyForm companyForm = form.getCompany(companyCode);
		System.out.println("company editTaxReturnInfo "+companyForm);
		ReferenceDao dao = new ReferenceDao();
		if (companyForm != null && companyForm.getCompanyCode() != null)
		{
			form.setCompanyCode(companyForm.getCompanyCode());
			form.setCompanyName(companyForm.getCompanyName());
			form.setBnNumber(companyForm.getBnNumber());
			form.setAddress1(companyForm.getAddress1());
			form.setAddress2(companyForm.getAddress2());
			form.setCity(companyForm.getCity());
			form.setProvince(companyForm.getProvince());
			form.setPostalCode(companyForm.getPostalCode());
			form.setClientType(companyForm.getClientType());
			form.setClientStatusActive(companyForm.getClientStatusActive());
			YearlyClientForm yearlyClientInfo = new YearlyClientForm();
			yearlyClientInfo.setYear(MASTER);
			ArrayList<YearlyClientForm> taxReturnInfos = dao.checkYearlyClientInfoExist(companyCode, getStore_id(req), null) ; // ClientTaxReturnInfo(companyCode, getStore_id(req), null);
				
			if (taxReturnInfos!= null && taxReturnInfos.size() > 0)
				{
					form.setYearlyClientInfo(taxReturnInfos.get(0));	
			}else
				{
					//System.out.println("insert Taxreturn Info for first Time "+yearlyClientInfo.getTaxFiled());
					//dao.insertTaxReturnInfo(yearlyClientInfo);
					form.setYearlyClientInfo(yearlyClientInfo);
				}
				
			if (taxReturnInfos != null && taxReturnInfos.size() > 1)
					form.setYearlyChargesExist(taxReturnInfos);
			else
					form.setYearlyChargesExist(null);
				
			System.out.println("Tax editTaxReturnInfo "+form.getYearlyClientInfo().getTaxFiled());
			form.setComment(companyForm.getComment());
			form.setBankAccNumber(dao.getBankAccountNumber1(companyForm.getCompanyCode(),getStore_id(req)));
			req.setAttribute("companyCode",companyForm.getCompanyCode());
		}
		return getForward(result, JspConstants.TAX_RETURN_INFO_JSP);
}
	
	public ActionForward editYearlyClient(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("editYearlyClient called ");
		ActionForward result = null;
		CompanyForm form = getCompanyForm(req);
		String companyCode = form.getEditId();
		System.out.println("editYearlyClient submitted  getEditId  "+form.getEditId());
		CompanyForm companyForm = form.getCompany(companyCode);
		System.out.println("company editYearlyClient "+companyForm);
		ReferenceDao dao = new ReferenceDao();
		if (companyForm != null && companyForm.getCompanyCode() != null)
		{
			form.setCompanyCode(companyForm.getCompanyCode());
			form.setCompanyName(companyForm.getCompanyName());
			form.setBnNumber(companyForm.getBnNumber());
			form.setAddress1(companyForm.getAddress1());
			form.setAddress2(companyForm.getAddress2());
			form.setCity(companyForm.getCity());
			form.setProvince(companyForm.getProvince());
			form.setPostalCode(companyForm.getPostalCode());
			form.setClientType(companyForm.getClientType());
			form.setClientStatusActive(companyForm.getClientStatusActive());
			if (form.getClientType() != null && form.getClientType().equals(JspConstants.YEARLY) )
			{
				YearlyClientForm yearlyClientInfo = new YearlyClientForm();
				yearlyClientInfo.setYear(MASTER);
				ArrayList<YearlyClientForm> yearlyClientInfos = dao.checkYearlyClientInfoExist(companyCode, getStore_id(req), null);
				
//				removeMasterChargeRecords(yearlyClientInfos);
				
				
				if (yearlyClientInfos!= null && yearlyClientInfos.size() > 0)
				{
					form.setYearlyClientInfo(yearlyClientInfos.get(0));	
				}else
				{
					form.setYearlyClientInfo(yearlyClientInfo);
				}
				
				
				if (yearlyClientInfos != null && yearlyClientInfos.size() > 1)
					form.setYearlyChargesExist(yearlyClientInfos);
				else
					form.setYearlyChargesExist(null);
				
				System.out.println("Tax getTaxFiled "+form.getYearlyClientInfo().getTaxFiled());
			}
			form.setComment(companyForm.getComment());
			form.setBankAccNumber(dao.getBankAccountNumber1(companyForm.getCompanyCode(),getStore_id(req)));
			req.setAttribute("companyCode",companyForm.getCompanyCode());
		}
		return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);
}

	


	private void removeMasterChargeRecords(ArrayList<YearlyClientForm> yearlyClientInfos) {
		
		if (yearlyClientInfos!= null && yearlyClientInfos.size() > 1)
		{
			for (int x=0;x < yearlyClientInfos.size(); x++)
			{
				YearlyClientForm yearlyClientInfo = yearlyClientInfos.get(x);
				System.out.println("yearlyClientInfo Checking   "+yearlyClientInfo.getYear());
				if (MASTER.equals(yearlyClientInfo.getYear()))
				{
					yearlyClientInfos.remove(yearlyClientInfo);
					System.out.println("********MASTER Record removed********** *"+yearlyClientInfo.getCompanyCode());
				}
			}
			
		}
	}


	public ActionForward searchYearlyClient(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("searchYearlyClient called ");
		ActionForward result = null;
		return getForward(result, JspConstants.LIST_YEARLY_COMPANY);
}

	public ActionForward updateYearlyClient(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("updateYearlyClient called ");
		ActionForward result = null;
		CompanyForm company = getCompanyForm(req);
		ReferenceDao dao  = new ReferenceDao();
		System.out.println("updateYearlyClient company "+company.getCompanyCode());
		
		company.getYearlyClientInfo().setGstRate(getGstRate(req));
		dao.updateYearlyClientInfo(company.getYearlyClientInfo());
		return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);
}	
	
	
	public ActionForward updateTaxReturnInfo(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("updateTaxReturnInfo called ");
		ActionForward result = null;
		CompanyForm company = getCompanyForm(req);
		ReferenceDao dao  = new ReferenceDao();
		System.out.println("updateTaxReturnInfo company "+company.getCompanyCode());
		
		//dao.updateTaxReturnInfo(company.getYearlyClientInfo());
		dao.updateYearlyClientInfo(company.getYearlyClientInfo());
		return getForward(result, JspConstants.TAX_RETURN_INFO_JSP);
}	
	
	public ActionForward unlockAccountOnCompanyPage(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		
		CompanyForm form = getCompanyForm(req);
		String year = form.getEditId();
		System.out.println("unlockAccountOnCompanyPage called for  "+form.getCompanyCode()+" Year "+year);
		CompanyForm company = getCompanyForm(req);
		ReferenceDao dao  = new ReferenceDao();
		dao.unlockYearlyClient(company.getYearlyClientInfo());
		return getYearlyCharge(mapping,form,req,resp);
}	
	
	
	public ActionForward unlockAccount(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		
		CompanyForm form = getCompanyForm(req);
		String year = form.getEditId();
		System.out.println("unlockAccount called for  "+form.getCompanyCode()+" Year "+year);
		ActionForward result = null;
		CompanyForm company = getCompanyForm(req);
		ReferenceDao dao  = new ReferenceDao();
		dao.unlockYearlyClient(company.getYearlyClientInfo());
		
		//return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);
		return getYearToUpdateCharge(mapping,form,req,resp);
}	

	
	//	 public ActionForward getYearToUpdateChargeOLD(ActionMapping mapping, ActionForm frm, 
//				HttpServletRequest req, HttpServletResponse resp)  {
//		 ActionForward result = null;
//		 CompanyForm form = null;
//		 System.out.println("getYearToUpdateCharge submitted ");
//		 try {
//				form = getCompanyForm(req);
//				ReferenceDao dao  = new ReferenceDao();
//				String year = form.getEditId();
//				YearlyClientForm yearlyClientInfo = new YearlyClientForm();
//				yearlyClientInfo.setYear("MASTER");
//				form.setYearlyClientInfo(yearlyClientInfo);
//				if (form.getClientType() != null && form.getClientType().equals(JspConstants.YEARLY) )
//				{
//					ArrayList<YearlyClientForm> yearlyClientInfos = dao.checkYearlyClientInfoExist(form.getCompanyCode(), getStore_id(req), year);
//					
//					System.out.println("getYearToUpdateCharge yearlyClientInfos size "+yearlyClientInfos.size());
//					
//					if (yearlyClientInfos!= null && yearlyClientInfos.size() > 1)
//					{
//						yearlyClientInfo = yearlyClientInfos.get(0);
//						if (year != null && year.equals(yearlyClientInfo.getYear()))
//						{
//							System.out.println("getYearToUpdateCharge yearlyClientInfos size "+yearlyClientInfos.size());
//							form.setYearlyClientInfo(yearlyClientInfo);
//						}
//					}
//
//					}
//					form.getYearlyClientInfo().setBalanceDue(yearlyClientInfo.getTotalCharges() - yearlyClientInfo.getPaymentReceived() )	;
//					req.getSession().setAttribute("companyForm",form);
//		 }
//		 catch (Exception e) {
//			 e.printStackTrace();
//		 }	
//		
//		 return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);
//	}	
	
		public ActionForward getYearToUpdateCharge(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			System.out.println("********************** getYearToUpdateCharge called **********************");
			ActionForward result = null;
			CompanyForm form = getCompanyForm(req);
			String year = form.getEditId();
			System.out.println("getYearToUpdateCharge submitted  getCompanyCode  "+form.getCompanyCode());
			System.out.println("getYearToUpdateCharge submitted  year  "+year);
			CompanyForm companyForm = form.getCompany(form.getCompanyCode());
			ReferenceDao dao = new ReferenceDao();
			if (form.getYearlyChargesExist() != null)
				form.getYearlyChargesExist().clear();
			if (companyForm != null && companyForm.getCompanyCode() != null)
			{
				form.setCompanyCode(companyForm.getCompanyCode());
				form.setCompanyName(companyForm.getCompanyName());
				form.setBnNumber(companyForm.getBnNumber());
				form.setAddress1(companyForm.getAddress1());
				form.setAddress2(companyForm.getAddress2());
				form.setCity(companyForm.getCity());
				form.setProvince(companyForm.getProvince());
				form.setPostalCode(companyForm.getPostalCode());
				form.setClientType(companyForm.getClientType());
				form.setClientStatusActive(companyForm.getClientStatusActive());

				if (form.getClientType() != null && form.getClientType().equals(JspConstants.YEARLY) )
				{
					YearlyClientForm yearlyClientInfo = new YearlyClientForm();
					yearlyClientInfo.setYear("MASTER");
					ArrayList<YearlyClientForm> yearlyClientInfos = dao.checkYearlyClientInfoExist(form.getCompanyCode(), getStore_id(req), null);
				
					if (yearlyClientInfos!= null && yearlyClientInfos.size() > 0)
					{
						for (int x=0;x < yearlyClientInfos.size(); x++)
						{
							yearlyClientInfo = yearlyClientInfos.get(x);
							System.out.println("comparing selected Year "+year+ " yearlyClientInfo.getYear "+yearlyClientInfo.getYear());
							if (year != null && year.equals(yearlyClientInfo.getYear()))
							{
								form.setYearlyClientInfo(yearlyClientInfo);
							}
						}
							
					}else
					{
						form.setYearlyClientInfo(yearlyClientInfo);
					}
					
					
					if (yearlyClientInfos != null && yearlyClientInfos.size() > 1)
						form.setYearlyChargesExist(yearlyClientInfos);
					else
						form.setYearlyChargesExist(null);

					
					
					System.out.println("Tax getTaxFiled "+form.getYearlyClientInfo().getYear());
					System.out.println("getYearlyChargesExist size "+form.getYearlyChargesExist().size());
				}
				form.setComment(companyForm.getComment());
				form.setBankAccNumber(dao.getBankAccountNumber1(companyForm.getCompanyCode(),getStore_id(req)));
//				form.getYearlyClientInfo().setBalanceDue(form.getYearlyChargeInfo().getTotalCharges() - form.getYearlyClientInfo().getPaymentReceived() )	;
				req.setAttribute("companyCode",companyForm.getCompanyCode());
			}
			
			System.out.println("********************** END getYearToUpdateCharge **********************");
			return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);
	}	
	
			
		
		
		
		
		
		public ActionForward gotoChangeNotes(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			try {
				CompanyForm form = getCompanyForm(req);
				System.out.println("gotoChangeNotes called for "+form.getEditId());
				
				MonthlyNoteForm currentForm = form.getNotesFromList(Integer.parseInt(form.getEditId()));
				System.out.println("Note Type "+currentForm.getNotesType());
				req.getSession().setAttribute("monthlyNoteForm",currentForm);
				System.out.println("sending to "+getPageByNoteType(currentForm));
				return getForward(result, getPageByNoteType(currentForm));
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
			System.out.println("gotoChangeNotes ERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR ");
			return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
		}
		
			
		public ActionForward gotoAddPayrollNotes(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			try {
				MonthlyNotesAction action  = new MonthlyNotesAction();
				action.gotoAddPayrollNotes(mapping, frm,req, resp);
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
			return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
		}		
		
		public ActionForward gotoAddChargeNotes(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			try {
				MonthlyNotesAction action  = new MonthlyNotesAction();
				action.gotoAddChargeNotes(mapping, frm,req, resp);
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
			return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
		}		
		
		public ActionForward lockCharges(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("lockCharges submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("lockCharges MONTH "+form.getMonth());
				System.out.println("lockCharges getCompanyCode "+form.getCompanyCode());

				ReferenceDao dao  = new ReferenceDao();
				ArrayList<CompanyForm > selectedList = form.getCompanyList();
				System.out.println("lockCharges YEAR "+selectedList);
				if (selectedList!= null)
				{
					for (CompanyForm companyForm: selectedList)
					{
						ChargeForm charge = companyForm.getCharge();
						System.out.println("lockCharges  amount "+charge.getTotal());
						System.out.println("lockCharges getCompanyCode "+charge.getCompanyCode()+" getMonthYear "+charge.getMonthYear());
						charge.setStore_id(getStore_id(req));
						charge.setLockAccount(YES);
						dao.lockCharge(charge);
					}
				}
					return getForward(result, JspConstants.LIST_CHARGE_JSP);
			
			}
			catch (Exception e) {
				e.printStackTrace();
				ActionErrors errors = new ActionErrors();
				errors.add("getCompanyCode", new ActionMessage("error.charge.cannotlock",e.getMessage()));
				saveErrors(req,errors);
			}		
		return getForward(result, JspConstants.LIST_CHARGE_JSP);
	}		
		
		
		
		public ActionForward unLockCharges(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("unLockCharges submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("unLockCharges MONTH "+form.getMonth());
				System.out.println("unLockCharges getCompanyCode "+form.getCompanyCode());

				ReferenceDao dao  = new ReferenceDao();
				ArrayList<CompanyForm > selectedList = form.getCompanyList();
				if (selectedList!= null)
				{
					for (CompanyForm companyForm: selectedList)
					{
						ChargeForm charge = companyForm.getCharge();
						System.out.println("unLockCharges getCompanyCode "+charge.getCompanyCode()+" getMonthYear "+charge.getMonthYear());
						charge.setStore_id(getStore_id(req));
						charge.setLockAccount(NO);
						dao.unLockCharge(charge);
					}
				}
					return getForward(result, JspConstants.LIST_CHARGE_JSP);
			
			}
			catch (Exception e) {
				e.printStackTrace();
				ActionErrors errors = new ActionErrors();
				errors.add("getCompanyCode", new ActionMessage("error.charge.cannotunlock",e.getMessage()));
				saveErrors(req,errors);
			}		
		return getForward(result, JspConstants.LIST_CHARGE_JSP);
	}	
		
	
	
		
		
	
					
		public ActionForward getTaxReturnForYear(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			System.out.println("********************** getTaxReturnForYear called **********************");
			ActionForward result = null;
			CompanyForm form = getCompanyForm(req);
			String year = form.getEditId();
			System.out.println("getTaxReturnForYear submitted  getCompanyCode  "+form.getCompanyCode());
			System.out.println("getTaxReturnForYear submitted  year  "+year);
			CompanyForm companyForm = form.getCompany(form.getCompanyCode());
			ReferenceDao dao = new ReferenceDao();
			if (form.getYearlyChargesExist() != null)
				form.getYearlyChargesExist().clear();
			if (companyForm != null && companyForm.getCompanyCode() != null)
			{
				form.setCompanyCode(companyForm.getCompanyCode());
				form.setCompanyName(companyForm.getCompanyName());
				form.setBnNumber(companyForm.getBnNumber());
				form.setAddress1(companyForm.getAddress1());
				form.setAddress2(companyForm.getAddress2());
				form.setCity(companyForm.getCity());
				form.setProvince(companyForm.getProvince());
				form.setPostalCode(companyForm.getPostalCode());
				form.setClientType(companyForm.getClientType());
				form.setClientStatusActive(companyForm.getClientStatusActive());

				YearlyClientForm yearlyClientInfo = new YearlyClientForm();
				yearlyClientInfo.setYear("MASTER");
				
				ArrayList<YearlyClientForm> yearlyClientInfos = dao.checkYearlyClientInfoExist(form.getCompanyCode(), getStore_id(req), null) ;             // checkClientTaxReturnInfo(form.getCompanyCode(), getStore_id(req), null);
				
				if (yearlyClientInfos!= null && yearlyClientInfos.size() > 0)
					{
						for (int x=0;x < yearlyClientInfos.size(); x++)
						{
							yearlyClientInfo = yearlyClientInfos.get(x);
							System.out.println("comparing selected Year "+year+ " yearlyClientInfo.getYear "+yearlyClientInfo.getYear());
							if (year != null && year.equals(yearlyClientInfo.getYear()))
							{
								form.setYearlyClientInfo(yearlyClientInfo);
							}
						}
							
					}else
					{
						form.setYearlyClientInfo(yearlyClientInfo);
					}
					
					
					if (yearlyClientInfos != null && yearlyClientInfos.size() > 1)
						form.setYearlyChargesExist(yearlyClientInfos);
					else
						form.setYearlyChargesExist(null);

					
					
				form.setComment(companyForm.getComment());
				form.setBankAccNumber(dao.getBankAccountNumber1(companyForm.getCompanyCode(),getStore_id(req)));
				req.setAttribute("companyCode",companyForm.getCompanyCode());
			}
			
			System.out.println("********************** END getTaxReturnForYear **********************");
			return getForward(result, JspConstants.TAX_RETURN_INFO_JSP);
	}	
			
		public ActionForward deleteTaxReturnInfo (ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;

			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&CompanyAction deleteTaxReturnInfo ");
			CompanyForm form = (CompanyForm)frm;
			try {
				System.out.println("CompanyAction deleteTaxReturnInfo "+form.getCompanyName());
				System.out.println("CompanyAction getEditId  "+form.getCompanyCode());
				ReferenceDao dao  = new ReferenceDao();
				if (form.getYearlyClientInfo() != null && form.getYearlyClientInfo().getLock()!= null &&
						form.getYearlyClientInfo().getLock().equals(YES))
				{
					ActionErrors errors = new ActionErrors();
					errors.add("getCompanyCode", new ActionMessage("error.TaxReturn.locked"));
					saveErrors(req,errors);
				}
				dao.deleteYearlyClientTaxReturnInfo(form.getYearlyClientInfo());
			}
			catch (Exception e) {
			}		
//			return getForward(result, JspConstants.TAX_RETURN_INFO_JSP);
			form.setEditId(form.getCompanyCode());
			return editTaxReturnInfo(mapping, form, req, resp);
			
			
		}		
		public ActionForward unlockTaxReturn(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			
			ActionForward result = null;
			CompanyForm form = getCompanyForm(req);
			String year = form.getEditId();
			System.out.println("unlockAccount called for  "+form.getCompanyCode()+" Year "+year);
			CompanyForm company = getCompanyForm(req);
			ReferenceDao dao  = new ReferenceDao();
			dao.unlockYearlyClient(company.getYearlyClientInfo());
			company.getYearlyClientInfo().setLock(NO);
			return getForward(result, JspConstants.TAX_RETURN_INFO_JSP);
	}

		
		public ActionForward gotoEditTaxReturnInfo (ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&CompanyAction gotoeditTaxReturnInfo ");
			CompanyForm form = (CompanyForm)frm;
			try {
				
				form.setEditId(form.getCompanyCode());
				System.out.println("CompanyAction deleteTaxReturnInfo "+form.getCompanyName());
				System.out.println("CompanyAction getEditId  "+form.getCompanyCode());
			}
			catch (Exception e) {
			}		
			return editTaxReturnInfo(mapping, form,req, resp);
		}				
		
		
		public ActionForward gotoEditYearlyClient (ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&CompanyAction gotoEditYearlyClient ");
			CompanyForm form = (CompanyForm)frm;
			try {
				form.setEditId(form.getCompanyCode());
				System.out.println("CompanyAction getEditId  "+form.getCompanyCode());
			}
			catch (Exception e) {
			}		
			return editYearlyClient(mapping, form,req, resp);
		}				

		public ActionForward printPayrollNotes (ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&CompanyAction printPayrollNotes ");
			CompanyForm form = (CompanyForm)frm;
			try {
				
				
				MonthlyNoteForm monthlyNoteForm  = getMonthlyNoteForm(req);
				monthlyNoteForm.setStore_id(getStore_id(req));
				monthlyNoteForm.setNotesType(PAYROLL);
				monthlyNoteForm.setStatus(OPEN_STATUS);
				monthlyNoteForm.setMonth(form.getMonth());
				monthlyNoteForm.setYear(form.getYear());
				monthlyNoteForm.setNotes(form.getNotes());
				MonthlyNotesAction action = new MonthlyNotesAction();
				return action.printPayrollNotes(mapping, monthlyNoteForm,req, resp);
			}
			catch (Exception e) {
			}		
			//print_notes.jsp
			return getForward(null, JspConstants.PAYROLL_NOTES_PRINT_JSP);
		}			

		public ActionForward printChargeNotes (ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&CompanyAction printPayrollNotes ");
			CompanyForm form = (CompanyForm)frm;
			try {
				
				MonthlyNoteForm monthlyNoteForm  = getMonthlyNoteForm(req);
				monthlyNoteForm.setStore_id(getStore_id(req));
				monthlyNoteForm.setNotesType(CHARGE);
				monthlyNoteForm.setStatus(OPEN_STATUS);
				monthlyNoteForm.setMonth(form.getMonth());
				monthlyNoteForm.setYear(form.getYear());
				monthlyNoteForm.setNotes(form.getNotes());
				MonthlyNotesAction action = new MonthlyNotesAction();
				return action.printChargeNotes(mapping, monthlyNoteForm,req, resp);
			}
			catch (Exception e) {
			}		
			return getForward(null, JspConstants.CHARGE_NOTES_PRINT_JSP);
		}			
		
		public ActionForward AddNewCharge(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("unLockCharges submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("unLockCharges MONTH "+form.getMonth());
				System.out.println("unLockCharges getCompanyCode "+form.getCompanyCode());

				ReferenceDao dao  = new ReferenceDao();
				ArrayList<CompanyForm > selectedList = form.getCompanyList();
				if (selectedList!= null)
				{
					for (CompanyForm companyForm: selectedList)
					{
						ChargeForm charge = companyForm.getCharge();
						System.out.println("unLockCharges getCompanyCode "+charge.getCompanyCode()+" getMonthYear "+charge.getMonthYear());
						charge.setStore_id(getStore_id(req));
						charge.setLockAccount(NO);
						dao.unLockCharge(charge);
					}
				}
					return getForward(result, JspConstants.LIST_CHARGE_JSP);
			
			}
			catch (Exception e) {
				e.printStackTrace();
				ActionErrors errors = new ActionErrors();
				errors.add("getCompanyCode", new ActionMessage("error.charge.cannotunlock",e.getMessage()));
				saveErrors(req,errors);
			}		
		return getForward(result, JspConstants.LIST_CHARGE_JSP);
	}		
	
		public ActionForward gotoAddDeleteCharge(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("gotoAddDeleteCharge submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				ReferenceDao dao  = new ReferenceDao();
				ArrayList<CompanyForm > activeCompanyList = dao.getActiveCompanies(getStore_id(req), CHARGE);
				form.setActiveCompanyList(activeCompanyList);
			
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
		return getForward(result, JspConstants.ADD_DELETE_CHARGE_JSP);
	}		
		
		public ActionForward gotoAddDeletePayroll(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("gotoAddDeletePayroll submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				ReferenceDao dao  = new ReferenceDao();
				ArrayList<CompanyForm> activeCompanyList = dao.getActiveCompanies(getStore_id(req), PAYROLL);
				form.setActiveCompanyList(activeCompanyList);
			
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
		return getForward(result, JspConstants.ADD_DELETE_PAYROLL_JSP);
	}		
	
		
		public ActionForward addNewChargeForCompany(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("addNewChargeForCompany submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("addNewChargeForCompany MONTH "+form.getMonth());
				System.out.println("addNewChargeForCompany YEAR "+form.getYear());
				System.out.println("addNewChargeForCompany Edit Id "+form.getEditId());
				System.out.println("addNewChargeForCompany getCompanyCode "+form.getCompanyCode());

				ReferenceDao dao  = new ReferenceDao();
				
				
				ChargeAction action = new ChargeAction();
				ChargeForm chargeForm = new ChargeForm();
				chargeForm.setStore_id(getStore_id(req));
				chargeForm.setMonth(form.getMonth());
				chargeForm.setYear(form.getYear());
				chargeForm.setWithdrawDate("ALL");
				chargeForm.setEditId(form.getEditId());
				
				if (action.copyCharge(chargeForm, req)!= null)
				{
					ActionErrors actionErrors = new ActionErrors();
					actionErrors.add("error.charges.exist.message", new ActionMessage("error.charges.exist.message"));
					saveErrors(req,actionErrors);
					System.out.println("Charges for the given month and year already exist. do you want to overright");
				}
				return searchCharges(mapping,frm,req,resp);
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
		return getForward(result, JspConstants.LIST_CHARGE_JSP);
	}
		
		
		public ActionForward deleteChargeForCompany(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("deleteChargeForCompany submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("deleteChargeForCompany MONTH "+form.getMonth());
				System.out.println("deleteChargeForCompany YEAR "+form.getYear());
				System.out.println("deleteChargeForCompany Edit Id "+form.getEditId());
				System.out.println("deleteChargeForCompany getCompanyCode "+form.getCompanyCode());

				ReferenceDao dao  = new ReferenceDao();
				dao.deleteCharge(form.getEditId(),form.getMonth()+form.getYear(),getStore_id(req));
				form.setEditId(null);
				return searchCharges(mapping,frm,req,resp);
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
		return getForward(result, JspConstants.LIST_CHARGE_JSP);
	}
	
		public ActionForward deletePayrollForCompany(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("deletePayroll submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("addPayrollForCompany MONTH "+form.getMonth());
				System.out.println("addPayrollForCompany YEAR "+form.getYear());
				System.out.println("addPayrollForCompany Edit Id "+form.getEditId());
				System.out.println("addPayrollForCompany getCompanyCode "+form.getCompanyCode());

				ReferenceDao dao  = new ReferenceDao();
				dao.deletePayrollByMonthYear(form.getMonth()+form.getYear(),getStore_id(req),form.getEditId());
				form.setEditId(null);
				return searchpayrolls(mapping,form,req,resp);
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
		return getForward(result, JspConstants.UPDATE_PAYROLLS_JSP);
	}

		public ActionForward addPayrollForCompany(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("addPayrollForCompany submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("addPayrollForCompany MONTH "+form.getMonth());
				System.out.println("addPayrollForCompany YEAR "+form.getYear());
				System.out.println("addPayrollForCompany Edit Id "+form.getEditId());
				System.out.println("addPayrollForCompany getCompanyCode "+form.getCompanyCode());
				PayrollForm payrollForm = new PayrollForm();
				payrollForm.setStore_id(getStore_id(req));
				payrollForm.setMonth(form.getMonth());
				payrollForm.setYear(form.getYear());
				payrollForm.setEditId(form.getEditId());
				copyPayroll(payrollForm,req);
				return searchpayrolls(mapping,form,req,resp);
			}
			catch (Exception e) {
				e.printStackTrace();
				ActionErrors errors = new ActionErrors();
				errors.add("getCompanyCode", new ActionMessage("error.charge.cannotunlock",e.getMessage()));
				saveErrors(req,errors);
			}		
			return getForward(result, JspConstants.UPDATE_PAYROLLS_JSP);
	}		
		
		public ActionErrors copyPayroll (PayrollForm form,HttpServletRequest req)  {
					System.out.println("PayrollAction copyPayroll  ");
			try {
				ActionErrors actionErrors = new ActionErrors();
				System.out.println("copyPayroll submitted "+form.getMonth()+form.getYear());
				String previousMonth = getPreviousMonth(form.getMonth()+form.getYear());
				System.out.println("copyPayroll submitted "+form.getMonth()+form.getYear());
				ReferenceDao dao  = new ReferenceDao();
				form.setStore_id(getStore_id(req));
				
				String companyCode =form.getEditId();
				ArrayList<PayrollForm > payrollList = null;  
				
				
				if (companyCode != null )
				{
					payrollList  = dao.checkPayrollExist(companyCode,form.getMonth()+form.getYear() , getStore_id(req));
					if (payrollList!= null && payrollList.size() > 0)
					{
						actionErrors.add("error.pauroll.exist.message", new ActionMessage("error.pauroll.exist.message"));
						req.setAttribute("error.pauroll.exist.message","error.pauroll.exist.message");
						saveErrors(req,actionErrors);
						System.out.println("payrolls for the given month and year already exist. do you want to overright");
						return actionErrors;
					}
				}
				PayrollForm  masterPayroll  = dao.checkMasterPayrollExist(companyCode,getStore_id(req));
				if (masterPayroll!= null)
				{
					masterPayroll.setPayrollMonth(form.getMonth()+form.getYear());
					ArrayList<PayrollForm> previousPayrolls = dao.checkPayrollExist(masterPayroll.getCompanyCode(),previousMonth, masterPayroll.getStore_id());
					if (previousPayrolls != null && previousPayrolls.size()  > 0)
						{
							PayrollForm previousPayroll = previousPayrolls.get(0);
							if (YES.equals(previousPayroll.getNsf()))
							{
								masterPayroll.setPreviousBalance(getNsfCharge(req)+masterPayroll.getPreviousBalance());
							}
						}
							
						System.out.println("adding payrolls for  "+form.getMonth()+form.getYear());
						ArrayList<PayrollForm> existingPayrolls= dao.checkPayrollExist(masterPayroll.getCompanyCode(),masterPayroll.getPayrollMonth(), masterPayroll.getStore_id());
						if (existingPayrolls != null && existingPayrolls.size() > 0)
						{
							if (form.getOverright()!= null && form.getOverright().equals("Yes"))
							{
								dao.updatePayroll(masterPayroll);
							}
							
							actionErrors.add("error.pauroll.exist.message", new ActionMessage("error.pauroll.exist.message"));
							req.setAttribute("error.pauroll.exist.message","error.pauroll.exist.message");
							saveErrors(req,actionErrors);
							System.out.println("payrolls for the given month and year already exist. do you want to overright");
							return actionErrors;
						}else
						{
							masterPayroll.setNsf("N");
							dao.addPayroll(masterPayroll);
						}
					}
			}
			catch (Exception e) {
				e.printStackTrace();
			}		
			return null;
		}			
		public ActionForward lockPayrolls(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("lockPayrolls submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("lockPayrolls MONTH "+form.getMonth());
				System.out.println("lockPayrolls getCompanyCode "+form.getCompanyCode());

				ReferenceDao dao  = new ReferenceDao();
				ArrayList<CompanyForm > selectedList = form.getCompanyList();
				System.out.println("lockPayrolls YEAR "+selectedList);
				if (selectedList!= null)
				{
					for (CompanyForm companyForm: selectedList)
					{
						PayrollForm payroll = companyForm.getPayroll();
						System.out.println("lockPayrolls getCompanyCode "+payroll.getCompanyCode()+" getMonthYear "+payroll.getPayrollMonth());
						payroll.setStore_id(getStore_id(req));
						payroll.setAccountLocked(YES);
						dao.lockPayroll(payroll);
					}
				}
					return getForward(result, JspConstants.UPDATE_PAYROLLS_JSP);
			
			}
			catch (Exception e) {
				e.printStackTrace();
				ActionErrors errors = new ActionErrors();
				errors.add("getCompanyCode", new ActionMessage("error.payroll.cannotlock",e.getMessage()));
				saveErrors(req,errors);
			}		
		return getForward(result, JspConstants.UPDATE_PAYROLLS_JSP);
	}		
		
		
		
		public ActionForward unLockPayrolls(ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
			ActionForward result = null;
			System.out.println("unLockPayrolls submitted ");
			try {
				CompanyForm form =(CompanyForm)frm  ;
				System.out.println("unLockPayrolls MONTH "+form.getMonth());
				System.out.println("unLockPayrolls getCompanyCode "+form.getCompanyCode());

				ReferenceDao dao  = new ReferenceDao();
				ArrayList<CompanyForm > selectedList = form.getCompanyList();
				if (selectedList!= null)
				{
					for (CompanyForm companyForm: selectedList)
					{
						PayrollForm payroll = companyForm.getPayroll();
						System.out.println("unLockPayrolls getCompanyCode "+payroll.getCompanyCode()+" getMonthYear "+payroll.getPayrollMonth());
						payroll.setStore_id(getStore_id(req));
						payroll.setAccountLocked(NO);
						dao.unLockPayroll(payroll);
					}
				}
					return getForward(result, JspConstants.UPDATE_PAYROLLS_JSP);
			
			}
			catch (Exception e) {
				e.printStackTrace();
				ActionErrors errors = new ActionErrors();
				errors.add("getCompanyCode", new ActionMessage("error.payroll.cannotunlock",e.getMessage()));
				saveErrors(req,errors);
			}		
		return getForward(result, JspConstants.UPDATE_PAYROLLS_JSP);
	}			

		
		
		public ActionForward addNextYearlyCharge (ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
				ActionForward result = null;
				ActionErrors actionErrors = null;
				System.out.println("******************* addNextYearlyCharge copyCharge  ");
				actionErrors = new ActionErrors();
				CompanyForm form = (CompanyForm)frm;
				try {
					String companyCode = form.getCompanyCode();
					String companyName = form.getCompanyName();
					System.out.println("addNewYearlyCharge submitted for CompanyCode "+companyCode+" companyName "+companyName+" year "+form.getYearTo());
					ReferenceDao dao  = new ReferenceDao();
					ArrayList<YearlyClientForm> clientList = dao.checkYearlyClientInfoExist(companyCode, getStore_id(req),form.getYearTo());
					if (clientList != null &&  clientList.size()>0 && clientList.get(0) != null)
					{
						YearlyClientForm yearlyClientFromDb = clientList.get(0);
						if (yearlyClientFromDb!= null && yearlyClientFromDb.getPaymentInfoExist().equals("N"))
						{
							dao.updateClientPaymentFlag(yearlyClientFromDb,"Y");
							return gotoEditYearlyClient(mapping, form, req, resp);
							
						}
					}
					
					if (clientList!= null && clientList.size() > 0 )
					{
						actionErrors.add("error.payment.exist.message", new ActionMessage("error.payment.exist.message"));
						//req.setAttribute("error.charges.exist","error.charges.exist");
						saveErrors(req,actionErrors);
						return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);		
					}
					
					
					form.setStore_id(getStore_id(req));
					YearlyClientForm  charge = dao.checkYearlyMasterClientInfoExist(companyCode, form.getStore_id());
					
					if (charge != null && charge.getActive()!= null && YES.equals(charge.getActive()))
					{
							System.out.println(" addNewYearlyCharge set year to  "+form.getYearTo());	
							charge.setYear(form.getYearTo());
							// we need to copy previous balance from previous month charge record
							String previousYear = getPreviousYear(form.getYear());
							System.out.println(" addNewYearlyCharge previousYear "+previousYear);
							ArrayList<YearlyClientForm> previousCharges = dao.checkYearlyClientInfoExist(charge.getCompanyCode(),charge.getStore_id(), previousYear);
							System.out.println("populating "+charge.getYear()+" for "+charge.getCompanyCode());
							if (previousCharges != null && previousCharges.size() > 0)
							{
								YearlyClientForm previousCharge = previousCharges.get(0);
								charge.setPreBalance(previousCharge.getBalanceDue());
								System.out.println("Balance Due carry forwarded  for "+charge.getYear()+" for "+charge.getCompanyCode());
							}
							
							System.out.println("populating "+charge.getYear()+" for "+charge.getCompanyCode());
							charge.setGstRate(getGstRate(req));
							charge.setTaxCompleted("N");
							charge.setTaxFiled("N");
							charge.setTaxMailed("N");
							charge.setPaymentReceived(0.0);
							charge.setPaymentInfoExist("Y");
							charge.setTaxReturnInfoExist("N");
							charge.setYearEnd(formatYearEnd(charge.getYearEnd(),charge.getYear() ));
							System.out.println(" addNewYearlyCharge Charge for  "+charge.getYear());
							dao.insertYearlyClientInfo(charge);
					}else
					{
						System.out.println("Charge is not active for the selected year to copy ");
						actionErrors.add("error.client.not.active", new ActionMessage("error.client.not.active"));
						//req.setAttribute("error.charges.exist","error.charges.exist");
						saveErrors(req,actionErrors);
						return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);		
					}
					if (actionErrors != null && actionErrors.size() > 0)
					{
						saveErrors(req,actionErrors);
						return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);				
					}
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				return gotoEditYearlyClient(mapping, form, req, resp);
			 //return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);
			}


		public ActionForward addNextYearTaxReturn (ActionMapping mapping, ActionForm frm, 
				HttpServletRequest req, HttpServletResponse resp)  {
				ActionForward result = null;
				ActionErrors actionErrors = null;
				System.out.println("******************* addNextYearTaxReturn copyCharge  ");
				actionErrors = new ActionErrors();
				CompanyForm form = (CompanyForm)frm;
				try {
					System.out.println("addNextYearTaxReturn submitted for year "+form.getYearTo());
					String companyCode = form.getCompanyCode();
					String companyName = form.getCompanyName();
					System.out.println("addNextYearTaxReturn submitted for getCompanyCode "+companyCode);
					System.out.println("addNextYearTaxReturn submitted for companyName "+companyName);
					ReferenceDao dao  = new ReferenceDao();
					form.setStore_id(getStore_id(req));
					
					ArrayList<YearlyClientForm>  taxReturns = dao.checkYearlyClientInfoExist(companyCode, form.getStore_id(),form.getYearTo()) ; //         checkClientTaxReturnInfo(companyCode, form.getStore_id(),form.getYearTo());
					
					if (taxReturns != null &&  taxReturns.size()>0 && taxReturns.get(0) != null)
					{
						YearlyClientForm yearlyClientFromDb = taxReturns.get(0);
						if (yearlyClientFromDb!= null && yearlyClientFromDb.getTaxReturnInfoExist().equals("N"))
						{
							dao.updateClientTaxReturnFlag(yearlyClientFromDb,"Y");
							return editTaxReturnInfo(mapping, form, req, resp);
						}
					}
					
					if (taxReturns!= null && taxReturns.size() > 0 )
					{
						actionErrors.add("error.tax.return.exist.message", new ActionMessage("error.tax.return.exist.message"));
						//req.setAttribute("error.charges.exist","error.charges.exist");
						saveErrors(req,actionErrors);
						return getForward(result, JspConstants.TAX_RETURN_INFO_JSP);		
					}
					
					
					YearlyClientForm  charge = dao.checkYearlyMasterClientInfoExist(companyCode, form.getStore_id());
					
					if (charge != null && charge.getActive()!= null && YES.equals(charge.getActive()))
					{
						
						
						System.out.println(" addNewYearlyCharge set year to  "+form.getYearTo());	
						charge.setYear(form.getYearTo());
						// we need to copy previous balance from previous month charge record
						String previousYear = getPreviousYear(form.getYear());
						System.out.println(" addNewYearlyCharge previousYear "+previousYear);
						ArrayList<YearlyClientForm> previousCharges = dao.checkYearlyClientInfoExist(charge.getCompanyCode(),charge.getStore_id(), previousYear);
						System.out.println("populating "+charge.getYear()+" for "+charge.getCompanyCode());
						if (previousCharges != null && previousCharges.size() > 0)
						{
							YearlyClientForm previousCharge = previousCharges.get(0);
							charge.setPreBalance(previousCharge.getBalanceDue());
							System.out.println("Balance Due carry forwarded  for "+charge.getYear()+" for "+charge.getCompanyCode());
						}
						System.out.println("populating "+charge.getYear()+" for "+charge.getCompanyCode());
						charge.setGstRate(getGstRate(req));
						charge.setTaxCompleted("N");
						charge.setTaxFiled("N");
						charge.setTaxMailed("N");
						charge.setPaymentReceived(0.0);
						charge.setYearEnd(formatYearEnd(charge.getYearEnd(),charge.getYear() ));
						charge.setPaymentInfoExist("N");
						charge.setTaxReturnInfoExist("Y");
						System.out.println(" addNewYearlyCharge Charge for  "+charge.getYear());
						charge.setLock(NO);
						charge.setYearEnd(formatYearEnd(charge.getYearEnd(),charge.getYear() ));
						dao.insertYearlyClientInfo(charge);
					}	
					else
					{
						if (form.getClientType() != null && form.getClientType().equals(MONTHLY))
						{
							YearlyClientForm  taxReturn = new YearlyClientForm();
							taxReturn.setYear(form.getYearTo());
							taxReturn.setCompanyCode(form.getCompanyCode());
							taxReturn.setStore_id(getStore_id(req));
							taxReturn.setTaxCompleted("N");
							taxReturn.setTaxFiled("N");
							taxReturn.setTaxMailed("N");
							taxReturn.setPaymentReceived(0.0);
							taxReturn.setYearEnd("");
							taxReturn.setPaymentInfoExist("N");
							taxReturn.setTaxReturnInfoExist("Y");
							System.out.println(" addNewYearlytaxReturn taxReturn for  "+taxReturn.getYear());
							taxReturn.setLock(NO);
							dao.insertYearlyClientInfo(taxReturn);
						}
						else
						{
							System.out.println("Client is not active for the selected year to copy ");
							actionErrors.add("error.client.not.active", new ActionMessage("error.client.not.active"));
							saveErrors(req,actionErrors);
							return getForward(result, JspConstants.TAX_RETURN_INFO_JSP);
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}	
				form.setEditId(form.getCompanyCode());
				//return getForward(result, JspConstants.TAX_RETURN_INFO_JSP);
				return editTaxReturnInfo(mapping, form, req, resp);
			}	

		public ActionForward deleteCompanyYearlyInfo (ActionMapping mapping, ActionForm frm,HttpServletRequest req, HttpServletResponse resp)  {
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&CompanyAction deleteCompanyYearlyInfo ");
			CompanyForm form = null;
			try {
				form = (CompanyForm)frm;
				System.out.println("deleteCompanyYearlyInfo getEditId  "+form.getCompanyCode());
				form.setEditId(form.getCompanyCode());
				ReferenceDao dao  = new ReferenceDao();
				dao.deleteCompanyYearlyInfo(form.getYearlyClientInfo());
			}
			catch (Exception e) {
			}		
			return editCompany( mapping,form,req,resp);
			//return getForward(null, JspConstants.COMPANY_JSP);
		}

}
