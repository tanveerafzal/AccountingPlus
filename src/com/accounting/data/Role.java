package com.accounting.data;

public class Role {
	private int role;
	private String roleDesc;
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
	
	
	public static void main(String[] args) throws InterruptedException
	{
		 
		Date date = new Date();
	      DateFormat dataformat =  DateFormat.getDateInstance(DateFormat.LONG);     
	      String s4 = dataformat.format(date);
	     long time = (new Date()).getTime();
		 System.out.println(""+(new Date()).getTime());
		
	}
}
