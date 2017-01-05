package com.accounting.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.accounting.dao.ReferenceDao;
import com.accounting.data.BankDateInfo;



public class ReportForm extends BaseForm {
	
	private String reportType;
	private String withdrawDate;
	private String monthName;
	private String nsf;
	private String orderBy;
	private String companyName; 
	private String companyCode;

	
	private int store_id;
	private String storeName;
	private ArrayList<CompanyForm> list;
	private ArrayList<CompanyForm> chargeList;
	private ArrayList<CompanyForm> yearlyChargeList;
	
	
	
	private double nsfAmount;
	private double totalWithdrawAmount;
	private String quaterlyFilter;
	
	private String paymentReceived;
	private String taxFiled;
	private String taxMailed;
	private String yearEnd;
	
	double totalRemittanceAmount = 0.0;
	double totalBankCharge= 0.0;
	double totalAmount = 0.0;
	double totalGrossPayroll= 0.0;
	
	double totalQRemittanceAmount = 0.0;
	double totalQBankCharge= 0.0;
	double totalQAmount = 0.0;
	double totalQGrossPayroll= 0.0;
	
	private ArrayList<BankDateInfo> withdrawDates = new ArrayList<BankDateInfo>();
	private ArrayList<CompanyForm> emptyPayrollList;
	private ArrayList<CompanyForm> quartlyPayrollList;
	
	private String[] quaterlyMonths = new String[12];
	private String quaterlyYear;
	public String getQuaterlyYear() {
		return quaterlyYear;
	}

	public void setQuaterlyYear(String quaterlyYear) {
		this.quaterlyYear = quaterlyYear;
	}

	public ArrayList<CompanyForm> getList() {
		return list;
	}

	public void setList(ArrayList<CompanyForm> list) {
		this.list = list;
	}

	public ReportForm(){
		
		 if(this.list == null)
	        {
	            this.list = new ArrayList<CompanyForm>();
	        }
		 if(this.chargeList == null)
	        {
	            this.chargeList = new ArrayList<CompanyForm>();
	        }
		 if(this.yearlyChargeList == null)
	        {
	            this.yearlyChargeList = new ArrayList<CompanyForm>();
	        }
		 if(this.emptyPayrollList == null)
	        {
	            this.emptyPayrollList = new ArrayList<CompanyForm>();
	        }
		 if(this.quartlyPayrollList == null)
	        {
	            this.quartlyPayrollList = new ArrayList<CompanyForm>();
	        }
		 quaterlyMonths = new String[12];
	 
	}
	
	@Override
	public void clear() {
		
		reportType = null;  
		quaterlyMonths = new String[12];
	}


