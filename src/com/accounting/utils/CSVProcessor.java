package com.accounting.utils;

public class CSVProcessor {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		
			String [][] numbers = new String [240][50];
			BufferedReader bufRdr = null;
			try {
				File file = new File("C:\\Downloads\\FireFox\\tax_return_info.csv");
				bufRdr = new BufferedReader(new FileReader(file));
				String line = null;
			  int index=0;
				while((line = bufRdr.readLine()) != null)
				  {
					index++;
					if (index==1)	continue;
				  StringTokenizer st = new StringTokenizer(line,",");
				  String[] split = line.split(",");
				  
				  String storeId = split[0];
				  String companyCode = split[1];
				  String year = split[2];
				  String year_end = split[3];
				  if (year_end!= null && year_end.length()==5)
				  {
					  year_end = "0"+year_end;
				  }
				  String tax_filed = split[4];
				  String tax_completed = split[5];
				  String tax_prepared_by = split[7];
				  
				  String comments = split[8];
				  String tax_filed_date = split[9];
				  String tax_completed_date = split[10];
				  String active = split[12];
				  String account_lock = split[13];

				  Connection con = getConnectionFromTmcat();	
				  
				  
				  
				  //System.out.println("Processing "+line);
				  
					String SQL_INSERT_INTO_COMPANY =	"UPDATE yearly_client_info set  " +
						" 	year_end = ?,tax_prepared_by=?,  comments=?, active=? , account_lock=?,   " +
						" tax_completed_date =?, tax_filed_date=? , tax_completed=?, tax_filed=? "+
						" where company_code = ? and store_id=? and year=? ";
					int i=1;
					//System.out.println("SQL_INSERT_INTO_COMPANY Store_id "+SQL_INSERT_INTO_COMPANY);
					PreparedStatement statement = null;
					statement = con.prepareStatement(SQL_INSERT_INTO_COMPANY);
					statement.setString(i++,year_end);
					statement.setString(i++,tax_prepared_by);
					statement.setString(i++,comments);
					statement.setString(i++,active);
					statement.setString(i++,account_lock);
					statement.setString(i++,tax_completed_date);
					statement.setString(i++,tax_filed_date);
					statement.setString(i++,tax_completed);
					statement.setString(i++,tax_filed);
					statement.setString(i++,companyCode);
					statement.setInt(i++,Integer.parseInt(storeId));
					statement.setString(i++,year);
					
					int count  = statement.executeUpdate();
					
					if (count==0)
					{
						
						String sql =	"INSERT INTO yearly_client_info (  company_code, store_id,year)  VALUES (?,?,? ) " ;

					int j=1;
					PreparedStatement statement2 = con.prepareStatement(sql);
					statement2.setString(j++,companyCode);
					statement2.setInt(j++,Integer.parseInt(storeId));
					statement2.setString(j++,year);
					count  = statement2.executeUpdate();
					System.out.println("inserting  "+line);
					System.out.println("inserting  "+count);
					}

				  }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      bufRdr.close();
		  
		  

	}
	public static Connection getConnectionFromTmcat() throws SQLException {
        Connection connection = null;
        String connectionURL = "jdbc:mysql://localhost:3306/AccountingPlus_db?autoReconnect=true";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "msm_dev", "msm_dev");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return connection;
    }	
}
