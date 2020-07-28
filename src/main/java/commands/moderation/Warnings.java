package commands.moderation;

import commands.Command;
import handlers.MessageHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Warnings extends Command {

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User mod = ((MessageReceivedEvent) misc).getAuthor(), punished = (msg.length() == getName().length()) ? null : gld.getMemberById(msg.substring(msg.indexOf("@") + 1, msg.indexOf(">"))).getUser();
		
		if(punished == null) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		}
		
		boolean result = verifyExecution(mod, punished, gld, chn);
		
		if(result) {
			
			
			
		}
		
		return false;
		
	}

}
