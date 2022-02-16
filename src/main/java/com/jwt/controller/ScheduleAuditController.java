package com.jwt.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import com.jwt.model.AuditArea;
import com.jwt.model.AuditTransaction;
import com.jwt.model.Employee;
import com.jwt.model.Ncr;
import com.jwt.model.ScheduleAudit;
import com.jwt.model.User;
import com.jwt.service.AuditAreaServiceImpl;
import com.jwt.service.AuditTransServiceImpl;
import com.jwt.service.EmployeeService;
import com.jwt.service.NcrServiceImpl;
import com.jwt.service.ScheduleAuditServiceImpl;
import com.jwt.service.UserServiceImpl;

import org.springframework.stereotype.Service;
@Controller
public class ScheduleAuditController {

	private static final Logger logger = Logger
			.getLogger(EmployeeController.class);

	public ScheduleAuditController() {
		System.out.println("ScheduleAuditController()");
	}


	@Autowired
    private JavaMailSender mailSender;
	@Autowired
	private ScheduleAuditServiceImpl scheduleAuditService;
	@Autowired
	private AuditTransServiceImpl auditTransService;
	
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private AuditAreaServiceImpl auditAreaService;
	@Autowired
	private NcrServiceImpl ncrServiceImpl;

	@Autowired
	private EmployeeService employeeService;

	
	@RequestMapping(value = "/InternalAudit")
	public ModelAndView internalAudit(ModelAndView model,HttpServletRequest request) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
        
		List<User> listUsers = userService.getAllUsers();
		List<AuditArea> listAuditAreas = auditAreaService.getAllAuditAreas();
		model.addObject("listUsers",listUsers );
		model.addObject("listAuditAreas",listAuditAreas );	
		int scheduleId = Integer.parseInt(request.getParameter("id"));
		ScheduleAudit schedule = scheduleAuditService.getSchedule(scheduleId);
		
		if(schedule.getSCHEDULE_STATE().equalsIgnoreCase("SUBMITTED")) {
			AuditTransaction tran= auditTransService.getAuditTransactionBySchedule(scheduleId);
			model.addObject("trans", tran);
		}
		else {
			
			AuditTransaction tran= auditTransService.getAuditTransactionBySchedule(scheduleId);
			if(tran==null) {
		 tran=new AuditTransaction();
		tran.setTrans_process_name("internal Audit");
		tran.setTrans_auditee_id(schedule.getSCHEDULE_AUDITOR_ID());
		tran.setTrans_date(schedule.getSCHEDULE_DATE());
		tran.setTrans_schedule_id(scheduleId);
		tran.setTrans_createdby_id(username);
		tran.setTrans_id(0);
			}
		model.addObject("trans", tran);
		}
		
