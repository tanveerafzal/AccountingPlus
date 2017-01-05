package com.accounting.form;


public class TrustForm extends BaseForm {

	private String companyCode;
	private String amount;
	private String description;
	private String dateWithheld;
	private String dateCompleted;
	private String comments;
	

	public TrustForm(){}
	
	@Override
	public void clear() {
		companyCode = null;  
		
	}


	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDateWithheld() {
		return dateWithheld;
	}

	public void setDateWithheld(String dateWithheld) {
		this.dateWithheld = dateWithheld;
	}

	public String getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(String dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}






}
