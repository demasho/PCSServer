import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ParkingNetwork {
	private static Map<String,Parking> parknet= new HashMap<String,Parking>();
	
	public static boolean AddParkingLot(String ParkingId,int columns)
	{
		if(parknet.containsKey(ParkingId)==true)
			return false;
		parknet.put(ParkingId, new Parking(ParkingId,columns));		
		return true;
	}
	
	public static  int GetParkingSize(String ParkingId) {
		if(parknet.containsKey(ParkingId)==false)
			return 0;
		return parknet.get(ParkingId).getSize();
	}
	public static boolean IsParkFULL(String ParkingId) {
		if(parknet.containsKey(ParkingId)==false)
			return true;
		return parknet.get(ParkingId).isFull();
	}
	
	public static Parking getParking(String ParkingId)
	{
		if(parknet.containsKey(ParkingId))
			return parknet.get(ParkingId);
		return null ;
	}
	
	public static boolean containsParking(String ParkingId)
	{
		return parknet.containsKey(ParkingId);
	}
	
	public static String getAvailableParkings()
	{
		StringBuilder str = new StringBuilder();
		for (Entry<String, Parking> it : parknet.entrySet())
		{
			if(!it.getValue().isFull())
				str.append(it.getKey()+" ");
		}
		return str.toString();
	}
}
