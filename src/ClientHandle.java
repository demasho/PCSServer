import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ClientHandle implements Runnable {
  
	ConnectionToClient client ;
	String msg ;
	public ClientHandle(Object msg, ConnectionToClient client)
	{
		this.msg = msg.toString();
		this.client = client ;	
	}
	
	public void run()
	{
		try {
	    System.out.println("Message received: " + msg + " from " + client);
	   String Action =msg.toString().substring(0, msg.toString().indexOf(":"));
	   if(Action.contains("AddCasual"))
	   {
		   System.out.println("Casual Recieved by client");
		   client.sendToClient("Done your signing");
	   }
	   if(Action.contains("AddMonthly"))
	   {
		   System.out.println("Monthly Recieved by client");
	   }
	   
	   if(Action.contains("AddOnetime"))
	   {
		   System.out.println("Onetime Recieved by client");
	   }
	   
	   if(Action.contains("AddComplaint"))
	   {
		   System.out.println("Monthly Recieved by client");
	   }
		}
		catch(Exception e ) {}
		
	}


}
