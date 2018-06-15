import java.util.HashMap;
import java.util.Map;

public class ParkingNetwork {
	private static Map<String,Parking> parknet;
	
	public void ParkingNetwork() {
	 parknet = new HashMap<String,Parking>();
	}
	public static  boolean AddParkingLot(String ParkingId,int columns)
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

}
