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
				System.out.println("one");
				break;
			case "Login":
				System.out.println("two");
				break;
			case "UPDATING_PRICES":
				System.out.println("three");
				break;
			case "TRACKING_THE_STATUS_OF_REQUEST":
				System.out.println("one");
				break;
			case "CANCEL_RESERVATION":
				System.out.println("two");
				break;
			case "SUBMISSION_COMPLAINT":
				System.out.println("three");
				break;
			case "APPROVES_PRICES":
				System.out.println("one");
				break;
			case "RESERVATION_UBDATE_SHOT":
				System.out.println("two");
				break;
			case "REPORT_NAME":
				System.out.println("three");
				break;
			case "DISABLED_PLACES_SYSTEM":
				System.out.println("one");
				break;
			case "REPORTS_FULL_PARKING_CONDITION":
				System.out.println("two");
				break;
			case "INIT_SIZE_OF_PARKING":
				System.out.println("three");
				break;
			case "SAVING_PARKING_SPACE":
				System.out.println("one");
				break;
			default:
				System.out.println("no match");
			}
		}
		catch(Exception e ) {}

	}

	//<CUSTOMER_ID> <PARKING_ID>  <ENTRY_DATE> <RELEASE_DATE> <E_MAIL> <CAR_NUMBER> 
	public void OneTimeOrders(String msg,ConnectionToClient client) {
		try {
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			String startdate=parts[2].replace("/", " ");
			String enddate=parts[3].replace("/", " ");
			if(ParkingNetwork.IsParkFULL(parts[1])== true)
			{
				client.sendToClient("Order Failed The ParkingLot that you ordered is full !!");
			}else {
				int answer= ConnectionToDataBaseSQL.AddOneTimeOrder(parts[0], parts[1], startdate , enddate , parts[4], parts[5]); 
				String status = answer==-1 ?  " Failed Try Again Later" :  " Sucessed , your Odred id = "+ answer ;
				client.sendToClient("Your Order is"+ status);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	//<PARKING_ID> <ENTRY_DATE> <RELEASE_DATE> <E-MAIL> <CUSTOMER_ID> <CAR_NUMBER>
	public void CasualParkingOrder(String msg,ConnectionToClient client) {
		try {
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			String[] parts = Substring.split(" ");
			String startdate=parts[1].replace("/", " ");
			String enddate=parts[2].replace("/", " ");
			if(ParkingNetwork.IsParkFULL(parts[1])== true)
			{
				client.sendToClient("Order Failed The ParkingLot that you ordered is full !!");
			}else {
				int answer= ConnectionToDataBaseSQL.AddCasualParking(parts[0], startdate, enddate, parts[3], parts[4], parts[5]); 
				String status = answer==-1 ?  " Failed Try Again Later" :  " Sucessed , your Odred id = "+ answer ;
				client.sendToClient("Your Order is"+ status);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	//<PARKING_ID> <CUSTOMER_ID> <STARTED_DATE> <E_MAIL> <IS_BUSINESS> <AMOUNT_OF_CARS> , <CAR_NUMBER>...<CAR_NUMBER>
	public void MonthlySubscription(String msg,ConnectionToClient client) {
		try {
			String Substring = msg.substring(msg.indexOf(":")+2, msg.length());
			int indexofcomm = msg.indexOf(",")+2;
			String AllCars= msg.substring(indexofcomm, msg.length());
			String[] parts = Substring.split(" ");
			int amountofcars=Integer.parseInt(parts[5]);
			boolean IsB = Boolean.parseBoolean(parts[4]);
			String startdate=parts[2].replace("/", " ");
				int answer= ConnectionToDataBaseSQL.AddMonthlySubscription(parts[0], parts[1],startdate, parts[3],IsB,amountofcars,AllCars); 
				String status = answer==-1 ?  " Failed Try Again Later" :  " Sucessed , your Odred id = "+ answer ;
				client.sendToClient("Your Subscription ID is"+ status);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
