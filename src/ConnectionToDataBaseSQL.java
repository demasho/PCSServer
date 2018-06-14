import java.io.*;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement ;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class ConnectionToDataBaseSQL {


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
			
			int res=AddMonthlySubscription("222222222", "2000-10-15 15:15:15","bla@gmail.com" ,false,1,"11111111");
			System.out.println(res);
			//res=SignUp("adam", "adamPCS7" ,"adam","azzam","bla@gmail.com","Dancer","123456789","15");
			System.out.println(res);
		}catch(Exception e) {
			System.out.println(e.getMessage());
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
			stmt.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -1;
		}		  	
		return SubscriptionID;
	}

}
