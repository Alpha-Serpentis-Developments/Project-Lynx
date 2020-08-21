package handlers;

import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;

import commands.Command;
import data.Data;
import init.InitData;
import init.Launcher;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.EventListener;

public class MessageHandler implements EventListener {
	
	private volatile HashMap<User, Long> limitCheck = new HashMap<User, Long>();

	@Override
	public void onEvent(GenericEvent event) {

		//System.out.println("DEBUG [MessageHandler.java]: " + event);

		/*
		 * Checks if the event triggered is a message type and ISN'T A BOT (you can remove it from the if statement if you wish).
		 */
		if((event instanceof MessageReceivedEvent || (InitData.acceptPriv && event instanceof PrivateMessageReceivedEvent)) && !((MessageReceivedEvent) event).getAuthor().isBot() && Launcher.initialized) {

			User author = ((MessageReceivedEvent) event).getAuthor();
			Guild g = null;
			long delayTimer = 150; // 150 ms
			String prefix; //Server's prefix... if it's even a server.
			
			try {
				g = ((MessageReceivedEvent) event).getGuild();
			} catch(IllegalStateException e) {
				return; //TODO: Support PrivateMessages??? (This is here to prevent NullPointers)
 			}
			
			if(Data.srvr_cache.get(g) == null && g != null) {
				Data.addGuild(g);
				prefix = String.valueOf(InitData.prefix);
			} else if(g != null) {
				prefix = ((String) Data.srvr_cache.get(g).get("prefix"));
			} else {
				prefix = String.valueOf(InitData.prefix);
			}
			
			//System.out.println(prefix);
			
			/*
			 * Checks the message uses the defined prefix found in InitData.java (you can change the prefix if you need to)
			 */
			if(((MessageReceivedEvent) event).getMessage().getContentRaw().indexOf(prefix) == 0) {

				String fullMsg = ((MessageReceivedEvent) event).getMessage().getContentRaw().substring(prefix.length());
				ChannelType c = event instanceof PrivateMessageReceivedEvent ? ChannelType.PRIVATE : ChannelType.TEXT;

				System.out.println(fullMsg);
				
				Command cmd;
				if(!fullMsg.contains(" ")) {
					cmd = CommandHandler.getCommand(fullMsg, g);
				} else {
					cmd = CommandHandler.getCommand(fullMsg.substring(0, fullMsg.indexOf(" ")), g);
				}
				
				if(cmd == null) {
					System.out.println("[MessageHandler.java] WARNING: Command is null?????");
					return;
				}
				
				// Check if user is trying to spam the command.
				if(!(limitCheck.get(((MessageReceivedEvent) event).getAuthor()) == null)) {
					long currentMs = System.currentTimeMillis();
					
					if(currentMs-delayTimer <= limitCheck.get(((MessageReceivedEvent) event).getAuthor())) {
						System.out.println("WARNING [MessageHandler.java] " + author + " has breached the delay timer!");
						return;
					}
				}

				System.out.println("[MessageHandler.java] For server... " + g.getName() + " ... " + Data.command_cache.get(g));
				System.out.println("[MessageHandler.java] Grabbed... " + cmd.getName());

				if(cmd == null || (g == null && cmd.getRequirePerms() == true)) return;

				//System.out.println("DEBUG [MessageHandler.java]: " + cmd.getName());
				//System.out.println("DEBUG [MessageHandler.java]: (ChannelType) " + c);
				
				new Thread() {
					public void run() {
						
						if(c.equals(ChannelType.PRIVATE))
							cmd.action(((MessageReceivedEvent) event).getAuthor().openPrivateChannel().complete(), fullMsg, event); //Just pass the entire thing to prevent NullPointers, each command will handle them appropriately
						else if(c.equals(ChannelType.TEXT))
							cmd.action(((MessageReceivedEvent) event).getChannel(), fullMsg, event);
						
					}
				}.run();
				
				limitCheck.put(((MessageReceivedEvent) event).getAuthor(), System.currentTimeMillis());
				
			}

		}

	}

	public static Consumer<Message> sendMessage(MessageChannel chn, String s) {

		Consumer<Message> callback = (response) -> System.out.printf("[MessageHandler.java] Sent \"%s\"", response);
		try {
			chn.sendMessage(s).queue(callback);
			return callback;
		} catch(InsufficientPermissionException e) {
			System.out.println("[MessageHandler.java]: Message was not sent due to insufficient permissions!");
		} catch(IllegalArgumentException e) {
			System.out.println("[MessageHandler.java]: Message was not sent due to an empty text field!");
		}
		
		return null;

	}

	public static Consumer<Message> sendMessage(MessageChannel chn, String s, File f) {

		if(f != null) {
			Consumer<Message> callback = (response) -> System.out.printf("[MessageHandler.java] Sent \"%s\"", response);
			try {
				chn.sendMessage(s).addFile(f).queue(callback);
				return callback;
			} catch(InsufficientPermissionException e) {
				System.out.println("[MessageHandler.java]: Message was not sent due to insufficient permissions!");
			}
		} else
			System.out.println("[MessageHandler.java] Unable to send a message, file doesn't exist?");
		
		return null;

	}

	//TODO: Do this...
	public void embedMessage() {

	}

}
