package com.accounting.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.accounting.dao.ManagementDao;
import com.accounting.data.UserDataInfo;



public class MonthlyNoteForm  extends BaseForm {
	
	int id;
	String status;
	String text;
	String notesType;
	private int  store_id;
	String createBy;
	String createdDate;
	private String editId;
	private String operation;
	private String assignedTo;
	int notesDate=0;
	private boolean checked;
	private String[] deleteNotes = new String[1000];
	
	
	private ArrayList<MonthlyNoteForm> notes = new ArrayList<MonthlyNoteForm>();
	private ArrayList<UserDataInfo> employeelist = new ArrayList<UserDataInfo>();
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getNotesType() {
		return notesType;
	}
	public void setNotesType(String notesType) {
		this.notesType = notesType;
	}
	


	public void reset(ActionMapping mapping,HttpServletRequest request) {
			checked = false;
			}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<MonthlyNoteForm> getNotes() {
		return notes;
	}
	public void setNotes(ArrayList<MonthlyNoteForm> notes) {
		this.notes = notes;
	}
	public String getEditId() {
		return editId;
	}
	public void setEditId(String editId) {
		this.editId = editId;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public MonthlyNoteForm getNotesFromList(int id) {
		if (notes == null) return null;
		for (int i = 0; i < notes.size(); i++)
		{
			MonthlyNoteForm comp = (MonthlyNoteForm)notes.get(i);
			if (id == comp.getId())
					return comp;
		}
		return null;	
	}
	public void removeNotesFromList(int id) {
		for (int i = 0; i < notes.size(); i++)
		{
			MonthlyNoteForm comp = (MonthlyNoteForm)notes.get(i);
			if (id == comp.getId())
			{
				notes.remove(comp);
				return;
			}
		}
	}
	public int getNotesDate() {
		return notesDate;
	}
	public void setNotesDate(int notesDate) {
		this.notesDate = notesDate;
	}
	@Override
	public void clear() {
		deleteNotes = new String[1000];
	}
	public String[] getDeleteNotes() {
		return deleteNotes;
	}
	public void setDeleteNotes(String[] deleteNotes) {
		this.deleteNotes = deleteNotes;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public ArrayList<UserDataInfo> getEmployeelist() {
		if (employeelist != null && employeelist.size()  > 0 )
			return employeelist;
		ManagementDao dao  = new ManagementDao();
		ArrayList<UserDataInfo>  newUsersList=null;
		try {
			employeelist = dao.getRegisteredUsers(101);
			newUsersList =  removeDuplicateUsers(employeelist);
			System.out.println("updatePayroll YEAR "+newUsersList.size());
			this.employeelist = newUsersList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  newUsersList;
	}
}