package com.accounting.data;

import com.accounting.form.BaseForm;


public class UserDataInfo extends BaseForm {

	private String login; 
	private String password; 
	
	private String firstName; 
	private String lastName; 
	
	private String admin;
	
	private int role;
	private ArrayList<String> roles;
	private String roleDesc;
	
	private int storeId;
	
	StyleInfo styleInfo = null;
	
	
	RefDataInfo refDataInfo = null;
	public RefDataInfo getRefDataInfo() {
		return refDataInfo;
	}

	public void setRefDataInfo(RefDataInfo refDataInfo) {
		this.refDataInfo = refDataInfo;
	}
	
	public UserDataInfo(){
		
		roles = new ArrayList<String>();
		styleInfo = new StyleInfo();
	}
	
	@Override
	public void clear() {
		login = null; 
		password = null;  
		roles = null;
		
	
		
	}

	public String getName() {
		return firstName +" "+lastName;
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

	

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public StyleInfo getStyleInfo() {
		return styleInfo;
	}

	public void setStyleInfo(StyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

	
}
