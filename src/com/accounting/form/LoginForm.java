package com.accounting.form;


public class LoginForm extends BaseForm {

	private String operation;
	private String editId;

	private String login; 
	private String password; 
	
	private boolean advertiser;

	private boolean msmUser;
	
	
	public LoginForm(){}
	
	@Override
	public void clear() {
		login = null; 
		password = null;  
		
		advertiser = false;
		msmUser=false;
	}

	public boolean isAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(boolean advertiser) {
		this.advertiser = advertiser;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isMsmUser() {
		return msmUser;
	}

	public void setMsmUser(boolean msmUser) {
		this.msmUser = msmUser;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
