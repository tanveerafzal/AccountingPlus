package com.accounting.form;



public class YearlyClientForm extends BaseForm {

	
	private String companyCode;
	private int store_id;
	
	private String yearEnd;
	
	private String taxFiled;
	private String taxCompleted;
	private String taxMailed;
	private double paymentReceived;
	private double balanceDue;
	private String taxPreparedBy;
	private String paymentComemnts;
	private String assignedTo;
	private String lastUpdatedBy;
	private String comments;
	
	private String taxCompletedDate;
	private String taxFiledDate;
	private String paymentReceivedDate;

	private String bnNumber;
	private double corpCharge;
	private double gstCharge;
	private double payrollCharge;
	private double otherCharge1;
	private double otherCharge2;
	private double otherCharge;
	private double totalCharges;
	private String otherCharge1Name;
	private String otherCharge2Name;
	private String chargeComments;
	private String gstIncluded;
	private String editId;
	private double currentCharges;
	private String  paymentReceivedBy = null;
	
	private String paymentInfoExist;
	private String taxReturnInfoExist;
	
	
	private int baseYear;
	private String active;
	private String lock;
	
	private double preBalance;
	double discount;
	
	
	public String getpaymentReceivedDouble() {return new java.text.DecimalFormat("0.00").format(paymentReceived);	}
	public String getbalanceDueDouble() {return new java.text.DecimalFormat("0.00").format(balanceDue);	}
		
	public String getcorpChargeDouble() {return new java.text.DecimalFormat("0.00").format(corpCharge);	}
	public String getgstChargeDouble() {return new java.text.DecimalFormat("0.00").format(gstCharge);	}
	public String getpayrollChargeDouble() {return new java.text.DecimalFormat("0.00").format(payrollCharge);	}
	public String getotherCharge1Double() {return new java.text.DecimalFormat("0.00").format(otherCharge1);	}
	public String getotherCharge2Double() {return new java.text.DecimalFormat("0.00").format(otherCharge2);	}
	public String getotherChargeDouble() {return new java.text.DecimalFormat("0.00").format(otherCharge);	}
	public String getpreBalanceDouble() {return new java.text.DecimalFormat("0.00").format(preBalance);	}
	public String gettotalChargesDouble() {return new java.text.DecimalFormat("0.00").format(totalCharges);	}

		public String getTotalOtherChargesDouble() {
		double total = otherCharge1+otherCharge2;
		return new java.text.DecimalFormat("0.00").format(total);	
		
	}

