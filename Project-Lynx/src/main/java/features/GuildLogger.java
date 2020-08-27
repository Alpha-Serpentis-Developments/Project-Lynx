package features;

import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class GuildLogger implements EventListener {
	
	public static volatile HashMap<Guild, List<GenericGuildEvent>> gldLogEvents = new HashMap<Guild, List<GenericGuildEvent>>();
	
	@Override
	public void onEvent(GenericEvent event) {
		
		if(event instanceof GenericGuildEvent) {
			
		}
		
	}
	
}
