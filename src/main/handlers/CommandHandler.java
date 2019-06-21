package main.handlers;

import main.commands.general.About;
import main.commands.general.Help;
import main.commands.moderation.Ban;
import main.commands.moderation.Kick;
import main.commands.moderation.Warn;
import main.init.InitData;
import main.init.Launcher;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandHandler {
	
	/**Searches for commands
	 * 
	 * @param search
	 * @param event
	 * @return {true, true} if everything is cleared, {true, false} if command is valid, but unable to execute, {false, false} if the command is not found
	 */
	public static boolean[] useCommand(String search, Event event) {
		
		//Use these?
		//User usr = ((MessageReceivedEvent) event).getMember().getUser();
	    //TextChannel chn = ((MessageReceivedEvent) event).getTextChannel();
		//boolean priv = (chn.getType() == ChannelType.PRIVATE);

		Object result = getCommand(true, search, event);
		
		if(result instanceof boolean[]) {
			return (boolean[]) result;
		} else {
			return new boolean[] {false, false};
		}
		
	}
	
	public static Object getCommand(boolean use, String search, Event evt) {
		
		boolean[] returnThis = new boolean[2];
		
		switch(search.toLowerCase()) {
		
		//General
		case "help":
			if(use) {
				returnThis[0] = true;
				returnThis[1] = new Help().action(((MessageReceivedEvent) evt).getTextChannel(), ((MessageReceivedEvent) evt).getMessage().getContentDisplay(), null);
				return returnThis;
			} else {
				return new Help().getDesc();
			}
		case "about":
			if(use) {
				returnThis[0] = true;
				returnThis[1] = new About().action(((MessageReceivedEvent) evt).getTextChannel(), null, null);
				return returnThis;
			} else {
				return new About().getDesc();
			}
			
		//Moderator
		case "kick":
			if(use) {
				returnThis[0] = true;
				returnThis[1] = new Kick().action(((MessageReceivedEvent) evt).getTextChannel(), ((MessageReceivedEvent) evt).getMessage().getContentRaw(), evt);
				return returnThis;
			} else {
				return new Kick().getDesc();
			}
		case "ban":
			if(use) {
				returnThis[0] = true;
				returnThis[1] = new Ban().action(((MessageReceivedEvent) evt).getTextChannel(), ((MessageReceivedEvent) evt).getMessage().getContentRaw(), evt);
				return returnThis;
			} else {
				return new Ban().getDesc();
			}
		case "warn":
			if(use) {
				returnThis[0] = true;
				returnThis[1] = new Warn().action(((MessageReceivedEvent) evt).getTextChannel(), ((MessageReceivedEvent) evt).getMessage().getContentRaw(), evt);
				return returnThis;
			} else {
				return new Warn().getDesc();
			}
			
		//Bot Owner Commands
		case "shutdown":
			if(use) {
				returnThis[0] = true;
				returnThis[1] = shutdown(((MessageReceivedEvent) evt).getTextChannel(), ((MessageReceivedEvent) evt).getAuthor());
				return returnThis;
			} else {
				return "Shuts down the bot";
			}
		}
		default:
			return new boolean[] {false, false};
		}
	}
	
	//Bot Owner Command
	
	/**
	 * TODO: Shutdown needs to have its own class
	 * 
	 * @param chn the channel where the shutdown was requested
	 * @param usr is the user requesting the shutdown
	 * @return TRUE if the shutdown request was done by a Bot Owner, otherwise false
	 */
	public static boolean shutdown(TextChannel chn, User usr) {
		
		for(Long id: InitData.botOwnerIDs) {
			if(id.equals(usr.getIdLong())) {
				MessageHandler.sendMessage(chn, "Shutting down!");
				
				Launcher.api.shutdown();
				return true;
			}
		}
		
		return false;
	}

}
