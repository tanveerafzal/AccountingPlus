package com.accounting.utils;

public class MyException extends Exception {

	private String[] parameters; 
	private String code;
	
	public MyException(String code, String[] params, String description, Throwable cause) {
		super(description, cause);
		
		this.parameters = params;
		this.code = code;
	}
	
	public MyException(String code, String[] params, String description){
		this(code, params, description, null);
	}

	public String[] getParameters() {
		return parameters;
	}

	public String getCode(){
		return code;
	}
}