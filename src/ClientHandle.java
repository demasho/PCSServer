import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.mysql.jdbc.Util;
import javafx.geometry.Point3D;
import java.sql.Connection;
public class ClientHandle implements Runnable
{
	ConnectionToClient client ;
	String msg ;
	public  Connection conn;
	/*********************************************************************************************/
	public ClientHandle(Object msg, ConnectionToClient client,Connection conn)
	{
		this.msg = msg.toString();
		this.client = client ;
		this.conn=conn;
	}
	/*********************************************************************************************/
	public void run()
	{
		try
		{
			System.out.println("Message received: " + msg + " from " + client);
			String Action =msg.toString().substring(0, msg.toString().indexOf(":"));
			switch(Action)
			{
			case "OneTimeOrders":
				OneTimeOrders(msg.toString(), client);
				break;
			case "CasualParking":
				CasualParkingOrder(msg.toString(), client);
				break;
			case "MonthlySubscription":
				MonthlySubscription(msg.toString(), client);
				break;
			case "SignUp":
				SignUpWorker(msg.toString(), client);
				break;
			case "Login":
				LoginWorker(msg.toString(),client);
				break;
			case "Logout":
				LogOutWorker(msg.toString(),client);
				break;
			case "UPDATING_PRICES":
				UpdatingPrices( msg.toString(),client);
				break;
			case "CANCEL_RESERVATION":
				CancelOrder(msg.toString(),client);
				break;
			case "SUBMISSION_COMPLAINT":
				SUBMISSION_COMPLAINT(msg.toString(),client);
				break;
			case "APPROVES_PRICES":
				APPROVES_PRICES(client);
				break;
			case "PARKING_SNAPSHOT":
				getParkingSnapshot(msg.toString(), client);
				break;
			case "REPORT_NAME":
				System.out.println("three");
				break;
			case "DISABLED_PLACES_SYSTEM":
				disabledParkingSpace(msg.toString(),client);
				break;
			case "INIT_SIZE_OF_PARKING":
				addNewParking(msg.toString(), client);
				break;
			case "SAVING_PARKING_SPACE":
				savingParkingSpace(msg.toString(),client);
				break;
			case "GET_UPDATED_PRICES":
				GET_UPDATED_PRICES(client);
				break;
			case "GET_PAYMENT":
				GET_PAYMENT(msg.toString(),client);
				break;
			case "PAY_ANDOUT":
				PAY_ANd_GO(msg.toString(),client);
				break;
			case "HANDLE_COMPLAINT":
				HandleComplaint(msg.toString(),client);
				break;
			case "INSERT_CAR":
				insertCar(msg.toString(),client);
				break ;
			default:
				System.out.println("no match");
			}
		}
		catch(Exception e )
		{
		}
	}

