import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement ;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
public class ConnectionToDataBaseSQL
{
	protected  static Connection conn;
	public static void conncetToDataBase() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://cs.telhai.ac.il/Group_7";
		String username = "cs205821473";
		String password = "DEma123**";
		conn = DriverManager.getConnection(url, username, password);
		System.out.println("SQL connection succeed");
	}
	public static void main(String[] args) 
	{
		try {
			conncetToDataBase();
			//				Date TIN=new Date();
			//				TIN.setHours(12);
			//				TIN.setMinutes(30);
			//				TIN.setSeconds(50);
			//				TIN.setYear(2018);
			//				TIN.setMonth(6);
			//				TIN.setDate(1);
			//				java.text.SimpleDateFormat sdf = 
			//						new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//				
			//				String D= sdf.format(TIN);
			//<PARKING_ID> <CUSTOMER_ID> <STARTED_DATE> <E_MAIL> <IS_BUSINESS> <AMOUNT_OF_CARS> , <CAR_NUMBER>...<CAR_NUMBER>

			//			int res=AddMonthlySubscription("222222222", "2000-10-15 15:15:15","bla@gmail.com" ,false,1,"11111111");
			//			System.out.println(res);
			//			//res=SignUp("adam", "adamPCS7" ,"adam","azzam","bla@gmail.com","Dancer","123456789","15");
			//			System.out.println(res);
			//	UPDATING_PRICES1(" : 10.0 10.0 10.0 10.0");
//			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//			ParkingNetwork p=new ParkingNetwork();
//			ParkingNetwork.AddParkingLot("333", 2);
//			Date Start =  format.parse("2018-06-17 14:00:00");
//			Parking  so=ParkingNetwork.getParking("333");
//			System.out.println(so.getSize());
//			so.enterToParking(Start, "2000001", "1716719");
//			Monitoring s= new Monitoring();
//			s.StartMonitoringEndTimeForOrders();
			String s=APPROVES_PRICES();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static String UPDATING_PRICES (double CasualParking,double OneTimeOrder,double FullMonthlySubscription,double BusinessMonthlySubscription){	

		Statement stmt;
		String result = null;
		try {
			stmt = conn.createStatement();
			String query="INSERT INTO Prices (`CasualParking`,`OneTimeOrders`,`FullMonthlySubscription`,`BusinessMonthlySubscription`,`status`) VALUES " +
					"("+CasualParking+","+OneTimeOrder+","+FullMonthlySubscription+","+BusinessMonthlySubscription+",'New')";
			System.out.println(query);
			stmt.executeUpdate(query);
			result="Updating is Successed";	
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("You Can't make Updating Sorry!!");
			e.printStackTrace();
			result="You Can't make Updating Sorry!!";
			return result;
		}
		return result;
	}
	/***************************************************************************************/
	public static String APPROVES_PRICES ()
	{
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			String deletequery;
			deletequery="DELETE FROM Prices where status = "+ " 'Now' ";
			String update = "update Prices set status = ? where status = 'New'";
			stmt.executeUpdate(deletequery);
			PreparedStatement preparedStmt = conn.prepareStatement(update);
			preparedStmt.setString (1,"Now");
			preparedStmt.executeUpdate();
		} 
		catch (SQLException e)
		{
			System.out.println("The Manager Not APProve The Request!!");
			e.printStackTrace();
			return "APPROVED FAILED";
		}
		return "APPROVED SUCCESSED";
	}
	/**************************************************************************************/
	public static String SUBMISSION_COMPLAINT(int ComplaintID,int OrderID,String DESCRIBTION,String Time) 
	{
		Statement stmt;

		java.text.SimpleDateFormat ft = 
				new java.text.SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		//Date date = null;
		/*	try 
			{
				date = format.parse(Time);
			} 
			catch (ParseException e2)
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			try 
			{
				stmt = conn.createStatement();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		String result;
		try 
		{
			stmt = conn.createStatement();
			// ft.format(date);
			String exe="INSERT INTO Complaints (`complaintID`, `description`, `orderID`, `AddDate`) VALUES" + 
					"('"+ComplaintID+"','"+DESCRIBTION+"','"+OrderID+"','"+Time+"')";
			stmt.executeUpdate(exe, Statement.RETURN_GENERATED_KEYS);
			result="SUBMISSION COMPLAINT SUCCESSED";
		}
		catch (Exception e)
		{
			result="SUBMISSION COMPLAINT Failed!!!";
			e.printStackTrace();
			return result;
		}
		return result;
	}
	/***************************************************************************************/

	public static  ResultSet GetAllSubscriper()
	{
		Statement stmt; 
		ResultSet rs =null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT SubscribeID,deadline, email, IsBusiness  FROM " +
					"MonthlySubscription");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public static  ResultSet GetAllOneTimeOrders()
	{
		Statement stmt; 
		ResultSet rs =null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT orderID,timeIn, parkingID , email ,carNumber, isLateToPark FROM " +
					"OneTimeOrders");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static  ResultSet GetAllOrders()
	{
		Statement stmt; 
		ResultSet rs =null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT orderID,timeOut,parkingID , email ,carNumber FROM OneTimeOrders UNION SELECT orderID,timeOut,parkingID , email ,carNumber FROM CasualParking");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static  void  putisLateToParkFlag(int orderID)
	{
		try {
			String query = "update OneTimeOrders set isLateToPark = ? where orderID = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setBoolean (1,true);
			preparedStmt.setInt(2,orderID);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static  void  PutFine(int orderID,double fine)
	{
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String quary="SELECT Pay FROM Payment where orderID = "+ orderID;
			String update = "update Payment set Pay = ? where orderID = ?";
			ResultSet rs = stmt.executeQuery(quary);
			rs.next();
			double pay=rs.getDouble(1);
			PreparedStatement preparedStmt = conn.prepareStatement(update);
			preparedStmt.setDouble(1,pay*fine);
			preparedStmt.setInt(2,orderID);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static String Login(String username,String password) { 
		Statement stmt;
		String result = null;
		String query = "update Users set IsOneConnected = ? where username = ?";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Count(userName) FROM " +
					"Users where username = '"+username+"'");
			rs.next();
			if(rs.getInt(1) == 0) {
				result= "There Is No UserName : "+username;
			}
			else{
				ResultSet rs2 = stmt.executeQuery("SELECT password , IsOneConnected FROM " +
						"Users where username = '"+username+"'");
				rs2.next();
				if(rs2.getString(1).equals(password))
				{
					if(rs2.getBoolean(2)==false)
					{
						PreparedStatement preparedStmt = conn.prepareStatement(query);
						preparedStmt.setBoolean (1,true);
						preparedStmt.setString (2,username);
						preparedStmt.executeUpdate();
						result="Welcome";
					}else {
						result="You Can't Access There Is Already Someone In";
					}			
				}
				else {
					result="Wrong Password";
				}
				rs2.close();
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static void LogOut(String username) {
		try {
			String query = "update Users set IsOneConnected = ? where username = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setBoolean (1,false);
			preparedStmt.setString (2,username);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String  SignUp(String username ,String password, String firstName,String  lastName , String  email,String  role, String WorkerID,String ParkingID) 
	{
		Statement stmt;
		String result=null;
		try {

			stmt = ConnectionToDataBaseSQL.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Count(userName) FROM " +
					"Users where username = '"+username+"'");
			rs.next();
			System.out.println(rs.getInt(1));
			if(rs.getInt(1) != 0) {
				result= "There Is Aleardy someone with the same UserName : "+username;
			}
			else {
				String exe="INSERT INTO  Users (`username`, `password`, `firstName`, `lastName`, `email`, `role`, `WorkerID`, `IsOneConnected`, `parkingID`) VALUES" +
						"('"+username+"','"+password+"','"+firstName+"','"+lastName+"','"+email+"','"+role+"','"+WorkerID +"',"+false+",'"+ParkingID+"')";
				stmt.executeUpdate(exe);
				result="Done SignUp";
			}
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}		  	
		return result;
	}
	public static int  AddOneTimeOrder(String CustomerID,String ParkingID,String TimeIn,String TimeOut,String email,String CarNumber) 
	{
		Statement stmt;
		int orderid;
		try {
			stmt = ConnectionToDataBaseSQL.conn.createStatement();
			String exe="INSERT INTO OneTimeOrders ( `customerID`, `parkingID`, `timeIn`, `timeOut`, `email`, `CarNumber`) VALUES " +
					"('"+CustomerID+"','"+ParkingID+"','"+TimeIn+"','"+TimeOut+"','"+email+"','"+CarNumber+"')";
			stmt.executeUpdate(exe, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			orderid = rs.getInt(1);
			AddToPayment(2,orderid, TimeIn,TimeOut);
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}		  	
		return orderid;
	}
	public static int  AddCasualParking(String ParkingID,String TimeIn,String TimeOut,String email,String CustomerID,String CarNumber) 
	{
		Statement stmt;
		int orderid;
		try {
			stmt = conn.createStatement();
			String exe="INSERT INTO CasualParking (`parkingID`, `timeIn`, `timeOut`, `email`, `customerID`, `carNumber`) VALUES " +
					"('"+ParkingID+"','"+TimeIn+"','"+TimeOut+"','"+email+"','"+CustomerID+"','"+CarNumber+"')";
			stmt.executeUpdate(exe, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			orderid = rs.getInt(1);
			AddToPayment(1,orderid, TimeIn,TimeOut);
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}		  	
		return orderid;
	}

	public static int  AddMonthlySubscription(String CustomerID,String TimeStart,String email,boolean IsBusniess,int amount,String CarsNumbers)
	{
		Statement stmt;
		int SubscriptionID;
		try {
			java.text.SimpleDateFormat sdf = 
					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] parts = CarsNumbers.split(" ");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			Date Startdate = format.parse(TimeStart);
			Calendar cal = Calendar.getInstance();
			cal.setTime(Startdate);
			cal.add(Calendar.DAY_OF_MONTH, 28); // add 28 days  
			Date Deadline  = (Date) cal.getTime();
			String DeadLine= sdf.format(Deadline);
			stmt = conn.createStatement();
			String exe="INSERT INTO MonthlySubscription (`customerID`, `startedDate`, `deadline`, `email`, `IsBusiness`) VALUES " +
					"('"+CustomerID+"','"+TimeStart+"','"+DeadLine+"','"+email+"',"+IsBusniess+")";
			stmt.executeUpdate(exe,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			SubscriptionID = rs.getInt(1);
			for(int i=0 ; i<amount ; i++ ) {
				String carStatment = "INSERT INTO Cars(`customerID`,`carNumber`) VALUES" + 
						" "+"('"+CustomerID+"','"+parts[i]+"')";
				stmt.executeUpdate(carStatment);
			}	
			if(IsBusniess==true) {
				AddToPayment(4,SubscriptionID, TimeStart,DeadLine);
			}else {
				AddToPayment(3,SubscriptionID, TimeStart,DeadLine);
			}
			stmt.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}		  	
		return SubscriptionID;
	}
	public static String CancelRESERVATION(String OrderID) {
		String quary=null,result,deletequery=null;
		Statement stmt;
		String update = "update Payment set Pay = ? where orderID = ?";
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		try {
			if(OrderID.charAt(0)=='1') {
				quary="SELECT timeIn FROM CasualParking where orderID = "+ OrderID;
				deletequery="DELETE FROM CasualParking where orderID = "+ OrderID;
			}
			if(OrderID.charAt(0)=='2') {
				quary="SELECT timeIn FROM OneTimeOrders where orderID = "+ OrderID;
				deletequery="DELETE FROM OneTimeOrders where orderID = "+ OrderID;
			}
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(quary);
			rs.next();
			Date now =new  Date();
			Date Start =  format.parse(rs.getString(1));
			long diff = Start.getTime() - now.getTime();
			double hours =  (diff / (1000.0*60.0*60.0));
			System.out.println(now.getHours()+":"+now.getMinutes());
			System.out.println(hours);
			double persent;
			if(hours > 3.0) {
				persent=0.1;
			}else {
				if(hours > 1.0) {
					persent=0.5;
				}else {
					persent=1.0;
				}
			}
			rs = stmt.executeQuery("SELECT Pay FROM Payment where orderID = "+ OrderID);
			rs.next();
			PreparedStatement preparedStmt = conn.prepareStatement(update);
			double pay= rs.getDouble(1)*persent;
			System.out.println(pay + " "+rs.getDouble(1) );
			preparedStmt.setDouble(1,pay);
			preparedStmt.setString (2,OrderID);
			preparedStmt.executeUpdate();
			result="Cancel Reservation Sucessed : but you have to Pay : "+( rs.getDouble(1)*persent);
			stmt.executeUpdate(deletequery);

		} catch (Exception e) {
			result="Cancel Reservation Failed";
			e.printStackTrace();
		}
		return result;
	}

	private static void AddToPayment(int type,int OrderID ,String TimeIn ,String TimeOut) {
		try {
			ResultSet rs;
			Statement stmt;
			stmt = conn.createStatement();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			Date Startdate = format.parse(TimeIn);
			Date Enddate = format.parse(TimeOut);
			long diff = Enddate.getTime() - Startdate.getTime();
			float hours =  (diff / (1000*60*60));
			Double pay=null;
			switch(type)
			{
			case 1:
				rs = stmt.executeQuery("SELECT CasualParking FROM Prices where status = "+" 'Now' ");
				rs.next();
				pay=rs.getDouble(1) *hours;
				break;
			case 2:
				rs = stmt.executeQuery("SELECT OneTimeOrders FROM Prices where status = "+ " 'Now' ");
				rs.next();
				pay=rs.getDouble(1) *hours;
				break;
			case 3:
				rs = stmt.executeQuery("SELECT FullMonthlySubscription FROM Prices where status = "+" 'Now' ");
				rs.next();
				pay=rs.getDouble(1);
				break;
			case 4:
				rs = stmt.executeQuery("SELECT BusinessMonthlySubscription FROM Prices where status = "+" 'Now' ");
				rs.next();
				pay=rs.getDouble(1);
				break;
			}
			stmt.executeUpdate("INSERT INTO Payment (`orderID`, `Pay`) VALUES "+ "("+ OrderID + "," + pay+")");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
