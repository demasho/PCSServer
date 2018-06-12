import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class OrdersHandler extends ConnectionToDataBaseSQL {
	
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

	
	public static boolean  AddMonthlySubscription(String ParkingID,String CustomerID,Date TimeStart,String email,Object CarNumber,boolean IsBusniess)
	{
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

	//	public static String[] getMonthlySubscriptionInfo(long subscriptionID)
	//	{ 
	//			//TODO : USERS Table
	//			Statement stmt = conn.createStatement();
	//			String result;
	//			ResultSet rs = stmt.executeQuery("SELECT Count(Subscribe) FROM " +
	//					"MonthlySubscription where Subscribe = '"+subscriptionID+"'");
	//			rs.next();
	//			if(rs.getInt(1) == 0) {
	//				result= "Wrong Subscription ID";
	//			}
	//			else{
	//				ResultSet rs2 = stmt.executeQuery("SELECT password FROM " +
	//						"Users where username = '"+username+"'");
	//				rs2.next();
	//				if(rs2.getString(1).equals(password)) {
	//					result="";
	//				}
	//				else {
	//					result="Wrong Password";
	//				}
	//				rs2.close();
	//			}
	//			rs.close();
	//			stmt.close();
	//			
	//	}

}