	public void insertCar(String msg,ConnectionToClient client)
	{
		try {
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts=Substring.split(" ");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
			Date deadline = format.parse(parts[3]);
			Date now = new Date() ;
			if(!ParkingNetwork.containsParking(parts[0]))
			{
				client.sendToClient("Failed : Parking: "+parts[0]+" does not exist!");
				return ;
			}
			if(deadline.before(now))
			{
				client.sendToClient("Failed : Invalid deadline!");
				return ;
			}
			boolean entered = ParkingNetwork.getParking(parts[0]).enterToParking(deadline, parts[1], parts[2]);
			if(entered)
			{
				client.sendToClient("Succeed : You've Entered The Car Successfully");
			}
			else client.sendToClient("Failed : Something Went Wrong While Inserting Car");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// <COMPLAINTID> <Compensation Money>
	private void HandleComplaint(String string, ConnectionToClient client) {
		try {
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts=Substring.split(" ");
			String ans=ConnectionToDataBaseSQL.HandleComplaints(parts[0],parts[1]);
			client.sendToClient(ans);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void PAY_ANd_GO(String string, ConnectionToClient client) {
		try {
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts=Substring.split(" ");
			String ans=ConnectionToDataBaseSQL.PayAndGo(parts[0]);
			client.sendToClient(ans);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}
	public void GET_PAYMENT(String msg,ConnectionToClient client) {
		try {
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts=Substring.split(" ");
			String ans=ConnectionToDataBaseSQL.GetPayment(parts[0]);
			client.sendToClient(ans);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public void GET_UPDATED_PRICES(ConnectionToClient client) {
		try {
			String ans=ConnectionToDataBaseSQL.GetUpdatedPrices();
			client.sendToClient(ans);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************************************************************************************/
	//<CUSTOMER_ID> <PARKING_ID>  <ENTRY_DATE> <RELEASE_DATE> <E_MAIL> <CAR_NUMBER> 
	public void OneTimeOrders(String msg,ConnectionToClient client)
	{
		try
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			String startdate=parts[2].replace("/", " ");
			String enddate=parts[3].replace("/", " ");
			if(ParkingNetwork.IsParkFULL(parts[1])== true)
			{
				client.sendToClient("Order Failed The ParkingLot that you ordered is full !!");
			}
			else
			{
				int answer= ConnectionToDataBaseSQL.AddOneTimeOrder(parts[0], parts[1], startdate , enddate , parts[4], parts[5]); 
				String status = answer==-1 ?  " Failed Try Again Later" :  " Sucessed , your Odred id = "+ answer ;
				client.sendToClient("Your Order is"+ status);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	/*********************************************************************************************/
	//<USERNAME> <PASSWORD>
	public void LoginWorker(String msg,ConnectionToClient client) 
	{
		try
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			String answer= ConnectionToDataBaseSQL.Login(parts[0], parts[1]); 
			client.sendToClient(answer);

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************************************************************************************/
	// <NEW_PRICE_FOR_OneTimeOrders> <NEW_PRICE_FOR_CasualParking> <NEW_PRICE_FOR_FullMonthlySubscription> <NEW_PRICE_FOR_BusinessMonthlySubscription>
	public static void UpdatingPrices(String msg,ConnectionToClient client)
	{
		try 
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts=Substring.split(" ");
			double CasualParking =Double.parseDouble(parts[1]);
			double OneTimeOrders =Double.parseDouble(parts[0]);
			double FullMonthlySubscription =Double.parseDouble(parts[2]);
			double BusinessMonthlySubscription =Double.parseDouble(parts[3]);
			String answer= ConnectionToDataBaseSQL.UPDATING_PRICES(CasualParking, OneTimeOrders, FullMonthlySubscription, BusinessMonthlySubscription); 
			client.sendToClient(answer);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***************************************************************************************/
	//<CUSTOMER_ID> <PARKING_ID> <DESCRIBTION>
	public static void SUBMISSION_COMPLAINT(String msg,ConnectionToClient client) 
	{
		try 
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			String res=ConnectionToDataBaseSQL.SUBMISSION_COMPLAINT(parts[0],parts[2] ,parts[1]);
			client.sendToClient(res);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	/**************************************************************************************/
	public static void APPROVES_PRICES(ConnectionToClient client)
	{
		try 
		{
			String res=ConnectionToDataBaseSQL.APPROVES_PRICES();
			client.sendToClient(res);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/***************************************************************************************/
	//<FIRST_NAME> <LAST_NAME> <WORKER_ID> <E-MAIL> <ROLE> <ParkingID> <USERNAME> <PASSWORD>
	public void SignUpWorker(String msg,ConnectionToClient client) 
	{
		try
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			String answer= ConnectionToDataBaseSQL.SignUp(parts[6], parts[7], parts[0], parts[1], parts[3], parts[4], parts[2],parts[5]); 
			client.sendToClient(answer);

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************************************************************************************/
	public void LogOutWorker(String msg,ConnectionToClient client)
	{
		String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
		String[] parts = Substring.split(" ");
		ConnectionToDataBaseSQL.LogOut(parts[0]); 
	}
	/*********************************************************************************************/
	//<PARKING_ID> <ENTRY_DATE> <RELEASE_DATE> <E-MAIL> <CUSTOMER_ID> <CAR_NUMBER>
	public void CasualParkingOrder(String msg,ConnectionToClient client) {
		try 
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			String startdate=parts[1].replace("/", " ");
			String enddate=parts[2].replace("/", " ");
			if(ParkingNetwork.IsParkFULL(parts[1])== true)
			{
				String availables = ParkingNetwork.getAvailableParkings();
				if(availables.isEmpty())
					client.sendToClient("Order Failed The ParkingLot that you ordered is full!!");
				else 
					client.sendToClient("Order Failed The ParkingLot that you ordered is full ,"
							+ "Alternative parkings : "+availables);
			}
			else 
			{
				int answer= ConnectionToDataBaseSQL.AddCasualParking(parts[0], startdate, enddate, parts[3], parts[4], parts[5]); 
				String status = answer==-1 ?  " Failed Try Again Later" :  " Sucessed , your Odred id = "+ answer ;
				client.sendToClient("Your Order is"+ status);
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	/*********************************************************************************************/
	//<CUSTOMER_ID> <STARTED_DATE> <E_MAIL> <IS_BUSINESS> <AMOUNT_OF_CARS> , <CAR_NUMBER>...<CAR_NUMBER>
	public void MonthlySubscription(String msg,ConnectionToClient client) 
	{
		try
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			int indexofcomm = msg.indexOf(",")+2;
			String AllCars= msg.substring(indexofcomm, msg.length());
			String[] parts = Substring.split(" ");
			int amountofcars=Integer.parseInt(parts[4]);
			boolean IsB = Boolean.parseBoolean(parts[3]);
			String startdate=parts[1].replace("/", " ");
			int answer= ConnectionToDataBaseSQL.AddMonthlySubscription(parts[0],startdate, parts[2],IsB,amountofcars,AllCars); 
			String status = answer==-1 ?  " Failed Try Again Later" :  " Sucessed , your Odred id = "+ answer ;
			client.sendToClient("Your Subscription ID is"+ status);			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	/*********************************************************************************************/
	//<PARKING_ID> <SPACE_LOCATION>
	public void disabledParkingSpace(String msg,ConnectionToClient client)
	{
		try 
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			Parking parking =null ;
			if(ParkingNetwork.containsParking(parts[0]))
				parking = ParkingNetwork.getParking(parts[0]);
			else
			{
				client.sendToClient(parts[0] + " is not exist");
				return ;
			}
			String point = parts[1].substring(parts[1].indexOf("(")+1, parts[1].length()-1);
			String[] pointParts = point.split(",");
			int x = Integer.parseInt(pointParts[0]);
			int y = Integer.parseInt(pointParts[1]);
			int z = Integer.parseInt(pointParts[2]);
			Point3D location = null ;
			if(x < parking.getColumns() && y < parking.getRows() && z < parking.getFloor())
				location = new Point3D(x, y, z);
			else 
			{
				client.sendToClient("Incorrect spot: "+parts[1]);
				return ;
			}
			parking.addBadSpace(location);
			client.sendToClient(parts[1] + " in " + parts[0]+" updated to be {disabled space} succefully");
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************************************************************************************/
	//<PARKING_ID> <ORDER_ID>
	public void savingParkingSpace(String msg,ConnectionToClient client)
	{
		try 
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			Parking parking =null ;
			if(ParkingNetwork.containsParking(parts[0]))
				parking = ParkingNetwork.getParking(parts[0]);
			else
			{
				client.sendToClient(parts[0] + " is not exist");
				return ;
			}
			boolean res = parking.addSavedSpace(parts[1]);
			if(res == true)
				client.sendToClient("Saving parking space for order number "+parts[1]+": SUCCEEDED");
			else client.sendToClient("Saving parking space for order number "+parts[1]+": FAILED");
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************************************************************************************/
	//<PARKING_ID> <COLUMNS>
	public void addNewParking(String msg,ConnectionToClient client)
	{
		try 
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			int columns = Integer.parseInt(parts[1]);
			if(columns > 8 || columns < 4)
			{
				client.sendToClient("FAILED: columns size must be 4-8");
				return ;
			}
			if(ParkingNetwork.containsParking(parts[0]))
			{
				client.sendToClient("FAILED: "+parts[0]+" Parking is exists");
				return ;
			}
			boolean added = ParkingNetwork.AddParkingLot(parts[0], columns);
			if(added)
			{
				client.sendToClient("SUCCEEDED: "+parts[0]+" has been added");
				return ;
			}
			else client.sendToClient("FAILED: Try Again Later");
		} 
		catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************************************************************************************/
	public void CancelOrder(String msg,ConnectionToClient client)
	{
		try 
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			String res=ConnectionToDataBaseSQL.CancelRESERVATION(parts[0]);
			client.sendToClient(res);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************************************************************************************/
	public void getParkingSnapshot(String msg,ConnectionToClient client)
	{
		try 
		{
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			if(!ParkingNetwork.containsParking(Substring))
			{
				client.sendToClient("FAILED: Invalid Parking ID");
				return ;
			}
			client.sendToClient(ParkingNetwork.getParking(Substring).getSnapshot());
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************************************************************************************/
}