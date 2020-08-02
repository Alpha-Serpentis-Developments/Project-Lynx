package features.urp;

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
	
	public double getMovingAvg() {
		return movingAvg;
	}
	public int getHoursCounted() {
		return hoursCounted;
	}
	public long getLastUNIX() {
		return lastUNIX;
	}
	
	@Override
	public boolean updateTelemetry() {
		// TODO Auto-generated method stub
		return false;
	}

}