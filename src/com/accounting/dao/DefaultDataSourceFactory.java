package com.accounting.dao;

import javax.activation.DataSource;

import org.apache.commons.chain.Context;



public class DefaultDataSourceFactory  {

    public DataSource getDataSource() throws SQLException
    {
        DataSource source = null;
        
        try {
        	Context initial = new InitialContext();
        	//Context env = (Context) initial.lookup("java:comp/env");
            
        	//Object s = initial.lookup("DefaultDS");
        	
        Object s = initial.lookup("jdbc.AccountingPlus.data");
        	
            source = (DataSource) PortableRemoteObject.narrow(s, DataSource.class);
        }catch(NamingException e){
        	
        	e.printStackTrace();
        }
        
        return source;
    }
}
