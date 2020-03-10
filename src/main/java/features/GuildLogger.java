package features;

import java.util.ArrayList;

import init.Launcher;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class GuildLogger implements EventListener {
	
	private Guild logging_gld;
	private MessageChannel logging_chnl;
	private ArrayList<LoggerEventTypes> log_types;

	// Constructors
	public GuildLogger(Guild gld) {
		setLoggingGuild(gld);
	}
	
	// -- Start Simple Getter and Setter Methods
	public void setLoggingGuild(Guild gld) {
		logging_gld = gld;
	}
	public void setLoggingChannel(MessageChannel chnl) {
		logging_chnl = chnl;
	}
	public void setLoggingChannel(long id) {
		logging_chnl = Launcher.api.getTextChannelById(id);
	}
	public void setLoggerEventTypes(ArrayList<LoggerEventTypes> types) {
		log_types = types;
	}
	
	public Guild getLoggingGuild() {
		return logging_gld;
	}
	public MessageChannel getLoggingChannel() {
		return logging_chnl;
	}
	public ArrayList<LoggerEventTypes> getLoggerEventTypes() {
		return log_types;
	}
	
	// -- End Simple Getter and Setter Methods
	
	public void addLoggerEventType(LoggerEventTypes type) {
		for(LoggerEventTypes val: log_types) {
			if(val.equals(type)) {
				return;
			}
		}
		
		log_types.add(type);
		
	}

	@Override
	public void onEvent(GenericEvent event) {
		
	}
	
}
