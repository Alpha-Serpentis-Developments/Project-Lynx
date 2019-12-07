package commands.utilities;

import commands.Command;
import data.Data;
import handlers.MessageHandler;
import handlers.ServerHandler;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Configure extends Command {
	
	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User usr = ((MessageReceivedEvent) misc).getAuthor();
		
		boolean result = ServerHandler.getServerOwner(gld.getIdLong()).equals(usr);
		String type;
		
		if(msg.substring(0, msg.indexOf(" ")).equalsIgnoreCase("config")) {
			
		} else {
			
		}
		
		if(result) {
			
			
			
		}
		
		return false;
		
	}
	
}
