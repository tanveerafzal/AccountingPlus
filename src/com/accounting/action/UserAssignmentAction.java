package com.accounting.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.accounting.dao.ReferenceDao;
import com.accounting.form.MonthlyNoteForm;
import com.accounting.form.UserAssignmentForm;
import com.accounting.utils.JspConstants;

public class UserAssignmentAction extends BaseAction {
	public UserAssignmentAction(){}
	
	public ActionForward submit(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		try {
			UserAssignmentForm form = getUserAssignmentForm(req);
			ActionErrors actionErrors = new ActionErrors();
			System.out.println("getMonth "+form.getMonth());
			System.out.println("getYear "+form.getYear());
			
			if(isEmpty(form.getMonth())){
				actionErrors.add("getMonth", new ActionMessage("error.month.missing"));
			}
			if(isEmpty(form.getYear())){
				actionErrors.add("getYear", new ActionMessage("error.year.missing"));
			}
			
			
			saveErrors(req,actionErrors);
			
			if (actionErrors.isEmpty())
			{
				ReferenceDao dao  = new ReferenceDao();
		//		dao.addMonthlyNotes(form);
			}
			else
			{
				System.out.println("sending to login jsp ");
				return getForward(result, JspConstants.LOGIN_JSP);
			}
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return getForward(result, JspConstants.LOGIN_JSP);
	}
	
	
	
	
	public ActionForward getPayrollNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			System.out.println("getPayrollNotes called Type");
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			form.setStore_id(getStore_id(req));
			form.setNotesType(PAYROLL);
			form.setNotesDate(0);
			getNotes(form);
			return getForward(result, JspConstants.PAYROLL_NOTES_JSP);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.PAYROLL_NOTES_JSP);
	}
	
	public ActionForward getChargeNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			System.out.println("getChargeNotes called Type");
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			form.setStore_id(getStore_id(req));
			form.setNotesType(CHARGE);
			getNotes(form);
			return getForward(result, JspConstants.CHARGE_NOTES_JSP);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.CHARGE_NOTES_JSP);
	}
	
	public ActionForward getYearlyNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			System.out.println("getYearlyNotes called Type");
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			form.setStore_id(getStore_id(req));
			form.setNotesType(YEARLY);
			form.setNotesDate(0);
			getNotes(form);
			return getForward(result, JspConstants.YEARLY_NOTES_JSP);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.YEARLY_NOTES_JSP);
	}
	
	
	
	public void getNotes(MonthlyNoteForm form)  {
		try {
			System.out.println("getNotes called Type "+form.getNotesType());
			
			System.out.println("getMonth "+form.getMonth());
			System.out.println("getYear "+form.getYear());
			int month = getTodaysMonth();
			int year = getTodaysYear();
			if (form.getMonth()!= null && form.getMonth().length()>0)
			{
				month = Integer.parseInt(form.getMonth());	
			}
			if (form.getYear()!= null && form.getYear().length()>0)
			{
				year = Integer.parseInt(form.getYear());	
			}
			form.setMonth(""+month);
			form.setYear(""+year);
			System.out.println("month  "+month);
			System.out.println("year  "+year);
			ReferenceDao refDao = new ReferenceDao();
			ArrayList<MonthlyNoteForm> notes = refDao.getMonthlyNotes(month, year, form.getStatus(),form.getStore_id(), form.getNotesType(), form.getNotesDate());
			System.out.println("Total MonthlyNote   "+notes.size());
			form.setNotes(notes);
			System.out.println("sending to success jsp ");
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	


}
