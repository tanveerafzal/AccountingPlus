/*
 * Created on 23-Dec-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.accounting.dao;

import javax.activation.DataSource;


/**
 * @author Tanver
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DataAccessObject
{
   
  
 public static String MASTER_PAYROLL="MASTER_PAYROLL";
 public static String MASTER_CHARGE="MASTER_CHARGE";
 public static String MASTER="MASTER";
 public static String ALL="ALL";
 public static String YES="Y";
 public static String yes="y";
 public static String NO="N";
 
 public static String PAYROLL="PAYROLL";
 public static String CHARGE="CHARGE";
 public static String YEARLY="YEARLY";
 
 
 public DataAccessObject() {
     createDataSource();
 }

 
 
 public static int getRandomNumber()
 	{
	 Random random = new Random();
 	return random.nextInt();
 	}
	
	
 public static String getPreviousYear(String payrollYear) {
		System.out.println("getPreviousYear submitted for "+payrollYear);
		if (payrollYear != null)
		{
			int year = Integer.parseInt(payrollYear);
			year = year-1;
			return ""+year;
		}
		return null;
	}
 
    public String getString(ResultSet set, String field) throws SQLException
    {
        return set.getString(field);
    }

    public String getStringNVL(ResultSet set, String field) 
    {
    	String result="";
    	try{
    		result = set.getString(field);
    	}
    	catch(Exception ex){
    		//do nothing if the filed was not found
    	}
    	
        return result;
    }
    
    public boolean getBoolean(ResultSet set, String field) 
    {
    	boolean result=false;
    	
    	try{
    		String val = set.getString(field);
    		result=(val!=null && val.trim().equalsIgnoreCase("Y"))?true:false;
    	}
    	catch(Exception ex){
    		//do nothing if the filed was not found
    	}
    	
        return result;
    }

    public Long getLong(ResultSet set, String field) throws SQLException
    {
        String current = set.getString(field);

        return ((current == null) ? null : new Long(current));
    }
    
    public Double getDouble(ResultSet set, String field) throws SQLException {
        String current = set.getString(field); 
        return ((current == null) ? null : new Double(current));
    }

    public Date getDate(ResultSet set, String field) throws SQLException
    {
        Date result = null;
        Timestamp stamp = set.getTimestamp(field);
        
        if(stamp != null){
            result = new Date(stamp.getTime());
        }
        
        return result;
    }


    
    public void close(ResultSet set)
    {
        try {
            if (set != null) {
                set.close();
            }
        }
         catch (Exception e) {
            // no implementation needed
        }
    }

    public void close(Statement statement)
    {
        try {
            if (statement != null) {
                statement.close();
            }
        }
         catch (Exception e) {
            // no implementation needed
        }
    }
    
	protected void setDate(PreparedStatement statement, int index, Date value) throws SQLException {
        statement.setTimestamp(index, value == null ? null : new Timestamp(value.getTime()));
	}
	
    protected void setLong(PreparedStatement statement, int index, Long value) throws SQLException {
        if(value == null){
            statement.setString(index, null);
        }
        else {
            statement.setLong(index, value.longValue());
        }
    }

    protected void setDouble(PreparedStatement statement, int index, Double value) throws SQLException {
        if(value == null){
            statement.setString(index, null);
        }
        else {
            statement.setDouble(index, value.doubleValue());
        }
    }
	
	protected void setString(PreparedStatement statement, int index, String value) throws SQLException{
		statement.setString(index, value);
	}

  

    protected void jump(ResultSet set, int number) throws SQLException {
    	int index = 0; 
    
    	while(index < number && set.next()){
    		index ++;
    	}
    }






  
    protected String getStringLong(ResultSet set, String name) throws SQLException {
        String result = getString(set, name);
        return result == null ? "" : result;
    }
    
    protected String getStringDate(ResultSet set, String name) throws SQLException {
        String result = null;
        
        java.sql.Date date = set.getDate(name);
        if(date != null){
            SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy");
            format.setLenient(false);
            
            result = format.format(date);
        } 
        
        return result == null ? "" : result;
    }
    
    protected Double parseDouble(String current) throws Exception {
        Double result = null;
        
        try {
            if(current != null && current.trim().length() > 0){
                result = new Double(current.trim());
            }
        }
        catch(Exception e){
            throw new Exception(e);
        }
        
        return result;
    }
    
    protected boolean isFilled(String current)
    {
        return current != null && current.trim().length() > 0;
    }
    
    protected boolean isEmpty(String current)
    {
        return ! isFilled(current);
    }

    protected void processParams(PreparedStatement statement, ArrayList params) throws Exception, SQLException
    {
    	Iterator iterator = params.iterator();
    
    	int index = 0; 
    	while(iterator.hasNext()){
    		index ++; // so that the first index for the prepared statemetn will be 1, as needed
    		
    		Object current = iterator.next();
    		
    		if(current instanceof String){
    			setString(statement, index, (String) current);
    		}
    		else if(current instanceof Long){
    			setLong(statement, index, (Long) current);
    		}
    		else if(current instanceof Date){
    			setDate(statement, index, (Date) current);
    		}
    		else {
    			throw new Exception("can't process parameters for the following type: " + current.getClass().getName());
    		}
    	} // while iterator has next
    }

    protected boolean isYes(String string)
    {
        return "Y".equalsIgnoreCase(string);
    }

    protected String getYes(boolean value)
    {
        return value ? "Y" : "N";
    }

    protected String getSearchString(String current)
    {
        String result = current == null ? "" : current;
        
        result = result.replaceAll("\\*", "\\%");
        result = result.replaceAll("\\?", "\\_");
        
        return result;
    }
    
 
	public Connection getConnectionFromTmcat() throws SQLException {
        Connection connection = null;
        String connectionURL = "jdbc:mysql://localhost:3307/ussols?autoReconnect=true";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "ussols", "viewsonic");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return connection;
    }		
	
	private com.accounting.dao.DefaultDataSourceFactory dataSource;
	 private void createDataSource() {
			dataSource = new DefaultDataSourceFactory();
		}
	public Connection getConnection() throws SQLException {
        Connection connection = null;
        	//if (true)
        	//	return getConnectionFromTmcat();
            DataSource source = dataSource.getDataSource();
            connection = source.getConnection();
            connection.setAutoCommit(true);

        return connection;
    }    
   
}
