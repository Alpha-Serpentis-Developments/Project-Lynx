package features;

import net.dv8tion.jda.api.events.guild.GenericGuildEvent;

public enum LoggerEventTypes {
	
	WARN("warn"),
	KICK("kick"),
	BAN("kick"),
	MESSAGE_DELETED("message_del"),
	MESSAGE_EDITED("message_edit"),
	USER_JOINED("user_join"),
	USER_LEFT("user_left"),
	USER_NICKNAME_CHANGED("user_nick_changed");
	
	private String name;
	private GenericGuildEvent eventType;
	
	LoggerEventTypes(String s) {
		
	}
	LoggerEventTypes(String s, GenericGuildEvent ge) {
		s = name;
		eventType = ge;
	}
	
	public String toString() {
		return name + ": " + eventType;
	}
	
}
