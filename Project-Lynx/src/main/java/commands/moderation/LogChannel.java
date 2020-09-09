package commands.moderation;

import commands.Command;
import net.dv8tion.jda.api.entities.MessageChannel;

public class LogChannel extends Command {
	
	public LogChannel() {
		setName("logchannel");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		// TODO Auto-generated method stub
		return false;
	}

}
