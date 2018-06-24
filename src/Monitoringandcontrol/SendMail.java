package Monitoringandcontrol;
import javax.mail.*;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class SendMail 
{
	/*********************************************************************************************/
	public static void send(String to, String subject, String msg) 
	{
		final String user = "CPS.PARKING.PROJECT@gmail.com";// change accordingly
		final String pass = "CPS123456789";
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");     
		props.setProperty("mail.host", "smtp.gmail.com");  
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth", "true");    
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.fallback", "false");  
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(user, pass);
			}
		});
		try 
		{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(msg);
			Transport.send(message);
		}
		catch (MessagingException e)
		{
			throw new RuntimeException(e);
		}
	}
	/*********************************************************************************************/
	public static void sendLateAlertMessage(String orderID ,String StartDate,String ParkingID,String email)
	{	
		String subject = "Late for parking";
		String msg = "Hello " + orderID  + ",\n"
				+ "According to the information we have, You have reserved a parking on " + ParkingID + " Parking Lot "
				+ "at " + StartDate + " today.\n"
				+ "You got this email as a reminder for being late.\nYou are asked to head to the lot within 30 minutes before the reservation being canceled.\nThank you,\nCPS team.";
		SendMail.send(email, subject, msg);
		return;
	}
	/*********************************************************************************************/
	public static void sendSubscriptionRenewEmail(String SubscribeID ,String EndDate,String email,boolean IsBusnisess)
	{
		String subject = "Subscription Renewal";
		String Busniess;
		Busniess = IsBusnisess ? 
				"your Business Subscription "
				: "your Full subscription";		
		String msg = "Hello SubscribeID : " + SubscribeID  + ",\n"
				+ "According to the information we have, " + Busniess
				+" will expire in " + EndDate+ ".\n"
				+ "If you wish to continue using our services, please renew your subscription.\n\n"
				+ "Thank you,\nCPS team.";

		SendMail.send(email, subject, msg);
		return;
	}
	/*********************************************************************************************/
	public static void sendExcessionEmail(String orderID ,String enddate,String ParkingID,String email)
	{	
		String subject = "Parking Excession Alert";
		String msg = "Hello " + orderID  + ",\n"
				+ "According to the information we have, You have exceeded your parking limit time : "+ enddate + " at Parking : "+ ParkingID
				+ ",\n You are asked to come and collect your car as fast as possible.\nThank you,\nCPS team.";
		SendMail.send(email, subject, msg);	
	}
	/*********************************************************************************************/
	public static void sendAlertForComplaintEmail(String ComplaintsID ,String start,String WorkerID,String email,String ParkingID)
	{	
		String subject = "Alert to Handle Complaint ";
		String msg = "Hello Worker : "+ WorkerID +" you have to handle Complaint :" + ComplaintsID  + ",\n"
				+ "We got the complaint in : "+ start + " in parking :"+ParkingID +" you need  to handle it in 24 hours .";
		SendMail.send(email, subject, msg);	
	}
	/*********************************************************************************************/

}