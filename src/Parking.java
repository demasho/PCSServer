import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;


public class Parking 
{
	private final int rows = 3 ;
	private final int floors = 3 ;
	private int size ;
	private static int carsInParking=0 ;
	private String parkingID ;
	private PriorityQueue<ParkingSpace> theParking ;
	public Parking(String id ,int columns)
	{
		parkingID = id ;	
		Comparator<ParkingSpace> comparator = new ParkingSpace();
		size = columns*rows*floors ;
		theParking = new PriorityQueue<ParkingSpace>(size,comparator);
	}
	
	public boolean enterToParking(ParkingSpace space)
	{
		if(carsInParking < size)
		{
			theParking.add(space);
			carsInParking++ ;
			return true ;
		}
		return false ;
	}
	
	public boolean releaseFromParking(String carId)
	{
		Stack<ParkingSpace> stack = new Stack<ParkingSpace>();
		boolean found = false ;
		while(!theParking.isEmpty())
		{
			if(theParking.peek().getCarID() != carId)
				stack.push(theParking.poll());
			else {
				found = true ;
				theParking.poll() ;
				carsInParking-- ;
				break ;
			}
		}	
		while(!stack.isEmpty())
		{
			theParking.add(stack.pop());
		}
		return found ;
	}
	
	//getters and setters 	
	public String getParkingID() {
		return parkingID;
	}
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setParkingID(String parkingID) {
		this.parkingID = parkingID;
	}
	public PriorityQueue<ParkingSpace> getTheParking() {
		return theParking;
	}
	public void setTheParking(PriorityQueue<ParkingSpace> theParking) {
		this.theParking = theParking;
	}
	public int getRows() {
		return rows;
	}
	public int getFloor() {
		return floors;
	}
	

}