	@Override
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		quaterlyMonths = new String[12];
		super.reset(arg0, arg1);
	}



	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public ArrayList<CompanyForm> getChargeList() {
		return chargeList;
	}

	public void setChargeList(ArrayList<CompanyForm> chargeList) {
		this.chargeList = chargeList;
	}

	public String getWithdrawDate() {
		return withdrawDate;
	}

	public void setWithdrawDate(String withdrawDate) {
		this.withdrawDate = withdrawDate;
	}

	public String getNsf() {
		return nsf;
	}

	public void setNsf(String nsf) {
		this.nsf = nsf;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public double getTotalWithdrawAmount() {
		return totalWithdrawAmount;
	}

	public void setTotalWithdrawAmount(double totalWithdrawAmount) {
		this.totalWithdrawAmount = totalWithdrawAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalBankCharge() {
		return totalBankCharge;
	}

	public void setTotalBankCharge(double totalBankCharge) {
		this.totalBankCharge = totalBankCharge;
	}

	public double getTotalRemittanceAmount() {
		return totalRemittanceAmount;
	}

	public void setTotalRemittanceAmount(double totalRemittanceAmount) {
		this.totalRemittanceAmount = totalRemittanceAmount;
	}

	public ArrayList<BankDateInfo> getWithdrawDates() {
		ReferenceDao dao = new ReferenceDao();
		try {
			withdrawDates = dao.getBankDateInfos();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return withdrawDates;
	}

	public void setWithdrawDates(ArrayList<BankDateInfo> withdrawDates) {
		this.withdrawDates = withdrawDates;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}



	

public static void main (String[] arge)
{
	ReportForm f = new ReportForm();
	f.setMonth("12");
	System.out.println(f.getMonthName());
	
	
}

public double getNsfAmount() {
	return nsfAmount;
}

public void setNsfAmount(double nsfAmount) {
	this.nsfAmount = nsfAmount;
}

public ArrayList<CompanyForm> getEmptyPayrollList() {
	return emptyPayrollList;
}

public void setEmptyPayrollList(ArrayList<CompanyForm> emptyPayrollList) {
	this.emptyPayrollList = emptyPayrollList;
}

public ArrayList<CompanyForm> getQuartlyPayrollList() {
	return quartlyPayrollList;
}

public void setQuartlyPayrollList(ArrayList<CompanyForm> quartlyPayrollList) {
	this.quartlyPayrollList = quartlyPayrollList;
}

public String[] getQuaterlyMonths() {
	return quaterlyMonths;
}

public void setQuaterlyMonths(String[] quaterlyMonths) {
	this.quaterlyMonths = quaterlyMonths;
}

public double getTotalQAmount() {
	return totalQAmount;
}

public void setTotalQAmount(double totalQAmount) {
	this.totalQAmount = totalQAmount;
}

public double getTotalQBankCharge() {
	return totalQBankCharge;
}

public void setTotalQBankCharge(double totalQBankCharge) {
	this.totalQBankCharge = totalQBankCharge;
}

public double getTotalQRemittanceAmount() {
	return totalQRemittanceAmount;
}

public void setTotalQRemittanceAmount(double totalQRemittanceAmount) {
	this.totalQRemittanceAmount = totalQRemittanceAmount;
}

public double getTotalGrossPayroll() {
	return totalGrossPayroll;
}

public void setTotalGrossPayroll(double totalGrossPayroll) {
	this.totalGrossPayroll = totalGrossPayroll;
}

public double getTotalQGrossPayroll() {
	return totalQGrossPayroll;
}

public void setTotalQGrossPayroll(double totalQGrossPayroll) {
	this.totalQGrossPayroll = totalQGrossPayroll;
}


public double getGrandTotalBankCharge() {
	return totalQBankCharge+totalBankCharge;
}


public double getGrandTotalRemittanceAmount() {
	return totalQRemittanceAmount+totalRemittanceAmount;
}


public double getGrandTotalGrossPayroll() {
	return totalQGrossPayroll + totalGrossPayroll;
}


public double getGrandTotalAmount() {
	return totalAmount+totalQAmount;
}

public String getQuaterlyFilter() {
	return quaterlyFilter;
}

public void setQuaterlyFilter(String quaterlyFilter) {
	this.quaterlyFilter = quaterlyFilter;
}

public String getPaymentReceived() {
	return paymentReceived;
}

public void setPaymentReceived(String paymentReceived) {
	this.paymentReceived = paymentReceived;
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

public String getYearEnd() {
	return yearEnd;
}

public void setYearEnd(String yearEnd) {
	this.yearEnd = yearEnd;
}

public ArrayList<CompanyForm> getYearlyChargeList() {
	return yearlyChargeList;
}

public void setYearlyChargeList(ArrayList<CompanyForm> yearlyChargeList) {
	this.yearlyChargeList = yearlyChargeList;
}

public String getCompanyCode() {
	return companyCode;
}

public void setCompanyCode(String companyCode) {
	this.companyCode = companyCode;
}

public String getCompanyName() {
	return companyName;
}

public void setCompanyName(String companyName) {
	this.companyName = companyName;
}


}
