package features.urp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class UserRaidProtection implements EventListener {
	
	public static volatile HashMap<Guild, URP_Telemetry> moving_avg = new HashMap<Guild, URP_Telemetry>();
	public static final Integer movingAverageTimeSeconds = 3600;
	public static final Double baseUserThreshold = 3.00; 
	public static final String detectedRoleName = "Lynx URP Triggered";
	
	/**
	 * This method is called every time a new user joins, so the count goes up by one.
	 * However, this method recalculates the moving average to account for the new user, but also checks for a new valid UNIX timestamp to calculate the moving average.
	 * 
	 * @param gld is the Guild to update.
	 * @param usr is the User to add to the pairs.
	 * @return a Double value with the new moving average for the guild.
	 */
	public static Double updateMovingAverage(Guild gld, User usr) {
		
		URP_Telemetry g_urp = moving_avg.get(gld);
		
		Long currentUNIX = Instant.now().getEpochSecond();
		//Double currentMA = g_urp.getMovingAvg();
		Integer newUserCount = null;
		
		ArrayList<Integer> indexMarked = new ArrayList<Integer>();
		ArrayList<Object[]> modifyUnixUserPair = new ArrayList<Object[]>();
		
		// Check if the UNIX is still valid
		if(currentUNIX - g_urp.getLastAcceptableUNIX() > 60 * 60) { //60 sec * 60 minutes 
			
			// Check the pairs if anyone of them are not within the 3600 seconds.
			for(int i = 0; i < g_urp.getUNIXUserPair().size(); i++) {
				
				// Object[0] is the UNIX timestamp
				// Object[1] is the user
				if((Long) g_urp.getUNIXUserPair().get(i)[0] > 60 * 60) {
					indexMarked.add(i); // Marks for deletion
				}
				
			}
			
		}
		
		for(int index: indexMarked) { // Loop to remove any potential outdated pairs.
			modifyUnixUserPair.remove(index);
		}
		
		// Add the user to the list of pair
		modifyUnixUserPair.add(new Object[] {currentUNIX, usr});
		
		g_urp.setUNIXUserPair(modifyUnixUserPair);
		g_urp.setLastAcceptableUNIX(currentUNIX);
		
		newUserCount = modifyUnixUserPair.size();
		
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
