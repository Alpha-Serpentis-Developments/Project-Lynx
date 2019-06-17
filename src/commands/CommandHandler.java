package commands;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandHandler {
	
	public boolean checkCommands(String search, Event event) {
		
		User usr = ((MessageReceivedEvent) event).getMember().getUser();
		TextChannel chn = ((MessageReceivedEvent) event).getTextChannel();
		boolean priv = (chn.getType() == ChannelType.PRIVATE);

		return false;
		
	}
	
	public void shutdown() {
		
	}
	
	public void shutdown(int s) {
		
	}

}
