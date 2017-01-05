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
import com.accounting.form.PayrollForm;
import com.accounting.utils.JspConstants;

public class PayrollAction extends BaseAction {
	public PayrollAction(){}

	
	public ActionForward backToCompany(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("backToCompany submitted ");
		try {
		return (new CompanyAction()).editCompany(mapping,frm,req,resp);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.COMPANY_JSP);
}
	
	
	public ActionForward submit(ActionMapping mapping, ActionForm frm, 
					HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("PayrollAction submitted .. adding new Payroll ");
		try {
			PayrollForm form = (PayrollForm)frm;
			CompanyForm company= getCompanyForm(req);
			System.out.println(" adding new Payroll "+form.getCompanyCode());
			ActionErrors actionErrors = new ActionErrors();
			if (form.getPayrollMonth() == null)
			{
				actionErrors.add("payrollMonth", new ActionMessage("error.payrollMonth.missing"));
			}
			if(isEmpty(form.getCompanyCode())){
				actionErrors.add("getCompanyCode", new ActionMessage("error.companyCode.missing"));
			}
			System.out.println("form.getGrossPayroll():"+form.getGrossPayroll());
			if(form.getGrossPayroll() == 0.0 && "FIX".equals(form.getPayrollType()) && YES.equals(form.getActive())){
				actionErrors.add("grossPayroll", new ActionMessage("error.grossPayroll.missing"));
			}
			if(form.getRemittanceAmount() == 0.0 && "FIX".equals(form.getPayrollType()) && YES.equals(form.getActive())){
				actionErrors.add("RemittanceAmount", new ActionMessage("error.remittanceAmount.missing"));
			}
			if(form.getNumberOfemployees() == 0.0 && "FIX".equals(form.getPayrollType()) && YES.equals(form.getActive())){
				actionErrors.add("NumberOfemployees", new ActionMessage("error.numberOfemployees.missing"));
			}
			System.out.println("PayrollAction submitted "+form.getCompanyCode());
			saveErrors(req,actionErrors);
			if (actionErrors.size()> 0)
			{
				req.setAttribute("companyCode",form.getCompanyCode());
				return getForward(result, JspConstants.PAYROLL_JSP);
			}
			
			
			ReferenceDao dao  = new ReferenceDao();
			
			form.setBankCharge(calculateBankCharges(form.getRemittanceAmount(),req));
			form.setTotalAmount(form.getRemittanceAmount()+form.getBankCharge());
			PayrollForm existingPayroll = dao.checkMasterPayrollExist(form.getCompanyCode(), getStore_id(req));
			saveErrors(req,actionErrors);
			form.setStore_id(getStore_id(req));
			
			boolean payrollUpdated=false;
			if (existingPayroll!= null )
			{
				System.out.println("existingPayroll "+existingPayroll+" updating existing payroll ...");
				payrollUpdated= dao.updateFirstPayroll(form);
				System.out.println("payrollUpdated "+payrollUpdated);
				System.out.println("Company name "+form.getCompanyName());
				
				req.setAttribute("companyCode",form.getCompanyCode());
				company.setCompanyCode(form.getCompanyCode());
				return getForward(result, JspConstants.PAYROLL_JSP);
			}
			else if ((!payrollUpdated) && (actionErrors.size()==0))
			{
				System.out.println("existingPayroll "+existingPayroll+" adding new payroll ...");
				dao.addPayroll(form);
				//form.clear();
				req.setAttribute("companyCode",form.getCompanyCode());
				company.setCompanyCode(form.getCompanyCode());
				return getForward(result, JspConstants.PAYROLL_JSP);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.PAYROLL_JSP);
	}
	
	
	public ActionForward copyPayroll (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("PayrollAction copyPayroll  ");
		try {
			PayrollForm form = (PayrollForm)frm;
			ActionErrors actionErrors = new ActionErrors();
			System.out.println("PayrollAction submitted "+form.getMonth()+form.getYear());
			String previousMonth = getPreviousMonth(form.getMonth()+form.getYear());
			System.out.println("PayrollAction submitted "+form.getMonth()+form.getYear());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(getStore_id(req));
			boolean overrightPayrolls = false;
			
//			String companyCode =form.getCompanyCode();
			ArrayList<PayrollForm > payrollList = null;  
			
			
			//payrollList  = dao.checkPayrollExist(companyCode,form.getMonth()+form.getYear() , getStore_id(req));
			
			payrollList  = dao.checkPayrollExistByMonthYear(form.getMonth()+form.getYear(),getStore_id(req));
				if (payrollList!= null && payrollList.size() > 0)
				{
					if (form.getOverright()!= null && form.getOverright().equals("Yes"))
					{
						overrightPayrolls = true;
						dao.deletePayrollByMonthYear(form.getMonth()+form.getYear(),getStore_id(req));
					}
					else
					{
					overrightPayrolls = false;
					actionErrors.add("error.payrolls.exist", new ActionMessage("error.payrolls.exist"));
					req.setAttribute("error.payrolls.exist","error.payrolls.exist");
					saveErrors(req,actionErrors);
					System.out.println("payrolls for the given month and year already exist. do you want to overright");
					return getForward(result, JspConstants.SELECT_MONTH_FOR_PAYROLL_COPY_JSP);
				}
			}
			ArrayList<PayrollForm > selectedList  = dao.getMasterPayrolls(getStore_id(req));
			if (selectedList!= null && selectedList.size() > 0)
			{
				for (PayrollForm payroll: selectedList )
				{
					payroll.setPayrollMonth(form.getMonth()+form.getYear());
					ArrayList<PayrollForm> previousPayrolls = dao.checkPayrollExist(payroll.getCompanyCode(),previousMonth, payroll.getStore_id());
					if (previousPayrolls != null && previousPayrolls.size()  > 0)
					{
						PayrollForm previousPayroll = previousPayrolls.get(0);
						if (YES.equals(previousPayroll.getNsf()))
						{
							payroll.setPreviousBalance(getNsfCharge(req)+payroll.getPreviousBalance());
						}
					}
						
					System.out.println("adding payrolls for  "+form.getMonth()+form.getYear());
					ArrayList<PayrollForm> existingPayrolls= dao.checkPayrollExist(payroll.getCompanyCode(),payroll.getPayrollMonth(), payroll.getStore_id());
					if (existingPayrolls != null && existingPayrolls.size() > 0)
					{
						if (form.getOverright()!= null && form.getOverright().equals("Yes"))
						{
							
							dao.updatePayroll(payroll);
							continue;
						}
						
						actionErrors.add("error.payrolls.exist", new ActionMessage("error.payrolls.exist"));
						req.setAttribute("error.payrolls.exist","error.payrolls.exist");
						saveErrors(req,actionErrors);
						System.out.println("payrolls for the given month and year already exist. do you want to overright");
						return getForward(result, JspConstants.SELECT_MONTH_FOR_PAYROLL_COPY_JSP);
					}else
					{
						payroll.setNsf("N");
						dao.addPayroll(payroll);
					}
				}
				
			}
			
			
			//// IF ALL NOT SELECTED 
/*			if (companyCode != null && !companyCode.equals(ALL))
			{
				payrollList  = dao.checkPayrollExist(companyCode,form.getMonth()+form.getYear() , getStore_id(req));
				if (payrollList!= null && payrollList.size() > 0)
				{
					if (form.getOverright()!= null && form.getOverright().equals("Yes"))
					{
						overrightPayrolls = true;
						dao.deletePayrollByMonthYear(form.getMonth()+form.getYear(),getStore_id(req),companyCode );
					}
					else
					{
					overrightPayrolls = false;
					actionErrors.add("error.payrolls.exist", new ActionMessage("error.payrolls.exist"));
					req.setAttribute("error.payrolls.exist","error.payrolls.exist");
					saveErrors(req,actionErrors);
					System.out.println("payrolls for the given month and year already exist. do you want to overright");
					return getForward(result, JspConstants.SELECT_MONTH_FOR_PAYROLL_COPY_JSP);
				}
			}
			PayrollForm  payroll  = dao.checkMasterPayrollExist(companyCode,getStore_id(req));
			if (payroll!= null)
			{
				payroll.setPayrollMonth(form.getMonth()+form.getYear());
				ArrayList<PayrollForm> previousPayrolls = dao.checkPayrollExist(payroll.getCompanyCode(),previousMonth, payroll.getStore_id());
				if (previousPayrolls != null && previousPayrolls.size()  > 0)
					{
						PayrollForm previousPayroll = previousPayrolls.get(0);
						if (YES.equals(previousPayroll.getNsf()))
						{
							payroll.setPreviousBalance(getNsfCharge(req)+payroll.getPreviousBalance());
						}
					}
						
					System.out.println("adding payrolls for  "+form.getMonth()+form.getYear());
					ArrayList<PayrollForm> existingPayrolls= dao.checkPayrollExist(payroll.getCompanyCode(),payroll.getPayrollMonth(), payroll.getStore_id());
					if (existingPayrolls != null && existingPayrolls.size() > 0)
					{
						if (form.getOverright()!= null && form.getOverright().equals("Yes"))
						{
							dao.updatePayroll(payroll);
						}
						
						actionErrors.add("error.payrolls.exist", new ActionMessage("error.payrolls.exist"));
						req.setAttribute("error.payrolls.exist","error.payrolls.exist");
						saveErrors(req,actionErrors);
						System.out.println("payrolls for the given month and year already exist. do you want to overright");
						return getForward(result, JspConstants.SELECT_MONTH_FOR_PAYROLL_COPY_JSP);
					}else
					{
						payroll.setNsf("N");
						dao.addPayroll(payroll);
					}
				}
				
			}*/
			
		
			
			form.setOverright(null);
			req.setAttribute("FromMonth",previousMonth);
			req.setAttribute("ToMonth",form.getMonth()+form.getYear());
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.COPY_CONFIRMATION_JSP);
	}
	public static void main(String[] args)
	{
		System.out.println(getPreviousMonth("072008"));
	}


	public ActionForward search (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("PayrollAction submitted ");
		try {
			PayrollForm form = getPayrollForm(req);
			
			System.out.println("PayrollAction submitted "+form.getCompanyName());
			ReferenceDao dao  = new ReferenceDao();
//			ArrayList<PayrollForm >  list =  dao.getCompanies(form);
//			form.setList(list);
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.LIST_COMPANY_JSP);
	}
	
	public ActionForward delete (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("PayrollAction delete ");
		try {
			PayrollForm form = getPayrollForm(req);
			
			System.out.println("PayrollAction delete "+form.getCompanyName());
			System.out.println("PayrollAction getEditId  "+form.getEditId());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(getStore_id(req));
			dao.deleteCompany(form.getEditId(),getStore_id(req));
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.LIST_COMPANY_JSP);
	}
	
	
	public ActionForward gotoAddCharge (ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			PayrollForm form = getPayrollForm(req);
			String companyCode = form.getCompanyCode();
			System.out.println("gotoAddCharge companyCode  "+companyCode);
			ReferenceDao dao = new ReferenceDao();
			ChargeForm masterCharge = dao.checkMasterChargeExist(companyCode,getStore_id(req));
			ChargeForm chargeForm = null;
			
			if (masterCharge != null )
			{
				chargeForm = masterCharge;
				System.out.println(" chargeForm Found for "+chargeForm.getCompanyCode());
				req.getSession().setAttribute("chargeForm",chargeForm);
			}
			else
			{
				chargeForm = new ChargeForm ();
				chargeForm.clear();
				chargeForm.setCompanyCode(companyCode);
				chargeForm.setMonthYear(JspConstants.MASTER_CHARGE);
				req.getSession().setAttribute("chargeForm",chargeForm);
			}
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.CHARGE_JSP);
	}
	
	public ActionForward gotoGenereateMonthlyPayrolls(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("gotoGenereateMonthlyPayrolls from charge page ");
		try {
			PayrollForm form = getPayrollForm(req);
			form.setStore_id(getStore_id(req));
			ReferenceDao dao  = new ReferenceDao();
			CompanyForm companyForm = new CompanyForm();
			companyForm.setStore_id(getStore_id(req));
			ArrayList<CompanyForm> companies = dao.getAllCompanies(companyForm);
			form.setCompanylist(companies);
			req.getSession().setAttribute(JspConstants.PAYROLL_FORM,form);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.SELECT_MONTH_FOR_PAYROLL_COPY_JSP);
	}	
}
