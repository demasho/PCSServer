

import java.sql.Array;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Monitoringandcontrol.SendMail;

public class Monitoring  {	
	private final ScheduledExecutorService Subscripers= Executors.newSingleThreadScheduledExecutor();
	private final ScheduledExecutorService Orders= Executors.newSingleThreadScheduledExecutor();
	public static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	public void StartMonitoringSubscripers() {
		final ScheduledFuture<?> taskHandle = Subscripers.scheduleAtFixedRate(
				new Runnable()
				{
					public void run() {
						JSONArray subscriptions=GetAllAlmostExpiredSubs();
						if(subscriptions.length()!=0)
						{ 
							for(int i = 0; i < subscriptions.length(); i ++){
								try {
									SendMail.sendSubscriptionRenewEmail(subscriptions.getJSONObject(i).getString("SubscribeID"),subscriptions.getJSONObject(i).getString("start"),subscriptions.getJSONObject(i).getString("email") ,subscriptions.getJSONObject(i).getBoolean("IsB"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}, 0 , 1 , java.util.concurrent.TimeUnit.DAYS);
	}
	public void StartMonitoringEnterTimeForOrders() {
		final ScheduledFuture<?> taskHandlestartorders = Orders.scheduleAtFixedRate(
				new Runnable()
				{
					public void run() {
						JSONArray orders=GetAllLateToPark();
						if(orders.length()!=0)
						{ 
							for(int i = 0; i < orders.length(); i ++){
								try {
									System.out.println("Send");
									SendMail.sendLateAlertMessage(orders.getJSONObject(i).getString("orderID"),orders.getJSONObject(i).getString("start"),orders.getJSONObject(i).getString("parkingID") ,orders.getJSONObject(i).getString("email"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}, 0 , 10 , java.util.concurrent.TimeUnit.MINUTES);
	}
	public void StartMonitoringEndTimeForOrders() {
		final ScheduledFuture<?> taskHandleendorders = Orders.scheduleAtFixedRate(
				new Runnable()
				{
					public void run() {
						JSONArray orders=GetAllExceededParkingTime();
						if(orders.length()!=0)
						{ 
							for(int i = 0; i < orders.length(); i ++){
								try {
									System.out.println("Send");
									SendMail.sendExcessionEmail(orders.getJSONObject(i).getString("orderID"),orders.getJSONObject(i).getString("start"),orders.getJSONObject(i).getString("parkingID") ,orders.getJSONObject(i).getString("email"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}, 0 ,59 , java.util.concurrent.TimeUnit.MINUTES);
	}

	private JSONArray GetAllAlmostExpiredSubs() {
		JSONArray subscriptions = new JSONArray();
		Date now =new  Date();
		ResultSet rs = ConnectionToDataBaseSQL.GetAllSubscriper();
		java.util.Date DeadLine;
		try {
			while(rs.next()){
				System.out.println(rs.getInt(1));
				DeadLine =  format.parse(rs.getString(2));
				long diff = DeadLine.getTime() - now.getTime();
				int days=(int) (diff / (1000*60*60*24));
				System.out.println(days);
				if(days<=7)
				{
					subscriptions.put(new JSONObject()
							.put("SubscribeID", rs.getInt(1))
							.put("start",rs.getString(2))
							.put("email",rs.getString(3))
							.put("IsB", Boolean.parseBoolean(rs.getString(4)))
							);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return subscriptions;
	}
	private JSONArray GetAllLateToPark() {
		JSONArray OneTimeOrders = new JSONArray();
		Date now =new  Date();
		ResultSet rs = ConnectionToDataBaseSQL.GetAllOneTimeOrders();
		java.util.Date StartLine;
		try {
			while(rs.next()){
				StartLine =  format.parse(rs.getString(2));
				boolean res=now.before(StartLine);
				boolean isInside = ParkingNetwork.getParking(rs.getString(3)).isInsideParking(rs.getString(5),Integer.toString(rs.getInt(1)));
				if(res == false && isInside == false && rs.getBoolean(6)==false)
				{					
					ConnectionToDataBaseSQL.putisLateToParkFlag(rs.getInt(1));
					ConnectionToDataBaseSQL.PutFine(rs.getInt(1),1.2);
					OneTimeOrders.put(new JSONObject()
							.put("orderID", rs.getInt(1))
							.put("start",rs.getString(2))
							.put("parkingID",rs.getString(3))
							.put("email", rs.getString(4))
							);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return OneTimeOrders;
	}
	private JSONArray GetAllExceededParkingTime() {
		JSONArray OneTimeOrders = new JSONArray();
		Date now =new  Date();
		ResultSet rs = ConnectionToDataBaseSQL.GetAllOrders();
		java.util.Date DeadLine;
		try {
			while(rs.next()){
				DeadLine =  format.parse(rs.getString(2));
				boolean res=now.after(DeadLine);
				boolean isInside = ParkingNetwork.getParking(rs.getString(3)).isInsideParking(rs.getString(5),Integer.toString(rs.getInt(1)));
				if(res == true && isInside == true)
				{	
					System.out.println("yooo");
					ConnectionToDataBaseSQL.PutFine(rs.getInt(1),1.3);
					OneTimeOrders.put(new JSONObject()
							.put("orderID", rs.getInt(1))
							.put("start",rs.getString(2))
							.put("parkingID",rs.getString(3))
							.put("email", rs.getString(4))
							);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return OneTimeOrders;
	}
}
