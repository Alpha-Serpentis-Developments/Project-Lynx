package features;

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
	
	LoggerEventTypes(String s) {
		s = name;
	}
	
	public String toString() {
		return name;
	}
	
}
