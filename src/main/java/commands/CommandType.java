package commands;

public enum CommandType {

	GENERAL("general"), MOD("mod"), GUILD_OWNER("guild_owner"), BOT_OWNER("bot_owner");
	
	private String name;
	
	CommandType(String s) {
		this.name = s;
	}
	
	public String toString() {
		return name;
	}
	
}
