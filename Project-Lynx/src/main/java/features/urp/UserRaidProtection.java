package features.urp;

import java.time.Instant;
import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class UserRaidProtection implements EventListener {
	
	public static volatile HashMap<Guild, URP_Telemetry> moving_avg = new HashMap<Guild, URP_Telemetry>();
	public static final Double baseUserThreshold = 5.00; 
	public static final String detectedRoleName = "URP Triggered";
	
	public static Double updateMovingAverage(Guild gld) {
		
		long currentInst = Instant.now().getEpochSecond();
		long telemetryInst = moving_avg.get(gld).getLastUNIX();
		
		long difference = currentInst - telemetryInst;
		// Check if it has been an hour
		if(difference > 60 * 60) {
			moving_avg.get(gld).setLastUNIX(currentInst);
		}
		
		// Reformulate as necessary
		
		return null;
		
	}
	
	public static boolean modifyThreshold(Guild gld, double percent) {
		
		boolean result = false;
		
		return result;
		
	}
	public static boolean thresholdReached(Guild gld) {
		
		boolean result = false;
		
		return result;
	}

	@Override
	public void onEvent(GenericEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
