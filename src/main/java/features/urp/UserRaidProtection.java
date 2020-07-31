package features.urp;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class UserRaidProtection implements EventListener {
	
	public static volatile HashMap<Guild, Object[]> moving_avg = new HashMap<Guild, Object[]>();
	public static final Double baseUserThreshold = 5.00; 
	
	public static Double updateMovingAverage(Guild gld) {
		
		
		
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
