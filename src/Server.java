// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
//import com.sun.corba.se.impl.ior.GenericTaggedComponent;
/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
/*********************************************************************************************/
public class Server extends AbstractServer 
{
	//Class variables *************************************************
	/**
	 * The default port to listen on.
	 */
	public  Connection conn;
	ParkingNetwork net;
	final public static int DEFAULT_PORT = 5555;
	//Constructors ****************************************************  
	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */

	/*********************************************************************************************/
	public Server(int port) 
	{
		super(port);
	}
	/*********************************************************************************************/
	//Instance methods ************************************************
	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	/*********************************************************************************************/
	public void handleMessageFromClient
	(Object msg, ConnectionToClient client)
	{
		ClientHandle p=new ClientHandle(msg,client,conn);	 
        p.handle();
	}
	/*********************************************************************************************/
	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server starts listening for connections.
	 */
	/*********************************************************************************************/
	protected void serverStarted()
	{
		System.out.println
		("Server listening for connections on port " + getPort());
	}
	/*********************************************************************************************/
	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections.
	 */
	/*********************************************************************************************/
	@Override
	protected void serverClosed()
	{
		System.out.println
		("Server has stopped listening for connections.");
	}
	/*********************************************************************************************/
	//Class methods ***************************************************
	/**
	 * This method is responsible for the creation of 
	 * the server instance (there is no UI in this phase).
	 *
	 * @param args[0] The port number to listen on.  Defaults to 5555 
	 *          if no argument is entered.
	 */
	/*********************************************************************************************/

	public void start() throws Exception {

		int port = 0; //Port to listen 

		port = DEFAULT_PORT; //Set port to 5555

		Server sv = new Server(port);
		try 
		{
			ConnectionToDataBaseSQL.conncetToDataBase();
			Monitoring mon=new Monitoring();
			mon.StartMonitoringComplaints();
			mon.StartMonitoringEndTimeForOrders();
			mon.StartMonitoringEnterTimeForOrders();
			mon.StartMonitoringSubscripers();
			ParkingNetwork.AddParkingLot("333", 5);
			ParkingNetwork.AddParkingLot("111", 5);
			ParkingNetwork.AddParkingLot("222", 8);
			ParkingNetwork.AddParkingLot("444", 4);
			ParkingNetwork.AddParkingLot("555", 6);
			ParkingNetwork.AddParkingLot("666", 7);
			Date now=new Date();
			ParkingNetwork.getParking("333").enterToParking(now, "2000002", "1234567");
			sv.listen(); //Start listening for connections      
		} 
		catch(SQLException e)
		{
			 throw new SQLException( "Connection to Database failed: " + e.getMessage());		
		}
		catch (Exception ex) 
		{
			 throw new Exception("ERROR - Could not listen for clients!");
		}
	}
	public void Stop() throws IOException {
		this.Stop();
		this.stopListening();
		this.close();
		
	}
}
//End of EchoServer class