package Monitoringandcontrol;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {
	final static String user = "CPS.PARKING.PROJECT@gmail.com";// change accordingly
	final static String pass = "CPS123456789";
	public PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(user, pass);
	}
}
