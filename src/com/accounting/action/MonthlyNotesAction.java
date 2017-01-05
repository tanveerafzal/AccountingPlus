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
import com.accounting.utils.JspConstants;

public class MonthlyNotesAction extends BaseAction {
	public MonthlyNotesAction(){}
	
	public ActionForward submit(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		
		try {
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			ActionErrors actionErrors = new ActionErrors();
			System.out.println("getMonth "+form.getMonth());
			System.out.println("getYear "+form.getYear());
			
			if(isEmpty(form.getMonth())){
				actionErrors.add("getMonth", new ActionMessage("error.month.missing"));
			}
			if(isEmpty(form.getYear())){
				actionErrors.add("getYear", new ActionMessage("error.year.missing"));
			}
			if(isEmpty(form.getText())){
				actionErrors.add("getText", new ActionMessage("error.text.missing"));
			}
			
			if(isEmpty(form.getNotesType())){
				actionErrors.add("getNotesType", new ActionMessage("error.notestype.missing"));
			}
			
			saveErrors(req,actionErrors);
			System.out.println("erros getNotesType  "+form.getNotesType());
			
			if (actionErrors.isEmpty())
			{
				ReferenceDao dao  = new ReferenceDao();
				dao.addMonthlyNotes(form);
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
			String monthString = getTodaysMonthString();
			int month = getTodaysMonth();
			int year = getTodaysYear();
			if (form.getMonth()!= null && form.getMonth().length()>0)
			{
				monthString = form.getMonth();
				month = Integer.parseInt(monthString);	
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
			form.setMonth(monthString);
			System.out.println("sending to success jsp ");
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	

	public ActionForward gotoAddPayrollNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			System.out.println("gotoAddPayrollNotes called Type");
			
			req.getSession().setAttribute("monthlyNoteForm",null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
	}

	public ActionForward gotoAddChargeNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			System.out.println("gotoAddChargeNotes called Type");
			req.getSession().setAttribute("monthlyNoteForm",null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.ADD_CHARGE_NOTES_JSP);
	}
	
	public ActionForward gotoAddYearlyNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			System.out.println("gotoAddYearlyNotes called Type");
			
			req.getSession().setAttribute("monthlyNoteForm",null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.ADD_YEARLY_NOTES_JSP);
	}
	
	
	
	
	public ActionForward addPayrollNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			System.out.println("addPayrollNotes called Type "+form.getNotesType());
			System.out.println("addPayrollNotes called getStatus "+form.getStatus());
			ActionErrors actionErrors  = validate(form,req);
			if (!actionErrors.isEmpty())
			{
				System.out.println("changeNotes Error ");
				return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
			}
			ReferenceDao dao = new ReferenceDao();
			form.setCreateBy(getLoginName(req));
			form.setStore_id(getStore_id(req));
			form.setCreatedDate(getTodaysDate());
			dao.addMonthlyNotes(form);
			return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
	}
	
	public ActionForward addChargeNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			System.out.println("addChargeNotes called Type "+form.getNotesType());
			System.out.println("addChargeNotes called getStatus "+form.getStatus());
			ActionErrors actionErrors  = validate(form,req);
			if (!actionErrors.isEmpty())
			{
				System.out.println("changeNotes Error ");
				return getForward(result, JspConstants.ADD_CHARGE_NOTES_JSP);
			}
			ReferenceDao dao = new ReferenceDao();
			form.setCreateBy(getLoginName(req));
			form.setStore_id(getStore_id(req));
			form.setCreatedDate(getTodaysDate());
			dao.addMonthlyNotes(form);
			return getForward(result, JspConstants.ADD_CHARGE_NOTES_JSP);
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.ADD_CHARGE_NOTES_JSP);
	}
	
	public ActionForward addYearlyNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			System.out.println("addYearlyNotes called Type "+form.getNotesType());
			System.out.println("addYearlyNotes called getStatus "+form.getStatus());
			ActionErrors actionErrors  = validate(form,req);
			if (!actionErrors.isEmpty())
			{
				System.out.println("changeNotes Error ");
				return getForward(result, JspConstants.ADD_YEARLY_NOTES_JSP);
			}
			ReferenceDao dao = new ReferenceDao();
			form.setCreateBy(getLoginName(req));
			form.setStore_id(getStore_id(req));
			form.setCreatedDate(getTodaysDate());
			dao.addMonthlyNotes(form);
			return getForward(result, JspConstants.ADD_YEARLY_NOTES_JSP);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.ADD_YEARLY_NOTES_JSP);
	}
	
	public ActionForward gotoChangeNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			System.out.println("gotoChangeNotes called for "+form.getEditId());
			
			MonthlyNoteForm currentForm = form.getNotesFromList(Integer.parseInt(form.getEditId()));
			System.out.println("Note Type "+currentForm.getNotesType());
			req.getSession().setAttribute("monthlyNoteForm",currentForm);
			System.out.println("sending to "+getPageByNoteType(currentForm));
			return getForward(result, getPageByNoteType(currentForm));
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		System.out.println("gotoChangeNotes ERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR ");
		return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
	}
	
	
	public ActionForward changeNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
		try {
			System.out.println("changeNotes called Type");
			MonthlyNoteForm form = getMonthlyNoteForm(req);
			ActionErrors actionErrors  = validate(form,req);
			System.out.println("changeNotes validate Done "+actionErrors.size());
			
			if (!actionErrors.isEmpty())
			{
				System.out.println("changeNotes Error ");
				return getForward(result, getPageByNoteType(form));
				//return getForward(result, JspConstants.ADD_NEW_NOTES_JSP);
			}
			
			System.out.println("getMonth "+form.getMonth());
			System.out.println("getYear "+form.getYear());
			System.out.println("changeNotes called Type "+form.getNotesType());
			ReferenceDao dao = new ReferenceDao();
			dao.updateMonthlyNotes(form);
			return getForward(result, getPageByNoteType(form));
			//return getForward(result, JspConstants.ADD_NEW_NOTES_JSP);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		return getForward(result, JspConstants.ADD_PAYROLL_NOTES_JSP);
	}
	
	
	public ActionErrors validate(MonthlyNoteForm form, HttpServletRequest req)  {
		ActionErrors actionErrors = new ActionErrors();
		try {
			
			System.out.println("validate "+form.getMonth());
			System.out.println("validate "+form.getYear());
			
			if(isEmpty(form.getMonth())){
				actionErrors.add("getMonth", new ActionMessage("error.month.missing"));
			}
			if(isEmpty(form.getYear())){
				actionErrors.add("getYear", new ActionMessage("error.year.missing"));
			}
			if(isEmpty(form.getText())){
				actionErrors.add("getText", new ActionMessage("error.text.missing"));
			}
			if(isEmpty(form.getNotesType())){
				actionErrors.add("getNotesType", new ActionMessage("error.notestype.missing"));
			}
			if(form.getText() != null && form.getText().length()> 1999){
				actionErrors.add("getText", new ActionMessage("error.text.too.long"));
			}			
			saveErrors(req,actionErrors);
			System.out.println("erros  "+actionErrors.size());
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		return actionErrors;
	}
		
	public void deleteNotes(ActionForm frm)  {
		try {
			MonthlyNoteForm form = (MonthlyNoteForm) frm;
			ReferenceDao dao = new ReferenceDao();
			MonthlyNoteForm deleteNote = form.getNotesFromList(Integer.parseInt(form.getEditId()));
			dao.deleteMonthlyNotes(deleteNote);
			form.removeNotesFromList(deleteNote.getId());
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
	
	public ActionForward deleteChargeNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		deleteNotes(frm);
		return getForward(null, JspConstants.CHARGE_NOTES_JSP);
	}
	public ActionForward deleteYearlyNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		deleteNotes(frm);
		return getForward(null, JspConstants.YEARLY_NOTES_JSP);
	}
	
	
	
	public ActionForward deletePayrollNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		MonthlyNoteForm form = (MonthlyNoteForm) frm;
//		for (int j=0; j< form.getDeleteNotes().length; j++)
//		{
//			if (form.getDeleteNotes()[j] == null)	continue;
//			System.out.println("Deleted Companies are "+form.getDeleteNotes()[j]);
//			
//		}
			
		deleteNotes(frm);
		return getForward(null, JspConstants.PAYROLL_NOTES_JSP);
	}
	
	public ActionForward printPayrollNotes(ActionMapping mapping, ActionForm frm, 
			
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
			return getForward(result, JspConstants.PAYROLL_NOTES_PRINT_JSP);
	}

	public ActionForward printChargeNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
			return getForward(result, JspConstants.CHARGE_NOTES_PRINT_JSP);
	}

	public ActionForward printYearlyNotes(ActionMapping mapping, ActionForm frm, 
			HttpServletRequest req, HttpServletResponse resp)  {
		ActionForward result = null;
			return getForward(result, JspConstants.YEARLY_NOTES_PRINT_JSP);
	}
	
	
	
}
