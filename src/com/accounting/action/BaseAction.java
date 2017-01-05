package com.accounting.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.accounting.data.ChargeFeeInfo;
import com.accounting.data.UserDataInfo;
import com.accounting.form.AdminForm;
import com.accounting.form.ChargeForm;
import com.accounting.form.CompanyForm;
import com.accounting.form.LoginForm;
import com.accounting.form.MonthlyNoteForm;
import com.accounting.form.PayrollForm;
import com.accounting.form.RegisterForm;
import com.accounting.form.ReportForm;
import com.accounting.form.TrustForm;
import com.accounting.form.UserAssignmentForm;
import com.accounting.form.YearlyClientForm;
import com.accounting.utils.AttributeHelper;
import com.accounting.utils.Constants;
import com.accounting.utils.JspConstants;
import com.accounting.utils.StringUtil;


public abstract class BaseAction extends DispatchAction {
	
	protected final static String  SELECTED_ID = "selectedId";
	public static final String REQUEST_FOCUS_SEL = "sel";
	public  final static String  YES = "Y";
	public  final static String  NO = "N";
	public  final static String  MASTER = "MASTER";
	public static String MASTER_PAYROLL="MASTER_PAYROLL";
	 public static String MASTER_CHARGE="MASTER_CHARGE";
	 public  final static String  ALL = "ALL";
	 public static String PAYROLL="PAYROLL";
	 public static String CHARGE="CHARGE";
	 public static String YEARLY="YEARLY";
	 public static final String MONTHLY = "M";
	 public static final String FIX = "FIX";
	 public static final String OPEN_STATUS = "O";
	 public static final String CLOSE_STATUS = "C";
	 
	 public void addCompanyToMapping(HttpServletRequest request, CompanyForm company) {

		 Map userCodeToNameMapping = (Map)request.getSession().getAttribute("UserCodeToNameMapping");
		 userCodeToNameMapping.put(company.getCompanyCode(), company.getCompanyName());
		 request.getSession().setAttribute("UserCodeToNameMapping",userCodeToNameMapping);
			
		}
	 
	 public String getCompanyName(HttpServletRequest request, String companyCode) {
			
			
			String name=null;
			Map userCodeToNameMapping = (Map)request.getSession().getAttribute("UserCodeToNameMapping");
			if (userCodeToNameMapping!= null)
			{
				name = (String)userCodeToNameMapping.get(companyCode);
			}
			return name;
		}

	 
	 
	 
	 
	 public String getTodaysMonthString() {
		 Date today = new Date();
		 String month = ""+today.getMonth()+1;
		 if (month!= null && month.length()==1)
			 month = "0"+month;
		return month;
	}
		public int getTodaysMonth() {
			 Date today = new Date();
			return today.getMonth()+1;
		}
		
		public int getTodaysYear() {
			 Date today = new Date();
				return today.getYear()+1900;
		}
		
		public String getTodaysDate() {
			 Date today = new Date();
				return today.getDay()+"-"+(today.getMonth()+1)+"-"+(today.getYear()+1900);
		}
	
		
	public double calculateGST(double netAmount,HttpServletRequest request) {
		return netAmount*(getGstRate(request))/100;
	}

	public static String getPreviousYear(String payrollYear) {
		System.out.println("getPreviousYear submitted for "+payrollYear);
		if (payrollYear != null)
		{
			int year = Integer.parseInt(payrollYear);
			year = year-1;
			return ""+year;
		}
		return null;
	}
	
