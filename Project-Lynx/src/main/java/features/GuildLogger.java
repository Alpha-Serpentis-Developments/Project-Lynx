package features;

import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class GuildLogger implements EventListener {
	
	public static volatile HashMap<Guild, List<GenericGuildEvent>> gldLogEvents = new HashMap<Guild, List<GenericGuildEvent>>();
	
	public void customEvent(String k) {
		
	}
	
	@Override
	public void onEvent(GenericEvent event) {
		
		if(event instanceof GenericGuildEvent) {
			
			GenericGuildEvent genericEvt = (GenericGuildEvent) event;
			
			if(!gldLogEvents.containsKey(genericEvt.getGuild())) // If the guild isn't in the HashMap, get out.
				return;
			
			// Loop the list
			for(GenericGuildEvent gEvnt: gldLogEvents.get(genericEvt.getGuild())) {
				
				switch(gEvnt.getClass().getSimpleName().toLowerCase()) {
				
				// JDA Events
				case "guildbanevent":
					
					break;
				case "guildkickevent":
					
					break;
				case "guildmessagedeleteevent":
					
					break;
					
				// Custom Events
				case "warn":
					
					break;
				case "userraiddetect":
					
					break;
				
				}
				
			}
			
		}
		
	}
	
}
