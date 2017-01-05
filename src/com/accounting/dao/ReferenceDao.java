package com.accounting.dao;

import com.accounting.data.BankDateInfo;
import com.accounting.data.ChargeFeeInfo;
import com.accounting.data.RefDataInfo;
import com.accounting.data.UserDataInfo;
import com.accounting.form.ChargeForm;
import com.accounting.form.CompanyForm;
import com.accounting.form.MonthlyNoteForm;
import com.accounting.form.PayrollForm;
import com.accounting.form.ReportForm;
import com.accounting.form.YearlyClientForm;
import com.accounting.utils.JspConstants;


public class ReferenceDao  extends DataAccessObject  {


	public ArrayList<BankDateInfo> getBankDateInfos() throws SQLException {
		ArrayList<BankDateInfo> result = new ArrayList<BankDateInfo>();
		ResultSet set = null;
		Statement statement = null;
		Connection con = null;
		try {

			con = getConnection();
			statement = con.createStatement();
			set = statement.executeQuery("select * from bank_withdraw_date ");
			BankDateInfo defaultOption = new BankDateInfo();
			defaultOption.setBankCode("ALL");
			defaultOption.setDateCode("ALL");
			result.add(defaultOption);
			while(set.next()){
				BankDateInfo item = new BankDateInfo();
				item.setBankCode(set.getString("bank_code"));
				item.setDateCode(set.getString("date_code"));
				result.add(item);
			}
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}

		return result;
	}
	
	public boolean checkIfStoreExist(RefDataInfo refDataInfo) throws SQLException {
		boolean refData = false;
		ResultSet set = null;
		Statement statement = null;
		Connection con = null;
		try {
			System.out.println("checkIfStoreExist storeName "+refDataInfo.getStoreName());
			con = getConnection();
			statement = con.createStatement();
			set = statement.executeQuery("select * from store where store_name = '"+refDataInfo.getStoreName()+"'");
			if(set.next()){
				refData = true;
			}
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}

		return refData;
	}
	
	public RefDataInfo getRefDataInfo(int store_id) throws SQLException {
		RefDataInfo refData = new RefDataInfo();
		ResultSet set = null;
		Statement statement = null;
		Connection con = null;
		try {
			System.out.println("UserDataInfo getStoreId "+store_id);
			con = getConnection();
			statement = con.createStatement();
			set = statement.executeQuery("select o.store_id, o.store_name,o.store_address,o.store_logo, ref.gst_rate, ref.nsf_fee from store o, store_ref_data ref where o.store_id = ref.store_id and o.store_id="+store_id);
			refData.setStoreId(store_id);
			while(set.next()){
				refData.setStoreId(set.getInt("store_id"));
				refData.setGstRate(set.getDouble("gst_rate"));
				refData.setNsfFee(set.getDouble("nsf_fee"));
				refData.setStoreName(set.getString("store_name"));
				refData.setStoreAddress(set.getString("store_address"));
				refData.setStoreLogo(set.getString("store_logo"));
				
			}
			
			close(set);
			
			set = statement.executeQuery("select * from  charge_fee cf where cf.store_id ="+store_id);
			while(set.next()){
				ChargeFeeInfo fee = new ChargeFeeInfo();
				fee.setFromAmount(set.getDouble("from_amount"));
				fee.setToAmount(set.getDouble("to_amount"));
				fee.setChargeFee(set.getDouble("fee"));
				fee.setCounter(set.getInt("counter"));
				//System.out.println("addin g ChargeFeeInfo  "+fee.getChargeFee());
				refData.getChargeFeeList().add(fee);
			}
			System.out.println("returning refData  "+refData.getStoreName());
			
		}
		finally {
			close(set);
			close(statement);
			close(con);
		}

		return refData;
	}
	
	
	public RefDataInfo updateRefDataInfo(UserDataInfo userDataInfo) throws SQLException {
		RefDataInfo refData = new RefDataInfo();
		Connection con = null;
		PreparedStatement statement = null;

		try {
			System.out.println("UserDataInfo getStoreId "+userDataInfo.getStoreId());

			con = getConnection();
			String query = "update store st set st.store_address=?, st.store_logo=?, st.store_name=? where st.store_id=? ";
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,userDataInfo.getRefDataInfo().getStoreAddress());
			statement.setString(i++,userDataInfo.getRefDataInfo().getStoreLogo());
			statement.setString(i++,userDataInfo.getRefDataInfo().getStoreName());
			statement.setInt(i++,userDataInfo.getRefDataInfo().getStoreId());
				
			int count  = statement.executeUpdate();
			System.out.println("store updated "+count);
			
			statement.close();
			query = "update store_ref_data set nsf_fee=?, gst_rate=? where store_id=? ";
			i=1;
			statement = con.prepareStatement(query);
			statement.setDouble(i++,userDataInfo.getRefDataInfo().getNsfFee());
			statement.setDouble(i++,userDataInfo.getRefDataInfo().getGstRate());
			statement.setInt(i++,userDataInfo.getRefDataInfo().getStoreId());
			count  = statement.executeUpdate();
			System.out.println("store_ref_data updated "+userDataInfo.getRefDataInfo().getStoreName());
			statement.close();
			
			// updating 
			
			if (userDataInfo!= null && userDataInfo.getRefDataInfo().getChargeFeeList() != null && userDataInfo.getRefDataInfo().getChargeFeeList().size() > 0 )
				for (ChargeFeeInfo chargeFeeInfo: userDataInfo.getRefDataInfo().getChargeFeeList())
				{
					System.out.println("updating ChargeFee: "+chargeFeeInfo.getChargeFee());
					System.out.println("updating getIndex: "+chargeFeeInfo.getCounter());
					query = "update charge_fee set from_amount=?, to_amount=? , fee=? where store_id=? and counter=? ";
					i=1;
					statement = con.prepareStatement(query);
					statement.setDouble(i++,chargeFeeInfo.getFromAmount());
					statement.setDouble(i++,chargeFeeInfo.getToAmount());
					statement.setDouble(i++,chargeFeeInfo.getChargeFee());
					statement.setInt(i++,userDataInfo.getStoreId());
					statement.setInt(i++,chargeFeeInfo.getCounter());
					System.out.println("executing : "+query);
						count  = statement.executeUpdate();
					System.out.println("charge_fee updated "+chargeFeeInfo.getChargeFee());
					statement.close();
				}
			
			

			
			
		}
		finally {
				close(statement);
				close(con);
		}