	public String formatYearEnd(String yearEnd, String year) {
		String newYearEnd = null;
		if (yearEnd != null && yearEnd.length() > 2)
		{
			String month = yearEnd.substring(0,2);
			newYearEnd =month+year;
		}
			
			
		return newYearEnd;
	}
	
	
	public ArrayList<CompanyForm> removeEmptyCharges(ArrayList<CompanyForm> newList) {
		
		ArrayList<CompanyForm> aList = null;
		if (newList != null)
		{
			aList = new ArrayList<CompanyForm>();
			for (int i=0; i< newList.size(); i++)
			{
				CompanyForm c = (CompanyForm)newList.get(i);
				if (c.getCharge().getMonthYear()== null || c.getCharge().getMonthYear().length() == 0 )
				{
					System.out.println("Month year is blank for charge.. removing from list "+c.getCompanyCode());
				}
				else
				{	
					aList.add(c);
				}
		}
	}
	return aList;
	}
	
	
	public String getStoreName(HttpServletRequest request) {
		String storeName = null;
		UserDataInfo userDataInfo = (UserDataInfo)request.getSession().getAttribute("UserDataInfo");
		if (userDataInfo!= null)
		{
			storeName = userDataInfo.getRefDataInfo().getStoreName();
		}
		
		return storeName;
	}
	public int getStore_id(HttpServletRequest request) {
		int storeId = 0;
		UserDataInfo userDataInfo = (UserDataInfo)request.getSession().getAttribute("UserDataInfo");
		if (userDataInfo!= null)
		{
			storeId = userDataInfo.getRefDataInfo().getStoreId();
		}
		
		return storeId;
	}
	
	public String getLoginName(HttpServletRequest request) {
		String login = null;
		UserDataInfo userDataInfo = (UserDataInfo)request.getSession().getAttribute("UserDataInfo");
		if (userDataInfo!= null)
		{
			login = userDataInfo.getLogin();
		}
		return login;
	}
	
	public double getGstRate(HttpServletRequest request)
	{
		double gstRate = 5.0;
		UserDataInfo userDataInfo = (UserDataInfo)request.getSession().getAttribute("UserDataInfo");
		if (userDataInfo!= null)
		{
			gstRate = userDataInfo.getRefDataInfo().getGstRate();
		}
		return gstRate;
	}
	public double getNsfCharge(HttpServletRequest request)
	{
		double nsfCharge = 25.0;
		UserDataInfo userDataInfo = (UserDataInfo)request.getSession().getAttribute("UserDataInfo");
		if (userDataInfo!= null)
		{
			nsfCharge = userDataInfo.getRefDataInfo().getNsfFee();
		}
		double nsfGst = calculateGST(nsfCharge,request);
		
		return nsfCharge+nsfGst;
	}
	public double calculateBankCharges(double remittanceAmount,HttpServletRequest request ) {

		if (remittanceAmount == 0.0) return 0.0;
		
		double chargeFee = 11.0;
		ArrayList<ChargeFeeInfo> chargeFeeList = null ;
		UserDataInfo userDataInfo = (UserDataInfo)request.getSession().getAttribute("UserDataInfo");
		if (userDataInfo!= null)
		{
			chargeFeeList = userDataInfo.getRefDataInfo().getChargeFeeList();
		}
		if (chargeFeeList== null)
		{
		System.out.println("******************** Using standerd values to calculate charge amount");
		 if (remittanceAmount < 4000)	chargeFee= 11.00;
		 else 	 if (remittanceAmount > 4000 && remittanceAmount <= 8000)	chargeFee= 16.00;
		 else 	 if (remittanceAmount > 8000 && remittanceAmount <= 12000)	chargeFee= 21.00;
		 else	 chargeFee= 26.00;
		} else
		{
			for (ChargeFeeInfo chargeFeeInfo: chargeFeeList)
			{
				 if (remittanceAmount > chargeFeeInfo.getFromAmount() && remittanceAmount <= chargeFeeInfo.getToAmount())
				 {
					 chargeFee=  chargeFeeInfo.getChargeFee();
					 System.out.println("******************** Using Charging Values from DB to calculate charge amount "+chargeFee);
					 
				 }
			}
		}
		System.out.println("******************** Adding GST to charge amount");
		chargeFee = chargeFee + calculateGST(chargeFee,request);
		System.out.println("******************** after Adding GST to charge amount "+chargeFee);
		return chargeFee;
	}
	
	
	protected void setError(HttpServletRequest request, Throwable error){
		HttpSession session = request.getSession();
		session.setAttribute(Constants.ERROR_KEY, error);
	}
   
    protected String getSearchString(String current){
    	String result = current == null ? "" : current.trim(); 
    	
    	result = result.replaceAll("\\*", "\\%");
    	result = result.replaceAll("\\?", "\\_");
    	
    	return result;
    }
	