		model.addObject("schedule", schedule);
		
//		model.setViewName("AuditPlan");
    	model.setViewName("InternalAudit");
	//	model.setViewName("NCR");
		return model;
	}

	@RequestMapping(value = "/newSchedule", method = RequestMethod.GET)
	public ModelAndView newSchedule(ModelAndView model) {
		
		List<User> listUsers = userService.getAllUsers();
		List<AuditArea> listAuditAreas = auditAreaService.getAllAuditAreas();
		model.addObject("listUsers",listUsers );
		model.addObject("listAuditAreas",listAuditAreas );
		ScheduleAudit schedule = new ScheduleAudit();
		schedule.setSCHEDULE_STATE("NEW");
		model.addObject("schedule", schedule);
		model.setViewName("AuditPlan");
		return model;
	}

	@RequestMapping(value = "/saveScheduleTrans", method = RequestMethod.POST)
	public ModelAndView saveScheduleTrans(@ModelAttribute AuditTransaction Audtrans) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		System.out.println("Operation is "+ Audtrans.getOperation());
		if(Audtrans.getTrans_id() == 0 &&Audtrans.getOperation().equalsIgnoreCase("Save")) {
			Audtrans.setTrans_audit_status("NEW");
			auditTransService.addAuditTransaction(Audtrans);
		}
		if(Audtrans.getTrans_id() == 0 &&Audtrans.getOperation().equalsIgnoreCase("SUBMIT")) {
			Audtrans.setTrans_audit_status("SUBMITTED");
	           auditTransService.addAuditTransaction(Audtrans);
	           
	           int scheduleId = Audtrans.getTrans_schedule_id();
	   		ScheduleAudit schedule = scheduleAuditService.getSchedule(scheduleId);
	   	    schedule.setSCHEDULE_STATE("SUBMITTED");
	   		scheduleAuditService.updateSchedule(schedule);
		}
		if(Audtrans.getTrans_id() > 0 &&Audtrans.getOperation().equalsIgnoreCase("Save")) {
			Audtrans.setTrans_audit_status("NEW");
			auditTransService.updateAuditTransaction(Audtrans);
		}
		if(Audtrans.getTrans_id() > 0 &&Audtrans.getOperation().equalsIgnoreCase("SUBMIT")) {
			Audtrans.setTrans_audit_status("SUBMITTED");
			auditTransService.updateAuditTransaction(Audtrans);
	           
	           int scheduleId = Audtrans.getTrans_schedule_id();
	   		ScheduleAudit schedule = scheduleAuditService.getSchedule(scheduleId);
	   	    schedule.setSCHEDULE_STATE("SUBMITTED");
	   		scheduleAuditService.updateSchedule(schedule);
		}
				
		return new ModelAndView("redirect:/allAcceptedSchedules");
	}

	
	@RequestMapping(value = "/NCR")
	public ModelAndView listEmployee(ModelAndView model) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		List<User> listUsers = userService.getAllUsers();
		List<AuditArea> listAuditAreas = auditAreaService.getAllAuditAreas();
		model.addObject("listUsers",listUsers );
		model.addObject("listAuditAreas",listAuditAreas );
		Ncr ncr = new Ncr();
		ncr.setNcr_status("NEW");
		ncr.setNcr_initiator(username);
		model.addObject("schedule", ncr);
			model.setViewName("NCR");
		return model;
	}
	@RequestMapping(value = "/EditNCR")
	public ModelAndView EditNcr(ModelAndView model,HttpServletRequest request) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		List<User> listUsers = userService.getAllUsers();
		List<AuditArea> listAuditAreas = auditAreaService.getAllAuditAreas();
		model.addObject("listUsers",listUsers );
		model.addObject("listAuditAreas",listAuditAreas );
		int ncrId = Integer.parseInt(request.getParameter("id"));
		Ncr ncr = ncrServiceImpl.getNcr(ncrId);
		model.addObject("schedule", ncr);
			model.setViewName("NCR");
		return model;
	}
	
	@RequestMapping(value = "/saveSchedule", method = RequestMethod.POST)
	public ModelAndView saveSchedule(@ModelAttribute ScheduleAudit schedule) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		if (schedule.getSCHEDULE_ID() == 0) { // if employee id is 0 then creating the
            schedule.setSCHEDULE_STATE("NEW");
            schedule.setSCHEDULE_ADMIN_ID(authentication.getName());
			scheduleAuditService.addSchedule(schedule);
		} else {
            schedule.setSCHEDULE_STATE("NEW");
            schedule.setSCHEDULE_ADMIN_ID(authentication.getName());
			scheduleAuditService.updateSchedule(schedule);
		}
				

		// SimpleMailMessage message = new SimpleMailMessage();
		 try {
			 System.out.println("before Send HTML  Email ");
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				
				 System.out.println(((org.springframework.mail.javamail.JavaMailSenderImpl)mailSender).getUsername());
				 System.out.println(((org.springframework.mail.javamail.JavaMailSenderImpl)mailSender).getPassword());

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "utf-8");
			helper.setTo(new String[]{"m.ibrahim@mts-mea.com","memam.quality@gmail.com"});
			helper.setSubject("New Audit Has Been Created");
			 helper.setText("Dear All , kindly inform you that the  admin <b>"+username+"</b> arranged an audit at <b>"+schedule.getSCHEDULE_DATE()+"</b>  </br> "
		        		+"Auditor : <b>"+schedule.getSCHEDULE_AUDITOR_ID()+"</b>"+
		        		"</br>"+"Auditee : <b>"+schedule.getSCHEDULE_AUDITEE_ID()+"</br>"+
		        		"Audit Area : <b>"+schedule.getSCHEDULE_AUDITAREA()+"</br>",true);
		        mailSender.send(mimeMessage);
		        System.out.println("after Send HTML  Email ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	        

		return new ModelAndView("redirect:/allSchedules");
	}

	
	
	@RequestMapping(value = "/saveNCR", method = RequestMethod.POST)
	public ModelAndView saveNCR(@ModelAttribute Ncr schedule) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
         System.out.println("Op is" +schedule.getOperation());
		if (schedule.getID()==null|| schedule.getID() == 0) { // if employee id is 0 then creating the
			schedule.setNcr_status("NEW");
			 ncrServiceImpl.addNcr(schedule);
			//scheduleAuditService.addSchedule(ncr);
		} else  {
                if(schedule.getOperation().equalsIgnoreCase("SAVE")) {
                	ncrServiceImpl.addNcr(schedule);
                }
                else {
                	schedule.setNcr_status("CLOSED");
                    	ncrServiceImpl.addNcr(schedule);
                    
                }
			//scheduleAuditService.updateSchedule(ncr);
		}
	
	        

		return new ModelAndView("redirect:/allNCRS");
	}

	
	@RequestMapping(value = "/deleteSchedule", method = RequestMethod.GET)
	public ModelAndView deleteSchedule(HttpServletRequest request) {
		int scheduleId = Integer.parseInt(request.getParameter("id"));
		scheduleAuditService.deleteSchedule(scheduleId);
		return new ModelAndView("redirect:/allSchedules");
	}

	@RequestMapping(value = "/editSchedule", method = RequestMethod.GET)
	public ModelAndView editSchedule(HttpServletRequest request) {
		int scheduleId = Integer.parseInt(request.getParameter("id"));
		ScheduleAudit schedule = scheduleAuditService.getSchedule(scheduleId);
	    System.out.println(schedule.getSCHEDULE_STATE());
		ModelAndView model = new ModelAndView("AuditPlan");
		model.addObject("schedule", schedule);
		List<User> listUsers = userService.getAllUsers();
		List<AuditArea> listAuditAreas = auditAreaService.getAllAuditAreas();
		model.addObject("listUsers",listUsers );
		model.addObject("listAuditAreas",listAuditAreas );
		System.out.println("After Add ListUsers");
		return model;
	}
	@RequestMapping(value = "/AcceptAuditor", method = RequestMethod.GET)
	public ModelAndView AcceptScheduleAuditor(HttpServletRequest request) {
		int scheduleId = Integer.parseInt(request.getParameter("id"));
		ScheduleAudit schedule = scheduleAuditService.getSchedule(scheduleId);
	    schedule.setSCHEDULE_STATE("AUDITOR_ACCEPTED");
		scheduleAuditService.updateSchedule(schedule);
		return new ModelAndView("redirect:/allSchedules");
		
	}
	@RequestMapping(value = "/RejectAuditor", method = RequestMethod.GET)
	public ModelAndView RejectScheduleAuditor(HttpServletRequest request) {
		int scheduleId = Integer.parseInt(request.getParameter("id"));
		ScheduleAudit schedule = scheduleAuditService.getSchedule(scheduleId);
	    schedule.setSCHEDULE_STATE("AUDITOR_REJECT");
		scheduleAuditService.updateSchedule(schedule);
		return new ModelAndView("redirect:/allSchedules");
	}
	
	@RequestMapping(value = "/AcceptAuditee", method = RequestMethod.GET)
	public ModelAndView AcceptScheduleAuditee(HttpServletRequest request) {
		int scheduleId = Integer.parseInt(request.getParameter("id"));
		ScheduleAudit schedule = scheduleAuditService.getSchedule(scheduleId);
	    schedule.setSCHEDULE_STATE("ACCEPTED");
		scheduleAuditService.updateSchedule(schedule);
		return new ModelAndView("redirect:/allSchedules");
	}
	@RequestMapping(value = "/RejectAuditee", method = RequestMethod.GET)
	public ModelAndView RejectScheduleAuditee(HttpServletRequest request) {
		int scheduleId = Integer.parseInt(request.getParameter("id"));
		ScheduleAudit schedule = scheduleAuditService.getSchedule(scheduleId);
	    schedule.setSCHEDULE_STATE("AUDITEE_REJECTED");
		scheduleAuditService.updateSchedule(schedule);
		return new ModelAndView("redirect:/allSchedules");
	}
	
	@RequestMapping(value = "/allSchedules", method = RequestMethod.GET)
	public ModelAndView allSchedules(ModelAndView model,SecurityContextHolderAwareRequestWrapper requestWrapper) throws IOException {
		List<ScheduleAudit> listSchedules=null;
		if(requestWrapper.isUserInRole("ROLE_ADMIN")) {
		 listSchedules = scheduleAuditService.getAllSchedules();
		}
		else {
			 listSchedules = scheduleAuditService.getAllCurrentUser(requestWrapper.getUserPrincipal().getName());

		}
        System.out.println("New One");
		model.addObject("listSchedules", listSchedules);
		// MAGED: create new page for all schedules like home page 
		model.setViewName("allSchedules");
		return model;
	}

	@RequestMapping(value = "/allSchedulesCal", method = RequestMethod.GET)
	public ModelAndView allSchedulesCal(ModelAndView model,SecurityContextHolderAwareRequestWrapper requestWrapper) throws IOException {
		List<ScheduleAudit> listSchedules=null;
		if(requestWrapper.isUserInRole("ROLE_ADMIN")) {
		 listSchedules = scheduleAuditService.getAllSchedules();
		}
		else {
			 listSchedules = scheduleAuditService.getAllCurrentUser(requestWrapper.getUserPrincipal().getName());

		}
        System.out.println("New One");
		model.addObject("listSchedules", listSchedules);
		// MAGED: create new page for all schedules like home page 
		model.setViewName("Calendar");
		return model;
	}

	
	@RequestMapping(value = "/allAcceptedSchedules", method = RequestMethod.GET)
	public ModelAndView allAcceptedSchedules(ModelAndView model,SecurityContextHolderAwareRequestWrapper requestWrapper) throws IOException {
		List<ScheduleAudit> listSchedules=null;
		if(requestWrapper.isUserInRole("ROLE_ADMIN")) {
		 listSchedules = scheduleAuditService.getAllAcceptedSchedules();
		}
		else {
			 listSchedules = scheduleAuditService.getAllCurrentUser(requestWrapper.getUserPrincipal().getName());

		}
        System.out.println("New One");
        
		model.addObject("listSchedules", listSchedules);
		// MAGED: create new page for all schedules like home page 
		model.setViewName("allAcceptedSchedules");
		return model;
	}

	
	@RequestMapping(value = "/allNCRS", method = RequestMethod.GET)
	public ModelAndView allNCRS(ModelAndView model,SecurityContextHolderAwareRequestWrapper requestWrapper) throws IOException {
		List<Ncr> listNCR=null;
		//if(requestWrapper.isUserInRole("ROLE_ADMIN")) {
		listNCR = ncrServiceImpl.getAllNcrs();
		//}
	//	else {
		//	 listSchedules = scheduleAuditService.getAllCurrentUser(requestWrapper.getUserPrincipal().getName());

		//}
        System.out.println("New One");
        
		model.addObject("listNCR", listNCR);
		// MAGED: create new page for all schedules like home page 
		model.setViewName("allNCRS");
		return model;
	}

	
}