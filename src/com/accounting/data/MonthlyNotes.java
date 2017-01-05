package com.accounting.data;

import com.accounting.form.MonthlyNoteForm;



public class MonthlyNotes {
	

	ArrayList<MonthlyNoteForm>  notes = new ArrayList<MonthlyNoteForm> ();

	public ArrayList<MonthlyNoteForm> getNotes() {
		return notes;
	}

	public void setNotes(ArrayList<MonthlyNoteForm> notes) {
		this.notes = notes;
	}
	
	
}