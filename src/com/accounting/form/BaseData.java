package com.accounting.form;

public class BaseData implements Serializable {
	public static final String ANCESTOR_STRING = " ANC "; 
	public static final String DESCENDANT_STRING = " DESC ";
	
	private String deviceAppVersion = null;
	private String userCd = null;
	private String password = null;
	
	public String getDeviceAppVersion() {
		return deviceAppVersion;
	}
	public void setDeviceAppVersion(String deviceAppVersion) {
		this.deviceAppVersion = deviceAppVersion;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserCd() {
		return userCd;
	}
	public void setUserCd(String userCd) {
		this.userCd = userCd;
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getClass().getName());
		
		buffer.append("| deviceAppVersion:");
		buffer.append(getDeviceAppVersion());
		buffer.append("| userCd:");
		buffer.append(getUserCd());
		buffer.append("| password:");
		buffer.append(getPassword());
		
		return buffer.toString();
	}
}
