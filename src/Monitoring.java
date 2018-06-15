

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
									System.out.println("Send");
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
   
//	public void StartMonitoringOrders() {
//		final ScheduledFuture<?> taskHandle = Orders.scheduleAtFixedRate(
//				new Runnable()
//				{
//					public void run() {
//						JSONArray subscriptions=GetAllAlmostExpiredSubs();
//						if(subscriptions.length()!=0)
//						{ 
//							for(int i = 0; i < subscriptions.length(); i ++){
//								try {
//									System.out.println("Send");
//									SendMail.sendSubscriptionRenewEmail(subscriptions.getJSONObject(i).getString("SubscribeID"),subscriptions.getJSONObject(i).getString("start"),subscriptions.getJSONObject(i).getString("email") ,subscriptions.getJSONObject(i).getBoolean("IsB"));
//								} catch (JSONException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							}
//						}
//					}
//				}, 0 , 10 , java.util.concurrent.TimeUnit.MINUTES);
//	}
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
}
