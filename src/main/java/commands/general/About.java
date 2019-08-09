package commands.general;

import commands.Command;
import handlers.MessageHandler;
import net.dv8tion.jda.core.entities.MessageChannel;

public class About extends Command {

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		MessageHandler.sendMessage(chn, getDesc());
		
		return true;
	}
	
}
