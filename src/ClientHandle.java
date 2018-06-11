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
	    System.out.println("Message received: " + msg + " from " + client);
	   String Action =msg.toString().substring(0, msg.toString().indexOf(":"));
	   if(Action.contains("AddCasual"))
	   {
		   System.out.println("Casual Recieved by client");
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
	
	  public void sendCasualToDB(String msg,ConnectionToClient client) throws SQLException, IOException {
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			Statement stmt = Server.getConn().createStatement();
		  	//ResultSet rs = stmt.executeQuery("SELECT Count(UserName) FROM " +
			//			"Users where UserName = '"+parts[0]+"'");
		  	stmt.close();
		  }
	

}
