package features.urp;

import java.util.ArrayList;

import org.json.JSONObject;

import features.Telemetry;

public class URP_Telemetry extends Telemetry {
	
	/**
	 * A double value that contains the moving average (based on 1 hour) of the amount of users joining.
	 */
	private double movingAvg;
	/**
	 * An int value that contains the amount of hours counted.
	 */
	private int hoursCounted;
	/**
	 * A long value that contains the last UNIX timestamp.
	 */
	private long lastUNIX;
	/**
	 * An ArrayList containing an Object array (which should be sized to only 2). Object[0] contains UNIX timetamp and Object[1] contains user.
	 */
	private ArrayList<Object[]> unixUserPair = new ArrayList<Object[]>();

	// Constructors
	public URP_Telemetry(JSONObject parse) {
		
	}
	public URP_Telemetry(JSONObject parse, long unix) {
		
	}
	
	// Simple Setter and Getter Methods
	public void setMovingAvg(double d) {
		movingAvg = d;
	}
	public void setHoursCounted(int h) {
		hoursCounted = h;
	}
	public void setLastUNIX(long u) {
		lastUNIX = u;
	}
	public void setUNIXUserPair(ArrayList<Object[]> pair) {
		unixUserPair = pair;
	}
	
	public double getMovingAvg() {
		return movingAvg;
	}
	public int getHoursCounted() {
		return hoursCounted;
	}
	public long getLastUNIX() {
		return lastUNIX;
	}
	public ArrayList<Object[]> getUNIXUserPair() {
		return unixUserPair;
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