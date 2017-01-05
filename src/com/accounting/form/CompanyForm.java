package com.accounting.form;

import com.accounting.dao.ManagementDao;
import com.accounting.dao.ReferenceDao;
import com.accounting.data.BankDateInfo;
import com.accounting.data.UserDataInfo;


public class CompanyForm extends BaseForm {

	private String companyName; 
	private int store_id;
	private String companyCode;
	private String bnNumber;
	private String address1;
	private String address2;
	private String city;
	private String province;
	private String postalCode;
	private String country;
	private String comment;
	private String bankAccNumber;
	private String editId;
	private PayrollForm payroll;
	private ChargeForm charge;
	private TrustForm trust;
	
	private String action;
	private String operation;
	private String flow;
	private String payrollType; 
	
	private String nextPage;
	private String orderBy;
	
	private String monthFrom;
	private String yearFrom;
	
	private String monthTO;
	private String yearTo;
	
	double totalRemittanceAmount = 0.0;
	double totalBankCharge= 0.0;
	double totalAmount = 0.0;

	double totalNetAmount = 0.0;
	double totalGst= 0.0;
	double totalWithdrawAmount = 0.0;
	
	//String clientStatusActive = null;
	 
	
	String notesStatus;
	
	private String allPayrollSentToCRA;
	
	private String withdrawDate;
	private String 	clientType;
	private ArrayList<BankDateInfo> withdrawDates = new ArrayList<BankDateInfo>();
	private ArrayList<UserDataInfo> employess = new ArrayList<UserDataInfo>();
	private ArrayList<CompanyForm> companylist = new ArrayList<CompanyForm>();
	private ArrayList<CompanyForm> quarterlyRemitterList = new ArrayList<CompanyForm>();
	private ArrayList<CompanyForm> company = new ArrayList<CompanyForm>();
	private ArrayList<PayrollForm> payrolllist = new ArrayList<PayrollForm>();
	private ArrayList<ChargeForm> chargelist = new ArrayList<ChargeForm>();
	private ArrayList<UserDataInfo> employeelist = new ArrayList<UserDataInfo>();
	
	private ArrayList<CompanyForm> originalList = new ArrayList<CompanyForm>();
	
	private ArrayList<CompanyForm> activeCompanyList = new ArrayList<CompanyForm>();
	
	
	YearlyClientForm yearlyClientInfo = new YearlyClientForm();
	private ArrayList<YearlyClientForm> yearlyChargesExist = new ArrayList<YearlyClientForm>();
	private ArrayList<MonthlyNoteForm> notes = new ArrayList<MonthlyNoteForm>();
	
	
	