	@Override
	public void clear() {
		
		companyCode=null;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public double getBalanceDue() {
		return balanceDue;
	}
	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getPaymentComemnts() {
		return paymentComemnts;
	}
	public void setPaymentComemnts(String paymentComemnts) {
		this.paymentComemnts = paymentComemnts;
	}
	public double getPaymentReceived() {
		return paymentReceived;
	}
	public void setPaymentReceived(double paymentReceived) {
		this.paymentReceived = paymentReceived;
	}
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	public String getTaxCompleted() {
		return taxCompleted;
	}
	public void setTaxCompleted(String taxCompleted) {
		this.taxCompleted = taxCompleted;
	}
	public String getTaxFiled() {
		return taxFiled;
	}
	public void setTaxFiled(String taxFiled) {
		this.taxFiled = taxFiled;
	}
	public String getTaxMailed() {
		return taxMailed;
	}
	public void setTaxMailed(String taxMailed) {
		this.taxMailed = taxMailed;
	}
	public String getTaxPreparedBy() {
		return taxPreparedBy;
	}
	public void setTaxPreparedBy(String taxPreparedBy) {
		this.taxPreparedBy = taxPreparedBy;
	}
	public String getYearEnd() {
		return yearEnd;
	}
	public void setYearEnd(String yearEnd) {
		this.yearEnd = yearEnd;
	}
	public String getPaymentReceivedDate() {
		return paymentReceivedDate;
	}
	public void setPaymentReceivedDate(String paymentReceivedDate) {
		this.paymentReceivedDate = paymentReceivedDate;
	}
	public String getTaxCompletedDate() {
		return taxCompletedDate;
	}
	public void setTaxCompletedDate(String taxCompletedDate) {
		this.taxCompletedDate = taxCompletedDate;
	}
	public String getTaxFiledDate() {
		return taxFiledDate;
	}
	public void setTaxFiledDate(String taxFiledDate) {
		this.taxFiledDate = taxFiledDate;
	}
	public String getBnNumber() {
		return bnNumber;
	}
	public void setBnNumber(String bnNumber) {
		this.bnNumber = bnNumber;
	}
	public String getChargeComments() {
		return chargeComments;
	}
	public void setChargeComments(String chargeComments) {
		this.chargeComments = chargeComments;
	}
	public double getCorpCharge() {
		return corpCharge;
	}
	public void setCorpCharge(double corpCharge) {
		this.corpCharge = corpCharge;
	}
	public String getEditId() {
		return editId;
	}
	public void setEditId(String editId) {
		this.editId = editId;
	}
	public double getGstCharge() {
		return gstCharge;
	}
	public void setGstCharge(double gstCharge) {
		this.gstCharge = gstCharge;
	}
	public String getGstIncluded() {
		return gstIncluded;
	}
	public void setGstIncluded(String gstIncluded) {
		this.gstIncluded = gstIncluded;
	}
	public double getOtherCharge1() {
		return otherCharge1;
	}
	public void setOtherCharge1(double otherCharge1) {
		this.otherCharge1 = otherCharge1;
	}
	public String getOtherCharge1Name() {
		return otherCharge1Name;
	}
	public void setOtherCharge1Name(String otherCharge1Name) {
		this.otherCharge1Name = otherCharge1Name;
	}
	public double getOtherCharge2() {
		return otherCharge2;
	}
	public void setOtherCharge2(double otherCharge2) {
		this.otherCharge2 = otherCharge2;
	}
	public String getOtherCharge2Name() {
		return otherCharge2Name;
	}
	public void setOtherCharge2Name(String otherCharge2Name) {
		this.otherCharge2Name = otherCharge2Name;
	}
	public double getOtherCharge() {
		return otherCharge;
	}
	public void setOtherCharge(double otherCharge) {
		this.otherCharge = otherCharge;
	}
	public double getPayrollCharge() {
		return payrollCharge;
	}
	public void setPayrollCharge(double payrollCharge) {
		this.payrollCharge = payrollCharge;
	}
	public double getTotalCharges() {
		return totalCharges;
	}
	public void setTotalCharges(double totalCharges) {
		this.totalCharges = totalCharges;
	}
	public double getPreBalance() {
		return preBalance;
	}
	public void setPreBalance(double preBalance) {
		this.preBalance = preBalance;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public int getBaseYear() {
		return baseYear;
	}
	public void setBaseYear(int baseYear) {
		this.baseYear = baseYear;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getLock() {
		return lock;
	}
	public void setLock(String lock) {
		this.lock = lock;
	}
	public double getCurrentCharges() {
		return currentCharges;
	}
	public void setCurrentCharges(double currentCharges) {
		this.currentCharges = currentCharges;
	}
	public String getPaymentComemntsFormat() {
		
		String paymentComemntsFormatted = "";
		if (paymentComemnts != null && paymentComemnts.length()> 20)
		{
			paymentComemntsFormatted = paymentComemnts.substring(0,19);
		}else
		{
			paymentComemntsFormatted=paymentComemnts;
		}
		return paymentComemntsFormatted;
	}
	public String getPaymentReceivedBy() {
		return paymentReceivedBy;
	}
	public void setPaymentReceivedBy(String paymentReceivedBy) {
		this.paymentReceivedBy = paymentReceivedBy;
	}
	public String getPaymentInfoExist() {
		return paymentInfoExist;
	}
	public void setPaymentInfoExist(String paymentInfoExist) {
		this.paymentInfoExist = paymentInfoExist;
	}
	public String getTaxReturnInfoExist() {
		return taxReturnInfoExist;
	}
	public void setTaxReturnInfoExist(String taxReturnInfoExist) {
		this.taxReturnInfoExist = taxReturnInfoExist;
	}
	
}
