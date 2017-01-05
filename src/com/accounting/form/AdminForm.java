package com.accounting.form;

import com.accounting.data.ChargeFeeInfo;


public class AdminForm extends BaseForm {


	int storeId;
	double gstRate;
	double nsfFee;
	private ArrayList<ChargeFeeInfo> chargeFeeList ;

	String storeName;
	String storeAddress;
	String storeLogo;
	RegisterForm registerForm = null;
	

	String contactName;
	String phone;
	String email;

	
	
	
	public AdminForm(){
		
		 if(this.chargeFeeList == null)
	        {
	            this.chargeFeeList = new ArrayList<ChargeFeeInfo>();
	        }
		 if(this.registerForm == null)
	        {
	            this.registerForm = new RegisterForm();
	        }
		
	 
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	
	public ArrayList<ChargeFeeInfo> getChargeFeeList() {
		return chargeFeeList;
	}
	public void setChargeFeeList(ArrayList<ChargeFeeInfo> chargeFeeList) {
		this.chargeFeeList = chargeFeeList;
	}
	public double getGstRate() {
		return format(gstRate);
	}

	public void setGstRate(double gstRate) {
		this.gstRate = gstRate;
	}

	public double getNsfFee() {
		return format(nsfFee);
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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
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
	public RegisterForm getRegisterForm() {
		return registerForm;
	}
	public void setRegisterForm(RegisterForm registerForm) {
		this.registerForm = registerForm;
	}
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
	


}
