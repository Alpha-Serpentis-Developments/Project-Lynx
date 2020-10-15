package lynx.features;

import java.io.File;

import net.dv8tion.jda.api.entities.Guild;

public abstract class Telemetry {
	
	private Guild guild;
	private File telemetryFile;
	
	public Telemetry() {
		setGuild(null);
	}
	public Telemetry(Guild gld) {
		setGuild(gld);
	}
	
	// Simple Setter & Getter Methods
	public void setGuild(Guild gld) {
		guild = gld;
	}
	public void setTelemetryFile(File f) {
		telemetryFile = f;
	}
	
	public Guild getGuild() {
		return guild;
	}
	public File getTelemetryFile() {
		return telemetryFile;
	}
	
	
	public abstract boolean updateTelemetry();
	
}
