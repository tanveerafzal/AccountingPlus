package com.accounting.data;

public class RefDataInfo {
	
	
	int storeId;
	double gstRate;
	double nsfFee;
	private ArrayList<ChargeFeeInfo> chargeFeeList = new ArrayList<ChargeFeeInfo>()  ;

	
	
	String storeName;
	String storeAddress;
	String storeLogo;

	String contactName;
	String phone;
	String email;
	
	
	
	
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getGstRate() {
		return gstRate;
	}
	public void setGstRate(double gstRate) {
		this.gstRate = gstRate;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public double getNsfFee() {
		return nsfFee;
	}
	public void setNsfFee(double nsfFee) {
		this.nsfFee = nsfFee;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getStoreLogo() {
		return storeLogo;
	}
	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public ArrayList<ChargeFeeInfo> getChargeFeeList() {
		return chargeFeeList;
	}
	public void setChargeFeeList(ArrayList<ChargeFeeInfo> chargeFeeList) {
		this.chargeFeeList = chargeFeeList;
	}
	
	
	
	

}