		return refData;
	}
	
	
	public RefDataInfo addRefDataInfo(UserDataInfo userDataInfo) throws SQLException {
		RefDataInfo refData = new RefDataInfo();
		Connection con = null;
		PreparedStatement statement = null;

		try {
			int store_id=0;
			System.out.println("addRefDataInfo getStoreId "+userDataInfo.getStoreId());

			con = getConnection();
			String query = "insert into store (store_address,store_logo,store_name,email, phone, contact_name) VALUES (?,?,?,?,?,? ) ";
			System.out.println("executing : "+query);
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,userDataInfo.getRefDataInfo().getStoreAddress());
			statement.setString(i++,userDataInfo.getRefDataInfo().getStoreLogo());
			statement.setString(i++,userDataInfo.getRefDataInfo().getStoreName());
			statement.setString(i++,userDataInfo.getRefDataInfo().getEmail());
			statement.setString(i++,userDataInfo.getRefDataInfo().getPhone());
			statement.setString(i++,userDataInfo.getRefDataInfo().getContactName());
			
			int count  = statement.executeUpdate();
			System.out.println("store added "+count);
			statement.close();
			
			String query2 = "select store_id from store where  store_address=? and store_name=? ";
			System.out.println("executing : "+query2);
			i=1;
			PreparedStatement st = con.prepareStatement(query2);
			st.setString(i++,userDataInfo.getRefDataInfo().getStoreAddress());
			st.setString(i++,userDataInfo.getRefDataInfo().getStoreName());
			
			ResultSet result = st.executeQuery();
			if (result.next())
			{
				store_id = result.getInt("store_id");
			}
			System.out.println("store added "+store_id);
			
			
			userDataInfo.setStoreId(store_id);
			userDataInfo.getRefDataInfo().setStoreId(store_id);
			
			query = "insert into store_ref_data (store_id,nsf_fee, gst_rate) VALUES (?,?,?) ";
			System.out.println("executing : "+query);
			i=1;
			statement = con.prepareStatement(query);
			statement.setInt(i++,userDataInfo.getStoreId());
			statement.setDouble(i++,userDataInfo.getRefDataInfo().getNsfFee());
			statement.setDouble(i++,userDataInfo.getRefDataInfo().getGstRate());
			count  = statement.executeUpdate();
			System.out.println("store_ref_data added "+userDataInfo.getRefDataInfo().getStoreName());
			statement.close();
			
			// Adding Charge Fee 
			
			for (int index=0; index< 4; index++)
				{
					query = "insert into charge_fee (store_id, from_amount, to_amount, fee ) VALUES (?,?,?,?) ";
					i=1;
					statement = con.prepareStatement(query);
					statement.setInt(i++,userDataInfo.getStoreId());
					//statement.setInt(i++,chargeFeeInfo.getCounter());
					statement.setDouble(i++,0*index);
					statement.setDouble(i++,(1*index*100));
					statement.setDouble(i++,(i*10));
					System.out.println("executing : "+query);
						count  = statement.executeUpdate();
					System.out.println("charge_fee added "+index);
					statement.close();
				}
		}
		finally {
				close(statement);
				close(con);
		}

		return refData;
	}
	
	
	public void addBankDateInfo(BankDateInfo bankDateInfo) throws SQLException {
		PreparedStatement statement = null;
		Connection con = null;
		try {
		String SQL_INSERT_INTO_COMPANY ="INSERT INTO bank_withdraw_date (bank_code,date_code, store_id) VALUES (?,? ) ";
		con = getConnection();
			int i=1;
			statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
			statement.setString(i++,bankDateInfo.getBankCode());
			statement.setString(i++,bankDateInfo.getDateCode());
			statement.setInt(i++,bankDateInfo.getStore_id());
			
			
			
			int count  = statement.executeUpdate();
			System.out.println("bank_withdraw_date added successfull "+count);

		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
	}
	
	 

//	public ArrayList<ChargeForm> checkChargeExist(String companyCode, String monthYear,int store_id) throws SQLException {
//		PreparedStatement statement = null;
//		Connection con = null;
//		ChargeForm existingCharge = null;
//		try {
//
//			System.out.println("checkChargeExist.caled for  companyCode "+companyCode);
//			System.out.println("checkChargeExist.  caled for  monthYear "+monthYear);
//			String SQL_SELECT_CHARGE =	"Select * from charge where company_code=? and charge_month=? and store_id=?";
//			if (monthYear == null )
//				 SQL_SELECT_CHARGE =	"Select * from charge where company_code=? and store_id=?";
//					
//			con = getConnection();
//			int i=1;
//			statement = con.prepareStatement(SQL_SELECT_CHARGE);
//			statement.setString(i++,companyCode);
//			statement.setString(i++,monthYear);
//			statement.setInt(i++,store_id);
//			ResultSet set = statement.executeQuery();
//			if(set.next()){
//				
//				existingCharge = populateChargeForm(set);
//			}
//
//		}catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//				finally {
//
//			close(statement);
//			close(con);
//		}
//	return existingCharge;
//	}

	public ArrayList<ChargeForm> checkChargeExist(String companyCode, String monthYear,int store_id) {
		PreparedStatement statement = null;
		Connection con = null;
		ChargeForm existingCharge = null;
		ArrayList<ChargeForm> existingCharges = new ArrayList<ChargeForm>();
		try {

			System.out.println("checkChargeExist.caled for  companyCode "+companyCode);
			String 	query =	    "Select * from charge WHERE company_code=? and store_id=? order by charge_month desc";
			
			if (monthYear != null )
				query =	"Select * from charge where company_code=? and store_id=? and charge_month=? order by charge_month desc";
			
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,companyCode);
			statement.setInt(i++,store_id);
			if (monthYear != null )
				statement.setString(i++,monthYear);
			ResultSet set = statement.executeQuery();
			while(set.next()){
				//System.out.println("checkChargeExist "+companyCode);
				existingCharge = populateChargeForm(set);
				existingCharges.add(existingCharge);
			}

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(statement);
			close(con);
		}
	return existingCharges;
	}
	
	
	public ArrayList<ChargeForm> checkChargeExistByMonthYear(String monthYear,int store_id, String withDrawDate) {
		PreparedStatement statement = null;
		Connection con = null;
		ChargeForm existingCharge = null;
		ArrayList<ChargeForm> existingCharges = new ArrayList<ChargeForm>();
		try {

			System.out.println("checkChargeExistByMonthYear.caled for  monthYear "+monthYear);
			String 	query =	"Select * from charge where store_id=? and charge_month=? and withdraw_date=? order by charge_month desc";
			
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setInt(i++,store_id);
			statement.setString(i++,monthYear);
			statement.setString(i++,withDrawDate);
			ResultSet set = statement.executeQuery();
			while(set.next()){
				existingCharge = populateChargeForm(set);
				existingCharges.add(existingCharge);
			}

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(statement);
			close(con);
		}
	return existingCharges;
	}
	
	
	
	
	public ChargeForm checkMasterChargeExist(String companyCode,int store_id) {
		PreparedStatement statement = null;
		Connection con = null;
		ChargeForm existingCharge = null;
		try {
			System.out.println("checkMasterChargeExist caled for  companyCode "+companyCode);
			String SQL_SELECT_CHARGE =	"Select * from charge where company_code=? and store_id=? and charge_month='"+JspConstants.MASTER_CHARGE+"'";
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(SQL_SELECT_CHARGE);
			statement.setString(i++,companyCode);
			statement.setInt(i++,store_id);
			ResultSet set = statement.executeQuery();
			if (set.next()){
				existingCharge = populateChargeForm(set);
				System.out.println(" Master Charge Exist for companyCode  "+existingCharge.getCompanyCode());
			}

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(statement);
			close(con);
		}
	return existingCharge;
	}
	
	
	

	
	
	public ArrayList<YearlyClientForm> checkYearlyClientInfoExist(String companyCode,int store_id, String year) {
		PreparedStatement statement = null;
		Connection con = null;
		ArrayList<YearlyClientForm> clientInfo = new ArrayList<YearlyClientForm>();
		String yearInfo="";
		try {
			System.out.println("checkYearlyClientInfoExist caled for  companyCode "+companyCode);
			System.out.println("checkYearlyClientInfoExist caled for  store_id "+store_id);
			if (year != null)  
				yearInfo = "AND year= '"+year+"'";
			
			String SQL_SELECT_CHARGE =	" Select * from yearly_client_info where company_code=? and store_id=? "+yearInfo +" order by YEAR ";
			System.out.println("checkYearlyClientInfoExist SQL "+SQL_SELECT_CHARGE);
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(SQL_SELECT_CHARGE);
			statement.setString(i++,companyCode);
			statement.setInt(i++,store_id);
			ResultSet set = statement.executeQuery();
			while (set.next()){
				YearlyClientForm info   = populateClientInfoForm(set);
				System.out.println("checkYearlyClientInfoExist YearlyClientForm found getBalanceDue "+info.getBalanceDue());
				clientInfo.add(info);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(statement);
			close(con);
		}
	return clientInfo;
	}
	
	
	public ArrayList<YearlyClientForm> getYearlyMasterClients(int store_id) {
		PreparedStatement statement = null;
		Connection con = null;
		ArrayList<YearlyClientForm> clientInfo = new ArrayList<YearlyClientForm>();
		try {
			System.out.println("getYearlyMasterClients caled for  store_id "+store_id);
			String SQL_SELECT_CHARGE =	" Select * from yearly_client_info where store_id=? AND year= 'MASTER'";
			System.out.println("getYearlyMasterClients SQL "+SQL_SELECT_CHARGE);
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(SQL_SELECT_CHARGE);
			statement.setInt(i++,store_id);
			ResultSet set = statement.executeQuery();
			while (set.next()){
				clientInfo.add(populateClientInfoForm(set));
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(statement);
			close(con);
		}
	return clientInfo;
	}
	
	
	public YearlyClientForm checkYearlyMasterClientInfoExist(String companyCode,int store_id) {
		PreparedStatement statement = null;
		Connection con = null;
		YearlyClientForm clientInfo = null;
		String yearInfo="AND year= 'MASTER'";
		try {
			System.out.println("checkYearlyMasterClientInfoExist caled for  companyCode "+companyCode);
			String SQL_SELECT_CHARGE =	" Select * from yearly_client_info where company_code=? and store_id=? "+yearInfo;
			System.out.println("checkYearlyMasterClientInfoExist SQL "+SQL_SELECT_CHARGE);
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(SQL_SELECT_CHARGE);
			statement.setString(i++,companyCode);
			statement.setInt(i++,store_id);
			ResultSet set = statement.executeQuery();
			if (set.next()){
				clientInfo   = populateClientInfoForm(set);
				System.out.println("checkYearlyMasterClientInfoExist YearlyClientForm found getBalanceDue "+clientInfo.getBalanceDue());
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(statement);
			close(con);
		}
	return clientInfo;
	}
	

	
	public ArrayList<PayrollForm> checkPayrollExist(String companyCode, String monthYear, int store_id)  {
		PreparedStatement statement = null;
		Connection con = null;
		ArrayList<PayrollForm> existingPayrolls = new ArrayList<PayrollForm>();
		try {
			System.out.println("checkPayrollExist.caled for  companyCode "+companyCode);
			System.out.println("checkPayrollExist.  caled for  monthYear "+monthYear);
			String query  =	"Select * from payroll where company_code=?  and store_id=? " ;
			if (monthYear!= null) query = query+ " AND payroll_month=? ";
			
			query = query+ " order by payroll_month desc ";
			
			
			
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,companyCode);
			statement.setInt(i++,store_id);
			if (monthYear!= null)
				statement.setString(i++,monthYear);
			
			System.out.println("query "+query);
			ResultSet set = statement.executeQuery();
			while (set.next()){
				existingPayrolls.add(populatePayrollForm(set));
			}

		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
	return existingPayrolls;
	}

	
	public ArrayList<PayrollForm> checkPayrollExistByMonthYear(String monthYear, int store_id)  {
		PreparedStatement statement = null;
		Connection con = null;
		ArrayList<PayrollForm> existingPayrolls = new ArrayList<PayrollForm>();
		try {
			System.out.println("checkPayrollExist.  caled for  monthYear "+monthYear);
			String query  =	"Select * from payroll where store_id=?  and payroll_month=?  order by payroll_month desc";
			
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setInt(i++,store_id);
			statement.setString(i++,monthYear);
			System.out.println("query "+query);
			ResultSet set = statement.executeQuery();
			while (set.next()){
				existingPayrolls.add(populatePayrollForm(set));
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {
			close(statement);
			close(con);
		}
	return existingPayrolls;
	}
	
	public void deletePayrollByMonthYear(String monthYear, int store_id, String companyCode)  {
		PreparedStatement statement = null;
		Connection con = null;
		try {
			System.out.println("deletePayrollByMonthYear.  caled for  monthYear "+monthYear+" companyCode "+companyCode);
			String query  =	"delete from payroll where store_id=?  and payroll_month=? and company_code=? ";
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setInt(i++,store_id);
			statement.setString(i++,monthYear);
			statement.setString(i++,companyCode);
			int count  = statement.executeUpdate();
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {
			close(statement);
			close(con);
		}
	}
	
	public void deletePayrollByMonthYear(String monthYear, int store_id)  {
		PreparedStatement statement = null;
		Connection con = null;
		try {
			System.out.println("deletePayrollByMonthYear.  caled for  monthYear "+monthYear);
			String query  =	"delete from payroll where store_id=?  and payroll_month=?  ";
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setInt(i++,store_id);
			statement.setString(i++,monthYear);
			int count  = statement.executeUpdate();
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {
			close(statement);
			close(con);
		}
	}
	
	public PayrollForm checkMasterPayrollExist(String companyCode, int store_id) {
		PreparedStatement statement = null;
		Connection con = null;
		PayrollForm existingPayroll = null;
		
		
		try {

			System.out.println("checkMasterPayrollExist.caled for  companyCode "+companyCode);
			String query  =	"Select * from payroll where company_code=? and store_id=? and payroll_month='"+JspConstants.MASTER_PAYROLL+"'" ;
		
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,companyCode);
			statement.setInt(i++, store_id );
			System.out.println("query "+query);
			ResultSet set = statement.executeQuery();
			if (set.next()){
				existingPayroll = populatePayrollForm(set);
			}

		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
	return existingPayroll;
	}
	
	

	public void addPayroll(PayrollForm payroll) throws SQLException {
		PreparedStatement statement = null;
		Statement st = null;
		Connection con = null;
		try {
			System.out.println("******* addPayroll FOR  PayrollMonth "+payroll.getPayrollMonth());
			if (MASTER_PAYROLL.equals(payroll.getPayrollMonth()))
			{
			if (payroll.getPayrollFrequency() != null  && "M".equals(payroll.getPayrollFrequency()))
			{		payroll.setSentToCRA("Y");			}
			else{	payroll.setSentToCRA("N");			}
			}	
			con = getConnection();
			System.out.println("Adding new payroll ");
			String query =	"INSERT   INTO payroll (company_code, store_id, payroll_month,   number_of_employees ,gross_payroll, government_remittance , bank_charge ,  " +
				" total_amount ,  payroll_type , nsf , revised_amount,previous_balance, active, comment,payroll_frequency, sent_to_cra  ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ,?) ";
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,payroll.getCompanyCode());
			statement.setInt(i++,payroll.getStore_id());
			statement.setString(i++,payroll.getPayrollMonth());
			statement.setInt(i++,payroll.getNumberOfemployees());
			statement.setDouble(i++,payroll.getGrossPayroll());
			statement.setDouble(i++,payroll.getRemittanceAmount());
			statement.setDouble(i++,payroll.getBankCharge());
			statement.setDouble(i++,payroll.getTotalAmount());
			statement.setString(i++,payroll.getPayrollType());
			statement.setString(i++,payroll.getNsf());
			statement.setDouble(i++,payroll.getRevisedAmount());
			statement.setDouble(i++,payroll.getPreviousBalance());
			statement.setString(i++,payroll.getActive());
			statement.setString(i++,payroll.getComment());
			statement.setString(i++,payroll.getPayrollFrequency());
			statement.setString(i++,payroll.getSentToCRA());
			int count  = statement.executeUpdate();
			System.out.println("company added successfull "+count);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
	}



	public boolean checkAccountLocked(ChargeForm charge) {
		PreparedStatement statement = null;
		Connection con = null;
		boolean locked= false;
		try {

			System.out.println("checkAccountLocked caled for  companyCode "+charge.getCompanyCode());
			String 	query =	"Select * from charge where company_code=? and store_id=? and charge_month=? order by charge_month desc";
			
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,charge.getCompanyCode());
			statement.setInt(i++,charge.getStore_id());
			statement.setString(i++,charge.getMonthYear());
			
			ResultSet set = statement.executeQuery();
			if (set.next()){
				String result = set.getString("lock_account");
				if (result != null && result .equals(YES))
					locked = true;
				else
					locked = false;
			}

		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			close(statement);
			close(con);
		}
	return locked;
	}
	
	
	public void addCharge(ChargeForm charge) throws SQLException {
		PreparedStatement statement = null;
		Connection con = null;
		try {
			boolean accountLocked = checkAccountLocked(charge);
			if (accountLocked) 
				{
					System.out.println("addCharge return .. Account is locked and system cannot overwrite the acocunt charge for Company: "+charge.getCompanyCode()+" MonthYear: "+charge.getMonthYear());
					return;
				}
			con = getConnection();
			String query =	"INSERT   INTO charge (company_code,store_id,  charge_month,   net_amount , gst , total_amount ,  " +
				" withdraw_date ,bank_account_number, comment , previous_amount, instalment, "+
				" previous_amount_balance , total_withdraw_amount, active, nsf, pre_amount_include_gst,pre_charges,lock_account ) " +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			System.out.println("query "+query);
			int i=1;
				statement = con.prepareStatement(query);
				statement.setString(i++,charge.getCompanyCode());
				statement.setInt(i++,charge.getStore_id());
				statement.setString(i++,charge.getMonthYear());
				statement.setDouble(i++,charge.getNetAmount());
				statement.setDouble(i++,charge.getGst());
				statement.setDouble(i++,charge.getTotal());
				statement.setString(i++, charge.getWithdrawDate());
				statement.setString(i++,charge.getAccountNumber());
				statement.setString(i++,charge.getComments());
				statement.setDouble(i++,charge.getPreviousAmount());
				statement.setDouble(i++,charge.getPreviousAmountInstallment());
				statement.setDouble(i++,charge.getPreviousAmountBalance());
				statement.setDouble(i++,charge.getTotalWithdrawAmount());
				statement.setString(i++,charge.getActive());
				statement.setString(i++,charge.getNsf());
				statement.setString(i++,charge.getPreviousAmountGstIncluded());
				statement.setDouble(i++,charge.getPreviousCharges());
				statement.setString(i++,charge.getLockAccount());
				
				int count  = statement.executeUpdate();
				System.out.println("charge added successfull "+count);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
	}
	
	public void updatePayroll(PayrollForm payroll) throws SQLException {
		PreparedStatement statement = null;
		Connection con = null;
		
		System.out.println("******* updatePayroll FOR  "+payroll.toString());
		try {
			if (MASTER_PAYROLL.equals(payroll.getPayrollMonth()))
			{
				if (payroll.getPayrollFrequency() != null  && "M".equals(payroll.getPayrollFrequency()) )
				{				payroll.setSentToCRA("Y");			}
				else		{	payroll.setSentToCRA("N");			}
			}
			
		String SQL_INSERT_INTO_COMPANY =	"UPDATE payroll set  " +
				"	gross_payroll=?, " +
				"	number_of_employees = ? , " +
				"	government_remittance =? ," +
				" 	bank_charge = ? ,  " +
				" 	total_amount = ? ,  " +
				"	payroll_type = ?, " +
				"	nsf = ?, " +
				"	revised_amount =?," +
				"	previous_balance =?," +
				"  comment = ? , active=? , payroll_frequency=? , sent_to_cra= ? where company_code = ? and  payroll_month = ? and store_id = ?";
		con = getConnection();
			int i=1;
			statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
			statement.setDouble(i++,payroll.getGrossPayroll());
			statement.setInt(i++,payroll.getNumberOfemployees());
			statement.setDouble(i++,payroll.getRemittanceAmount());
			statement.setDouble(i++,payroll.getBankCharge());
			statement.setDouble(i++,payroll.getTotalAmount());
			statement.setString(i++,payroll.getPayrollType());
			statement.setString(i++,payroll.getNsf());
			statement.setDouble(i++,payroll.getRevisedAmount());
			statement.setDouble(i++,payroll.getPreviousBalance());
			statement.setString(i++,payroll.getComment());
			statement.setString(i++,payroll.getActive());
			statement.setString(i++,payroll.getPayrollFrequency());
			statement.setString(i++,payroll.getSentToCRA());
			statement.setString(i++,payroll.getCompanyCode());
			statement.setString(i++,payroll.getPayrollMonth());
			statement.setInt(i++,payroll.getStore_id());
			int count  = statement.executeUpdate();
			System.out.println(" payroll.getPayrollMonth() "+payroll.getPayrollMonth());
			System.out.println("payroll Updated successfull "+count);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
	}

	public boolean updateFirstPayroll(PayrollForm payroll) throws SQLException {
		PreparedStatement statement = null;
		boolean payrollUpdated=false;
		Connection con = null;
		try {
			System.out.println("updateFirstPayroll getPayrollMonth "+payroll.getPayrollMonth());
			if (MASTER_PAYROLL.equals(payroll.getPayrollMonth()))
			{
				System.out.println("update SentToCRA FOR "+payroll.getPayrollMonth());
				if (payroll.getPayrollFrequency() != null  && "M".equals(payroll.getPayrollFrequency()) )
				{				payroll.setSentToCRA("Y");			}
				else		{	payroll.setSentToCRA("N");			}
			}
			System.out.println("updateFirstPayroll getPayrollMonth "+payroll.getSentToCRA());	
		String query =	"UPDATE payroll set  " +
				"	gross_payroll=?, " +
				"	number_of_employees = ? , " +
				"	government_remittance =? ," +
				" 	bank_charge = ? ,  " +
				" 	total_amount = ? ,  " +
				"	payroll_type = ?, " +
				"	nsf = ?, " +
				"	revised_amount =?," +
				"	previous_balance =?, active =?," +
				"  comment = ? , payroll_frequency = ? , sent_to_cra=? where company_code = ? and store_id = ?  and payroll_month=?  ";
		con = getConnection();
			int i=1;
			System.out.println(query);
			statement = con.prepareStatement(query);

			statement.setDouble(i++,payroll.getGrossPayroll());
			statement.setInt(i++,payroll.getNumberOfemployees());
			statement.setDouble(i++,payroll.getRemittanceAmount());
			statement.setDouble(i++,payroll.getBankCharge());
			statement.setDouble(i++,payroll.getTotalAmount());
			statement.setString(i++,payroll.getPayrollType());
			statement.setString(i++,payroll.getNsf());
			statement.setDouble(i++,payroll.getRevisedAmount());
			statement.setDouble(i++,payroll.getPreviousBalance());
			statement.setString(i++,payroll.getActive());
			statement.setString(i++,payroll.getComment());
			statement.setString(i++,payroll.getPayrollFrequency());
			statement.setString(i++,payroll.getSentToCRA());
			statement.setString(i++,payroll.getCompanyCode());
			statement.setInt(i++,payroll.getStore_id());
			statement.setString(i++,payroll.getPayrollMonth());
			
			int count  = statement.executeUpdate();
			if (count > 0 ) 
					payrollUpdated=  true;
			System.out.println("updateFirstPayroll successfull "+count);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
		return payrollUpdated;		
	}
	
	public boolean updateMasterCompany(CompanyForm company) throws SQLException {
		PreparedStatement statement = null;
		boolean payrollUpdated=false;
		Connection con = null;
		try {
			System.out.println("updateMasterCompany method "+company.getCompanyName());
		String query =	"UPDATE company "+
			" SET  company_name =?, bn_number = ?, "+
			" address1 = ?, "+
			" address2 =?,"+
			" city = ?, "+
			" province = ?, "+ 
			" postal_code =?, "+
			" comment = ?,  "+
			" client_type  = ?,  "+
			" client_status_active  = ?  "+
			" WHERE store_id = ? "+
			" and company_code = ? ";
		con = getConnection();
			int i=1;
			System.out.println(query);
			System.out.println("Updating company with new Name "+company.getCompanyName());
			statement = con.prepareStatement(query);

			statement.setString(i++,company.getCompanyName());
			statement.setString(i++,company.getBnNumber());
			statement.setString(i++,company.getAddress1());
			statement.setString(i++,company.getAddress2());
			statement.setString(i++,company.getCity());
			statement.setString(i++,company.getProvince());
			statement.setString(i++,company.getPostalCode());
			statement.setString(i++,company.getComment());
			statement.setString(i++,company.getClientType());
			statement.setString(i++,company.getClientStatusActive());
			statement.setInt(i++,company.getStore_id());
			statement.setString(i++,company.getCompanyCode());
			int count  = statement.executeUpdate();
			if (count > 0 ) 
					payrollUpdated=  true;
			System.out.println("company Updated successfull "+count);
			System.out.println("company Updated successfull "+count);
			updateBankAccountNumber(con,company.getBankAccNumber(), company.getCompanyCode(),company.getStore_id());
			if (company.getClientType() != null && company.getClientType().equals("Y"))
			{
				
				System.out.println("**** company.getYearlyClientInfo().setTotalCharges "+company.getYearlyClientInfo().getCurrentCharges());
				System.out.println("**** company.getYearlyClientInfo().getOtherCharge "+company.getYearlyClientInfo().getOtherCharge());
				System.out.println("**** company.getYearlyClientInfo().getOtherCharge1 "+company.getYearlyClientInfo().getOtherCharge1());
				System.out.println("**** company.getYearlyClientInfo().getOtherCharge2 "+company.getYearlyClientInfo().getOtherCharge2());
				
		
				company.getYearlyClientInfo().setYear(company.getYearlyClientInfo().getYear());	
				
				ArrayList<YearlyClientForm> clientInfos= checkYearlyClientInfoExist(company.getCompanyCode(),company.getStore_id(),company.getYearlyClientInfo().getYear());
						
				if (clientInfos != null && clientInfos.size() > 0 )
				{
					company.getYearlyClientInfo().setGstRate(company.getGstRate());
					updateYearlyClientInfo(company.getYearlyClientInfo());	
				}
				else
				{
					YearlyClientForm yearlyClientInfo =  company.getYearlyClientInfo();
					yearlyClientInfo.setStore_id(company.getStore_id());
					yearlyClientInfo.setCompanyCode(company.getCompanyCode());
					yearlyClientInfo.setGstRate(company.getGstRate());
					insertYearlyClientInfo(yearlyClientInfo);
					
//					 add Base year automatically for first time
					if (MASTER.equals(yearlyClientInfo.getYear()))
					{
					System.out.println("*****************  Added MASTER yearlyClientInfo Check base year "+yearlyClientInfo.getBaseYear());
					ArrayList<YearlyClientForm>  baseCharges = checkYearlyClientInfoExist(yearlyClientInfo.getCompanyCode(),yearlyClientInfo.getStore_id(),""+yearlyClientInfo.getBaseYear());
					if (baseCharges != null && baseCharges.size()>0)
					{
						YearlyClientForm  baseCharge = baseCharges.get(0);
						System.out.println("*****************  Adding Base Yearly charges found ... donot add "+yearlyClientInfo.getBaseYear());
					}
					else
					{
						System.out.println("*****************  Base Yearly charges NOT found ... add new for "+yearlyClientInfo.getBaseYear());
						YearlyClientForm baseYearlyClientInfo =  yearlyClientInfo;
						baseYearlyClientInfo.setYear(""+baseYearlyClientInfo.getBaseYear());
						yearlyClientInfo.setGstRate(company.getGstRate());
						yearlyClientInfo.setPaymentInfoExist("Y");
						yearlyClientInfo.setTaxReturnInfoExist("Y");
						insertYearlyClientInfo(baseYearlyClientInfo);
						yearlyClientInfo.setYear(MASTER);
					}
					}	
				}
			}

		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
		return payrollUpdated;		
	}
	

	
	
	public void updateYearlyClientInfo(YearlyClientForm yearlyClientInfo ) {
		Connection con = null;
		PreparedStatement statement = null;
		
		try {
			con = getConnection();
			
			System.out.println("updateYearlyClientInfo Store_id "+yearlyClientInfo.getStore_id());
			double currentCharges= yearlyClientInfo.getCurrentCharges();
			double paymentReceived = yearlyClientInfo.getPaymentReceived();
			if (yearlyClientInfo.getYear() != null && "MASTER".equals(yearlyClientInfo.getYear()))
			{
				System.out.println("insertYearlyClientInfo Adding MASTER yearlyClientInfo ");
				yearlyClientInfo.setPaymentReceived(0);
				yearlyClientInfo.setPaymentReceivedDate(null);
				yearlyClientInfo.setBalanceDue(0);
				yearlyClientInfo.setPreBalance(0);
				yearlyClientInfo.setTaxCompleted(NO);
				yearlyClientInfo.setTaxMailed(NO);
				yearlyClientInfo.setTaxFiled(NO);
				yearlyClientInfo.setTaxFiledDate(null);
				yearlyClientInfo.setCurrentCharges(0);
			}
			else
			{
				yearlyClientInfo.setPreBalance(getPreviousYearBalance(yearlyClientInfo));
				
			}
			
			yearlyClientInfo.setCurrentCharges(yearlyClientInfo.getCorpCharge()+yearlyClientInfo.getGstCharge()+
					yearlyClientInfo.getPayrollCharge()+yearlyClientInfo.getOtherCharge1()+yearlyClientInfo.getOtherCharge2());
			//yearlyClientInfo.setTotalCharges(yearlyClientInfo.getCurrentCharges()+yearlyClientInfo.getOtherCharge());
			yearlyClientInfo.setTotalCharges(yearlyClientInfo.getCurrentCharges()+yearlyClientInfo.getPreBalance()+yearlyClientInfo.getOtherCharge());

		if (yearlyClientInfo.getGstIncluded()!= null && yearlyClientInfo.getGstIncluded().equals("N"))
			{
				double currentWithGst = yearlyClientInfo.getCurrentCharges()* yearlyClientInfo.getGstRate()/100;
				yearlyClientInfo.setCurrentCharges(yearlyClientInfo.getCurrentCharges()+currentWithGst);
				
				double totalWithGst = yearlyClientInfo.getTotalCharges()* yearlyClientInfo.getGstRate()/100;
				yearlyClientInfo.setTotalCharges(yearlyClientInfo.getTotalCharges()+totalWithGst+yearlyClientInfo.getPreBalance());
			}
			yearlyClientInfo.setBalanceDue((yearlyClientInfo.getTotalCharges()) - (yearlyClientInfo.getDiscount()+paymentReceived));
			
			
			
			System.out.println("updateYearlyClientInfo Store_id "+yearlyClientInfo.getStore_id());
			String SQL_INSERT_INTO_COMPANY =	"UPDATE yearly_client_info set  " +
				" 	year_end = ?, payment_received_amount=?,balance_due=?, " +
				" 	tax_prepared_by=?,payment_comments=?,comments=?,assigned_to=?,  last_updated_by=?,  " +
				" payment_received_date =? , corp_charges = ?, gst_charges = ?, payroll_charges = ?,other_charge1_name  = ?, other_charge1_value  = ?, " +
				" other_charge2_name  = ?, other_charge2_value  = ?, other_charge = ?,gst_included = ? , " +
				"  charge_comments  = ?, total_charges  = ? , pre_balance=? , discount= ?, base_year=?, active=? , account_lock=?, current_charges=?, payment_received_by=?,  " +
				" tax_completed_date =?, tax_filed_date=? , tax_completed=?, tax_filed=?  "+
				" where company_code = ? and store_id=? and year=? ";
			int i=1;
			System.out.println("SQL_INSERT_INTO_COMPANY Store_id "+SQL_INSERT_INTO_COMPANY);
			statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
			statement.setString(i++,yearlyClientInfo.getYearEnd());
			statement.setDouble(i++,yearlyClientInfo.getPaymentReceived());
			statement.setDouble(i++,yearlyClientInfo.getBalanceDue());
			statement.setString(i++,yearlyClientInfo.getTaxPreparedBy());
			statement.setString(i++,yearlyClientInfo.getPaymentComemnts());
			statement.setString(i++,yearlyClientInfo.getComments());
			statement.setString(i++,yearlyClientInfo.getAssignedTo());
			statement.setString(i++,yearlyClientInfo.getLastUpdatedBy());
			statement.setString(i++,yearlyClientInfo.getPaymentReceivedDate());
			
			statement.setDouble(i++,yearlyClientInfo.getCorpCharge());
			statement.setDouble(i++,yearlyClientInfo.getGstCharge());
			statement.setDouble(i++,yearlyClientInfo.getPayrollCharge());
			statement.setString(i++,yearlyClientInfo.getOtherCharge1Name());
			statement.setDouble(i++,yearlyClientInfo.getOtherCharge1());
			statement.setString(i++,yearlyClientInfo.getOtherCharge2Name());
			statement.setDouble(i++,yearlyClientInfo.getOtherCharge2());
			statement.setDouble(i++,yearlyClientInfo.getOtherCharge());
			statement.setString(i++,yearlyClientInfo.getGstIncluded());
			statement.setString(i++,yearlyClientInfo.getChargeComments());
			statement.setDouble(i++,yearlyClientInfo.getTotalCharges());
			statement.setDouble(i++,yearlyClientInfo.getPreBalance());
			statement.setDouble(i++,yearlyClientInfo.getDiscount());
			statement.setInt(i++,yearlyClientInfo.getBaseYear());
			statement.setString(i++,yearlyClientInfo.getActive());
			statement.setString(i++,yearlyClientInfo.getLock());
			statement.setDouble(i++,yearlyClientInfo.getCurrentCharges());
			statement.setString(i++,yearlyClientInfo.getPaymentReceivedBy());
			statement.setString(i++,yearlyClientInfo.getTaxCompletedDate());
			statement.setString(i++,yearlyClientInfo.getTaxFiledDate());
			statement.setString(i++,yearlyClientInfo.getTaxCompleted());
			statement.setString(i++,yearlyClientInfo.getTaxFiled());
//			statement.setString(i++,yearlyClientInfo.getPaymentInfoExist());
//			statement.setString(i++,yearlyClientInfo.getTaxReturnInfoExist());
			
			statement.setString(i++,yearlyClientInfo.getCompanyCode());
			statement.setInt(i++,yearlyClientInfo.getStore_id());
			statement.setString(i++,yearlyClientInfo.getYear());
			
			
			int count  = statement.executeUpdate();
			System.out.println("updateYearlyClientInfo updated successfull "+count);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement);
			close(con);
		}
	}
	

	private double getPreviousYearBalance(YearlyClientForm yearlyClientInfo) {
		double preBalance = 0;
		String preYear = getPreviousYear(yearlyClientInfo.getYear());
		System.out.println("getting previous year Balance "+preYear+" for "+yearlyClientInfo.getCompanyCode());
		ArrayList<YearlyClientForm> previousCharges = checkYearlyClientInfoExist(yearlyClientInfo.getCompanyCode(),yearlyClientInfo.getStore_id(), preYear);
		if (previousCharges != null && previousCharges.size() > 0)
		{
			YearlyClientForm preClient = (YearlyClientForm)previousCharges.get(0);
			System.out.println("***** GOT previous year Balance "+preClient.getYear()+" for "+preClient.getBalanceDue());
			if (preClient != null) 
			{
				preBalance = preClient.getBalanceDue();
				System.out.println("$$$$$$$$$$ Balance Due is "+yearlyClientInfo.getBalanceDue()+" for "+yearlyClientInfo.getCompanyCode());
			}
			
		}
		return preBalance;
	}

	public void prepare() throws SQLException {

		Statement statement2 = null;
		Connection con = null;
		try {


		String SQL_CREATE_COMPANY = " CREATE CACHED  TABLE company  (company_name VARCHAR,  company_code VARCHAR, "+
			 " bn_number VARCHAR,  address1 VARCHAR, address2 VARCHAR,  city VARCHAR,  province VARCHAR, "+
			 " postal_code VARCHAR,  comment VARCHAR, client_type  VARCHAR, DATE_CREATED date)  ";


			String SQL_DROP_COMPANY = " DROP TABLE company  ";

		con = getConnection();
		statement2 = con.createStatement();
		try{
			statement2.executeUpdate(SQL_DROP_COMPANY);
			System.out.println("COMOANY Table dropped.. creating again ...");
		}catch (SQLException e)

		{
			e.printStackTrace();
		}
		statement2.executeUpdate(SQL_CREATE_COMPANY);
		System.out.println("COMOANY Table created successfully.....");


		String SQL_DROP_PAYROLL = " DROP TABLE payroll  ";
		String SQL_CREATE_PAYROLL = " CREATE CACHED TABLE payroll (company_code VARCHAR,  payroll_month VARCHAR, "+
		 " number_of_employees VARCHAR,  government_remittance double, bank_charge double,  total_amount double,  " +
		 " payroll_type varchar , nsf varchar, revised_amount VARCHAR, comment varchar ,  DATE_CREATED date)  ";
		try{
		statement2.executeUpdate(SQL_DROP_PAYROLL);
		System.out.println("payroll Table dropped .....");
		}catch (SQLException e)
		{	e.printStackTrace();
		}
		statement2.executeUpdate(SQL_CREATE_PAYROLL);
		System.out.println("payroll Table created successfully.....");


		String SQL_DROP_CHARGE = " DROP TABLE charge  ";
		String SQL_CREATE_CHARGE = " CREATE CACHED TABLE charge (company_code VARCHAR,  charge_month VARCHAR, "+
		 " net_amount double,  gst double, total_amount double,  withdraw_date date, bank_account_number VARCHAR, " +
		 " comment varchar , previous_amount double, instalment double, previous_amount_balance double,total_withdraw_amount double, DATE_CREATED date)  ";
		try{
		statement2.executeUpdate(SQL_DROP_CHARGE);
		System.out.println("charge Table dropped .....");
		}catch (SQLException e)
		{	e.printStackTrace();
		}
		statement2.executeUpdate(SQL_CREATE_CHARGE);
		System.out.println("charge Table created successfully.....");


		String SQL_DROP_TRUST = " DROP TABLE TRUST  ";
		String SQL_CREATE_TRUST = " CREATE CACHED TABLE TRUST (company_code VARCHAR, total_amount double, DESCRIPTION VARCHAR, " +
				"  DATE_HELD date, status VARCHAR, comment varchar,  DATE_COMPLETED date)  ";
		try{
		statement2.executeUpdate(SQL_DROP_TRUST);
		System.out.println("TRUST Table dropped .....");
		}catch (SQLException e)
		{	e.printStackTrace();
		}
		statement2.executeUpdate(SQL_CREATE_TRUST);
		System.out.println("TRUST Table created successfully.....");

		}catch (Exception e)
		{
			e.printStackTrace();
		}
				finally {

			close(statement2);
			close(con);
		}

	}

	
	
	
		public void addCompany(CompanyForm company) throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
			String SQL_INSERT_INTO_COMPANY =	"INSERT   INTO company (store_id, company_code,  company_name,   bn_number ,  " +
					" address1 , address2 ,  city ,  province , "+
					" postal_code ,  comment, client_type ) "+
					"   VALUES (?,?,?,?,?,?,?,?,?,?,? ) ";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
				statement.setInt(i++,company.getStore_id());
				statement.setString(i++,company.getCompanyCode());
				statement.setString(i++,company.getCompanyName());
				statement.setString(i++,company.getBnNumber());
				statement.setString(i++,company.getAddress1());
				statement.setString(i++,company.getAddress2());
				statement.setString(i++,company.getCity());
				statement.setString(i++,company.getProvince());
				statement.setString(i++,company.getPostalCode());
				statement.setString(i++,company.getComment());
				statement.setString(i++,company.getClientType());
				int count  = statement.executeUpdate();
				System.out.println("company added successfull "+count);
				
				if (company.getClientType() != null && company.getClientType().equals("Y"))
				{
		
					company.getYearlyClientInfo().setYear(company.getYearlyClientInfo().getYear());
					company.getYearlyClientInfo().setStore_id(company.getStore_id());
					company.getYearlyClientInfo().setCompanyCode(company.getCompanyCode());
					company.getYearlyClientInfo().setGstRate(company.getGstRate());
					insertYearlyClientInfo(company.getYearlyClientInfo());
					YearlyClientForm yearlyClientInfo =  company.getYearlyClientInfo();
					
					
//					 add Base year automatically for first time
					if (MASTER.equals(yearlyClientInfo.getYear()))
					{					
					System.out.println("*****************  Added MASTER yearlyClientInfo Check base year "+yearlyClientInfo.getBaseYear());
					ArrayList<YearlyClientForm>  baseCharges = checkYearlyClientInfoExist(yearlyClientInfo.getCompanyCode(),yearlyClientInfo.getStore_id(),""+yearlyClientInfo.getBaseYear());
					if (baseCharges != null && baseCharges.size()>0)
					{
						System.out.println("*****************  Adding Base Yearly charges found ... donot add "+yearlyClientInfo.getBaseYear());
					}
					else
					{
						System.out.println("*****************  Base Yearly charges NOT found ... add new for "+yearlyClientInfo.getBaseYear());
						YearlyClientForm baseYearlyClientInfo =  yearlyClientInfo;
						baseYearlyClientInfo.setYear(""+baseYearlyClientInfo.getBaseYear());
						baseYearlyClientInfo.setGstRate(company.getGstRate());
						baseYearlyClientInfo.setPaymentInfoExist("Y");
						baseYearlyClientInfo.setTaxReturnInfoExist("Y");
						insertYearlyClientInfo(baseYearlyClientInfo);
						yearlyClientInfo.setYear(MASTER);
					}
					}
					
					
					
					
					
					
					//			insertYearlyChargeInfo(company,con);
				}
				
				}		
				finally {
					close(statement);
					close(con);
			}
		}
		
		
		public void insertYearlyClientInfo(YearlyClientForm yearlyClientInfo) {
			PreparedStatement statement = null;
			Connection con = null;
			try {
				
				if (yearlyClientInfo.getYear() != null && "MASTER".equals(yearlyClientInfo.getYear()))
				{
					System.out.println("insertYearlyClientInfo Adding MASTER yearlyClientInfo ");
					yearlyClientInfo.setBalanceDue(0);
					yearlyClientInfo.setPaymentReceived(0);
					yearlyClientInfo.setPaymentReceivedDate(null);
					yearlyClientInfo.setTaxCompleted(NO);
					yearlyClientInfo.setTaxMailed(NO);
					yearlyClientInfo.setTaxFiled(NO);
					yearlyClientInfo.setTaxFiledDate(null);
					
				}else
				{
					yearlyClientInfo.setPreBalance(getPreviousYearBalance(yearlyClientInfo));
				}
					
				yearlyClientInfo.setCurrentCharges(yearlyClientInfo.getCorpCharge()+yearlyClientInfo.getGstCharge()+
						yearlyClientInfo.getPayrollCharge()+yearlyClientInfo.getOtherCharge1()+yearlyClientInfo.getOtherCharge2());
				yearlyClientInfo.setTotalCharges(yearlyClientInfo.getCurrentCharges()+yearlyClientInfo.getPreBalance()+yearlyClientInfo.getOtherCharge());

			if (yearlyClientInfo.getGstIncluded()!= null && yearlyClientInfo.getGstIncluded().equals("N"))
				{
					double currentWithGst = yearlyClientInfo.getCurrentCharges()* yearlyClientInfo.getGstRate()/100;
					yearlyClientInfo.setCurrentCharges(yearlyClientInfo.getCurrentCharges()+currentWithGst);
					
					double totalWithGst = yearlyClientInfo.getTotalCharges()* yearlyClientInfo.getGstRate()/100;
					yearlyClientInfo.setTotalCharges(yearlyClientInfo.getTotalCharges()+totalWithGst);
				}
				yearlyClientInfo.setBalanceDue((yearlyClientInfo.getTotalCharges()) - (yearlyClientInfo.getDiscount()+yearlyClientInfo.getPaymentReceived()));
					
				
				
				con = getConnection();
				System.out.println("insertYearlyClientInfo ");
				String SQL_INSERT_INTO_COMPANY =	"INSERT INTO yearly_client_info (  " +
					" store_id,company_code,year_end, tax_filed, tax_completed,tax_mailed,payment_received_amount,balance_due, " +
					" tax_prepared_by,payment_comments,comments,assigned_to,  last_updated_by, year, tax_completed_date,tax_filed_date," +
					" corp_charges , gst_charges , payroll_charges ,other_charge1_name  , other_charge1_value  , " +
				" other_charge2_name  , other_charge2_value  , other_charge, " +
				" gst_included  , charge_comments  , total_charges , payment_received_date, pre_balance, discount, base_year,active, " +
				" account_lock, current_charges,payment_received_by, payment_info_exist,tax_return_info) " +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ,?,? ,?,?,?,?,?,?,?  ) " ;

				int i=1;
				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
				statement.setInt(i++,yearlyClientInfo.getStore_id());
				statement.setString(i++,yearlyClientInfo.getCompanyCode());
				statement.setString(i++,yearlyClientInfo.getYearEnd());
				statement.setString(i++,yearlyClientInfo.getTaxFiled());
				statement.setString(i++,yearlyClientInfo.getTaxCompleted());
				statement.setString(i++,yearlyClientInfo.getTaxMailed());
				statement.setDouble(i++,yearlyClientInfo.getPaymentReceived());
				statement.setDouble(i++,yearlyClientInfo.getBalanceDue());
				statement.setString(i++,yearlyClientInfo.getTaxPreparedBy());
				statement.setString(i++,yearlyClientInfo.getPaymentComemnts());
				statement.setString(i++,yearlyClientInfo.getComments());
				statement.setString(i++,yearlyClientInfo.getAssignedTo());
				statement.setString(i++,yearlyClientInfo.getLastUpdatedBy());
				statement.setString(i++,yearlyClientInfo.getYear());
				statement.setString(i++,yearlyClientInfo.getTaxCompletedDate());
				statement.setString(i++,yearlyClientInfo.getTaxFiledDate());
				
				statement.setDouble(i++,yearlyClientInfo.getCorpCharge());
				statement.setDouble(i++,yearlyClientInfo.getGstCharge());
				statement.setDouble(i++,yearlyClientInfo.getPayrollCharge());
				statement.setString(i++,yearlyClientInfo.getOtherCharge1Name());
				statement.setDouble(i++,yearlyClientInfo.getOtherCharge1());
				statement.setString(i++,yearlyClientInfo.getOtherCharge2Name());
				statement.setDouble(i++,yearlyClientInfo.getOtherCharge2());
				statement.setDouble(i++,yearlyClientInfo.getOtherCharge());
				statement.setString(i++,yearlyClientInfo.getGstIncluded());
				statement.setString(i++,yearlyClientInfo.getChargeComments());
				statement.setDouble(i++,yearlyClientInfo.getTotalCharges());
				statement.setString(i++,yearlyClientInfo.getPaymentReceivedDate());
				statement.setDouble(i++,yearlyClientInfo.getPreBalance());
				statement.setDouble(i++,yearlyClientInfo.getDiscount());
				statement.setInt(i++,yearlyClientInfo.getBaseYear());
				statement.setString(i++,yearlyClientInfo.getActive());
				statement.setString(i++,yearlyClientInfo.getLock());
				statement.setDouble(i++,yearlyClientInfo.getCurrentCharges());
				statement.setString(i++,yearlyClientInfo.getPaymentReceivedBy());
				
				statement.setString(i++,yearlyClientInfo.getPaymentInfoExist());
				statement.setString(i++,yearlyClientInfo.getTaxReturnInfoExist());
				
				
				int count  = statement.executeUpdate();
				
				System.out.println("insertYearlyClientInfo updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
						close(con);
						close(statement);
			}
		}
		

		
		public void deleteCompany(String companyCode,int store_id) throws SQLException {
			Statement statement = null;
			Connection con = null;
			try {
			String query =	"delete from company where company_code ='"+companyCode+"' and store_id="+store_id;
			con = getConnection();
				System.out.println("deleting company "+companyCode);
				statement = con.createStatement();
				int count  = statement.executeUpdate(query);
				System.out.println("company deleted successfull "+count);
				deletePayrolls(con,companyCode, store_id);
				deleteCharges(con, companyCode,store_id);
				
				System.out.println("Every thing deleted for the company "+companyCode);
				
			}catch (Exception e)
			{
				e.printStackTrace();
			}
			finally {

				close(statement);
				close(con);
			}
		}

		private void deletePayrolls(Connection con, String companyCode, int store_id) throws SQLException {
			Statement statement = null;
			try {
			String query =	"delete from payroll where company_code ='"+companyCode+"' and store_id="+store_id;
			con = getConnection();
				System.out.println("deleting payrolls for company "+companyCode);
				statement = con.createStatement();
				int count  = statement.executeUpdate(query);
				System.out.println("payrolls deleted successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
						close(statement);
						close(con);
			}
		}
		private void deleteCharges(Connection con, String companyCode, int store_id) throws SQLException {
			Statement statement = null;
			try {
			String query =	"delete from charge where company_code ='"+companyCode+"' and store_id="+store_id;
			con = getConnection();
				System.out.println("deleting CHARGEs for company "+companyCode);
				statement = con.createStatement();
				int count  = statement.executeUpdate(query);
				System.out.println("CHARGEs deleted successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
						close(statement);
						close(con);
			}
		}

		public void deleteCharge(String companyCode,String monthYear, int store_id) throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
				
			String query =	"delete from charge where company_code =? AND charge_month=? and store_id=?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setString(i++,companyCode);
				statement.setString(i++,monthYear);
				statement.setInt(i++,store_id);
				int count  = statement.executeUpdate();
				System.out.println("deleteCharge successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}
		public void deleteChargeByMonthYear(String monthYear, int store_id) throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
				
			String query =	"delete from charge where charge_month=? and store_id=? and lock_account='N'";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setString(i++,monthYear);
				statement.setInt(i++,store_id);
				int count  = statement.executeUpdate();
				System.out.println("deleteCharge successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
				close(statement);
				close(con);
			}
		}
			
		
		public void deleteYearlyClientTaxReturnInfo(YearlyClientForm yearlyClientInfo ) throws SQLException {
			PreparedStatement statement = null;
			Connection con= null;
			try {
				
				ArrayList<YearlyClientForm> clientList =  checkYearlyClientInfoExist(yearlyClientInfo.getCompanyCode(), yearlyClientInfo.getStore_id(),yearlyClientInfo.getYear());	
				if (clientList != null &&  clientList.size()>0 && clientList.get(0) != null)
				{
					YearlyClientForm yearlyClientFromDb = clientList.get(0);
					if (yearlyClientFromDb!= null && yearlyClientFromDb.getPaymentInfoExist().equals("Y"))
					{
						updateClientTaxReturnFlag(yearlyClientFromDb,"N");
						return;
					}
				}

			System.out.println("deleting deleteYearlyClientTaxReturnInfo for company "+yearlyClientInfo.getCompanyCode() + " Year "+yearlyClientInfo.getYear() +"Store_id "+yearlyClientInfo.getStore_id());
					
			String query =	"delete from yearly_client_info where company_code=?  and store_id=? and year=? ";
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,yearlyClientInfo.getCompanyCode());
			statement.setInt(i++,yearlyClientInfo.getStore_id());
			statement.setString(i++,yearlyClientInfo.getYear());
			
			int count  = statement.executeUpdate();
			System.out.println("yearly_client_info deleted successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
						close(statement);
						close(con);
			}
		}	
		
		public void deleteYearlyClientPaymentInfo(YearlyClientForm yearlyClientInfo ) throws SQLException {
			PreparedStatement statement = null;
			Connection con= null;
			try {
			
			ArrayList<YearlyClientForm> clientList =  checkYearlyClientInfoExist(yearlyClientInfo.getCompanyCode(), yearlyClientInfo.getStore_id(),yearlyClientInfo.getYear());	
			
			if (clientList != null &&  clientList.size()>0 && clientList.get(0) != null)
			{
				YearlyClientForm yearlyClientFromDb = clientList.get(0);
				if (yearlyClientFromDb!= null && yearlyClientFromDb.getTaxReturnInfoExist().equals("Y"))
				{
					updateClientPaymentFlag(yearlyClientFromDb,"N");
					return;
				}
			}
			
			System.out.println("deleting deleteYearlyClientPaymentInfo for company "+yearlyClientInfo.getCompanyCode() + " Year "+yearlyClientInfo.getYear() +"Store_id "+yearlyClientInfo.getStore_id());
			
			String query =	"delete from yearly_client_info where company_code=?  and store_id=? and year=? ";
			con = getConnection();
			int i=1;
			
			
			statement = con.prepareStatement(query);
			statement.setString(i++,yearlyClientInfo.getCompanyCode());
			statement.setInt(i++,yearlyClientInfo.getStore_id());
			statement.setString(i++,yearlyClientInfo.getYear());
			
			int count  = statement.executeUpdate();
			System.out.println("yearly_client_info deleted successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
						close(statement);
						close(con);
			}
		}	
		
		public ArrayList<CompanyForm> searchCharges(CompanyForm company) throws SQLException {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			String COMPANY_CODE_COND = "";
			String COMPANY_NAME_COND = "";
			String UPDATE_PAYROLL_CONDITION = "";
			String WITHDRAW_DATE_CONDITION = "";
			
			UPDATE_PAYROLL_CONDITION = 	" AND charge.charge_month='"+company.getMonth()+company.getYear()+"' ";
			if (company.getCompanyCode() != null && company.getCompanyCode().length() > 0)
			{
				COMPANY_CODE_COND=" AND company.company_code like '%"+company.getCompanyCode()+"%'";
			}
			if (company.getCompanyName() != null && company.getCompanyName().length() > 0)
			{
				COMPANY_NAME_COND=" AND company.company_name like '%"+company.getCompanyName()+"%'";
			}
			
			if (company.getWithdrawDate() != null && company.getWithdrawDate().length() > 0 && !company.getWithdrawDate().equals("ALL") )
			{
				WITHDRAW_DATE_CONDITION = " AND charge.withdraw_date = '"+company.getWithdrawDate()+"'";
			}
			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
			try {
			String SQL_SELECT_COUNTRIES =	"select *, charge.comment charge_comment, charge.total_amount TOTAL_CHARGE_AMOUNT from company, charge  "+
				" WHERE company.company_code=charge.company_code  AND charge.active='Y' and company.store_id="+company.getStore_id()+ UPDATE_PAYROLL_CONDITION+WITHDRAW_DATE_CONDITION;
			
				if (company.getOrderBy()!= null)
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY company."+company.getOrderBy();
				else
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY company.company_code";;
					
			
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created");
			System.out.println("QUERY "+SQL_SELECT_COUNTRIES);
			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
			while(set.next()){
				//System.out.println("query executed  created "+SQL_SELECT_COUNTRIES);
				CompanyForm companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company_code"));
				companyForm.setStore_id(set.getInt("store_id"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setComment(set.getString("comment"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
				
				//				System.out.println("getting chagre for "+companyForm.getCompanyName());
//				companyForm.setPayroll(populatePayrollForm(set));
				companyForm.setCharge(populateChargeForm(set));
				
//				companyForm.getPayroll().setComment(set.getString("payroll_comment"));
				companyForm.getCharge().setComments(set.getString("charge_comment"));
				
				//System.out.println("adding comoany "+companyForm.getCompanyName());
				result.add(companyForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
				//throw e;
			}
					finally {

				close(statement);
				close(con);
			}
			return result;
		}
		
//		
//		public ArrayList<CompanyForm> getCompanies(CompanyForm company, String action) throws SQLException {
//			Statement statement = null;
//			Connection con = null;
//			ResultSet set = null;
//			String COMPANY_CODE_COND = "";
//			String COMPANY_NAME_COND = "";
//			if (company.getCompanyCode() != null && company.getCompanyCode().length() > 0)
//			{
//				COMPANY_CODE_COND=" AND company.company_code like '%"+company.getCompanyCode()+"%'";
//			}
//			if (company.getCompanyName() != null && company.getCompanyName().length() > 0)
//			{
//				COMPANY_NAME_COND=" AND company.company_name like '%"+company.getCompanyName()+"%'";
//			}
//			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
//			try {
//			String SQL_SELECT_COUNTRIES =	"select * from company "+
//				" WHERE company.store_id="+company.getStore_id();
//			
//				if (company.getOrderBy()!= null)
//					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY company."+company.getOrderBy();
//				else
//					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY company.company_code";;
//					
//			
//			con = getConnection();
//			statement = con.createStatement();
//			System.out.println("statement created");
//			System.out.println("QUERY "+SQL_SELECT_COUNTRIES);
//			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
//			while(set.next()){
//				System.out.println("query executed  created "+SQL_SELECT_COUNTRIES);
//				CompanyForm companyForm = new CompanyForm();
//				companyForm.setCompanyCode(set.getString("company_code"));
//				companyForm.setStore_id(set.getInt("store_id"));
//				companyForm.setCompanyName(set.getString("company_name"));
//				companyForm.setBnNumber(set.getString("bn_number"));
//				companyForm.setAddress1(set.getString("address1"));
//				companyForm.setAddress2(set.getString("address2"));
//				companyForm.setCity(set.getString("city"));
//				companyForm.setProvince(set.getString("province"));
//				companyForm.setPostalCode(set.getString("postal_code"));
//				companyForm.setComment(set.getString("comment"));
////				System.out.println("getting payroll and chagre for "+companyForm.getCompanyName());
////				companyForm.setPayroll(populatePayrollForm(set));
////				companyForm.setCharge(populateChargeForm(set));
////				companyForm.getPayroll().setComment(set.getString("payroll_comment"));
////				companyForm.getCharge().setComments(set.getString("charge_comment"));
//				
//				System.out.println("adding comoany "+companyForm.getCompanyName());
//				result.add(companyForm);
//			}
//			}catch (Exception e)
//			{
//				e.printStackTrace();
//				//throw e;
//			}
//					finally {
//
//				close(statement);
//				close(con);
//			}
//			return result;
//		}

		
		public ArrayList<CompanyForm> getCompanies(CompanyForm company, String action) throws SQLException {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			String COMPANY_CODE_COND = "";
			String COMPANY_NAME_COND = "";
			String YEARLY_CONDITION = "";
//			if (action!= null && action.equals(JspConstants.ACTION_YEARLY))
//			{
//				System.out.println("getCompanies for ACTION_YEARLY created "+action);
//				YEARLY_CONDITION = 	" AND company.client_type='Y' ";
//			}
				if (company.getCompanyCode() != null && company.getCompanyCode().length() > 0)
			{
				COMPANY_CODE_COND=" AND company.company_code = '"+company.getCompanyCode()+"'";
			}
			if (company.getCompanyName() != null && company.getCompanyName().length() > 0)
			{
				COMPANY_NAME_COND=" AND company.company_name like '%"+company.getCompanyName()+"%'";
			}
			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
			try {
			String SQL_SELECT_COUNTRIES =	"select company.*, payroll.payroll_frequency  from company, payroll WHERE company.company_code = payroll.company_code "+
											" and company.store_id="+company.getStore_id()+
											" and  payroll.store_id="+company.getStore_id()+  
											" and payroll.payroll_month='MASTER_PAYROLL' "+YEARLY_CONDITION;
			
				if (company.getOrderBy()!= null && company.getOrderBy().equals("payroll_frequency"))
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY payroll."+company.getOrderBy();
				
				else if (company.getOrderBy()!= null && !company.getOrderBy().equals("PAYROLL"))
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY company."+company.getOrderBy();
				else
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY company.company_code";;
					
			
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created");
			System.out.println("QUERY "+SQL_SELECT_COUNTRIES);
			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
			HashMap<String,String> bankAccountNumbers = loadBankAccountNumbers(company.getStore_id());
			while(set.next()){
				CompanyForm companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company_code"));
				companyForm.setStore_id(set.getInt("store_id"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setComment(set.getString("comment"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
				companyForm.setPayrollType(set.getString("payroll_frequency"));
				System.out.println("getClientStatusActive "+companyForm.getCompanyCode()+" Status="+companyForm.getClientStatusActive());
				if (companyForm.getPayrollType() == null)
					companyForm.setPayrollType("M");
				companyForm.setBankAccNumber(getBankAccountNumber(company.getCompanyCode(),bankAccountNumbers ));
				//companyForm.setBankAccNumber(getBankAccountNumber(company.getCompanyCode(),company.getStore_id() ));
				//companyForm.setPayroll(populatePayrollForm(set));
				//companyForm.setCharge(populateChargeForm(set));
				//companyForm.getPayroll().setComment(set.getString("payroll_comment"));
				//companyForm.getCharge().setComments(set.getString("charge_comment"));
				
				//				System.out.println("adding comoany "+companyForm.getCompanyName());
				result.add(companyForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
				//throw e;
			}
					finally {

				close(statement);
				close(con);
			}
			return result;
		}
	
		public CompanyForm checkCompanyExist(CompanyForm company) throws SQLException {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			CompanyForm companyForm = null;
			try {
			String SQL_SELECT_COUNTRIES =	"select *  from company WHERE company_code = '"+company.getCompanyCode()+"'  and store_id="+company.getStore_id();
			con = getConnection();
			statement = con.createStatement();
			System.out.println("QUERY "+SQL_SELECT_COUNTRIES);
			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
			if(set.next()){
				companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company_code"));
				companyForm.setStore_id(set.getInt("store_id"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
				companyForm.setComment(set.getString("comment"));
				System.out.println("adding comoany "+companyForm.getCompanyName());
			}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
						close(statement);
						close(con);
			}
			return companyForm;
		}		
		
		public ArrayList<CompanyForm> searchPayrolls(CompanyForm company) throws SQLException {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			String COMPANY_CODE_COND = "";
			String COMPANY_NAME_COND = "";
			String PAYROLL_TYPE_COND = "";
			
	
			System.out.println("searchPayrolls for updatePayroll created "+company.getMonth()+company.getYear());
			
			if (company.getCompanyCode() != null && company.getCompanyCode().length() > 0)
			{
				COMPANY_CODE_COND=" AND company.company_code like '%"+company.getCompanyCode()+"%'";
			}
			if (company.getCompanyName() != null && company.getCompanyName().length() > 0)
			{
				COMPANY_NAME_COND=" AND company.company_name like '%"+company.getCompanyName()+"%' ";
			}
			if (company.getPayrollType() != null && company.getPayrollType().length() > 0)
			{
				PAYROLL_TYPE_COND=" AND payroll.payroll_type like '%"+company.getPayrollType()+"%' ";
			}
			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
			try {

				String SQL_SELECT_COUNTRIES =	"select *,payroll.comment payroll_comment from company, payroll "+
				" WHERE company.company_code=payroll.company_code AND payroll.active='Y' "+
				" AND company.store_id="+company.getStore_id()+ " AND payroll.payroll_month='"+company.getMonth()+company.getYear()+"' ";
			
				if (company.getOrderBy()!= null && "payroll_type".equals(company.getOrderBy()))
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  +PAYROLL_TYPE_COND+ " ORDER BY payroll."+company.getOrderBy();
				else if (company.getOrderBy()!= null && !"payroll_type".equals(company.getOrderBy()))
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  +PAYROLL_TYPE_COND+ " ORDER BY company."+company.getOrderBy();
				else
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  +PAYROLL_TYPE_COND+ " ORDER BY company.company_code";;
					
			
			con = getConnection();
			statement = con.createStatement();
			System.out.println("QUERY "+SQL_SELECT_COUNTRIES);
			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
			while(set.next()){
				//				System.out.println("query executed  created "+SQL_SELECT_COUNTRIES);
				CompanyForm companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company_code"));
				companyForm.setStore_id(set.getInt("store_id"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setComment(set.getString("comment"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
					
				companyForm.setMonth(company.getMonth());
				companyForm.setYear(company.getYear());
				//			System.out.println("getting payroll and chagre for "+companyForm.getCompanyName());
				companyForm.setPayroll(populatePayrollForm(set));
				companyForm.getPayroll().setComment(set.getString("payroll_comment"));
				//			System.out.println("adding comoany "+companyForm.getCompanyName());
				result.add(companyForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
				//throw e;
			}
					finally {

				close(statement);
				close(con);
			}
			return result;
		}
		
		
		public ArrayList<CompanyForm> getPayrollsByMonth(String payrollMonth, String orderBy, String showNsf, int store_id) throws SQLException {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
			String orderByClause = " ORDER BY payroll.payroll_type";
			String showNsfClause = "";
			if (orderBy!= null && !orderBy.equals("payroll_type"))
				orderByClause = " ORDER BY company."+orderBy;

			else if (orderBy!= null && orderBy.equals("payroll_type"))
				orderByClause = " ORDER BY payroll."+orderBy;
			
			if (showNsf != null && showNsf.equals("Y"))
				{
					showNsfClause = " AND payroll.nsf='Y' ";
				}
			if (showNsf != null && showNsf.equals("N"))
				{
					showNsfClause = " AND payroll.nsf='N' ";
				}
			try {
			String SQL_SELECT_COUNTRIES =	"select * from payroll , company "+
				"  WHERE company.company_code = payroll.company_code AND  payroll.active='Y' AND company.store_id = "+store_id + 
				" AND company.company_code=payroll.company_code "+
				"  AND payroll.payroll_month='"+ payrollMonth+"'"+showNsfClause+orderByClause;

			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created "+SQL_SELECT_COUNTRIES);
			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
			System.out.println("query executed  created "+SQL_SELECT_COUNTRIES);
			while(set.next()){
				CompanyForm companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company_code"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setComment(set.getString("comment"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
				//				System.out.println("getting payroll and chagre for "+companyForm.getCompanyName());
				companyForm.setPayroll(populatePayrollForm(set));
				companyForm.getPayroll().setComment(set.getString("comment"));
				//				System.out.println("adding comoany "+companyForm.getCompanyName());
				result.add(companyForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
			return result;
		}

		
		public ArrayList<CompanyForm> getChargesByMonth(String month, String withdrawDate, String orderBy,String showNsf, int store_id) throws SQLException {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
			String showNsfClause = "";
			String WITHDRAW_DATE_CONDITION= "";
			try {
				
				if (showNsf != null && showNsf.equals("Y"))
					{
						showNsfClause = " AND charge.nsf='Y' ";
					}
				if (showNsf != null && showNsf.equals("N"))
					{
						showNsfClause = " AND charge.nsf='N' ";
					}
			
				if (withdrawDate != null && withdrawDate.length() > 0 && !withdrawDate.equals("ALL") )
				{
					WITHDRAW_DATE_CONDITION = " AND charge.withdraw_date = '"+withdrawDate+"' ";
				}

				
			String SQL_SELECT_COUNTRIES =	"select *,charge.comment charge_comment,charge.total_amount TOTAL_CHARGE_AMOUNT  from company, charge "+
				" WHERE company.company_code=charge.company_code AND  charge.active='Y' and company.store_id="+store_id + 
				"  AND charge.charge_month='"+month+"'"+
				WITHDRAW_DATE_CONDITION +showNsfClause;
			
			if (orderBy != null && orderBy.equalsIgnoreCase("company_code"))
				SQL_SELECT_COUNTRIES = SQL_SELECT_COUNTRIES+" Order by company.company_code";
			else if (orderBy != null && orderBy.equalsIgnoreCase("company_name"))
				SQL_SELECT_COUNTRIES = SQL_SELECT_COUNTRIES+" Order by company.company_name";
			else if (orderBy != null && orderBy.equalsIgnoreCase("bn_number"))
				SQL_SELECT_COUNTRIES = SQL_SELECT_COUNTRIES+" Order by company.bn_number";
				//" ORDER BY payroll.payroll_type";
			
			
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created "+SQL_SELECT_COUNTRIES);
			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
			System.out.println("query executed  created "+SQL_SELECT_COUNTRIES);
			while(set.next()){
//				System.out.println("query executed  created "+SQL_SELECT_COUNTRIES);
				CompanyForm companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company_code"));
				companyForm.setStore_id(set.getInt("store_id"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setComment(set.getString("comment"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
				//				System.out.println("getting payroll and chagre for "+companyForm.getCompanyName());
				companyForm.setCharge(populateChargeForm(set));
				companyForm.getCharge().setComments(set.getString("charge_comment"));
				//System.out.println("adding comoany "+companyForm.getCompanyName());
				result.add(companyForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
			return result;
		}


		public PayrollForm populatePayrollForm(ResultSet set) throws SQLException
		{
			PayrollForm payroll =new PayrollForm();
			payroll.setStore_id(set.getInt("store_id"));
			payroll.setBankCharge(set.getDouble("bank_charge"));
			payroll.setCompanyCode(set.getString("company_code"));
			payroll.setNsf(set.getString("nsf"));
			payroll.setNumberOfemployees(set.getInt("number_of_employees"));
			payroll.setPayrollMonth(set.getString("payroll_month"));
			payroll.setPayrollType(set.getString("payroll_type"));
			payroll.setRemittanceAmount(set.getDouble("government_remittance"));
			payroll.setRevisedAmount(set.getDouble("revised_amount"));
			payroll.setPreviousBalance(set.getDouble("previous_balance"));
			payroll.setTotalAmount(set.getDouble("total_amount"));
			payroll.setGrossPayroll(set.getDouble("gross_payroll"));
			payroll.setComment(set.getString("comment"));
			payroll.setPayrollFrequency(set.getString("payroll_frequency"));
			payroll.setSentToCRA(set.getString("sent_to_cra"));
			payroll.setAccountLocked(set.getString("account_locked"));
			payroll.setTotalAmountDue(payroll.getTotalAmount()+payroll.getPreviousBalance());
/*			if (payroll.getRevisedAmount() > 0)
				payroll.setTotalAmountDue(payroll.getRevisedAmount()+payroll.getPreviousBalance());
*/					
//			System.out.println("company CODE "+payroll.getCompanyCode());
			if (payroll.getNsf() == null)
				payroll.setNsf("N");

//			System.out.println("comment "+payroll.getComment());
			payroll.setUpdating(true);
			payroll.setActive(set.getString("active"));
			return payroll;

		}

		public ChargeForm populateChargeForm(ResultSet set) throws SQLException
		{
			ChargeForm charge =new ChargeForm();
			charge.setStore_id(set.getInt("store_id"));
			charge.setAccountNumber(set.getString("bank_account_number"));
			charge.setComments(set.getString("comment"));
			charge.setCompanyCode(set.getString("company_code"));
			charge.setGst(set.getDouble("gst"));
			charge.setTotal(set.getDouble("total_amount"));
			charge.setMonthYear(set.getString("charge_month"));
			charge.setNetAmount(set.getDouble("net_amount"));
			if (set.getString("withdraw_date") != null)
				charge.setWithdrawDate(set.getString("withdraw_date").toString());
			else
				charge.setWithdrawDate(null);
			//charge.setTotal(set.getDouble("total_amount"));
			charge.setPreviousAmount(set.getDouble("previous_amount"));
			charge.setPreviousAmountBalance(set.getDouble("previous_amount_balance"));
			charge.setPreviousAmountInstallment(set.getDouble("instalment"));
			charge.setTotalWithdrawAmount(set.getDouble("total_withdraw_amount"));
			charge.setPreviousCharges(set.getDouble("pre_charges"));
			charge.setPreviousAmountGstIncluded(set.getString("pre_amount_include_gst"));  
			charge.setActive(set.getString("active"));
			charge.setLockAccount(set.getString("lock_account"));
			charge.setNsf(set.getString("nsf"));
			if (charge.getNsf() == null)
				charge.setNsf("N");
			
			return charge;

		}

		public YearlyClientForm populateClientInfoForm(ResultSet set) throws SQLException
		{
			YearlyClientForm clientInfo =new YearlyClientForm();
			clientInfo.setStore_id(set.getInt("store_id"));
			clientInfo.setYearEnd(set.getString("year_end"));
			clientInfo.setYear(set.getString("year"));
			clientInfo.setCompanyCode(set.getString("company_code"));
			clientInfo.setTaxCompleted(set.getString("tax_completed"));
			clientInfo.setTaxMailed(set.getString("tax_mailed"));
			clientInfo.setTaxFiled(set.getString("tax_filed"));
			clientInfo.setPaymentReceived(set.getDouble("payment_received_amount"));
			clientInfo.setBalanceDue(set.getDouble("balance_due"));
			clientInfo.setTaxPreparedBy(set.getString("tax_prepared_by"));
			clientInfo.setPaymentComemnts(set.getString("payment_comments"));
			clientInfo.setComments(set.getString("comments"));
			clientInfo.setAssignedTo(set.getString("assigned_to"));
			clientInfo.setTaxCompletedDate(set.getString("tax_completed_date"));
			clientInfo.setTaxFiledDate(set.getString("tax_filed_date"));
			clientInfo.setPaymentReceivedDate(set.getString("payment_received_date"));
			
			clientInfo.setCorpCharge(set.getDouble("corp_charges"));
			clientInfo.setGstCharge(set.getDouble("gst_charges"));
			clientInfo.setPayrollCharge(set.getDouble("payroll_charges"));
			clientInfo.setOtherCharge1Name(set.getString("other_charge1_name"));
			clientInfo.setOtherCharge1(set.getDouble("other_charge1_value"));
			clientInfo.setOtherCharge2Name(set.getString("other_charge2_name"));
			clientInfo.setOtherCharge2(set.getDouble("other_charge2_value"));
			clientInfo.setOtherCharge(set.getDouble("other_charge"));
			clientInfo.setGstIncluded(set.getString("gst_included"));
			clientInfo.setChargeComments(set.getString("charge_comments"));
			clientInfo.setTotalCharges(set.getDouble("total_charges"));
			clientInfo.setPreBalance(set.getDouble("pre_balance"));
			clientInfo.setDiscount(set.getDouble("discount"));
			clientInfo.setBaseYear(set.getInt("base_year"));
			clientInfo.setActive(set.getString("active"));
			clientInfo.setLock(set.getString("account_lock"));
			clientInfo.setCurrentCharges(set.getDouble("current_charges"));
			clientInfo.setPaymentReceivedBy(set.getString("payment_received_by"));
			
			clientInfo.setPaymentInfoExist(set.getString("payment_info_exist"));
			clientInfo.setTaxReturnInfoExist(set.getString("tax_return_info"));
			clientInfo.setLastUpdatedBy(set.getString("last_updated_by"));  
			System.out.println("base_year "+clientInfo.getBaseYear());
			return clientInfo;
		}

		
		
//		public YearlyChargeForm populateChargeInfoForm(ResultSet set) throws SQLException
//		{
//			YearlyChargeForm clientInfo =new YearlyChargeForm();
//			clientInfo.setCorpCharge(set.getDouble("corp_charges"));
//			clientInfo.setGstCharge(set.getDouble("gst_charges"));
//			clientInfo.setPayrollCharge(set.getDouble("payroll_charges"));
//			clientInfo.setOtherCharge1Name(set.getString("other_charge1_name"));
//			clientInfo.setOtherCharge1(set.getDouble("other_charge1_value"));
//			clientInfo.setOtherCharge2Name(set.getString("other_charge2_name"));
//			clientInfo.setOtherCharge2(set.getDouble("other_charge2_value"));
//			clientInfo.setOtherCharge3Name(set.getString("OTHER_CHARGE3_NAME"));
//			clientInfo.setOtherCharge3(set.getDouble("OTHER_CHARGE3_VALUE"));
//			clientInfo.setGstIncluded(set.getString("gst_included"));
//			clientInfo.setComments(set.getString("comments"));
//			clientInfo.setLastUpdatedBy(set.getString("last_updated_by"));  
//			clientInfo.setTotalCharges(set.getDouble("total_charges"));
//			clientInfo.setYear(set.getString("year"));
//			return clientInfo;
//
//		}
		
		
		
		
		
		public static void main (String arg[])
		{
			ReferenceDao dao = new ReferenceDao();
			try {
				dao.getConnectionFromTmcat();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		    public void close(Connection connection){
	        if(connection != null){
	            try {
	                connection.close();
	            }
	            catch(Exception e){
	                e.printStackTrace();
	            }
	        }
	    }

		public ArrayList<PayrollForm> getMasterPayrolls(int store_id) {

			Statement statement = null;
			Connection con = null;
			ResultSet set = null;

			ArrayList<PayrollForm> result = new ArrayList<PayrollForm>();
			try {
			String SQL_SELECT_COUNTRIES =	"select *,payroll.comment payroll_comment from payroll "+
				" WHERE payroll.payroll_month ='"+JspConstants.MASTER_PAYROLL+"'  AND  payroll.active='Y' and store_id = "+store_id+" group by company_code ";
			System.out.println("SQL_SELECT_COUNTRIES "+SQL_SELECT_COUNTRIES);
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created");
			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
			while(set.next()){
				PayrollForm payrollForm = populatePayrollForm(set);
				//	System.out.println("adding comoany "+payrollForm.getCompanyCode());
				result.add(payrollForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
				close(statement);
				close(con);
			}
			return result;
		}


		
		
		public ArrayList<ChargeForm> getMasterCharges(int store_id, String withdrawDate, String companyCode) {

			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			ArrayList<ChargeForm> result = new ArrayList<ChargeForm>();
			String WithDrawSQL = "";
			String CompanyCodeQL= "";
			
			System.out.println("getMasterCharges companyCode "+companyCode);
			
			if (withdrawDate != null && !withdrawDate.equals(ALL))
			{
				WithDrawSQL  =" AND withdraw_date='"+withdrawDate+"' ";
			}
			if (companyCode != null && companyCode.length()>0)
			{
				CompanyCodeQL  =" AND company_code='"+companyCode+"'";
			}
			
			
			
			try {
			String query =	"select *  from charge"+
				" WHERE charge_month ='"+JspConstants.MASTER_CHARGE+"' "+WithDrawSQL+CompanyCodeQL+" AND store_id = "+store_id+" group by company_code ";
			System.out.println("query "+query);
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created");
			set = statement.executeQuery(query);
			while(set.next()){
				ChargeForm form = populateChargeForm(set);
				//			System.out.println("adding comoany "+form.getCompanyCode());
				result.add(form);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
				close(statement);
				close(con);
			}
			return result;
		}
		public void updateCharge(ChargeForm charge)  throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
				boolean accountLocked = checkAccountLocked(charge);
				if (accountLocked) 
					{
						System.out.println("updateCharge return .. Account is locked and system cannot overwrite the acocunt charge for Company: "+charge.getCompanyCode()+" MonthYear: "+charge.getMonthYear());
						return;
					}
				System.out.println("updateCharge method gst "+charge.getGst());
			String SQL_INSERT_INTO_COMPANY =	"UPDATE charge set  " +
					"	net_amount=?, " +
					"	gst = ? , " +
					"	total_amount =? ," +
					" 	withdraw_date = ? ,  " +
					" 	bank_account_number = ? ,  " +
					"	comment = ?, " +
					"	previous_amount = ?, " +
					"	instalment =?," +
					"  previous_amount_balance = ? ,total_withdraw_amount=? , active=?, nsf=?, pre_amount_include_gst=?, pre_charges=? , lock_account=? where company_code = ? and  charge_month = ? and store_id=?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
				statement.setDouble(i++,charge.getNetAmount());
				statement.setDouble(i++,charge.getGst());
				statement.setDouble(i++,charge.getTotal());
				statement.setString(i++,charge.getWithdrawDate());
				statement.setString(i++,charge.getAccountNumber());
				statement.setString(i++,charge.getComments());
				statement.setDouble(i++,charge.getPreviousAmount());
				statement.setDouble(i++,charge.getPreviousAmountInstallment());
				statement.setDouble(i++,charge.getPreviousAmountBalance());
				statement.setDouble(i++,charge.getTotalWithdrawAmount());
				statement.setString(i++,charge.getActive());
				statement.setString(i++,charge.getNsf());
				statement.setString(i++,charge.getPreviousAmountGstIncluded());
				statement.setDouble(i++,charge.getPreviousCharges());
				statement.setString(i++,charge.getLockAccount());
				statement.setString(i++,charge.getCompanyCode());
				statement.setString(i++,charge.getMonthYear());
				statement.setInt(i++,charge.getStore_id());
				int count  = statement.executeUpdate();
				System.out.println("updateCharge updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}
		
		private void updateBankAccountNumber(Connection con, String bankAccountNumber, String companyCode, int store_id) {
			PreparedStatement statement = null;
			try {
				System.out.println("updateBankAccountNumber "+companyCode);
				System.out.println("bankAccountNumber "+bankAccountNumber);
				String SQL_INSERT_INTO_COMPANY =	"UPDATE Charge set  " +
					" 	bank_account_number = ?  " +
					" where company_code = ? and  charge_month = 'MASTER_CHARGE' and store_id=?";
				int i=1;
				System.out.println("updateBankAccountNumber SQL "+SQL_INSERT_INTO_COMPANY);
				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
				statement.setString(i++,bankAccountNumber);
				statement.setString(i++,companyCode);
				statement.setInt(i++,store_id);
				int count  = statement.executeUpdate();
				System.out.println("updateBankAccountNumber updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
			}
		}
		
		
		public String getBankAccountNumber1(String companyCode, int store_id) {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;

			String result = new String();
			try {
			String SQL =	"SELECT  bank_account_number FROM charge where charge_month='MASTER_CHARGE' and company_code='"+companyCode+"' and store_id="+store_id+" and bank_account_number is not null; ";
			System.out.println("SQL "+SQL);
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created");
			set = statement.executeQuery(SQL);
			if(set.next()){
				result = set.getString("bank_account_number");
			}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
				close(statement);
				close(con);
			}
			return result;
		}

		public String getBankAccountNumber(String companyCode, HashMap<String,String> bankAccountNumbers) {
		
			String result = null;
			if (bankAccountNumbers == null) result = null;
			if (bankAccountNumbers.containsKey(companyCode))
				result =  bankAccountNumbers.get(companyCode);
			return result;
		}
		
		public HashMap<String,String> loadBankAccountNumbers(int store_id) {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			HashMap<String,String> hashMap = new HashMap<String,String>(); 
			try {
			String SQL =	"SELECT  bank_account_number , company_code FROM charge where charge_month='MASTER_CHARGE'  and store_id="+store_id+" and bank_account_number is not null; ";
			System.out.println("SQL "+SQL);
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created");
			set = statement.executeQuery(SQL);
			while (set.next()){
				hashMap.put(set.getString("company_code"),set.getString("bank_account_number"));
			}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
				close(statement);
				close(con);
			}
			return hashMap;
		}
		
		
		public CompanyForm searchHistory(CompanyForm company) {
			
			System.out.println("DAO.searchHistory called "+company.getCompanyCode());
			company.setChargelist(checkChargeExist(company.getCompanyCode(),null, company.getStore_id()));
			company.setPayrolllist(checkPayrollExist(company.getCompanyCode(), null,company.getStore_id()));
			return company;
		}
		
		
			public ArrayList<CompanyForm> getAllCompanies(CompanyForm company) throws SQLException {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			String COMPANY_CODE_COND = "";
			String COMPANY_NAME_COND = "";
			
			if (company.getCompanyCode() != null && company.getCompanyCode().length() > 0)
			{
				COMPANY_CODE_COND=" AND company.company_code like '%"+company.getCompanyCode()+"%'";
			}
			if (company.getCompanyName() != null && company.getCompanyName().length() > 0)
			{
				COMPANY_NAME_COND=" AND company.company_name like '%"+company.getCompanyName()+"%'";
			}
			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
			try {
			String SQL_SELECT_COUNTRIES =	"SELECT * from company   "+
				" WHERE company.store_id="+company.getStore_id();
			
				if (company.getOrderBy()!= null)
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY company."+company.getOrderBy();
				else
					SQL_SELECT_COUNTRIES	= SQL_SELECT_COUNTRIES + COMPANY_CODE_COND+COMPANY_NAME_COND  + " ORDER BY company.company_code";;
					
			
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created");
			System.out.println("QUERY "+SQL_SELECT_COUNTRIES);
			set = statement.executeQuery(SQL_SELECT_COUNTRIES);
			while(set.next()){
				//		System.out.println("query executed  created "+SQL_SELECT_COUNTRIES);
				CompanyForm companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company_code"));
				companyForm.setStore_id(set.getInt("store_id"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setComment(set.getString("comment"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
				System.out.println("adding comoany "+companyForm.getCompanyName());
				result.add(companyForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
				//throw e;
			}
					finally {

				close(statement);
				close(con);
			}
			return result;
		}
				
		public ArrayList<CompanyForm> searchYearlyCharges(ReportForm form) throws SQLException {
			Connection con = null;
			ResultSet set = null;
			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
			String tax_filed = "";
			String tax_mailed = "";
			String paymentReceived = "";
			String companyCode = "";
			String companyName = "";
			PreparedStatement statement = null;
			try {
				if (form.getCompanyCode()!= null && form.getCompanyCode().length() > 0  )
				{
					companyCode = " and company.company_code=? " ;
				}
				if (form.getCompanyName()!= null && form.getCompanyName().length() > 0  )
				{
					companyName = " and company.company_name=? " ;
				}
				if (form.getTaxFiled()!= null && !form.getTaxFiled().equals(ALL))
				{
					tax_filed = " and client.tax_filed=? " ;
				}
				if (form.getTaxMailed()!= null && !form.getTaxMailed().equals(ALL))
				{
					tax_mailed = " and client.tax_mailed=? " ;
				}
				if (form.getTaxFiled()!= null && !form.getPaymentReceived().equals(ALL))
				{
					if (form.getPaymentReceived().equals(YES))
						paymentReceived = " and client.payment_received_amount > 0 ";
					if (form.getPaymentReceived().equals(NO))
						paymentReceived = " and client.payment_received_amount = 0 ";
				}

				String SQL_SELECT_COUNTRIES =	" select *  from company, yearly_client_info client  "+
				" WHERE  company.store_id= ? "+
				" and company.client_type='Y' "+
				" and company.store_id = client.store_id "+
				" and company.company_code = client.company_code "+
				" and client.year >= ?  and  client.year <= ?  "+ tax_filed +tax_mailed +paymentReceived+companyCode+companyName+
				" group by company.store_id,company.company_code,client.year ";
				int i=1;
				con = getConnection();
				statement = con.prepareStatement(SQL_SELECT_COUNTRIES);
				statement.setInt(i++,form.getStore_id());
				statement.setString(i++,form.getYear());
				statement.setString(i++,form.getYearEnd());
				if (form.getTaxFiled()!= null && !form.getTaxFiled().equals(ALL))
						statement.setString(i++,form.getTaxFiled());
				if (form.getTaxMailed()!= null && !form.getTaxMailed().equals(ALL))
					statement.setString(i++,form.getTaxMailed());
				if (form.getCompanyCode()!= null && form.getCompanyCode().length() > 0  )
				{
					statement.setString(i++,form.getCompanyCode());
				}
				if (form.getCompanyName()!= null && form.getCompanyName().length() > 0  )
				{
					statement.setString(i++,form.getCompanyName());
				}				
			System.out.println("statement created");
			System.out.println("QUERY "+SQL_SELECT_COUNTRIES);
			set = statement.executeQuery();
			while(set.next()){
				CompanyForm companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company.company_code"));
				companyForm.setStore_id(set.getInt("company.store_id"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setComment(set.getString("company.comment"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
				companyForm.setYear(set.getString("client.year"));
				companyForm.setYearlyClientInfo(populateClientInfoForm(set));
				result.add(companyForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
				//throw e;
			}
					finally {

				close(statement);
				close(con);
			}
			return result;
		}

		
		
	
		
		public void unlockYearlyClient(YearlyClientForm yearlyClientInfo) {
			Connection con = null;
			PreparedStatement statement = null;
			try {
				con = getConnection();
				
				System.out.println("unlockYearlyClient Store_id "+yearlyClientInfo.getStore_id());
				String unlockYearlyClientSQL =	"UPDATE yearly_client_info set  " +
					" account_lock=? " +
					" where company_code = ? and store_id=? and year=? ";
				int i=1;
				System.out.println("SQL_INSERT_INTO_COMPANY Store_id "+unlockYearlyClientSQL);
				statement = con.prepareStatement(unlockYearlyClientSQL);
				statement.setString(i++,NO);
				statement.setString(i++,yearlyClientInfo.getCompanyCode());
				statement.setInt(i++,yearlyClientInfo.getStore_id());
				statement.setString(i++,yearlyClientInfo.getYear());
				int count  = statement.executeUpdate();
				System.out.println("unlockYearlyClient updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
			
		}
		public void updateChargePreviousBalance(ChargeForm charge)  throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
				System.out.println("updatePreviousCharge method gst "+charge.getPreviousAmount());
				System.out.println("updatePreviousCharge method gst "+charge.getPreviousAmountInstallment());
				System.out.println("updatePreviousCharge method gst "+charge.getPreviousAmountBalance());
			String SQL_INSERT_INTO_COMPANY =	"UPDATE Charge set  " +
					" previous_amount_balance = ? " +
					" where company_code = ? and  charge_month = ? and store_id=?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
				
				statement.setDouble(i++,charge.getPreviousAmountBalance());

				statement.setString(i++,charge.getCompanyCode());
				statement.setString(i++,charge.getMonthYear());
				statement.setInt(i++,charge.getStore_id());
				int count  = statement.executeUpdate();
				System.out.println("updatePreviousCharge updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}	
	

		public double getTotalInstallmentPaid(String companyCode, int store_id) {
			PreparedStatement statement = null;
			Connection con = null;
			double totalInstalment = 0;
			
			try {

				System.out.println("checkChargeExist.caled for  companyCode "+companyCode);
				String 	query =	    "Select sum(instalment) as totalInstalment  from charge where company_code=? and store_id=? and charge_month <> 'MASTER_CHARGE' ";
				
				
				con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setString(i++,companyCode);
				statement.setInt(i++,store_id);
				ResultSet set = statement.executeQuery();
				if (set.next()){
					totalInstalment = set.getDouble("totalInstalment");
				}

			}catch (Exception e)
			{
				e.printStackTrace();
			}
			finally {
				close(statement);
				close(con);
			}
		return totalInstalment;
		}

		public ArrayList<MonthlyNoteForm> getMonthlyNotes(int month, int year, String status, int store_id, String type, int date) throws SQLException {
			ArrayList<MonthlyNoteForm> result = new ArrayList<MonthlyNoteForm>();
			PreparedStatement statement = null;
			Connection con = null;
			try {

				String query = null;
				con = getConnection();
				if (status == null || status.equals("ALL"))
				{
					query = "select * from monthly_notes where month=? and year=? and store_id=? and notes_type=? order by TEXT";
				}
				if (status != null && !status.equals("ALL"))
				{
					query = "select * from monthly_notes where month=? and year=? and store_id=? and notes_type=? and status=?  order by TEXT ";
				}
				if (date > 0)
				{
					query= query+"  and notes_date="+date;
				}
				
				System.out.println("getMonthlyNotes Query "+query);
				System.out.println("getMonthlyNotes store_id "+store_id);
				System.out.println("getMonthlyNotes year "+year);
				System.out.println("getMonthlyNotes month "+month);
				System.out.println("getMonthlyNotes status "+status);
				statement = con.prepareStatement(query);
				int i=1;
				statement.setInt(i++,month);
				statement.setInt(i++,year);
				statement.setInt(i++,store_id);
				statement.setString(i++,type);
				if (status != null && !status.equals("ALL"))
					statement.setString(i++,status);	

				
				ResultSet set = statement.executeQuery();
				while(set.next()){
					MonthlyNoteForm note = new MonthlyNoteForm();
					System.out.println("getMonthlyNotes adding "+set.getString("text"));
					note.setStore_id(set.getInt("store_id"));
					note.setMonth(set.getString("month"));
					note.setYear(set.getString("year"));
					note.setStatus(set.getString("status"));
					note.setText(set.getString("text"));
					note.setCreateBy(set.getString("created_by"));
					note.setCreatedDate(set.getString("created_date"));
					note.setNotesType(set.getString("notes_type"));
					note.setAssignedTo(set.getString("assigned_to"));
					note.setNotesDate(set.getInt("notes_date"));
					note.setId(set.getInt("id"));
					result.add(note);
				}
			}
			finally {
				close(statement);
				close(con);
			}
			return result;
		}
		
		public void addMonthlyNotes(MonthlyNoteForm note) throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
			int id = getRandomNumber();
			note.setId(id);
			String SQL_INSERT_INTO_monthly_notes ="INSERT INTO monthly_notes (id,month,year,store_id,status,text ,created_by,created_date,notes_type, notes_date, assigned_to ) VALUES (?,?,?,?,?,?,?,?,?,?,? ) ";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_INSERT_INTO_monthly_notes);
				statement.setInt(i++,id);
				statement.setInt(i++,Integer.parseInt(note.getMonth()));
				statement.setInt(i++,Integer.parseInt(note.getYear()));
				statement.setInt(i++,note.getStore_id());
				statement.setString(i++,note.getStatus());
				statement.setString(i++,note.getText());
				statement.setString(i++,note.getCreateBy());
				statement.setString(i++,note.getCreatedDate());
				statement.setString(i++,note.getNotesType());
				statement.setInt(i++,note.getNotesDate());
				statement.setString(i++,note.getAssignedTo());
				int count  = statement.executeUpdate();
				System.out.println("addMonthlyNotes added successfull "+id);

			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}
		
		
		public void updateMonthlyNotes(MonthlyNoteForm note) throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
			String query ="UPDATE monthly_notes set status=? ,text =?, month=? , year=?, notes_type=? , notes_date=?, assigned_to=? where store_id =? and id =?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				
				statement.setString(i++,note.getStatus());
				statement.setString(i++,note.getText());
				statement.setInt(i++,Integer.parseInt(note.getMonth()));
				statement.setInt(i++,Integer.parseInt(note.getYear()));
				statement.setString(i++,note.getNotesType());
				statement.setInt(i++,note.getNotesDate());
				statement.setString(i++,note.getAssignedTo());
				statement.setInt(i++,note.getStore_id());
				statement.setInt(i++,note.getId());
				
				int count  = statement.executeUpdate();
				System.out.println("updateMonthlyNotes added successfull "+count);

			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}	
		
		public void deleteMonthlyNotes(MonthlyNoteForm note) throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
				System.out.println("deleteMonthlyNotes id "+note.getId());
				System.out.println("deleteMonthlyNotes month "+note.getMonth());
				System.out.println("deleteMonthlyNotes year "+note.getYear());
				System.out.println("deleteMonthlyNotes store id "+note.getStore_id());
				
			String query ="DELETE  from monthly_notes where month=? and year=?  and store_id =? and id =?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(query);
				statement.setInt(i++,Integer.parseInt(note.getMonth()));
				statement.setInt(i++,Integer.parseInt(note.getYear()));
				statement.setInt(i++,note.getStore_id());
				statement.setInt(i++,note.getId());
				int count  = statement.executeUpdate();
				System.out.println("deleteMonthlyNotes added successfull "+count);

			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}		
		
		public void lockCharge(ChargeForm charge)  throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
				System.out.println("dao.lockCharge method gst "+charge.getGst());
			String SQL_UPDATE_charge =	"UPDATE charge set lock_account=? where company_code = ? and  charge_month = ? and store_id=?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_UPDATE_charge);
				statement.setString(i++,YES);
				statement.setString(i++,charge.getCompanyCode());
				statement.setString(i++,charge.getMonthYear());
				statement.setInt(i++,charge.getStore_id());
				int count  = statement.executeUpdate();
				System.out.println("lockCharge updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}
		
		public void unLockCharge(ChargeForm charge)  throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
				System.out.println("dao.lockCharge method gst "+charge.getGst());
			String SQL_UPDATE_charge =	"UPDATE charge set lock_account=? where company_code = ? and  charge_month = ? and store_id=?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_UPDATE_charge);
				statement.setString(i++,NO);
				statement.setString(i++,charge.getCompanyCode());
				statement.setString(i++,charge.getMonthYear());
				statement.setInt(i++,charge.getStore_id());
				int count  = statement.executeUpdate();
				System.out.println("lockCharge updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}
		
		
//		public ArrayList<YearlyClientForm> checkClientTaxReturnInfo(String companyCode,int store_id, String year) {
//			PreparedStatement statement = null;
//			Connection con = null;
//			ArrayList<YearlyClientForm> clientInfo = new ArrayList<YearlyClientForm>();
//			String yearInfo="";
//			try {
//				System.out.println("checkClientTaxReturnInfo caled for  companyCode "+companyCode);
//				System.out.println("checkClientTaxReturnInfo caled for  store_id "+store_id);
//				if (year != null)  
//					yearInfo = "AND year= '"+year+"'";
//				
//				String SQL_SELECT_CHARGE =	" Select * from client_tax_return_info where company_code=? and store_id=? "+yearInfo;
//				System.out.println("checkClientTaxReturnInfo SQL "+SQL_SELECT_CHARGE);
//				con = getConnection();
//				int i=1;
//				statement = con.prepareStatement(SQL_SELECT_CHARGE);
//				statement.setString(i++,companyCode);
//				statement.setInt(i++,store_id);
//				ResultSet set = statement.executeQuery();
//				while (set.next()){
//					YearlyClientForm info   = populateClientTaxReturnInfoForm(set);
//					System.out.println("checkClientTaxReturnInfo YearlyClientForm found getBalanceDue "+info.getBalanceDue());
//					clientInfo.add(info);
//				}
//			}catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//			finally {
//				close(statement);
//				close(con);
//			}
//		return clientInfo;
//		}
		
//		public void updateTaxReturnInfo(YearlyClientForm yearlyClientInfo ) {
//			Connection con = null;
//			PreparedStatement statement = null;
//			
//			try {
//				con = getConnection();
//				
//				System.out.println("updateTaxReturnInfo Store_id "+yearlyClientInfo.getStore_id());
//				
//				System.out.println("updateTaxReturnInfo Store_id "+yearlyClientInfo.getStore_id());
//				String SQL_INSERT_INTO_COMPANY =	"UPDATE client_tax_return_info set  " +
//					" 	year_end = ?, tax_filed=?, tax_completed=?, tax_mailed=?, " +
//					" 	tax_prepared_by=?, comments=?, assigned_to=?,  last_updated_by=?, tax_completed_date=?, tax_filed_date=?, " +
//					" payment_received_date =? ,  active=? , account_lock=? " +
//					" where company_code = ? and store_id=? and year=? ";
//				int i=1;
//				System.out.println("updateTaxReturnInfo  Store_id "+SQL_INSERT_INTO_COMPANY);
//				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
//				statement.setString(i++,yearlyClientInfo.getYearEnd());
//				statement.setString(i++,yearlyClientInfo.getTaxFiled());
//				statement.setString(i++,yearlyClientInfo.getTaxCompleted());
//				statement.setString(i++,yearlyClientInfo.getTaxMailed());
//				statement.setString(i++,yearlyClientInfo.getTaxPreparedBy());
//				statement.setString(i++,yearlyClientInfo.getComments());
//				statement.setString(i++,yearlyClientInfo.getAssignedTo());
//				statement.setString(i++,yearlyClientInfo.getLastUpdatedBy());
//				statement.setString(i++,yearlyClientInfo.getTaxCompletedDate());
//				statement.setString(i++,yearlyClientInfo.getTaxFiledDate());
//				statement.setString(i++,yearlyClientInfo.getPaymentReceivedDate());
//				statement.setString(i++,yearlyClientInfo.getActive());
//				statement.setString(i++,yearlyClientInfo.getLock());
//				
//				statement.setString(i++,yearlyClientInfo.getCompanyCode());
//				statement.setInt(i++,yearlyClientInfo.getStore_id());
//				statement.setString(i++,yearlyClientInfo.getYear());
//				
//				
//				int count  = statement.executeUpdate();
//				System.out.println("updateTaxReturnInfo updated successfull "+count);
//			}catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//					finally {
//
//				close(statement);
//				close(con);
//			}
//		}
		
//		public void insertTaxReturnInfo(YearlyClientForm yearlyClientInfo) {
//			PreparedStatement statement = null;
//			Connection con = null;
//			try {
//				
//				con = getConnection();
//				System.out.println("insertTaxReturnInfo ");
//				String SQL_INSERT_INTO_COMPANY =	"INSERT INTO client_tax_return_info (  " +
//					" store_id, company_code, year_end, tax_filed, tax_completed, tax_mailed, " +
//					" tax_prepared_by, comments, assigned_to,  last_updated_by, year, tax_completed_date, tax_filed_date," +
//			        " payment_received_date, active, account_lock ) " +
//				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) " ;
//				System.out.println("insertTaxReturnInfo  "+SQL_INSERT_INTO_COMPANY);
//				int i=1;
//				statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
//				statement.setInt(i++,yearlyClientInfo.getStore_id());
//				statement.setString(i++,yearlyClientInfo.getCompanyCode());
//				statement.setString(i++,yearlyClientInfo.getYearEnd());
//				statement.setString(i++,yearlyClientInfo.getTaxFiled());
//				statement.setString(i++,yearlyClientInfo.getTaxCompleted());
//				statement.setString(i++,yearlyClientInfo.getTaxMailed());
//				statement.setString(i++,yearlyClientInfo.getTaxPreparedBy());
//				statement.setString(i++,yearlyClientInfo.getComments());
//				statement.setString(i++,yearlyClientInfo.getAssignedTo());
//				statement.setString(i++,yearlyClientInfo.getLastUpdatedBy());
//				statement.setString(i++,yearlyClientInfo.getYear());
//				statement.setString(i++,yearlyClientInfo.getTaxCompletedDate());
//				statement.setString(i++,yearlyClientInfo.getTaxFiledDate());
//				statement.setString(i++,yearlyClientInfo.getPaymentReceivedDate());
//				statement.setString(i++,yearlyClientInfo.getActive());
//				statement.setString(i++,yearlyClientInfo.getLock());
//				
//				int count  = statement.executeUpdate();
//				
//				System.out.println("insertTaxReturnInfo updated successfull "+count);
//			}catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//					finally {
//						close(con);
//						close(statement);
//			}
//		}
//			
		
//		public void deleteTaxReturnInfo(YearlyClientForm yearlyClientInfo ) throws SQLException {
//			PreparedStatement statement = null;
//			Connection con= null;
//			try {
//			String query =	"delete from client_tax_return_info where company_code=?  and store_id=? and year=? ";
//			con = getConnection();
//			int i=1;
//			System.out.println("deleteTaxReturnInfo for company "+yearlyClientInfo.getCompanyCode());
//			
//			statement = con.prepareStatement(query);
//			statement.setString(i++,yearlyClientInfo.getCompanyCode());
//			statement.setInt(i++,yearlyClientInfo.getStore_id());
//			statement.setString(i++,yearlyClientInfo.getYear());
//			
//			int count  = statement.executeUpdate();
//			System.out.println("deleteTaxReturnInfo deleted successfull "+count);
//			}catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//					finally {
//						close(statement);
//						close(con);
//			}
//		}			
//				
//		public void unlockTaxReturn(YearlyClientForm yearlyClientInfo) {
//			Connection con = null;
//			PreparedStatement statement = null;
//			try {
//				con = getConnection();
//				
//				System.out.println("unlockTaxReturn Store_id "+yearlyClientInfo.getStore_id());
//				String unlockYearlyClientSQL =	"UPDATE client_tax_return_info set  " +
//					" account_lock=? " +
//					" where company_code = ? and store_id=? and year=? ";
//				int i=1;
//				System.out.println("unlockYearlyClientSQL Store_id "+unlockYearlyClientSQL);
//				statement = con.prepareStatement(unlockYearlyClientSQL);
//				statement.setString(i++,NO);
//				statement.setString(i++,yearlyClientInfo.getCompanyCode());
//				statement.setInt(i++,yearlyClientInfo.getStore_id());
//				statement.setString(i++,yearlyClientInfo.getYear());
//				int count  = statement.executeUpdate();
//				System.out.println("unlockTaxReturn updated successfull "+count);
//			}catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//					finally {
//
//				close(statement);
//				close(con);
//			}
//			
//		}
		
		public ArrayList<CompanyForm> getActiveCompanies(int storeId, String action ) throws SQLException {
			Statement statement = null;
			Connection con = null;
			ResultSet set = null;
			
			ArrayList<CompanyForm> result = new ArrayList<CompanyForm>();
			try {
				
				String query = null; 
				if (action != null && action.equals(PAYROLL))
				{
					query =	"SELECT * from company  , payroll  "+
						" WHERE company.company_code = payroll.company_code "+
						" and payroll.active='Y' "+
						" and company.store_id="+storeId+" and payroll.payroll_month='"+MASTER_PAYROLL+"' ORDER BY company.company_code ";
				}
				if (action != null && action.equals(CHARGE))
				{
					query =	"SELECT * from company  , charge  "+
						" WHERE company.company_code = charge.company_code "+
						" and charge.active='Y' "+
						" and company.store_id="+storeId+" and charge.charge_month='"+MASTER_CHARGE+"' ORDER BY company.company_code ";
				}
			
			
			con = getConnection();
			statement = con.createStatement();
			System.out.println("statement created");
			System.out.println("QUERY "+query);
			set = statement.executeQuery(query);
			while(set.next()){
				CompanyForm companyForm = new CompanyForm();
				companyForm.setCompanyCode(set.getString("company_code"));
				companyForm.setStore_id(set.getInt("store_id"));
				companyForm.setCompanyName(set.getString("company_name"));
				companyForm.setBnNumber(set.getString("bn_number"));
				companyForm.setAddress1(set.getString("address1"));
				companyForm.setAddress2(set.getString("address2"));
				companyForm.setCity(set.getString("city"));
				companyForm.setProvince(set.getString("province"));
				companyForm.setPostalCode(set.getString("postal_code"));
				companyForm.setComment(set.getString("comment"));
				companyForm.setClientType(set.getString("client_type"));
				companyForm.setClientStatusActive(set.getString("client_status_active"));
				result.add(companyForm);
			}
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
			return result;
		}
		
		
		public void lockPayroll(PayrollForm charge)  throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
			String SQL_UPDATE_payroll =	"UPDATE payroll set account_locked=? where company_code = ? and  payroll_month = ? and store_id=?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_UPDATE_payroll);
				statement.setString(i++,YES);
				statement.setString(i++,charge.getCompanyCode());
				statement.setString(i++,charge.getPayrollMonth());
				statement.setInt(i++,charge.getStore_id());
				int count  = statement.executeUpdate();
				System.out.println("lockPayroll updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}
		
		public void unLockPayroll(PayrollForm charge)  throws SQLException {
			PreparedStatement statement = null;
			Connection con = null;
			try {
			String SQL_UPDATE_payroll =	"UPDATE payroll set account_locked=? where company_code = ? and  payroll_month = ? and store_id=?";
			con = getConnection();
				int i=1;
				statement = con.prepareStatement(SQL_UPDATE_payroll);
				statement.setString(i++,NO);
				statement.setString(i++,charge.getCompanyCode());
				statement.setString(i++,charge.getPayrollMonth());
				statement.setInt(i++,charge.getStore_id());
				int count  = statement.executeUpdate();
				System.out.println("lockPayroll updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
		}
		
		public void updateClientPaymentFlag(YearlyClientForm yearlyClientInfo, String flag) {
			Connection con = null;
			PreparedStatement statement = null;
			try {
				con = getConnection();
				
				System.out.println("updateClientPaymentInfo Store_id "+yearlyClientInfo.getStore_id());
				String unlockYearlyClientSQL =	"UPDATE yearly_client_info set payment_info_exist=? " +
					" where company_code = ? and store_id=? and year=? ";
				int i=1;
				System.out.println("updateClientPaymentInfo sql "+unlockYearlyClientSQL);
				statement = con.prepareStatement(unlockYearlyClientSQL);
				statement.setString(i++,flag);
				statement.setString(i++,yearlyClientInfo.getCompanyCode());
				statement.setInt(i++,yearlyClientInfo.getStore_id());
				statement.setString(i++,yearlyClientInfo.getYear());
				int count  = statement.executeUpdate();
				System.out.println("updateClientPaymentInfo updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
			
		}
		public void updateClientTaxReturnFlag(YearlyClientForm yearlyClientInfo, String flag) {
			Connection con = null;
			PreparedStatement statement = null;
			try {
				con = getConnection();
				
				System.out.println("updateClientTaxReturnFlag Store_id "+yearlyClientInfo.getStore_id());
				String unlockYearlyClientSQL =	"UPDATE yearly_client_info set tax_return_info=? " +
					" where company_code = ? and store_id=? and year=? ";
				int i=1;
				System.out.println("updateClientTaxReturnFlag sql "+unlockYearlyClientSQL);
				statement = con.prepareStatement(unlockYearlyClientSQL);
				statement.setString(i++,flag);
				statement.setString(i++,yearlyClientInfo.getCompanyCode());
				statement.setInt(i++,yearlyClientInfo.getStore_id());
				statement.setString(i++,yearlyClientInfo.getYear());
				int count  = statement.executeUpdate();
				System.out.println("updateClientTaxReturnFlag updated successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {

				close(statement);
				close(con);
			}
			
		}

		public void deleteCompanyYearlyInfo(YearlyClientForm yearlyClientInfo ) throws SQLException {
			PreparedStatement statement = null;
			Connection con= null;
			try {
			
			System.out.println(" deleteCompanyYearlyInfo for company "+yearlyClientInfo.getCompanyCode() + " Year "+yearlyClientInfo.getYear() +"Store_id "+yearlyClientInfo.getStore_id());
			
			String query =	"delete from yearly_client_info where company_code=?  and store_id=? and year=? ";
			con = getConnection();
			int i=1;
			statement = con.prepareStatement(query);
			statement.setString(i++,yearlyClientInfo.getCompanyCode());
			statement.setInt(i++,yearlyClientInfo.getStore_id());
			statement.setString(i++,yearlyClientInfo.getYear());
			
			int count  = statement.executeUpdate();
			System.out.println("deleteCompanyYearlyInfo  deleted successfull "+count);
			}catch (Exception e)
			{
				e.printStackTrace();
			}
					finally {
						close(statement);
						close(con);
			}
		}
		
		public Map getUserCodeToNameMapping(int storeId) {
				Map mappings = new HashMap();
				PreparedStatement statement = null;
				Connection con = null;
				try {
					System.out.println("getUserCodeToNameMapping caled");
					String SQL_SELECT_COMPANY =	"Select company_code, company_name  from company where store_id=?";
					con = getConnection();
					int i=1;
					statement = con.prepareStatement(SQL_SELECT_COMPANY);
					statement.setInt(i++,storeId);
					ResultSet set = statement.executeQuery();
					while (set.next()){
						mappings.put(set.getString("company_code"), set.getString("company_name"));
					}
					System.out.println(" getUserCodeToNameMapping companyCode  "+mappings.size());
				}catch (Exception e)
				{
					e.printStackTrace();
				}
				finally {
					close(statement);
					close(con);
				}
			return mappings;
			}

		
}



