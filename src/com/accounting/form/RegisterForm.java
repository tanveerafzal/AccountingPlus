package com.accounting.form;

import com.accounting.data.Role;
import com.accounting.data.UserDataInfo;


public class RegisterForm extends BaseForm {

	private String login; 
	private String password;
	private String firstName; 
	private String lastName; 
	private int storeId;
	private int role;
	private String roleDesc;
	private String editId;
	private String[] roles = new String[12];
	private ArrayList<UserDataInfo> usersList;
	private ArrayList<Role> roleList;
	public RegisterForm(){
		
		 if(this.usersList == null)
	        {
	            this.usersList = new ArrayList<UserDataInfo>();
	            this.roleList = new ArrayList<Role>();
	        }
	}
	
	@Override
	public void clear() {
		login = null; 
		password = null;  
		firstName = null;
		lastName = null;
		roleDesc = null;
		editId = null;
		roles = new String[12];
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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public ArrayList<UserDataInfo> getUsersList() {
		return usersList;
	}

	public void setUsersList(ArrayList<UserDataInfo> usersList) {
		this.usersList = usersList;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	
	public UserDataInfo getUser(String login2)
	{
			
		if (usersList == null) return null;
		for (int i = 0; i < usersList.size(); i++)
		{
			UserDataInfo user = (UserDataInfo)usersList.get(i);
			if (login2.equals(user.getLogin()))
					return user;
		}
		return null;	
	}

	public ArrayList<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(ArrayList<Role> roleList) {
		this.roleList = roleList;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}
}
