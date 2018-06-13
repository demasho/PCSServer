import java.util.Comparator;
import java.util.Date;


public class ParkingSpace implements Comparator<ParkingSpace>
{
	private Date deadline ;
	private int x ;
	private int y ;
	private int z ;
	private String carID ;
	private String OrderID ;
	private boolean available ;
	
	public ParkingSpace(){}
	public ParkingSpace(Date deadline)
	{
		this.deadline = deadline ;
	}

	
	
	
	public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int compare(ParkingSpace o1, ParkingSpace o2) {
		if(o1.deadline.before(o2.deadline))
			return 1 ;
		else if(o1.deadline.after(o2.deadline))
			return -1 ;
		return 0;
	}
	
	
	

}
