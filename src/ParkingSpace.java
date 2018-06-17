import java.util.Comparator;
import java.util.Date;


public class ParkingSpace implements Comparator<ParkingSpace>
{
	private Date deadline ;
	private String carID ;
	private String orderID ;
	private boolean available ;

	public ParkingSpace(){}
	public ParkingSpace(Date deadline , String orderid , String carID)
	{
		this.deadline = deadline ;
		this.orderID = orderid ;
		this.carID = carID ;
		available = false ;
	}




	public String getCarID() {
		return carID;
	}

	public void setCarID(String carID) {
		this.carID = carID;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
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

	@Override
	public boolean equals( Object of) 
	{
		ParkingSpace o =(ParkingSpace)of;
		if(this.carID.equals(o.carID) && this.orderID.equals(o.orderID))
			return true;
		return false;
	}




}
