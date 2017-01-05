package com.accounting.form;

public class UserAssignmentForm extends BaseForm {

	int storeId;
	int id;
	int assignment_id;
	String login;
	String assignment_type;
	String assignment_completed;
	RegisterForm completed_date = null;
	String status;
	String updated_by;
	
	
	
	
	public UserAssignmentForm(){
		
		
		
	 
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	public String getAssignment_completed() {
		return assignment_completed;
	}
	public void setAssignment_completed(String assignment_completed) {
		this.assignment_completed = assignment_completed;
	}
	public int getAssignment_id() {
		return assignment_id;
	}
	public void setAssignment_id(int assignment_id) {
		this.assignment_id = assignment_id;
	}
	public String getAssignment_type() {
		return assignment_type;
	}
	public void setAssignment_type(String assignment_type) {
		this.assignment_type = assignment_type;
	}
	public RegisterForm getCompleted_date() {
		return completed_date;
	}
	public void setCompleted_date(RegisterForm completed_date) {
		this.completed_date = completed_date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}




}
