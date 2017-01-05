package com.accounting.form;

import com.accounting.dao.ReferenceDao;
import com.accounting.data.BankDateInfo;


public class ChargeForm extends BaseForm {

	
	private String companyCode;
	private int store_id;
	
	private String companyName; 
	private String bnNumber;
	private double netAmount;
	private double gst;
	private double total;
	private String monthYear;
	private String withdrawDate;
	private String accountNumber;
	private double previousAmount;
	private double previousAmountInstallment;
	private double previousAmountBalance;
	private String previousAmountGstIncluded;
	private String comments;
	private String editId;
	private String active;
	private double installamentGst;
	private double installamentTotal;
	private String nsf;
	private String  lockAccount;
	private double previousCharges;
	private double totalPreviousAmountPaid;
			
	private ArrayList<BankDateInfo> withdrawDates = new ArrayList<BankDateInfo>();
	private double totalWithdrawAmount;
	private String overright;

	public String getnetAmountDouble() {return new java.text.DecimalFormat("0.00").format(netAmount);	}
	public String getgstDouble() {return new java.text.DecimalFormat("0.00").format(gst);	}
	public String gettotalDouble() {return new java.text.DecimalFormat("0.00").format(total+previousCharges);	}
	public String getpreviousAmountDouble() {return new java.text.DecimalFormat("0.00").format(previousAmount);	}
	public String getpreviousAmountInstallmentDouble() {return new java.text.DecimalFormat("0.00").format(previousAmountInstallment);	}
	public String getpreviousAmountBalanceDouble() {return new java.text.DecimalFormat("0.00").format(previousAmountBalance);	}
	public String getinstallamentGstDouble() {return new java.text.DecimalFormat("0.00").format(installamentGst);	}
	public String getinstallamentTotalDouble() {return new java.text.DecimalFormat("0.00").format(installamentTotal);	}
	public String getpreviousChargesDouble() {return new java.text.DecimalFormat("0.00").format(previousCharges);	}

	public String gettotalWithdrawAmountDouble() {return new java.text.DecimalFormat("0.00").format(totalWithdrawAmount);	}
	public String getTotalPreviousAmountPaidDouble() {return new java.text.DecimalFormat("0.00").format(totalPreviousAmountPaid);	}
	
	private ArrayList<CompanyForm> companylist = new ArrayList<CompanyForm>();
	
	
	public ArrayList<CompanyForm> getCompanylist() {
		return companylist;
	}
	public void setCompanylist(ArrayList<CompanyForm> companylist) {
		this.companylist = companylist;
	}
	
	public ArrayList<String> getCompanyCodes() {
		ArrayList<String> companyCodes = new ArrayList<String>();
		if (companylist!= null)
			{
			for (int i=0; i <companylist.size(); i++ )
				{
				CompanyForm c = companylist.get(i);
				companyCodes.add(c.getCompanyCode());
				}
				
			}
		return companyCodes;
	}
	
	public ArrayList<String> getCompanyNames() {
		ArrayList<String> companyNames= new ArrayList<String>();
		if (companylist!= null)
			{
			for (int i=0; i <companylist.size(); i++ )
				{
				CompanyForm c = companylist.get(i);
				companyNames.add(c.getCompanyName());
				}
				
			}
		return companyNames;
	}
	
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public ChargeForm(){}
	
	@Override
	public void clear() {
		
		companyCode=null;
		companyName=null ;
		netAmount=0.00;
		gst=0.00;
		total=0.00;
		monthYear=null;
		withdrawDate=null;
		previousAmount=0.00;
		previousAmountInstallment=0.00;
		previousAmountBalance=0.00;
		comments=null;
		overright = null;
		
		
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	
	public double getNetAmount() {
		return format(netAmount);
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public double getGst() {
		return format(gst);
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public double getTotal() {
		return format(total);
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public String getWithdrawDate() {
		return withdrawDate;
	}

	public void setWithdrawDate(String withdrawDate) {
		this.withdrawDate = withdrawDate;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getComments() {
		return comments;
	}
	public String getCommentsShort() {
		
		String paymentComemntsFormatted = "";
		if (comments != null && comments.length()> 20)
		{
			paymentComemntsFormatted = comments.substring(0,19);
		}else
		{
			paymentComemntsFormatted=comments;
		}
		return paymentComemntsFormatted;
	}

	
	public void setComments(String comments) {
		this.comments = comments;
	}

	public double getPreviousAmount() {
		return format(previousAmount);
	}

	public void setPreviousAmount(double previousAmount) {
		this.previousAmount = previousAmount;
	}

	public double getPreviousAmountBalance() {
		return format(previousAmountBalance);
	}

	public void setPreviousAmountBalance(double previousAmountBalance) {
		this.previousAmountBalance = previousAmountBalance;
	}

	public double getPreviousAmountInstallment() {
		return format(previousAmountInstallment);
	}

	public void setPreviousAmountInstallment(double previousAmountInstallment) {
		this.previousAmountInstallment = previousAmountInstallment;
	}

	



	public String getBnNumber() {
		return bnNumber;
	}

	public void setBnNumber(String bnNumber) {
		this.bnNumber = bnNumber;
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

	public double getTotalWithdrawAmount() {
		return format(totalWithdrawAmount);
	}

	public void setTotalWithdrawAmount(double totalWithdrawAmount) {
		this.totalWithdrawAmount = totalWithdrawAmount;
	}

	public double getInstallamentGst() {
		return format(installamentGst);
	}

	public void setInstallamentGst(double installamentGst) {
		this.installamentGst = installamentGst;
	}

	public double getInstallamentTotal() {
		return format(installamentTotal);
	}

	public void setInstallamentTotal(double installamentTotal) {
		this.installamentTotal = installamentTotal;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}



	public String getOverright() {
		return overright;
	}
	public void setOverright(String overright) {
		this.overright = overright;
	}
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getNsf() {
		return nsf;
	}

	public void setNsf(String nsf) {
		this.nsf = nsf;
	}

	public String getPreviousAmountGstIncluded() {
		return previousAmountGstIncluded;
	}

	public void setPreviousAmountGstIncluded(String previousAmountGstIncluded) {
		this.previousAmountGstIncluded = previousAmountGstIncluded;
	}

	public double getPreviousCharges() {
		return previousCharges;
	}

	public void setPreviousCharges(double previousCharges) {
		this.previousCharges = previousCharges;
	}
	public String getCompanyCodeByName(String companyName2) {
		System.out.println("getCompanyCodeByNamecalled for "+companyName2);
		if (companylist!= null)
			{
			for (int i=0; i <companylist.size(); i++ )
				{
				CompanyForm c = companylist.get(i);
				if (companyName2.equals(c.getCompanyName()))
				{
					System.out.println("getCompanyCodeByNamecalled returning  "+c.getCompanyCode());
					return c.getCompanyCode();
				}
					
				}
				
			}
		return null;
	}
	public double getTotalPreviousAmountPaid() {
		return totalPreviousAmountPaid;
	}
	public void setTotalPreviousAmountPaid(double totalPreviousAmountPaid) {
		this.totalPreviousAmountPaid = totalPreviousAmountPaid;
	}
	public String getLockAccount() {
		return lockAccount;
	}
	public void setLockAccount(String lockAccount) {
		this.lockAccount = lockAccount;
	}




}
