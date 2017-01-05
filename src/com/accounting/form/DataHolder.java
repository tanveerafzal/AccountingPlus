package com.accounting.form;


public class DataHolder extends BaseData {
	private boolean selected; 
	private Object data;
	
	public DataHolder(){
		this(false, null);
	}
	
	public DataHolder(Object current){
		this(false, current);
	}
	
	public DataHolder(boolean sel, Object current){
		setData(current);
		setSelected(sel);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