	public String toString ()
	{
		return companyCode+" "+companyName;
	}
	public ArrayList<BankDateInfo> getWithdrawDates() {
		ReferenceDao dao = new ReferenceDao();
		try {
			withdrawDates = dao.getBankDateInfos();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return withdrawDates;
	}

	public void setWithdrawDates(ArrayList<BankDateInfo> withdrawDates) {
		this.withdrawDates = withdrawDates;
	}
	
	
	public CompanyForm getCompanyList(int index)
    {
        if(this.companylist == null)
        {
            this.companylist = new ArrayList();
        }
        while(index >= this.companylist.size())
        {
            this.companylist.add(new CompanyForm());
        }
 
        return (CompanyForm) companylist.get(index);
    }
 
public CompanyForm(){
		
		companylist = new ArrayList<CompanyForm>();
		quarterlyRemitterList = new ArrayList<CompanyForm>();
	}
	
	
	public CompanyForm getCompany(String code)
	{
			
		if (companylist == null) return null;
		for (int i = 0; i < companylist.size(); i++)
		{
			CompanyForm comp = (CompanyForm)companylist.get(i);
			if (code.equals(comp.getCompanyCode()))
					return comp;
		}
		return null;	
	}
	@Override
	public void clear() {
		companyName = null; 
		companyCode = null;  
		bnNumber = null; 
		address1 = null; 
		address2 = null;
		bankAccNumber = null;
		city= null;
		province= null;
		postalCode= null;
		country= null;
		comment= null;
		companylist.clear();
		quarterlyRemitterList.clear();
		clientType = null;
		yearlyChargesExist=null;
		yearlyClientInfo = new YearlyClientForm();
	}

	public CompanyForm getProductList(int index) {
	    return  (CompanyForm)companylist.get(index);
	}

	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getBnNumber() {
		return bnNumber;
	}

	public void setBnNumber(String bnNumber) {
		this.bnNumber = bnNumber;
	}

	
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String ciry) {
		this.city = ciry;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public ArrayList<CompanyForm> getCompanyList() {
		return companylist;
	}

	public void setCompanyList(ArrayList<CompanyForm> list) {		
		this.companylist = list;
	}

	public ChargeForm getCharge() {
		return charge;
	}

	public void setCharge(ChargeForm charge) {
		this.charge = charge;
	}

	public PayrollForm getPayroll() {
		return payroll;
	}

	public void setPayroll(PayrollForm payroll) {
		this.payroll = payroll;
	}

	public TrustForm getTrust() {
		return trust;
	}

	public void setTrust(TrustForm trust) {
		this.trust = trust;
	}

/*	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}*/

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public ArrayList<CompanyForm> getCompany() {
		return company;
	}

	public void setCompany(ArrayList<CompanyForm> company) {
		this.company = company;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public void deleteFromList(String code) {
		if (companylist!= null && companylist.size() > 0 && code != null)
		{
			for (int i=0; i< companylist.size() ; i++ )
			{
				CompanyForm comp = (CompanyForm)companylist.get(i);
				if (comp!= null && comp.getCompanyCode() != null && comp.getCompanyCode().equalsIgnoreCase(code))
					companylist.remove(i);
			}
		}
		
	}

	public String getBankAccNumber() {
		return bankAccNumber;
	}

	public void setBankAccNumber(String bankAccNumber) {
		this.bankAccNumber = bankAccNumber;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public ArrayList<ChargeForm> getChargelist() {
		return chargelist;
	}

	public void setChargelist(ArrayList<ChargeForm> chargelist) {
		this.chargelist = chargelist;
	}

	public ArrayList<PayrollForm> getPayrolllist() {
		return payrolllist;
	}

	public void setPayrolllist(ArrayList<PayrollForm> payrolllist) {
		this.payrolllist = payrolllist;
	}



	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getWithdrawDate() {
		return withdrawDate;
	}

	public void setWithdrawDate(String withdrawDate) {
		this.withdrawDate = withdrawDate;
	}
	public String getPayrollType() {
		return payrollType;
	}
	public void setPayrollType(String payrollType) {
		this.payrollType = payrollType;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getTotalBankCharge() {
		return totalBankCharge;
	}
	public void setTotalBankCharge(double totalBankCharge) {
		this.totalBankCharge = totalBankCharge;
	}
	public double getTotalRemittanceAmount() {
		return totalRemittanceAmount;
	}
	public void setTotalRemittanceAmount(double totalRemittanceAmount) {
		this.totalRemittanceAmount = totalRemittanceAmount;
	}
	public double getTotalGst() {
		return totalGst;
	}
	public void setTotalGst(double totalGst) {
		this.totalGst = totalGst;
	}
	public double getTotalNetAmount() {
		return totalNetAmount;
	}
	public void setTotalNetAmount(double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}
	public double getTotalWithdrawAmount() {
		return totalWithdrawAmount;
	}
	public void setTotalWithdrawAmount(double totalWithdrawAmount) {
		this.totalWithdrawAmount = totalWithdrawAmount;
	}
	public String getMonthFrom() {
		return monthFrom;
	}
	public void setMonthFrom(String monthFrom) {
		this.monthFrom = monthFrom;
	}
	public String getMonthTO() {
		return monthTO;
	}
	public void setMonthTO(String monthTO) {
		this.monthTO = monthTO;
	}
	public String getYearFrom() {
		return yearFrom;
	}
	public void setYearFrom(String yearFrom) {
		this.yearFrom = yearFrom;
	}
	public String getYearTo() {
		return yearTo;
	}
	public void setYearTo(String yearTo) {
		this.yearTo = yearTo;
	}
	public ArrayList<CompanyForm> getQuarterlyRemitterList() {
		return quarterlyRemitterList;
	}
	public void setQuarterlyRemitterList(
			ArrayList<CompanyForm> quarterlyRemitterList) {
		this.quarterlyRemitterList = quarterlyRemitterList;
	}
	public String getAllPayrollSentToCRA() {
		return allPayrollSentToCRA;
	}
	public void setAllPayrollSentToCRA(String allPayrollSentToCRA) {
		this.allPayrollSentToCRA = allPayrollSentToCRA;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public YearlyClientForm getYearlyClientInfo() {
		return yearlyClientInfo;
	}
	public void setYearlyClientInfo(YearlyClientForm yearlyClientInfo) {
		this.yearlyClientInfo = yearlyClientInfo;
	}
	public ArrayList<UserDataInfo> getEmployeelist() {
		System.out.println("getEmployeelist ");
		if (employeelist != null && employeelist.size()  > 0 )
			return employeelist;
		ManagementDao dao  = new ManagementDao();
		ArrayList<UserDataInfo>  newUsersList=null;
		try {
			employeelist = dao.getRegisteredUsers(101);
			newUsersList =  removeDuplicateUsers(employeelist);
			System.out.println("updatePayroll YEAR "+newUsersList.size());
			this.employeelist = newUsersList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  newUsersList;
	}
	public ArrayList<YearlyClientForm> getYearlyChargesExist() {
		return yearlyChargesExist;
	}
	public void setYearlyChargesExist(ArrayList<YearlyClientForm> yearlyChargesExist) {
		this.yearlyChargesExist = yearlyChargesExist;
	}
	
	public ArrayList<MonthlyNoteForm> getNotes() {
		return notes;
	}
	public void setNotes(ArrayList<MonthlyNoteForm> notes) {
		this.notes = notes;
	}
	public MonthlyNoteForm getNotesFromList(int id) {
		if (notes == null) return null;
		for (int i = 0; i < notes.size(); i++)
		{
			MonthlyNoteForm comp = (MonthlyNoteForm)notes.get(i);
			if (id == comp.getId())
					return comp;
		}
		return null;	
	}
	public String getNotesStatus() {
		return notesStatus;
	}

	public void setNotesStatus(String notesStatus) {
		this.notesStatus = notesStatus;
	}
	public ArrayList<CompanyForm> getOriginalList() {
		return originalList;
	}
	public void setOriginalList(ArrayList<CompanyForm> originalList) {
		this.originalList = originalList;
	}
	public ArrayList<CompanyForm> getActiveCompanyList() {
		return activeCompanyList;
	}
	public void setActiveCompanyList(ArrayList<CompanyForm> activeCompanyList) {
		this.activeCompanyList = activeCompanyList;
	}
	
	
	
}
