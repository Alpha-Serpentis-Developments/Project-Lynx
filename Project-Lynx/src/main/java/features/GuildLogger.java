package features;

import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class GuildLogger implements EventListener {
	
	public static volatile HashMap<Guild, List<GenericGuildEvent>> gldLogJDAEvents = new HashMap<Guild, List<GenericGuildEvent>>();
	public static volatile HashMap<Guild, List<String>> gldLogCustomEvents = new HashMap<Guild, List<String>>();
	
	public void customEvent(String k) {
		
	}
	
	@Override
	public void onEvent(GenericEvent event) {
		
		if(event instanceof GenericGuildEvent) {
			
			new Thread() {
				public void run() {
					
					GenericGuildEvent genericEvt = (GenericGuildEvent) event;
					
					if(!gldLogJDAEvents.containsKey(genericEvt.getGuild())) // If the guild isn't in the HashMap, get out.
						return;
					
					// Loop the list
					for(GenericGuildEvent gEvnt: gldLogJDAEvents.get(genericEvt.getGuild())) {
						
						switch(gEvnt.getClass().getSimpleName().toLowerCase()) {
						
						// JDA Events
						case "guildbanevent":
							
							GBan((GuildBanEvent) gEvnt);
							
							break;
						case "guildkickevent":
							
							GKick();
							
							break;
						case "guildmessagedeleteevent":
							
							GMessageDelete();
							
							break;
							
						// Custom Events
						case "warn":
							
							Warn();
							
							break;
						case "userraiddetect":
							
							UserRaid();
							
							break;
						
						}
						
					}
					
				}
			}.run();
			
		}
		
	}
	
	public boolean GBan(GuildBanEvent evt) {
		
		
		
		return false;
	}
	
	public boolean GKick() {
		return false;
	}
	
	public boolean GMessageDelete() {
		return false;
	}
	
	public boolean Warn() {
		return false;
	}
	
	public boolean UserRaid() {
		return false;
	}
	
}
