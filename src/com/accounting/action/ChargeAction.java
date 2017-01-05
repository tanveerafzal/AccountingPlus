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
import com.accounting.form.YearlyClientForm;
import com.accounting.utils.JspConstants;

public class ChargeAction extends BaseAction {
	public ChargeAction(){}

	public ActionForward submit(ActionMapping mapping, ActionForm frm, 
					HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("ChargeAction submitted .. adding new Charge ");
		try {
			ChargeForm form = (ChargeForm)frm;
			CompanyForm company= getCompanyForm(req);
			System.out.println(" adding new Charge "+form.getCompanyCode());
			ActionErrors actionErrors = new ActionErrors();
			
			if(isEmpty(form.getCompanyCode())){
				actionErrors.add("getCompanyCode", new ActionMessage("error.companyCode.missing"));
			}
			if(form.getNetAmount() == 0.0 && YES.equals(form.getActive())){
				actionErrors.add("NetAmount", new ActionMessage("error.netAmount.missing"));
			}
			if(isEmpty(form.getMonthYear())){
				actionErrors.add("monthYear", new ActionMessage("error.monthYear.missing"));
			}

			if(YES.equals(form.getActive())){
				System.out.println("getInstallamentTotal :"+form.getPreviousAmountInstallment());
				System.out.println("getPreviousAmount : "+form.getPreviousAmount());
				
				System.out.println("getPreviousAmount : "+(form.getPreviousAmountInstallment() >  form.getPreviousAmount()));
				
				if (form.getPreviousAmountInstallment() >  form.getPreviousAmount()) 
				{
					actionErrors.add("NetAmount", new ActionMessage("error.installament.greaterThan.previousamount"));
				}
			}

			
			
			saveErrors(req,actionErrors);
			if (actionErrors.size() > 0)
				return getForward(result, JspConstants.CHARGE_JSP);	
			ReferenceDao dao  = new ReferenceDao();
			form.setGst(calculateGST(form.getNetAmount(),req));
			form.setTotal(form.getNetAmount()+calculateGST(form.getNetAmount(),req));
			//form.setPreviousAmountBalance(form.getPreviousAmount() - form.getPreviousAmountInstallment());
			form.setTotalPreviousAmountPaid(dao.getTotalInstallmentPaid(form.getCompanyCode(), getStore_id(req)));
			form.setPreviousAmountBalance(form.getPreviousAmount() - form.getTotalPreviousAmountPaid());
			if (form.getPreviousAmountBalance() == 0)
			{
				form.setPreviousAmountInstallment(0);
			}
			if (YES.equals(form.getPreviousAmountGstIncluded()))
					{
						form.setInstallamentGst(0.0);
					}
			else
			{
				form.setInstallamentGst(calculateGST(form.getPreviousAmountInstallment(),req));
			}
			form.setInstallamentTotal(form.getPreviousAmountInstallment()+form.getInstallamentGst());
			form.setTotalWithdrawAmount(form.getTotal()+form.getInstallamentTotal());
			form.setStore_id(getStore_id(req));
			boolean chargeUpdated=false;
			ArrayList<ChargeForm> existingCharges  = dao.checkChargeExist(form.getCompanyCode(), form.getMonthYear(), getStore_id(req));
			if (existingCharges!= null && existingCharges.size() > 0)
			{
				System.out.println("form gst "+form.getGst());
				dao.updateCharge(form);
				System.out.println("Charge ALREADY EXIST -- updated "+form.getMonthYear());
				chargeUpdated = true;
				return getForward(result, JspConstants.CHARGE_JSP);
			}
			//form.setAccountNumber(dao.getBankAccountNumber(form.getCompanyCode(), getStore_id(req)));
			saveErrors(req,actionErrors);
			System.out.println("accountNumber "+form.getAccountNumber());
			System.out.println("getStore_id "+form.getStore_id());
			
			if (actionErrors.size() == 0 && !chargeUpdated)
			{
				dao.addCharge(form);
				company.setCompanyCode(form.getCompanyCode());
				company.setCompanyName(form.getCompanyName());
				return getForward(result, JspConstants.CHARGE_JSP);
			}
		
			req.setAttribute("companyCode",form.getCompanyCode());
			company.clear();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.CHARGE_JSP);
	}
	
	
	public ActionForward addNewYearlyCharge (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		ActionErrors actionErrors = null;
		boolean singleAccount = false;
		System.out.println("******************* addNewYearlyCharge addNewYearlyCharge  ");
		try {
			actionErrors = new ActionErrors();
			ChargeForm form = (ChargeForm)frm;
			System.out.println("addNewYearlyCharge submitted for year "+form.getYear());
			String companyCode = form.getCompanyCode();
			String companyName = form.getCompanyName();
			String active = form.getActive();

			System.out.println("addNewYearlyCharge submitted for getCompanyCode "+companyCode);
			System.out.println("addNewYearlyCharge submitted for companyName "+companyName);
			System.out.println("addNewYearlyCharge submitted for getActive "+form.getActive());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(getStore_id(req));
			ArrayList<YearlyClientForm > masterClients = new ArrayList<YearlyClientForm >();
			
			if (companyCode == null && companyName== null )
			{
				masterClients = dao.getYearlyMasterClients(getStore_id(req));
				singleAccount = false;
			}

			else if (companyCode != null && companyCode.equals("ALL") && companyName != null && companyName.equals("ALL"))
			{
				masterClients = dao.getYearlyMasterClients(getStore_id(req));
				singleAccount = false;
			}
			else if (active!= null && active.equals("CODE") ){
					singleAccount = true;
					form.setCompanyName(null);
					System.out.println("addNewYearlyCharge submitted for year "+companyName);
					YearlyClientForm  masterClient  = dao.checkYearlyMasterClientInfoExist(companyCode,getStore_id(req));
					if (masterClient != null)
						masterClients.add(masterClient);
			}
			else if (active!= null && active.equals("NAME")){
				singleAccount = true;
				form.setCompanyCode(null);				
				System.out.println("addNewYearlyCharge submitted for year "+companyName);
				companyCode = form.getCompanyCodeByName(companyName);
				YearlyClientForm  masterClient  = dao.checkYearlyMasterClientInfoExist(companyCode,getStore_id(req));
				if (masterClient != null)
					masterClients.add(masterClient);
				}
			if (masterClients!= null && masterClients.size() == 0)
			{
				actionErrors.add("error.basecharges.exist", new ActionMessage("error.basecharges.notexist"));
			}
			else if (masterClients!= null && masterClients.size() > 0)
			{
				for (YearlyClientForm charge: masterClients )
				{
					if (charge.getActive()!= null && NO.equals(charge.getActive()))
					{
						System.out.println("Charge is not active for Year "+charge.getYear()+" for "+charge.getCompanyCode());
						continue;
					}
						
					charge.setYear(form.getYear());
					// we need to copy previous balance from previous month charge record
					String previousYear = getPreviousYear(form.getYear());
					
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
					System.out.println(" addNewYearlyCharge Charge for  "+charge.getYear());
					ArrayList<YearlyClientForm> existingCharges = dao.checkYearlyClientInfoExist(charge.getCompanyCode(),charge.getStore_id(), charge.getYear());
					if (existingCharges != null && existingCharges.size() > 0)
					{
						if (form.getOverright()!= null && form.getOverright().equals("Yes"))
						{
							System.out.println(" &&&&&&&&&&&&&&&&&&& Charge for  "+existingCharges.get(0).getYear());
							System.out.println(" &&&&&&&&&&&&&&&&&&& Charge is Locked  "+existingCharges.get(0).getLock());
							if (existingCharges.get(0).getLock()!= null && existingCharges.get(0).getLock().equals(YES))
							{
								if (singleAccount)
								{
									actionErrors.add("error.yearlyaccunt.exist", new ActionMessage("error.yearlyaccunt.locked",existingCharges.get(0).getYear()));
									System.out.println(" &&&&&&&&&&&&&&&&&&& Account is locked  for  "+charge.getCompanyCode());
									continue;
								}
								else
								{
									System.out.println(" &&&&&&&&&&&&&&&&&&& Accounts locked  for  "+charge.getCompanyCode());
									continue;
								}
									
								
							}
							dao.updateYearlyClientInfo(charge);
							continue;
						}
						actionErrors.add("error.charges.exist", new ActionMessage("error.charges.exist"));
						req.setAttribute("error.charges.exist","error.charges.exist");
					}else
					{
						dao.insertYearlyClientInfo(charge);
					}
				}
			}
			if (actionErrors != null && actionErrors.size() > 0)
			{
				saveErrors(req,actionErrors);
				return getForward(result, JspConstants.SELECT_YEAR_FOR_CHARGE_COPY_JSP);				
			}
			
			
			form.setOverright(null);
			req.setAttribute("ToYear",form.getYear());
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.CONFIRM_YEAR_FOR_CHARGE_COPY_JSP);
	}
	
	
	


/*	public ActionForward copyCharge (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("ChargeAction copyCharge  ");
		try {
			ChargeForm form = (ChargeForm)frm;
			System.out.println("copyCharge submitted "+form.getMonth()+form.getYear());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(getStore_id(req));
			String withdrawDate = form.getWithdrawDate();
			ActionErrors actionErrors = new ActionErrors();
			boolean overrightCharges = true;
			
			ArrayList<ChargeForm > list  = dao.checkChargeExistByMonthYear(form.getMonth()+form.getYear(),getStore_id(req),withdrawDate);
			if (list!= null && list.size() > 0)
			{
				if (form.getOverright()!= null && form.getOverright().equals("Yes"))
				{
					overrightCharges = true;
					dao.deleteChargeByMonthYear(form.getMonth()+form.getYear(),getStore_id(req));
				}
				else
				{
					overrightCharges = false;
					actionErrors.add("error.charges.exist", new ActionMessage("error.charges.exist"));
					req.setAttribute("error.charges.exist","error.charges.exist");
					saveErrors(req,actionErrors);
					System.out.println("Charges for the given month and year already exist. do you want to overright");
					return getForward(result, JspConstants.SELECT_MONTH_FOR_CHARGE_COPY_JSP);
				}
			}
			
			
			if (overrightCharges)
			{
			ArrayList<ChargeForm > selectedList  = dao.getMasterCharges(getStore_id(req),withdrawDate);
			if (selectedList!= null && selectedList.size() > 0)
			{
				for (ChargeForm masterCharge: selectedList )
				{
					if (masterCharge!= null)
					{
						if (masterCharge.getActive()!= null && masterCharge.getActive().equals(NO))
						{
							System.out.println("********************copyCharge Master Charge is not active "+masterCharge.getCompanyCode()+"  "+masterCharge.getCompanyName());
							dao.deleteCharge(masterCharge.getCompanyCode(),form.getMonth()+form.getYear(),masterCharge.getStore_id());
							continue;
						}
						if (masterCharge.getNetAmount() == 0)
						{
							System.out.println("********************copyCharge Master Charge have NetAmount == 0"+masterCharge.getCompanyCode()+"  "+masterCharge.getCompanyName());
							dao.deleteCharge(masterCharge.getCompanyCode(),form.getMonth()+form.getYear(),masterCharge.getStore_id());
							continue;
						}
					}
					// Check previous MOnth
					String preMonth = getPreviousMonth(form.getMonth()+form.getYear());
					double balanceDueFromPreviousMonth = 0.0;
					ArrayList<ChargeForm> previousCharges = dao.checkChargeExist(masterCharge.getCompanyCode(),preMonth, masterCharge.getStore_id());
					if (previousCharges != null && previousCharges.size() > 0)
					{
						ChargeForm preCharge = (ChargeForm)previousCharges.get(0);
						if (preCharge == null) continue;
						if (YES.equals(preCharge.getNsf()))
						{
							masterCharge.setPreviousCharges(preCharge.getTotalWithdrawAmount());
							masterCharge.setComments("Previous Month NSF Charges Included");
							System.out.println("********************copyCharge setPreviousCharges "+masterCharge.getPreviousCharges());
							
						}
					}
					
					// Check if Charge Already Exist and USer want to overright
					
					
					double totalInstalmentPaid = dao.getTotalInstallmentPaid(masterCharge.getCompanyCode(), masterCharge.getStore_id());
					double instalment= masterCharge.getPreviousAmountInstallment();
					double previousAmount = masterCharge.getPreviousAmount();
					double previousBalanceDue = masterCharge.getPreviousAmountBalance();
					
					if (previousAmount - (totalInstalmentPaid+instalment) >= 0)
					{
						masterCharge.setPreviousAmountBalance(previousAmount - (totalInstalmentPaid+instalment));
						dao.updateChargePreviousBalance(masterCharge);
					}else if (previousBalanceDue > 0)
					{
						masterCharge.setPreviousAmountBalance(0.0);
						dao.updateChargePreviousBalance(masterCharge);
						masterCharge.setPreviousAmountInstallment(instalment - previousBalanceDue);
						//masterCharge.setPreviousAmount(instalment - previousBalanceDue);
					}
						
					if (previousBalanceDue == 0)
					{
						masterCharge.setPreviousAmountInstallment(0.0);
						masterCharge.setPreviousAmount(0.0);
					}
						
					//masterCharge.setPreviousAmountBalance(masterCharge.getPreviousAmount() - masterCharge.getPreviousAmountInstallment());
					masterCharge.setMonthYear(form.getMonth()+form.getYear());
					//masterCharge.setPreviousAmount(masterCharge.getPreviousAmountBalance()+ masterCharge.getPreviousAmountInstallment());

					
					if (!YES.equals(masterCharge.getPreviousAmountGstIncluded()))
						masterCharge.setTotalWithdrawAmount(masterCharge.getPreviousCharges()+masterCharge.getTotal()+masterCharge.getPreviousAmountInstallment()+calculateGST(masterCharge.getPreviousAmountInstallment(),req));
					else
						masterCharge.setTotalWithdrawAmount(masterCharge.getPreviousCharges()+masterCharge.getTotal()+masterCharge.getPreviousAmountInstallment());
					
					
					
					
					System.out.println("adding Charge for  "+form.getMonth()+form.getYear());
					masterCharge.setNsf("N");
					dao.addCharge(masterCharge);
				}
			}
			form.setOverright(null);
			req.setAttribute("ToMonth",form.getMonth()+form.getYear());
			}
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.CHARGE_COPY_CONFIRMATION_JSP);
	}
	*/
	
	
	
	
	public ActionForward copyCharge (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("ChargeAction copyCharge  ");
		ChargeForm form = (ChargeForm)frm;
		if (copyCharge(form,req) != null )
			return getForward(result, JspConstants.SELECT_MONTH_FOR_CHARGE_COPY_JSP);
		
		return getForward(result, JspConstants.CHARGE_COPY_CONFIRMATION_JSP);
	}
	
	
	
	
	public ActionErrors copyCharge (ChargeForm form, HttpServletRequest req)  {
		ActionForward result = null;

		System.out.println("ChargeAction copyCharge  ");
		try {
			System.out.println("copyCharge submitted "+form.getMonth()+form.getYear());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(form.getStore_id());
			String withdrawDate = form.getWithdrawDate();
			ActionErrors actionErrors = new ActionErrors();
			boolean overrightCharges = true;
			ArrayList<ChargeForm > list = null;
			
			if (form.getEditId()!= null) 				
				list  = dao.checkChargeExist(form.getEditId(),form.getMonth()+form.getYear(),form.getStore_id());
			else
				list  = dao.checkChargeExistByMonthYear(form.getMonth()+form.getYear(),form.getStore_id(),withdrawDate);
			if (list!= null && list.size() > 0)
			{
				if (form.getOverright()!= null && form.getOverright().equals("Yes"))
				{
					overrightCharges = true;
					dao.deleteChargeByMonthYear(form.getMonth()+form.getYear(),form.getStore_id());
				}
				else
				{
					overrightCharges = false;
					actionErrors.add("error.charges.exist", new ActionMessage("error.charges.exist"));
					req.setAttribute("error.charges.exist","error.charges.exist");
					saveErrors(req,actionErrors);
					System.out.println("Charges for the given month and year already exist. do you want to overright");
					return actionErrors;
				}
			}
			
			
			if (overrightCharges)
			{
			ArrayList<ChargeForm > selectedList  = dao.getMasterCharges(getStore_id(req),withdrawDate,form.getEditId());
			if (selectedList!= null && selectedList.size() > 0)
			{
				for (ChargeForm masterCharge: selectedList )
				{
					if (masterCharge!= null)
					{
						if (masterCharge.getActive()!= null && masterCharge.getActive().equals(NO))
						{
							System.out.println("********************copyCharge Master Charge is not active "+masterCharge.getCompanyCode()+"  "+masterCharge.getCompanyName());
							dao.deleteCharge(masterCharge.getCompanyCode(),form.getMonth()+form.getYear(),masterCharge.getStore_id());
							continue;
						}
						if (masterCharge.getNetAmount() == 0)
						{
							System.out.println("********************copyCharge Master Charge have NetAmount == 0"+masterCharge.getCompanyCode()+"  "+masterCharge.getCompanyName());
							dao.deleteCharge(masterCharge.getCompanyCode(),form.getMonth()+form.getYear(),masterCharge.getStore_id());
							continue;
						}
					}
					// Check previous MOnth
					String preMonth = getPreviousMonth(form.getMonth()+form.getYear());
					ArrayList<ChargeForm> previousCharges = dao.checkChargeExist(masterCharge.getCompanyCode(),preMonth, masterCharge.getStore_id());
					if (previousCharges != null && previousCharges.size() > 0)
					{
						ChargeForm preCharge = (ChargeForm)previousCharges.get(0);
						if (preCharge == null) continue;
						if (YES.equals(preCharge.getNsf()))
						{
							masterCharge.setPreviousCharges(preCharge.getTotalWithdrawAmount());
							masterCharge.setComments("Previous Month NSF Charges Included");
							System.out.println("********************copyCharge setPreviousCharges "+masterCharge.getPreviousCharges());
							
						}
					}
					
					// Check if Charge Already Exist and USer want to overright
					
					
					double totalInstalmentPaid = dao.getTotalInstallmentPaid(masterCharge.getCompanyCode(), masterCharge.getStore_id());
					double instalment= masterCharge.getPreviousAmountInstallment();
					double previousAmount = masterCharge.getPreviousAmount();
					double previousBalanceDue = masterCharge.getPreviousAmountBalance();
					
					if (previousAmount - (totalInstalmentPaid+instalment) >= 0)
					{
						masterCharge.setPreviousAmountBalance(previousAmount - (totalInstalmentPaid+instalment));
						dao.updateChargePreviousBalance(masterCharge);
					}else if (previousBalanceDue > 0)
					{
						masterCharge.setPreviousAmountBalance(0.0);
						dao.updateChargePreviousBalance(masterCharge);
						masterCharge.setPreviousAmountInstallment(instalment - previousBalanceDue);
						//masterCharge.setPreviousAmount(instalment - previousBalanceDue);
					}
						
					if (previousBalanceDue == 0)
					{
						masterCharge.setPreviousAmountInstallment(0.0);
						masterCharge.setPreviousAmount(0.0);
					}
						
					masterCharge.setMonthYear(form.getMonth()+form.getYear());
					if (!YES.equals(masterCharge.getPreviousAmountGstIncluded()))
						masterCharge.setTotalWithdrawAmount(masterCharge.getPreviousCharges()+masterCharge.getTotal()+masterCharge.getPreviousAmountInstallment()+calculateGST(masterCharge.getPreviousAmountInstallment(),req));
					else
						masterCharge.setTotalWithdrawAmount(masterCharge.getPreviousCharges()+masterCharge.getTotal()+masterCharge.getPreviousAmountInstallment());
					
					
					
					
					System.out.println("adding Charge for  "+form.getMonth()+form.getYear());
					masterCharge.setNsf("N");
					dao.addCharge(masterCharge);
				}
			}
			form.setOverright(null);
			req.setAttribute("ToMonth",form.getMonth()+form.getYear());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static void main(String[] args)
	{
		System.out.println(getPreviousMonth("072008"));
	}

	
	public ActionForward search (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("ChargeAction submitted ");
		try {
			CompanyForm form = getCompanyForm(req);
			System.out.println("user action is "+form.getAction());
			System.out.println("ChargeAction submitted "+form.getCompanyName());
			ReferenceDao dao  = new ReferenceDao();
			form.setStore_id(getStore_id(req));
			ArrayList<CompanyForm >  list =  dao.getCompanies(form, form.getAction());
			ArrayList<CompanyForm > newList = removeDuplicates(list);
		//	ArrayList<CompanyForm > newList1 = removeEmptyCharges(newList);
			form.setCompanyList(newList);
			if (form.getAction().equals("add"))
				return getForward(result, JspConstants.LIST_COMPANY_JSP);
			else
				return getForward(result, form.getNextPage());

		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.LIST_COMPANY_JSP);
	}
	


	public ActionForward delete (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("ChargeAction delete ");
		try {
			PayrollForm form = getPayrollForm(req);
			form.setStore_id(getStore_id(req));
			System.out.println("ChargeAction delete "+form.getCompanyName());
			System.out.println("ChargeAction getEditId  "+form.getEditId());
			ReferenceDao dao  = new ReferenceDao();
			dao.deleteCompany(form.getEditId(), getStore_id(req));
		}
		catch (Exception e) {
		}		
		return getForward(result, JspConstants.LIST_COMPANY_JSP);
	}
	
	
	public ActionForward backToPayroll (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("backToPayroll from charge page ");
		try {
			ChargeForm form = getChargeForm(req);
			PayrollForm payrollForm = getPayrollForm(req);
			form.setStore_id(getStore_id(req));
			System.out.println("backToPayroll CompanyCode  "+form.getCompanyCode());
			ReferenceDao dao  = new ReferenceDao();
			payrollForm = dao.checkMasterPayrollExist(form.getCompanyCode(), getStore_id(req));
			if (payrollForm ==  null)
			{
				payrollForm = new PayrollForm();
				payrollForm.setCompanyCode(form.getCompanyCode());
				payrollForm.setPayrollMonth("MASTER_PAYROLL");
				payrollForm.setStore_id(form.getStore_id());
			}
			System.out.println("backToPayroll payroll found for   "+payrollForm.getCompanyName());
			req.getSession().setAttribute(JspConstants.PAYROLL_FORM,payrollForm);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.PAYROLL_JSP);
	}
	
	public ActionForward gotoGenereateYearlyClient (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		System.out.println("gotoGenereateYearlyClient from charge page ");
		try {
			ChargeForm form = getChargeForm(req);
			form.setStore_id(getStore_id(req));
			System.out.println("gotoGenereateYearlyClient CompanyCode  ");
			ReferenceDao dao  = new ReferenceDao();
			CompanyForm companyForm = new CompanyForm();
			companyForm.setStore_id(getStore_id(req));
			ArrayList<CompanyForm> companies = dao.getAllCompanies(companyForm);
			form.setCompanylist(companies);
			req.getSession().setAttribute(JspConstants.CHARGE_FORM,form);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.SELECT_YEAR_FOR_CHARGE_COPY_JSP);
	}
	
	
//     <a href="javascript: trigger('addPayroll', '<nested:write property="companyCode" />');">Add payroll</a>&nbsp;|
//     <a href="javascript: trigger('changePayroll', '<nested:write property="companyCode" />');">Change payroll</a>&nbsp;|
//     <a href="javascript: trigger('addCharge', '<nested:write property="companyCode" />');">Add Charge</a>&nbsp;|
//     <a href="javascript: trigger('changeCharge', '<nested:write property="companyCode" />');">Change Charge</a>&nbsp;|
//     <a href="javascript: trigger('deleteCharge', '<nested:write property="companyCode" />');">Delete Charge</a>&nbsp;|
//     <a href="javascript: trigger('addTrustAmount', '<nested:write property="companyCode" />');">Add Trust Amount</a>&nbsp;
//     <a href="javascript: trigger('changeTrustAmount', '<nested:write property="companyCode" />');">Change Trust Amount</a>&nbsp;
//     <a href="javascript: trigger('deleteTrustAmount', '<nested:write property="companyCode" />');">Delete Trust Amount</a>&nbsp;
     
     
     
}
