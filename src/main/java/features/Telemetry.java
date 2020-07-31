package features;

import net.dv8tion.jda.api.entities.Guild;

public abstract class Telemetry {
	
	private Guild guild;
	
	public Telemetry() {
		setGuild(null);
	}
	public Telemetry(Guild gld) {
		setGuild(gld);
	}
	
	public void setGuild(Guild gld) {
		guild = gld;
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public abstract boolean updateTelemetry();
	
}
