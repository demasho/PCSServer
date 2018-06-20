import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Vector;
import javafx.geometry.Point3D;
public class Parking 
{
	private final int rows = 3 ;
	private final int floors = 3 ;
	private int columns ;
	private int size ;
	private int carsInParking ;
	private String parkingID ;
	private PriorityQueue<ParkingSpace> theParking ;
	private Vector<Point3D> badSpaces ;
	private Vector<String> savedSpaces;
	/*********************************************************************************************/
	public Parking(String id ,int columns)
	{
		carsInParking=0 ;
		parkingID = id ;	
		Comparator<ParkingSpace> comparator = new ParkingSpace();
		this.columns = columns ;
		size = columns*rows*floors ;
		theParking = new PriorityQueue<ParkingSpace>(size,comparator);
		badSpaces= new Vector<Point3D>() ;
		savedSpaces = new Vector<String>();
	}
	/*********************************************************************************************/
	public boolean enterToParking(Date deadline , String orderid , String carID)
	{
		ParkingSpace space = new ParkingSpace(deadline,orderid,carID);
		if(savedSpaces.contains(orderid))
		{
			theParking.add(space);
			savedSpaces.remove(orderid);
			carsInParking++ ;
			return true ;
		}
		if(carsInParking < size - savedSpaces.size())
		{
			theParking.add(space);
			carsInParking++ ;
			return true ;
		}
		return false ;
	}
	/*********************************************************************************************/
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
		System.out.println("got OUT car number = "+ carId);
		return found ;
	}
	
	/*********************************************************************************************/
	public String getSnapshot()
	{
		int content = carsInParking - badSpaces.size() ;
		StringBuilder strBul = new StringBuilder() ;
		int x=0 , y=0 , z=0 , badSpotCounter = badSpaces.size() ;
		while(content > 0)
		{
			Point3D p = new Point3D(x,y,z);
			if(x < columns && !badSpaces.contains(p))
			{
				strBul.append("P(");
				strBul.append(x);
				strBul.append(',');
				strBul.append(y);
				strBul.append(',');
				strBul.append(z);
				strBul.append(") ");
				--content ;
				++x ;
				continue ;
			}
			else if(x < columns && badSpaces.contains(p))
			{
				strBul.append("B(");
				strBul.append(x);
				strBul.append(',');
				strBul.append(y);
				strBul.append(',');
				strBul.append(z);
				strBul.append(") ");
				--badSpotCounter ;
				--content ;
				++x ;
				continue ;
			}
			if(x >= columns && y<rows)
			{
				++y;
				x=0;
				continue ;
			}
			if(x >= columns && y >= rows && z < floors)
			{
				++z ;
				y=0 ;
				x=0 ;
			}
		}
		int available = size - carsInParking + badSpotCounter ;
		while(available > 0 )
		{
			Point3D p = new Point3D(x,y,z);
			if(x < columns && !badSpaces.contains(p))
			{
				strBul.append("A(");
				strBul.append(x);
				strBul.append(',');
				strBul.append(y);
				strBul.append(',');
				strBul.append(z);
				strBul.append(") ");
				--available ;
				++x ;
				continue ;
			}
			else if(x < columns && badSpaces.contains(p))
			{
				strBul.append("B(");
				strBul.append(x);
				strBul.append(',');
				strBul.append(y);
				strBul.append(',');
				strBul.append(z);
				strBul.append(") ");
				--badSpotCounter ;
				--available ;
				++x ;
				continue ;
			}
			if(x >= columns && y<rows-1)
			{
				++y;
				x=0;
				continue ;
			}
			if(x >= columns && y >= rows-1 && z < floors-1)
			{
				++z ;
				y=0 ;
				x=0 ;
			}
		}
		strBul.append(" "+savedSpaces.size()+" "+columns);
		
		return strBul.toString() ;
	}
	/*********************************************************************************************/
	public void addBadSpace(Point3D p)
	{
		if(p.getX() < columns && p.getY() < rows && p.getY() < floors)
		{
			badSpaces.addElement(p);
			++carsInParking;
		}
	}
	/*********************************************************************************************/
	public void removeBadSpace(Point3D p)
	{
		if(badSpaces.contains(p))
		{
			badSpaces.removeElement(p);
		}
		--carsInParking;
	}
	/*********************************************************************************************/
	//return true if the parking is not full and succeeded saving a parkSpace   
	public boolean addSavedSpace(String orderId)
	{
		if(carsInParking < size)
		{
			savedSpaces.addElement(orderId);;
			return true ;
		}
		return false ;
	}
	/*********************************************************************************************/
	//use this before entering the parking for booked spot , returns true if succeed
	public boolean removeSavedSpace(String orderId)
	{
		if(savedSpaces.contains(orderId))
		{
			savedSpaces.remove(orderId);
			return true ;
		}
		return false ;
	}
	/*********************************************************************************************/
	public boolean isThereSavedSpaces()
	{
		return savedSpaces.size()>0 ;
	}
	/*********************************************************************************************/
	public boolean isFull()
	{
		if(theParking.size() == size)
			return true ;
		return false ; 
	}
	/*********************************************************************************************/
	public String getBadSpaces()
	{
		StringBuilder str = new StringBuilder() ;
		if(!badSpaces.isEmpty())
		{
			for (Point3D p : badSpaces)
			{
				str.append("B(");
				str.append(p.getX());
				str.append(',');
				str.append(p.getY());
				str.append(',');
				str.append(p.getZ());
				str.append(") ");
			}
			return str.toString();
		}
		return "" ;
	}
	/*********************************************************************************************/
	public boolean fixBadSpace(Point3D p)
	{
		if(badSpaces.contains(p))
		{
			badSpaces.remove(p);
			return true ;
		}
		return false ;
	}
	/*********************************************************************************************/
	public boolean isInsideParking(String carID , String orderID)
	{ 
		boolean res = theParking.contains(new ParkingSpace(new Date(),orderID,carID));
		return res;
	}
	/*********************************************************************************************/
	//getters and setters 	
	public int getColumns()
	{
		return columns;
	}
	/*********************************************************************************************/
	public void setColumns(int columns) 
	{
		this.columns = columns;
	}
	/*********************************************************************************************/
	public String getParkingID() 
	{
		return parkingID;
	}
	/*********************************************************************************************/
	public int getSize()
	{
		return size;
	}
	/*********************************************************************************************/
	public void setSize(int size) 
	{
		this.size = size;
	}
	/*********************************************************************************************/
	public void setParkingID(String parkingID)
	{
		this.parkingID = parkingID;
	}
	/*********************************************************************************************/
	public PriorityQueue<ParkingSpace> getTheParking()
	{
		return theParking;
	}
	/*********************************************************************************************/
	public void setTheParking(PriorityQueue<ParkingSpace> theParking) 
	{
		this.theParking = theParking;
	}
	/*********************************************************************************************/
	public int getRows()
	{
		return rows;
	}
	/*********************************************************************************************/
	public int getFloor() 
	{
		return floors;
	}
	/*********************************************************************************************/
}