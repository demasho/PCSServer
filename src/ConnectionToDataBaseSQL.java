import java.io.*;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement ;
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
//
//			boolean res=AddMonthlySubscription("00000000005","205821473",TIN,"dema.shofe@gmail.com",true,"02018927");
//		}catch(Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}

	public static String SignIn(String username,String password) throws SQLException { 
		//TODO : USERS Table
		Statement stmt = conn.createStatement();
		String result;
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
		return result;
	}


	public static boolean  AddOneTimeOrder(String CustomerID,String ParkingID,Date TimeIn,Date TimeOut,String email,String CarNumber) 
	{
		Statement stmt;
		try {
			java.text.SimpleDateFormat sdf = 
					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeIn= sdf.format(TimeIn);
			String timeOut= sdf.format(TimeOut);
			boolean isInside = false ;
			stmt = ConnectionToDataBaseSQL.conn.createStatement();
			String exe="INSERT INTO OneTimeOrders ( `customerID`, `parkingID`, `timeIn`, `timeOut`, `email`, `CarNumber`) VALUES " +
					"('"+CustomerID+"','"+ParkingID+"','"+timeIn+"','"+timeOut+"','"+email+"','"+CarNumber+"')";
			String carStatment = "INSERT INTO Cars(`customerID`,`carNumber`,`isParked`)VALUES" + 
					" "+"('"+CustomerID+"','"+CarNumber+"','"+isInside+"')";
			stmt.executeUpdate(exe);
			stmt.executeUpdate(carStatment);
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}		  	
		return true;
	}


	public static boolean  AddCasualParking(String ParkingID,Date TimeIn,Date TimeOut,String email,String CustomerID,String CarNumber) 
	{
		Statement stmt;
		try {
			java.text.SimpleDateFormat sdf = 
					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeIn= sdf.format(TimeIn);
			String timeOut= sdf.format(TimeOut);
			boolean isInside = true;
			stmt = conn.createStatement();
			stmt = conn.createStatement();
			String exe="INSERT INTO CasualParking (`parkingID`, `timeIn`, `timeOut`, `email`, `customerID`, `carNumber`) VALUES " +
					"('"+ParkingID+"','"+timeIn+"','"+timeOut+"','"+email+"','"+CustomerID+"','"+CarNumber+"')";
			String carStatment = "INSERT INTO Cars(`customerID`,`carNumber`,`isParked`)VALUES" + 
					" "+"('"+CustomerID+"','"+CarNumber+"','"+isInside+"')";
			stmt.executeUpdate(exe);
			stmt.executeUpdate(carStatment);
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}		  	
		return true;
	}


	public static boolean  AddMonthlySubscription(String ParkingID,String CustomerID,Date TimeStart,String email,boolean IsBusniess,Object CarNumber)
	{
		//TODO add the cars addtion
		Statement stmt;
		try {
			java.text.SimpleDateFormat sdf = 
					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime= sdf.format(TimeStart);
			Calendar cal = Calendar.getInstance();
			cal.setTime(TimeStart);
			cal.add(Calendar.DAY_OF_MONTH, 28); // add 28 days  
			Date Deadline  = (Date) cal.getTime();
			String DeadLine= sdf.format(Deadline);
			boolean isInside = false ;
			stmt = conn.createStatement();
			String exe="INSERT INTO MonthlySubscription (`parkingID`, `customerID`, `startedDate`, `deadline`, `email`, `IsBusiness`) VALUES " +
					"('"+ParkingID+"','"+CustomerID+"','"+startTime+"','"+DeadLine+"','"+email+"',"+IsBusniess+")";
//			String carStatment = "INSERT INTO Cars(`customerID`,`carNumber`,`isParked`)VALUES" + 
//					" "+"('"+CustomerID+"','"+CarNumber+"','"+isInside+"')";
			stmt.executeUpdate(exe);
			//stmt.executeUpdate(carStatment);
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}		  	
		return true;
	}

}
