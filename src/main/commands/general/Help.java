package main.commands.general;

import main.commands.Command;
import main.handlers.CommandHandler;
import main.handlers.MessageHandler;
import main.init.InitData;
import net.dv8tion.jda.core.entities.TextChannel;

public class Help extends Command {

	public Help() {
		super("help", "Use this command to obtain information on both the bot and commands available!\n\n`" + InitData.prefix + "help [command name]`");
	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) { // misc can be used as a command type ident
		
		if(msg.toLowerCase().matches(InitData.prefix + "help")) {
			MessageHandler.sendMessage(chn, getDesc());
			
			return true;
		} else {
			
			String search = msg.substring(msg.indexOf(" ") + 1);
			Object result = CommandHandler.getCommand(false, search, null);
			
			if(result instanceof String) {
				MessageHandler.sendMessage(chn, (String) result);
				return true;
			} else if(result instanceof boolean[]) {
				if(result == new boolean[] {false, false}) {
					MessageHandler.sendMessage(chn, "Unable to find " + search);
					return false; 
				}
			}
			
			return false;
			
		}
	}

}
