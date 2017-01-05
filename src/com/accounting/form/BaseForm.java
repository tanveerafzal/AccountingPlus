package com.accounting.form;


import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.struts.action.ActionForm;

import com.accounting.data.UserDataInfo;

public abstract class BaseForm extends ActionForm {

	public BaseForm(){
		
		month = getTodaysMonthString();
		year = ""+getTodaysYear();
		System.out.println("month "+month);
		
	}

	private String month;
	private String year;
	double gstRate = 0.0;
	
	private String clientStatusActive;
	
	
	 
	 public String getTodaysMonthString() {
		 Date today = new Date();
		 String month = ""+(today.getMonth()+1);
		 if (month!= null && month.length()==1)
			 month = "0"+month;
		return month;
	}
		
		public int getTodaysYear() {
			 Date today = new Date();
				return today.getYear()+1900;
		}
		
		
	public String getClientStatusActive() {
		return clientStatusActive;
	}

	public void setClientStatusActive(String clientStatusActive) {
		this.clientStatusActive = clientStatusActive;
	}

	public double getGstRate() {
		return gstRate;
	}

	public void setGstRate(double gstRate) {
		this.gstRate = gstRate;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String getMonthName() {
		return getMonthName(month);
	}

	public void setMonthName(String monthName) {
		
	}
	public String getNull(String current) {
		return current == null || current.trim().length() == 0 
					? null : current;
	}
	
public String getMonthName(String month2) {
		
		try{
			System.out.println("getMonthName "+month2);
			int mInt = Integer.parseInt(month2);
			if (mInt > 0)
			{
				switch (mInt) {
	            case 1:  return "JAN"  ; 
	            case 2:  return "FEB"; 
	            case 3:  return "MAR"; 
	            case 4:  return "APR"; 
	            case 5:  return "MAY"; 
	            case 6:  return "JUN"; 
	            case 7:  return "JUL"; 
	            case 8:  return "AUG"; 
	            case 9:  return "SEP"; 
	            case 10: return "OCT"; 
	            case 11: return "NOV"; 
	            case 12: return "DEC"; 
	            default: return "Invalid month.";
			}
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return "Invalid month.";
	}

	public static String encode(Object crt){
		String result = null; 
		
		try {
			ByteArrayOutputStream output;
			output = new ByteArrayOutputStream(1024);
			ObjectOutputStream stream = new ObjectOutputStream(output);
			
			stream.writeObject(crt);
			stream.flush();
			output.close();

			byte[] current = output.toByteArray();
		
			// get the base 64 value
			result = new BASE64Encoder().encode(current);
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	public double format(double value)
	{
		NumberFormat formatter = new DecimalFormat("#0.00");
		return Double.parseDouble(formatter.format(value));
	}
	public static Object decode(String current){
		Object result = null;

		if(current != null && current.length() > 0){
			try {
				byte[] bytes = new BASE64Decoder().decodeBuffer(current);
				ByteArrayInputStream input = new ByteArrayInputStream(bytes);
				
				ObjectInputStream stream = new ObjectInputStream(input);
				result = stream.readObject();
			} 
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return result;
	}
	
	public String getSerial(){
		return encode(this);
	}
	
	public void setSerial(){
	}
	
	public static Long parseLong(String current, String key) throws Exception {
		Long result = null;
		
		
			if(current != null && current.trim().length() > 0){
				result = new Long(current);
			}
		return result;
	}
    
	public static Double parseDouble(String current) throws Exception {
		Double result = null;
			if(current != null && current.trim().length() > 0){
				result = new Double(current);
			}
		return result;
	}
	
	public static String formatDate(Date current){
		String result = null;
		
		if(current != null){
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
            format.setLenient(false);
            
			result = format.format(current);
		}
		
		return result;
	}
	
	public static String formatLong(Long current){
		String result = null;
		
		if(current != null){
			result = current.toString();
		}
		
		return result;
	}
	
	public abstract void clear();
    
    protected void log(Object message){
        try {
            System.out.println(message.toString());
        }
        catch(Exception e){
            // nothing - logging should not affect app functionality
        }
    }
    
    protected void log(Object message, Throwable error){
        try {
        	System.out.println(error.toString());
        }
        catch(Exception e){
            // nothing - logging should not affect app functionality
        }
    }

	protected TreeMap<Object, Object> getTreeMap(ArrayList<String> items) {
		TreeMap<Object, Object> result = new TreeMap<Object, Object>();
		
		Iterator<String> iterator = items.iterator();
		while(iterator.hasNext()){
			String current = iterator.next();
			
			if(current != null){
				result.put(current, "");
			}
		}
		
		return result;
	}

	protected TreeMap<Object, Object> getTreeMap(String[] items) {
		TreeMap<Object, Object> result = new TreeMap<Object, Object>();
		

		for(String current : items){			
			if(current != null){
				result.put(current, "");
			}
		}
		
		return result;
	}
	
	protected String getStringList(Collection items, String methodName, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		
		if(items != null){
			boolean first = true; 
			Iterator iterator = items.iterator();
			
			while(iterator.hasNext()){
				Object current = iterator.next();
				
				try {
					Class cls = current.getClass();
					Method mt = cls.getMethod(methodName, new Class[]{});
					
					Object item = mt.invoke(current, new Object[]{});
					
					// null items will not be displayed
					if(item != null){
						if(first){
							first = false;
						}
						else {
							buffer.append(delimiter);
						}
						
						buffer.append(item.toString());
					}
				}
				catch(Exception e){
					// no implementation required
				}
			}
		}
		
		return buffer.toString();
	}

	public String[] getArrayListIds(Collection items, String methodName) {
		ArrayList<String> result = new ArrayList<String>();
		
		if(items != null){ 
			Iterator iterator = items.iterator();
			
			while(iterator.hasNext()){
				Object current = iterator.next();
				
				try {
					Class cls = current.getClass();
					Method mt = cls.getMethod(methodName, new Class[]{});
					
					Object item = mt.invoke(current, new Object[]{});
					
					// null items will not be displayed
					if(item != null){
						result.add(item.toString());
					}
				}
				catch(Exception e){
					// no implementation required
				}
			}
		}
		
		return (String[])result.toArray(new String[]{});
	}
	
	
	protected TreeMap<String, String> getStringMap(String[] selected) {
		TreeMap<String, String> map = new TreeMap<String, String>();
		
		for(int count = 0; count < selected.length; count ++){
			String current = selected[count];
			
			map.put(current, "");
		}
		return map;
	}
	 public  ArrayList<UserDataInfo> removeDuplicateUsers(ArrayList<UserDataInfo> users) {
		   
		   ArrayList<UserDataInfo> newUsersList = new ArrayList<UserDataInfo>();
		   HashMap map = new HashMap();
		   if (users == null || users.size() == 0)
			   return newUsersList;
		   
		   for (int i=0; i< users.size(); i++ )
			{
			   UserDataInfo user = users.get(i);
			   System.out.println("Checking  "+user.getLogin());
			   if (map.containsKey(user.getLogin()))
			   {
				   System.out.println("Updateing "+user.getLogin());
				   UserDataInfo item =  (UserDataInfo) map.get(user.getLogin());
				   item.getRoles().add(""+user.getRole());
			   }else
			   {
				   System.out.println("inserting into map "+user.getLogin());
				   user.getRoles().add(""+user.getRole());
				   map.put(user.getLogin(), user);
			   }
			}
		   newUsersList.addAll(map.values());
		   return newUsersList;
	}	
}
