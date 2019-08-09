package commands.general;

import commands.Command;
import handlers.CommandHandler;
import handlers.MessageHandler;
import init.InitData;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Help extends Command {

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
	
		MessageHandler.sendMessage(chn, getDesc());
		
		return true;
		
	}

}