	protected void processException(HttpServletRequest req, Exception exception) {
		setError(req, exception);
	}
	protected Date goToTheEnd(Date endDate) {
		Date result = null;
		
		if(endDate != null){
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(endDate);
			
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			
			result = calendar.getTime();
		}
		
		return result;
	}
	public ArrayList<CompanyForm> removeDuplicates(ArrayList<CompanyForm> list) {
		ArrayList<CompanyForm> newList = null;
		HashSet dups = new HashSet();
	if (list != null)
	{
		newList = new ArrayList<CompanyForm>();
		for (int i=0; i< list.size(); i++)
		{
			CompanyForm c = (CompanyForm)list.get(i);
			if (dups.contains(c.getCompanyCode()))
			{
				//System.out.println("user action is "+c.getCompanyCode());
			}
			else
			{
				newList.add(c);
				dups.add(c.getCompanyCode());	
			}
			
		}
	}
	return newList;
	
}
	
	public UserAssignmentForm getUserAssignmentForm(HttpServletRequest request) {
		return (UserAssignmentForm) AttributeHelper.getObjectForm(request, JspConstants.USER_ASSIGNMENT_FORM, UserAssignmentForm.class, false);
	}

	
	
	public MonthlyNoteForm getMonthlyNoteForm(HttpServletRequest request) {
		return (MonthlyNoteForm) AttributeHelper.getObjectForm(request, JspConstants.MONTHLY_NOTE_FORM, MonthlyNoteForm.class, false);
	}

	public LoginForm getLoginForm(HttpServletRequest request) {
		return (LoginForm) AttributeHelper.getObjectForm(request, JspConstants.LOGIN_FORM, LoginForm.class, false);
	}

	
	public RegisterForm getRegisterForm(HttpServletRequest request) {
		return (RegisterForm) AttributeHelper.getObjectForm(request, JspConstants.REGISTER_FORM, RegisterForm.class, false);
	}
	
	
	
	public AdminForm getAdminForm(HttpServletRequest request) {
		return (AdminForm) AttributeHelper.getObjectForm(request, JspConstants.ADMIN_FORM, AdminForm.class, false);
	}

	
	public void resetCompanyForm(HttpServletRequest request) {
		request.getSession().setAttribute(JspConstants.COMPANY_FORM, null);
//		return (CompanyForm) AttributeHelper.getObjectForm(request, JspConstants.COMPANY_FORM, CompanyForm.class, false);
	}
	
	
	public CompanyForm getCompanyForm(HttpServletRequest request) {
		return (CompanyForm) AttributeHelper.getObjectForm(request, JspConstants.COMPANY_FORM, CompanyForm.class, false);
	}
	public ChargeForm getChargeForm(HttpServletRequest request) {
		return (ChargeForm) AttributeHelper.getObjectForm(request, JspConstants.CHARGE_FORM, ChargeForm.class, false);
	}
	public PayrollForm getPayrollForm(HttpServletRequest request) {
		return (PayrollForm) AttributeHelper.getObjectForm(request, JspConstants.PAYROLL_FORM, PayrollForm.class, false);
	}
	public TrustForm getTrustForm(HttpServletRequest request) {
		return (TrustForm) AttributeHelper.getObjectForm(request, JspConstants.TRUST_FORM, TrustForm.class, false);
	}
	public ReportForm getReportForm(HttpServletRequest request) {
		return (ReportForm) AttributeHelper.getObjectForm(request, JspConstants.REPORT_FORM, ReportForm.class, false);
	}
	protected ActionForward getForward(ActionForward forward, String constant){
		ActionForward result = forward;
		
		if(result == null){
			result = new ActionForward(constant);
		}
		return result;
	}
	protected ActionForward getForward(String constant){
		return new ActionForward(constant);
	}
	
	protected boolean isEmpty(String current){
		return current == null || current.trim().length() == 0;
	}

	protected boolean isFilled(String current){
		return ! isEmpty(current);
	}

	
	protected boolean isDouble(String current) {
		boolean res = false;
		
		try {
			Double.parseDouble(current);
			res = true;
		}
		catch(Exception e){
			// nothing
		}
		
		return res; 
	}
	
