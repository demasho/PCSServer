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
//	public static void main(String[] args) 
//	{
//		try {
//			conncetToDataBase();
//			Date TIN=new Date();
//			TIN.setHours(12);
//			TIN.setMinutes(30);
//			TIN.setSeconds(50);
//			TIN.setYear(2018);
//			TIN.setMonth(6);
//			TIN.setDate(1);
//			java.text.SimpleDateFormat sdf = 
//					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			
//			String D= sdf.format(TIN);
//			//<PARKING_ID> <CUSTOMER_ID> <STARTED_DATE> <E_MAIL> <IS_BUSINESS> <AMOUNT_OF_CARS> , <CAR_NUMBER>...<CAR_NUMBER>
//			CasualParkingOrder(" : 00000000005 2018-06-13/21:24:00 2018-06-19/22:24:00 dema.shofe@gmail.com 205821473 00000000");
//			;
//			
//		}catch(Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}

	public static String Login(String username,String password) { 
		Statement stmt;
		String result = null;
		try {
			stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT Count(UserName) FROM " +
				"Users where UserName = '"+username+"'");
		rs.next();
		if(rs.getInt(1) == 0) {
			result= "Wrong UserName";
		}
		else{
			ResultSet rs2 = stmt.executeQuery("SELECT Password FROM " +
					"Users where UserName = '"+username+"'");
			rs2.next();
			if(rs2.getString(1).equals(password)) {
				result="";
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
	
	public static int  AddMonthlySubscription(String ParkingID,String CustomerID,String TimeStart,String email,boolean IsBusniess,int amount,String CarsNumbers)
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
			String exe="INSERT INTO MonthlySubscription (`parkingID`, `customerID`, `startedDate`, `deadline`, `email`, `IsBusiness`) VALUES " +
					"('"+ParkingID+"','"+CustomerID+"','"+TimeStart+"','"+DeadLine+"','"+email+"',"+IsBusniess+")";
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
