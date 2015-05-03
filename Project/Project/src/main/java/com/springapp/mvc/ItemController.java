package com.springapp.mvc;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

@Controller
@RequestMapping("/")
public class ItemController
{
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.HEAD})
	public @ResponseBody String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");

		return "hello";
	}

	LostItemDaoImpl lostItemDao = new LostItemDaoImpl();

	@RequestMapping(value = "/reportlost/{userid}",method = {RequestMethod.POST,RequestMethod.HEAD} ,
			consumes = "application/json" , headers = "Accept=application/json, application/xml")
	public @ResponseBody String addLostItem(@PathVariable("userid") int userid,@RequestBody LostItem item)
	{
		System.out.println("in method");
		LostItemCounter.increment();
		lostItemDao.create(item, (int) FoundItemCounter.getCounter(), userid);

		return "success";
	}

	@RequestMapping(value = "/reportlost/{userid}", method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
	public @ResponseBody Collection<LostItem> register(@PathVariable("userid") String userid) {
		System.out.println("get1");
		LostItemDaoImpl jed = new LostItemDaoImpl();
		List<LostItem> items = jed.getItemList("select * from lost_item");
		for(LostItem item : items){
			System.out.println(item.getItem_name()+","+item.getItem_location());
		}
		return items;
	}

	@RequestMapping(value = "/sendmail",method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
	public @ResponseBody String sendmail(@RequestParam("userid")String userid , @RequestParam("itemid") LostItem item){
		System.out.println("in new method");
		int IDofPersonWithLostItem = lostItemDao.getUserID(item);
		String emailOfPersonWithLostItem = lostItemDao.getEmailID(IDofPersonWithLostItem);
		String emailOfPersonReportingLostItemAsFound = lostItemDao.getEmailID(Integer.parseInt(userid));

		String to = emailOfPersonWithLostItem;
		String from = "shayan10078@iiitd.ac.in";
		String host = "192.168.48.144";
		String subject = "Your item has been found!";
		String messageToRecipient = "Your item has been found by " + emailOfPersonReportingLostItemAsFound + ". " +
				"Please contact person for more details";

		//Get the session object
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(messageToRecipient);

			// Send message
			Transport.send(message);
			System.out.println("message sent successfully....");

		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return "success";
	}

	FoundItemDaoImpl foundItemDao = new FoundItemDaoImpl();

	@RequestMapping(value = "/sendmail",method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
	public @ResponseBody String sendmail(@RequestParam("userid")String userid , @RequestParam("itemid") FoundItem item){
		System.out.println("in new method");
		int IDofPersonReportingFoundItem = foundItemDao.getUserID(item);
		String emailOfPersonReportingFoundItem = foundItemDao.getEmailID(IDofPersonReportingFoundItem);
		String emailOfPersonClaimingLostItemAsFound = foundItemDao.getEmailID(Integer.parseInt(userid));

		String to = emailOfPersonReportingFoundItem;
		String from = "shayan10078@iiitd.ac.in";
		String host = "192.168.48.144";
		String subject = "Someone has claimed your found item!";
		String messageToRecipient = "Your item has been claimed by " + emailOfPersonClaimingLostItemAsFound + ". " +
				"Please contact person for more details";

		//Get the session object
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(messageToRecipient);

			// Send message
			Transport.send(message);
			System.out.println("message sent successfully....");

		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return "success";
	}


	@RequestMapping(value = "/reportfound/{userid}",method = {RequestMethod.GET,RequestMethod.HEAD} , produces = "application/json")
	public @ResponseBody
	Collection<FoundItem> getEvent(@PathVariable("userid") String userid)
	{
		FoundItemDaoImpl jed = new FoundItemDaoImpl();
		return jed.getItemList("select * from found_item");
	}

	/*LostItemDaoImpl lostItemDao = new LostItemDaoImpl();
	FoundItemDaoImpl foundItemDao = new FoundItemDaoImpl();
	//@Autowired
	private JavaMailSender mailSender;


	@RequestMapping(value = "/{userid}",method = {RequestMethod.POST,RequestMethod.HEAD} , consumes = "application/json" , headers = "Accept=application/json, application/xml")
	public @ResponseBody
	String addlostitem(@PathVariable("userid") int userid,@RequestBody LostItem item)
	{
		System.out.println("in method");
		lostItemDao.create(item);

		//return "sucess";
	}

	@RequestMapping(value="/")
	public ModelAndView listItem(ModelAndView model) throws IOException {
		List<LostItem> listItem = lostItemDao.getItemList();
		model.addObject("item", listItem);
		model.setViewName("itemList");
		return model;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView newItem(ModelAndView model)
	{
		LostItem newItem = new LostItem();
		model.addObject("item", newItem);
		model.setViewName("addItem");
		return model;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveItem(@ModelAttribute LostItem item) {
		if(item!=null)
			lostItemDao.create(item);
		return new ModelAndView("redirect:/getItems");
	}
	@RequestMapping("/getItems")
	public ModelAndView getItemList()
	{
		List<LostItem> itemList = lostItemDao.getItemList();
		return new ModelAndView("itemList", "itemList", itemList);
	}

	@RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
	public String doSendEmail(HttpServletRequest request) {
		// takes input from e-mail form
		String recipientAddress = request.getParameter("recipient");
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");

		// prints debug info
		System.out.println("To: " + recipientAddress);
		System.out.println("Subject: " + subject);
		System.out.println("Message: " + message);

		// creates a simple e-mail object
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message);

		// sends the e-mail
		mailSender.send(email);

		// forwards to the view named "Result"
		return "/getItems";
	}*/
}