	protected Date getDate(String current) throws Exception{
		Date res = null;
		
		if(! isEmpty(current)){
				res = StringUtil.parseDate(current);
		}
		
		return res;
	}
	
	public String trim(String string)
	{
		if (string != null && string.length()>0)
			return string.trim();
		else
			return string;
		
	}
	
	
	
	public ActionForward gotoHome (ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;

		System.out.println("gotoHome submitted ");
		return getForward(result, JspConstants.INDEX_JSP);
	}
	
	public ActionForward gotoMonthlyChargesSummery(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		return getForward(result, JspConstants.CHARGING_REPORT_JSP);
	}

	public ActionForward gotoChangeCharges(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		return getForward(result, JspConstants.SELECT_MONTH_FOR_CHARGE_UPDATE_JSP);
		//return (new CompanyAction()).changeCharges(mapping, frm, req, resp);
	}

	public ActionForward gotoSearchPayrolls(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
			System.out.println("searchpayrolls submitted ");
			CompanyAction action = new CompanyAction();
			
			return action.searchpayrolls(mapping, frm,req, resp) ;
		//return getForward(result, JspConstants.LIST_COMPANY_UPDATe_JSP);
		//return (new CompanyAction()).changeCharges(mapping, frm, req, resp);
	}

	public ActionForward gotoSearchCharges(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		CompanyAction action = new CompanyAction();
		return action.searchCharges(mapping, frm,req, resp) ;
		//return getForward(result, JspConstants.LIST_CHARGE_JSP);
		//return (new CompanyAction()).changeCharges(mapping, frm, req, resp);
	}

	public ActionForward gotoChangePayrolls(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		return getForward(result, JspConstants.SELECT_MONTH_FOR_PAYROLL_UPDATE_JSP);
		//return (new CompanyAction()).changeCharges(mapping, frm, req, resp);
	}

	
	
	public ActionForward gotoAddNewCompany(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		System.out.println("gotoAddNewCompany called ");
		ActionForward result = null;
		CompanyForm form = getCompanyForm(req);
		form.clear();
		YearlyClientForm yearlyClientInfo = new YearlyClientForm();
		yearlyClientInfo.setYear(MASTER);
		form.setYearlyClientInfo(yearlyClientInfo);
		return getForward(result, JspConstants.COMPANY_JSP);
}	
	public ActionForward gotoAdmin(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		return getForward(result, JspConstants.ADMIN_JSP);
	}

	public ActionForward gotoRemittanceSummery(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		return getForward(result, JspConstants.REMITTANCE_REPORT_JSP);
	}

	public ActionForward gotoYearlyPayments(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		return getForward(result, JspConstants.YEARLY_PAYMENTS_JSP);
	}
	
	
	public String getPageByNoteType(MonthlyNoteForm currentForm)
	{
		if (currentForm!= null)
		{
			if (currentForm.getNotesType().equals(PAYROLL))
			{
				return JspConstants.ADD_PAYROLL_NOTES_JSP;
			}
			else if (currentForm.getNotesType().equals(CHARGE))
			{
				return JspConstants.ADD_CHARGE_NOTES_JSP;
			}
			if (currentForm.getNotesType().equals(YEARLY))
			{
				return JspConstants.ADD_YEARLY_NOTES_JSP;
			}
		}
	return null;	
	}
	
	
//	protected String getAppPath() throws Exception{
//		return Props.getInstance().getProperty(Props.APP_PATH);
//	}

	public static String getPreviousMonth(String payrollMonth) {
		if (payrollMonth != null && payrollMonth.length() == 6)
		{
			int month =Integer.parseInt(payrollMonth.substring(0,2));
			int year = Integer.parseInt(payrollMonth.substring(2,6));
			System.out.println("month "+month);
			System.out.println("year "+year);
			month = month-1;
			if (month==0 )
				{
					month = 12; year = year-1;
				}
			String smonth = String.valueOf(month);
			String syear = String.valueOf(year);
			if (smonth.length()==1) smonth = "0"+smonth;
			if (syear.length()==1) syear = "0"+syear;
			return smonth+syear;
		}
		return null;
	}
	
}
