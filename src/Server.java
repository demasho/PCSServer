// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		Thread Go =new Thread(p);
		Go.start();
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
	protected void serverStopped()
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
	public static void main(String[] args) 
	{
		int port = 0; //Port to listen on

		try
		{
			port = Integer.parseInt(args[0]); //Get port from command line
		}
		catch(Throwable t)
		{
			port = DEFAULT_PORT; //Set port to 5555
		}
		Server sv = new Server(port);
		try 
		{
			sv.conncetToDataBase();
			ParkingNetwork net=new ParkingNetwork();
			sv.listen(); //Start listening for connections      
		} 
		catch(SQLException e)
		{
			System.err.println("Connection to Database failed: " + e.getMessage());		
		}
		catch (Exception ex) 
		{
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
	/*********************************************************************************************/
	public void conncetToDataBase() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://cs.telhai.ac.il/Group_7";
		String username = "cs302863774";
		String password = "adamsAdam132";conn = DriverManager.getConnection(url, username, password);
		System.out.println("SQL connection succeed");
	}
	/*********************************************************************************************/
}
//End of EchoServer class