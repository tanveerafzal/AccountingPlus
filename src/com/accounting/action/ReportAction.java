package com.accounting.action;
 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.accounting.dao.ReferenceDao;
import com.accounting.form.CompanyForm;
import com.accounting.form.ReportForm;
import com.accounting.utils.JspConstants;

public class ReportAction extends BaseAction {
	public ReportAction(){}

	
	
	
	
	
	public ActionForward printRemittanceReport(ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("printRemittanceReport submitted .. ");
		try {
			ReportForm form = (ReportForm)frm;
			form.setReportType(JspConstants.REMITTANCE_REPORT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.REMITTANCE_REPORT_PRINT_JSP);
	}
	
	
	
	public ActionForward printQRemittanceReport(ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("printQRemittanceReport submitted .. ");
		try {
			ReportForm form = (ReportForm)frm;
			form.setReportType(JspConstants.REMITTANCE_REPORT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.REMITTANCE_Q_REPORT_PRINT_JSP);
	}
	public ActionForward printMQRemittanceReport(ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("printQRemittanceReport submitted .. ");
		try {
			ReportForm form = (ReportForm)frm;
			form.setReportType(JspConstants.REMITTANCE_REPORT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.REMITTANCE_MQ_REPORT_PRINT_JSP);
	}
	
	public ActionForward printALLRemittanceReport(ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("printQRemittanceReport submitted .. ");
		try {
			ReportForm form = (ReportForm)frm;
			form.setReportType(JspConstants.REMITTANCE_REPORT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.REMITTANCE_ALL_REPORT_PRINT_JSP);
	}

	
	public ActionForward printChargingReport(ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("printChargingReport submitted .. adding new Payroll ");
		try {
			ReportForm form = (ReportForm)frm;
			form.setReportType(JspConstants.REMITTANCE_REPORT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.CHARGING_REPORT_PRINT_JSP);
	}

	
	
	public ActionForward remittanceReport(ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("ReportAction submitted .. adding new Payroll ");
		try {
			ReportForm form = (ReportForm)frm;
			form.setReportType(JspConstants.REMITTANCE_REPORT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.PAYROLL_JSP);
	}
	
	
	public ActionForward chargingreport(ActionMapping mapping, ActionForm frm, HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		System.out.println("ReportAction submitted .. chargingreport ");
		try {
			ReportForm form = (ReportForm)frm;
			form.setReportType(JspConstants.CHARGING_REPORT);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.CHARGING_REPORT_JSP);
	}
	
	// this is generate remittance report action
	public ActionForward generateReport (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("generateReport submitted ");
		try {
			ReportForm form = getReportForm(req);
			
			System.out.println("generateReport month "+form.getMonth());
			System.out.println("generateReport year "+form.getYear());


			System.out.println("generateReport getQuaterlyMonths "+form.getQuaterlyMonths().length);
			System.out.println("generateReport Quaterly Year "+form.getQuaterlyYear());
			
				
			System.out.println("Generated YearMonth "+form.getQuaterlyYear());
			
			ReferenceDao dao  = new ReferenceDao();
			form.setNsfAmount(getNsfCharge(req));
			System.out.println("generateReport submitted ");
			form.setStore_id(getStore_id(req));
			form.setStoreName(getStoreName(req));
			ArrayList<CompanyForm >  list =  dao.getPayrollsByMonth(form.getMonth()+form.getYear(),form.getOrderBy(),form.getNsf(),getStore_id(req));
			ArrayList<CompanyForm > newList = removeDuplicates(list);
			ArrayList<CompanyForm > zeroPayrolls = removeZeroPayrollsFromList(list);
			System.out.println("newList.size before "+newList.size());
			System.out.println("zeroPayrolls.size before "+zeroPayrolls.size());
			newList.removeAll(zeroPayrolls);
			System.out.println("newList.size After "+newList.size());
			
			double totalRemittanceAmount = 0.0;
			double totalBankCharge= 0.0;
			double totalAmount = 0.0;
			double totalGrossPayroll= 0.0;
			ArrayList<CompanyForm> quartlyPayrolls =new  ArrayList<CompanyForm>();
			
			for (int i=0; i< newList.size(); i++)
			{
				CompanyForm payroll = (CompanyForm)newList.get(i);
				System.out.println("*****************setting month?year for "+payroll.getCompanyCode()+" to "+form.getMonth()+form.getYear());
				payroll.setMonth(form.getMonth());
				payroll.setYear(form.getYear());
				if (payroll != null
					&& "Q".equals(payroll.getPayroll().getPayrollFrequency()))
					{
						System.out.println("quartlyPayrolls "+payroll.getCompanyCode());
						quartlyPayrolls.add(payroll);
//						newList.remove(payroll);
						continue;
					}
					totalRemittanceAmount =totalRemittanceAmount+payroll.getPayroll().getRemittanceAmount();
					totalBankCharge =totalBankCharge+payroll.getPayroll().getBankCharge();
					totalAmount =totalAmount+payroll.getPayroll().getTotalAmount();
					totalGrossPayroll = totalGrossPayroll+payroll.getPayroll().getGrossPayroll();
			}
			
			newList.removeAll(quartlyPayrolls);
			form.setTotalRemittanceAmount(totalRemittanceAmount);
			form.setTotalBankCharge(totalBankCharge);
			form.setTotalAmount(totalAmount);
			form.setTotalGrossPayroll(totalGrossPayroll);
			form.setList(newList);
			form.setEmptyPayrollList(zeroPayrolls);
			
			ArrayList<CompanyForm>  allQuaterlyPayrols = new  ArrayList<CompanyForm>();
			System.out.println("*********************************** PROCESS quaterLyPayrolls ********************************* ");
			
			for (int i=0; i< quartlyPayrolls.size(); i++)
			{
				CompanyForm payroll = (CompanyForm)quartlyPayrolls.get(i);
				System.out.println("search other quaterLyPayrolls for "+payroll.getCompanyName());
				allQuaterlyPayrols.add(payroll);
				for (int j=0; j< form.getQuaterlyMonths().length; j++)
				{
					if (form.getQuaterlyMonths()[j] == null)	continue;
					String monthYear = form.getQuaterlyMonths()[j]+form.getQuaterlyYear();
					System.out.println("month year selected for report "+form.getMonth()+form.getYear());
					System.out.println("month year selected for Quatrer "+monthYear);
					
					if (monthYear != null && monthYear.equals(form.getMonth()+form.getYear())) continue;
					
					System.out.println("search quaterLyPayrolls for month: "+form.getQuaterlyMonths()[j]);
					CompanyForm company = new CompanyForm();
					company.setCompanyCode(payroll.getCompanyCode());
					company.setMonth(form.getQuaterlyMonths()[j]);
					company.setYear(form.getQuaterlyYear());
					company.setStore_id(getStore_id(req));
					ArrayList<CompanyForm> quaterLyPayrolls = dao.searchPayrolls(company);
					System.out.println("quaterLyPayrolls for "+monthYear+"  "+quaterLyPayrolls.size());
					allQuaterlyPayrols.addAll(quaterLyPayrolls);
				}
		
			}
			
			
			 totalRemittanceAmount = 0.0;
			 totalBankCharge= 0.0;
			 totalAmount = 0.0;
			 totalGrossPayroll= 0.0;
			 ArrayList<CompanyForm>  filterPayrols = new  ArrayList<CompanyForm>();
			System.out.println("total quaterLyPayrolls "+allQuaterlyPayrols.size());
			for (int i=0; i< allQuaterlyPayrols.size(); i++)
			{
				CompanyForm payroll = (CompanyForm)allQuaterlyPayrols.get(i);
				if (payroll != null && "Q".equals(payroll.getPayroll().getPayrollFrequency()))
					{
						if (form.getQuaterlyFilter()!= null && !form.getQuaterlyFilter().equals("ALL") )
						{	
							System.out.println("getQuaterlyFilter "+form.getQuaterlyFilter());
							if (form.getQuaterlyFilter().equals("Y") && payroll.getPayroll().getSentToCRA() != null && payroll.getPayroll().getSentToCRA().equals("N"))
							{
								filterPayrols.add(payroll);
								continue;
							}
							else if (form.getQuaterlyFilter().equals("N") && payroll.getPayroll().getSentToCRA() != null && payroll.getPayroll().getSentToCRA().equals("Y"))
							{
								filterPayrols.add(payroll);
								continue;
							}
						}
						
						System.out.println("quartlyPayrolls "+payroll.getCompanyCode());
						totalRemittanceAmount =totalRemittanceAmount+payroll.getPayroll().getRemittanceAmount();
						totalBankCharge =totalBankCharge+payroll.getPayroll().getBankCharge();
						totalAmount =totalAmount+payroll.getPayroll().getTotalAmount();
						totalGrossPayroll = totalGrossPayroll+payroll.getPayroll().getGrossPayroll();
					}
			}
				allQuaterlyPayrols.removeAll(filterPayrols);
				form.setQuartlyPayrollList(allQuaterlyPayrols);
				form.setTotalQRemittanceAmount(totalRemittanceAmount);
				form.setTotalQBankCharge(totalBankCharge);
				form.setTotalQAmount(totalAmount);
				form.setTotalQGrossPayroll(totalGrossPayroll);
				

		}catch (Exception e) {
		e.printStackTrace();	
		}		
		return getForward(result, JspConstants.REMITTANCE_REPORT_JSP);
	}
	
	
	
private ArrayList<CompanyForm> removeZeroPayrollsFromList(ArrayList<CompanyForm> list) {
	ArrayList<CompanyForm> zeroPayrollList = null;
	HashSet dups = new HashSet();
	if (list != null)
		{
			zeroPayrollList = new ArrayList<CompanyForm>();
			for (int i=0; i< list.size(); i++)
			{
				CompanyForm company = (CompanyForm)list.get(i);
				if (company.getPayroll()!=null && company.getPayroll().getNumberOfemployees()==0 
					&& company.getPayroll().getGrossPayroll()==0 && company.getPayroll().getRemittanceAmount() == 0) 
				{
					zeroPayrollList.add(company);
					System.out.println("removing zeroPayrollList company  : "+company.getCompanyCode());
				}
				
			}
		}
		return zeroPayrollList;
		
	}
	

	public ActionForward exportRemittanceReport (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("ReportAction submitted ");
		try {
			ReportForm form = getReportForm(req);
			
			System.out.println("ReportAction month "+form.getMonth());
			System.out.println("ReportAction year "+form.getYear());
			System.out.println("ReportAction submitted "+form.getReportType());
			
			ReferenceDao dao  = new ReferenceDao();
			
			System.out.println("CompanyAction submitted ");
			form.setStore_id(getStore_id(req));
			form.setStoreName(getStoreName(req));
			ArrayList<CompanyForm >  list =  dao.getPayrollsByMonth(form.getMonth()+form.getYear(),form.getOrderBy(),form.getNsf(),getStore_id(req));
			ArrayList<CompanyForm > newList = removeDuplicates(list);
			form.setList(newList);

		}catch (Exception e) {
		e.printStackTrace();	
		}		
		return getForward(result, JspConstants.REMITTANCE_REPORT_JSP);
	}
	


	
	
	public ActionForward generateChargeReport (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("ReportAction submitted ");
		try {
			ReportForm form = getReportForm(req);
			
			System.out.println("ReportAction month "+form.getMonth());
			System.out.println("ReportAction year "+form.getYear());
			System.out.println("ReportAction submitted "+form.getReportType());
			System.out.println("ReportAction submitted "+form.getWithdrawDate());
			System.out.println("ReportAction getOrderBy "+form.getOrderBy());
			
			
			ReferenceDao dao  = new ReferenceDao();
			form.setNsfAmount(getNsfCharge(req));
			System.out.println("CompanyAction submitted ");
			form.setStore_id(getStore_id(req));
			form.setStoreName(getStoreName(req));
				ArrayList<CompanyForm >  list =  dao.getChargesByMonth(form.getMonth()+form.getYear(),form.getWithdrawDate(),form.getOrderBy(),form.getNsf(),getStore_id(req));
			ArrayList<CompanyForm > newList = removeDuplicates(list);
			double totalWithdrawAmount = 0;
			for (int i=0; i< newList.size(); i++)
			{
				CompanyForm charge = (CompanyForm)newList.get(i);
				if (charge != null)
					totalWithdrawAmount =totalWithdrawAmount+charge.getCharge().getTotalWithdrawAmount();
			}
			form.setTotalWithdrawAmount(totalWithdrawAmount);
			form.setChargeList(newList);

		}catch (Exception e) {
		e.printStackTrace();	
		}		
		return getForward(result, JspConstants.CHARGING_REPORT_JSP);
	}

	
	
	public ActionForward back(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		return getForward(result, JspConstants.INDEX_JSP);
}
	
	public ActionForward generateYearlyChargeReport (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("ReportAction submitted ");
		try {
			ReportForm form = getReportForm(req);
			System.out.println("ReportAction year "+form.getYear());
			System.out.println("ReportAction getYearEnd "+form.getYearEnd());
			System.out.println("ReportAction getTaxFiled "+form.getTaxFiled());
			System.out.println("ReportAction getTaxMailed "+form.getTaxMailed());
			System.out.println("ReportAction getPaymentReceived "+form.getPaymentReceived());
			
			
			ReferenceDao dao  = new ReferenceDao();
			System.out.println("CompanyAction submitted ");
			form.setStore_id(getStore_id(req));
			form.setStoreName(getStoreName(req));
			ArrayList<CompanyForm >  list =  dao.searchYearlyCharges(form);
			form.setYearlyChargeList(list);

		}catch (Exception e) {
		e.printStackTrace();	
		}		
		return getForward(result, JspConstants.YEARLY_CHARGING_REPORT_JSP);
	}

}
