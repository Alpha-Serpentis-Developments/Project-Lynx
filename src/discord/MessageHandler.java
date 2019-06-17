package discord;

import init.InitData;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class MessageHandler implements EventListener {

	@Override
	public void onEvent(Event event) {
	
		/*
		 * Checks if the event triggered is a message type
		 */
		if(event instanceof MessageReceivedEvent) {
			
			/*
			 * Checks the message uses the defined prefix found in InitData.java (you can change the prefix if you need to)
			 */
			if(((MessageReceivedEvent) event).getMessage().getContentDisplay().indexOf(InitData.prefix) == 0) {
				
				
				
			}
			
		}
		
	}
	
	public void sendMessage(TextChannel chn, String s) {
		chn.sendMessage(s).queue();
	}
	public void embedMessage() {
		
	}
	
}
