package com.accounting.form;



public class YearlyChargeForm extends BaseForm {

	
	private String companyCode;
	private int store_id;
	
	private String yearEnd;
	
	private String bnNumber;
	private double corpCharge;
	private double gstCharge;
	private double payrollCharge;
	private double otherCharge1;
	private double otherCharge2;
	private double otherCharge3;
	private double totalCharges;
	private String otherCharge1Name;
	private String otherCharge2Name;
	private String otherCharge3Name;
	private String comments;
	private String gstIncluded;
	private String editId;
	private String lastUpdatedBy;
			
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getcorpChargeDouble() {return new java.text.DecimalFormat("0.00").format(corpCharge);	}
	public String getgstChargeDouble() {return new java.text.DecimalFormat("0.00").format(gstCharge);	}
	public String getpayrollChargeDouble() {return new java.text.DecimalFormat("0.00").format(payrollCharge);	}
	public String getotherCharge1Double() {return new java.text.DecimalFormat("0.00").format(otherCharge1);	}
	public String getotherCharge2Double() {return new java.text.DecimalFormat("0.00").format(otherCharge2);	}
	public String getotherCharge3Double() {return new java.text.DecimalFormat("0.00").format(otherCharge3);	}
		
	
	
	@Override
	public void clear() {
		
		companyCode=null;
	
		
		
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public double getCorpCharge() {
		return corpCharge;
	}
	public void setCorpCharge(double corpChanrge) {
		this.corpCharge = corpChanrge;
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
	public double getOtherCharge3() {
		return otherCharge3;
	}
	public void setOtherCharge3(double otherCharge3) {
		this.otherCharge3 = otherCharge3;
	}
	public String getOtherCharge3Name() {
		return otherCharge3Name;
	}
	public void setOtherCharge3Name(String otherCharge3Name) {
		this.otherCharge3Name = otherCharge3Name;
	}
	public double getPayrollCharge() {
		return payrollCharge;
	}
	public void setPayrollCharge(double payrollCharge) {
		this.payrollCharge = payrollCharge;
	}
		public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	public double getTotalCharges() {
		return totalCharges;
	}
	public void setTotalCharges(double totalCharges) {
		this.totalCharges = totalCharges;
	}
	public String getYearEnd() {
		return yearEnd;
	}
	public void setYearEnd(String yearEnd) {
		this.yearEnd = yearEnd;
	}

	

}
