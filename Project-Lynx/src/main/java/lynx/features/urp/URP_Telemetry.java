package lynx.features.urp;

import java.time.Instant;
import java.util.ArrayList;

import org.json.JSONObject;

import lynx.features.Telemetry;

public class URP_Telemetry extends Telemetry {
	
	/**
	 * A double value that represents the server's accepted threshold from the moving average.
	 */
	private double maximumThreshold;
	/**
	 * A double value that contains the moving average (based on 1 hour) of the amount of users joining.
	 */
	private double movingAvg;
	/**
	 * An int value that contains the amount of hours counted.
	 * <br></br>
	 * <b>Doesn't save to the JSON!</b>
	 */
	private int hoursCounted;
	/**
	 * A long value that contains the last acceptable UNIX timestamp.
	 * <br></br>
	 * <b>Doesn't save to the JSON!</b>
	 */
	private long lastAcceptableUNIX;
	/**
	 * An ArrayList containing an Object array (which should be sized to only 2). 
	 * 
	 * Object[0] contains UNIX timetamp and Object[1] contains user.
	 * <br></br>
	 * <b>Doesn't save to the JSON!</b>
	 */
	private ArrayList<Object[]> unixUserPair = new ArrayList<Object[]>();
	/**
	 * A sized Integer array containing 3 dimensions, containing the user counts. Each dimension representing the hour.
	 * Integer[0] is the oldest of the three hours. Integer[2] is the current of the three hours.
	 * This is used for calculating the moving average.
	 * <br></br>
	 * <b>Doesn't save to the JSON!</b>
	 */
	private Integer[] tripletUsrCountArr = new Integer[3];
	
	// Constructors
	public URP_Telemetry(JSONObject parse) {
		parseJSON(parse);
		setLastAcceptableUNIX(Instant.EPOCH.getEpochSecond());
	}
	public URP_Telemetry(JSONObject parse, long unix) {
		parseJSON(parse);
		setLastAcceptableUNIX(unix);
	}
	
	// Simple Setter and Getter Methods
	public void setMaximumThreshold(double d) {
		maximumThreshold = d;
	}
	public void setMovingAvg(double d) {
		movingAvg = d;
	}
	public void setHoursCounted(int h) {
		hoursCounted = h;
	}
	public void setLastAcceptableUNIX(long u) {
		lastAcceptableUNIX = u;
	}
	public void setUNIXUserPair(ArrayList<Object[]> pair) {
		unixUserPair = pair;
	}
	public void setTripletUsrCountArr(Integer[] triplet) {
		tripletUsrCountArr = triplet;
	}
	
	public double getMaximumThreshold() {
		return maximumThreshold;
	}
	public double getMovingAvg() {
		return movingAvg;
	}
	public int getHoursCounted() {
		return hoursCounted;
	}
	public long getLastAcceptableUNIX() {
		return lastAcceptableUNIX;
	}
	public ArrayList<Object[]> getUNIXUserPair() {
		return unixUserPair;
	}
	public Integer[] getTripletUsrCountArr() {
		return tripletUsrCountArr;
	}
	
	/**
	 * Method that converts the JSONObject into variables within this object.
	 * @param parse is the JSONObject that will be parsed into the variables.
	 */
	public void parseJSON(JSONObject parse) {
		if(parse.getBoolean("active")) { // Check if URP is active for the server
			setMaximumThreshold(parse.getDouble("maximum_threshold"));
			setMovingAvg(parse.getDouble("user_moving_avg"));
		}
	}
	
	/**
	 * Method that updates the telemetry data as necessary.
	 * @return true if update is successful, otherwise false.
	 */
	@Override
	public boolean updateTelemetry() {
		// TODO Auto-generated method stub
		return false;
	}

}