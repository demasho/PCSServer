import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.sql.Connection;


public class ClientHandle implements Runnable {
  
	ConnectionToClient client ;
	String msg ;
	 public  Connection conn;
	public ClientHandle(Object msg, ConnectionToClient client,Connection conn)
	{
		this.msg = msg.toString();
		this.client = client ;
		this.conn=conn;
	}
	
	public void run()
	{
		try {
	    System.out.println("Message received: " + msg + " from " + client);
	   String Action =msg.toString().substring(0, msg.toString().indexOf(":"));
	   if(Action.contains("Sign"))
	   {
		   System.out.println("Sign Recieved by client");
		   client.sendToClient("Done your Sign");
	   }
	   if(Action.contains("Login"))
	   {
		   System.out.println("Login Recieved by client");
		   client.sendToClient("Done your Login");
	   }
	   if(Action.contains("AddCasual"))
	   {
		   System.out.println("Casual Recieved by client");
		   client.sendToClient("Done your Casual");
	   }
	   if(Action.contains("AddMonthly"))
	   {
		   System.out.println("Monthly Recieved by client");
		   client.sendToClient("Done your signing");
	   }
	   
	   if(Action.contains("AddOnetime"))
	   {
		   System.out.println("Onetime Recieved by client");
		   client.sendToClient("Done your AddOnetime");
	   }
	   
	   if(Action.contains("AddComplaint"))
	   {
		   System.out.println("Monthly Recieved by client");
		   client.sendToClient("Done your AddComplaint");
	   }
		}
		catch(Exception e ) {}
		
	}

	  
	  public void addtoDB(String msg,ConnectionToClient client) throws SQLException, IOException {
		  String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			Statement stmt = conn.createStatement();
		  	ResultSet rs = stmt.executeQuery("SELECT Count(UserName) FROM " +
						"Users where UserName = '"+parts[0]+"'");
		  	rs.next();
		  	if(rs.getInt(1)==0) {
		  		String exe="INSERT INTO Users VALUES " +
	    				"('"+parts[0]+"',"+parts[1]+",'"+parts[2]+"',"+parts[3]+",'"+parts[4]+"')";
		  		stmt.executeUpdate(exe);
		  		client.sendToClient("Done your signing");
		  	}
		  	else {
		  		client.sendToClient("There is already exsist a userName like yours please try again !!");
		  	}
		  	rs.close();
		  	stmt.close();
		} 
	  
	  
	  public void getFromDB(String msg,ConnectionToClient client) throws SQLException, IOException {
		String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
		String[] parts = Substring.split(" ");
		Statement stmt = conn.createStatement();
	  	ResultSet rs = stmt.executeQuery("SELECT Count(UserName) FROM " +
					"Users where UserName = '"+parts[0]+"'");
	  	rs.next();
	  	if(rs.getInt(1) == 0) {
	  		client.sendToClient("there is no userName you gave!!");
	  	}
	  	else{
	  	  	ResultSet rs2 = stmt.executeQuery("SELECT Password FROM " +
					"Users where UserName = '"+parts[0]+"'");
	  	  	rs2.next();
	  		if(rs2.getString(1).equals(parts[1])) {
	  			client.sendToClient("Welcome !!");
	  		}
	  		rs2.close();
	  	}
	  	rs.close();
	  	stmt.close();
	  }
}
