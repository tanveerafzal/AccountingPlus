package com.accounting.form;

public class PayrollForm extends BaseForm {

		
	private int store_id;
	private String companyCode;
	private String companyName;
	private String payrollMonth;
	private int numberOfemployees;
	private double remittanceAmount;
	private double bankCharge;
	private double totalAmount;
	private String payrollType;
	private String nsf;
	private double revisedAmount=0.00;
	private double grossPayroll=0.00;
	private double previousBalance;
	private String comment;
	private String active;
	private String payrollFrequency;
	private String sentToCRA;
	
	private String editId;
	private boolean isUpdating=false;
	private double totalAmountDue;
	private int totalPayrolls;
	private String overright;
	private String accountLocked;
	
	private boolean payrollChanged;
	
	int i=0;
	

	
	public String getRemittanceAmountDouble() {return new java.text.DecimalFormat("0.00").format(remittanceAmount);	}
	public String getGrossPayrollDouble() {return new java.text.DecimalFormat("0.00").format(grossPayroll);	}
	public String getbankChargeDouble() {return new java.text.DecimalFormat("0.00").format(bankCharge);	}
	public String gettotalAmountDouble() {return new java.text.DecimalFormat("0.00").format(totalAmount);	}
	public String getrevisedAmountDouble() {return new java.text.DecimalFormat("0.00").format(revisedAmount);	}
	public String getpreviousBalanceDouble() {return new java.text.DecimalFormat("0.00").format(previousBalance);	}
	public String gettotalAmountDueDouble() {return new java.text.DecimalFormat("0.00").format(totalAmountDue);	}
	
	private ArrayList<CompanyForm> companylist = new ArrayList<CompanyForm>();

	
	public String toString()
	{
		return "Payroll: CompanyCode="+getCompanyCode()+" PayrollMonth="+getPayrollMonth()+" ,Active="+getActive()
		+" ,PayrollType="+getPayrollType()+" ,NumberOfemployees="+getNumberOfemployees()+" ,RemittanceAmount="+getRemittanceAmount()
		+" ,BankCharge="+getBankCharge()+" ,TotalAmount="+getTotalAmount()+" ,GrossPayroll(="+getGrossPayroll()
		+" ,PayrollFrequency="+getPayrollFrequency()+" ,PayrollFrequency= "+getSentToCRA()+" ,Nsf= "+getNsf();
	}
	
	
	public String hashValue()
	{
		DecimalFormat df = new DecimalFormat("0");
		return 	payrollMonth+active+payrollType+numberOfemployees+df.format(remittanceAmount)+df.format(bankCharge)+df.format(totalAmount)+df.format(grossPayroll)+payrollFrequency+nsf;
	}
	
	
	
	public ArrayList<String> getCompanyCodes() {
		ArrayList<String> companyCodes= new ArrayList<String>();
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
	
	
	public ArrayList<CompanyForm> getCompanylist() {
		return companylist;
	}
	public void setCompanylist(ArrayList<CompanyForm> companylist) {
		this.companylist = companylist;
	}
	public String getOverright() {
		return overright;
	}

	public void setOverright(String overright) {
		this.overright = overright;
	}

	public int getTotalPayrolls() {
		return totalPayrolls;
	}

	public void setTotalPayrolls(int totalPayrolls) {
		this.totalPayrolls = totalPayrolls;
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

	public PayrollForm(){}
	
	@Override
	public void clear() {
		
		companyCode = null;  
		isUpdating = false;
		payrollMonth=null;
		numberOfemployees=0;
		remittanceAmount=0.00;
		bankCharge=0.00;
		grossPayroll = 0.00;
		comment=null;
		overright = null;
	
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getPayrollMonth() {
		return payrollMonth;
	}

	public void setPayrollMonth(String payrollMonth) {
		this.payrollMonth = payrollMonth;
	}

	public int getNumberOfemployees() {
		return numberOfemployees;
	}

	public void setNumberOfemployees(int numberOfemployees) {
		this.numberOfemployees = numberOfemployees;
	}

	public double getRemittanceAmount() {
		return format(remittanceAmount);
	}
	
	
	
	public void setRemittanceAmount(double remittanceAmount) {
		this.remittanceAmount = remittanceAmount;
	}

	public double getBankCharge() {
		return format(bankCharge);
	}

	public void setBankCharge(double bankCharge) {
		this.bankCharge = bankCharge;
	}

	public double getTotalAmount() {
		return format(totalAmount);
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPayrollType() {
		return payrollType;
	}

	public void setPayrollType(String payrollType) {
		this.payrollType = payrollType;
	}

	

	public String getNsf() {
		if (nsf == null || nsf.length() == 0) return "N";
		return nsf;
	}

	public void setNsf(String nsf) {
		this.nsf = nsf;
	}

	public double getRevisedAmount() {
		return format(revisedAmount);
	}

	public void setRevisedAmount(double revisedAmount) {
		this.revisedAmount = revisedAmount;
	}

	public boolean isUpdating() {
		return isUpdating;
	}

	public void setUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getGrossPayroll() {
		return format(grossPayroll);
	}

	public void setGrossPayroll(double grossPayroll) {
		this.grossPayroll = grossPayroll;
	}

	public double getPreviousBalance() {
		return format(previousBalance);
	}

	public void setPreviousBalance(double previousBalance) {
		this.previousBalance = previousBalance;
	}

	public double getTotalAmountDue() {
			return format(totalAmountDue);
	}

	public void setTotalAmountDue(double totalAmountDue) {
		this.totalAmountDue = totalAmountDue;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getPayrollFrequency() {
		return payrollFrequency;
	}

	public void setPayrollFrequency(String payrollFrequency) {
		this.payrollFrequency = payrollFrequency;
	}
	public String getSentToCRA() {
		return sentToCRA;
	}
	public void setSentToCRA(String sentToCRA) {
		this.sentToCRA = sentToCRA;
	}
	public boolean isPayrollChanged() {
		return payrollChanged;
	}
	public void setPayrollChanged(boolean payrollChanged) {
		this.payrollChanged = payrollChanged;
	}
	public String getAccountLocked() {
		return accountLocked;
	}
	public void setAccountLocked(String accountLocked) {
		this.accountLocked = accountLocked;
	}
	


}
