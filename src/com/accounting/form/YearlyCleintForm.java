package com.accounting.form;



public class YearlyCleintForm extends BaseForm {

	
	private String companyCode;
	private int store_id;
	
	private String yearEnd;
	private int  year;
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

	public String getpaymentReceivedDouble() {return new java.text.DecimalFormat("0.00").format(paymentReceived);	}
	public String getbalanceDueDouble() {return new java.text.DecimalFormat("0.00").format(balanceDue);	}
		
	
	
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
	

	

}
