package main.handlers;

import main.init.InitData;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
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
		if(event instanceof MessageReceivedEvent && !((MessageReceivedEvent) event).getAuthor().isBot()) {
			
			/*
			 * Checks the message uses the defined prefix found in InitData.java (you can change the prefix if you need to)
			 */
			if(((MessageReceivedEvent) event).getMessage().getContentDisplay().indexOf(InitData.prefix) == 0) {
				
				String fullMsg = ((MessageReceivedEvent) event).getMessage().getContentDisplay(), msg;
				
				if(fullMsg.indexOf(" ") == -1) //If there's no space 
					msg = fullMsg.substring(1);
				else
					msg = fullMsg.substring(1, fullMsg.indexOf(" "));
				
				//Checks the result of the command request
				boolean[] result = CommandHandler.useCommand(msg, event);
				
				if(!result[0]) {
					System.out.println("Command not found!");
				} else {
					if(!result[1]) {
						System.out.println("Command could not execute! (Are you allowed to use the command?)");
						//sendMessage(((MessageReceivedEvent) event).getTextChannel(), "Command failed to execute"); //Uncomment if you wish.
					}
				}
				
			}
			
		}
		
	}
	

	/**Sends a message to the specified channel 
	 * 
	 * @param chn is REQUIRED in order to send a message
	 * @param s is the message
	 */
	public static void sendMessage(TextChannel chn, String s) {
		
		chn.sendMessage(s).queue();
		
	}
	
	public static void sendMessage(TextChannel chn, Message m) {
		
		chn.sendMessage(m).queue();
		
	}
	

	/**Sends a message to the DMs (it may execute, but not go through)
	 * 
	 * @param chn is REQUIRED in order to send a message privately
	 * @param s is the message
	 */
	public static void sendMessage(PrivateChannel chn, String s) {
		
		chn.sendMessage(s).queue();
		
	}
	
	public void embedMessage() {
		
	}
	
}
