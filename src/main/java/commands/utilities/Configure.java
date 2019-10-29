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

		String[] configKeys = {"ban", "kick", "warn", "warnings", "welcome"};

		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User usr = ((MessageReceivedEvent) misc).getAuthor();

		boolean result = ServerHandler.getServerOwner(gld.getIdLong()).equals(usr); //TODO: Change this to allow various type of users and set the server owner as the default.
		String type, config;

		//Checks what type of message to send
		if(msg.substring(0, msg.indexOf(" ")).equalsIgnoreCase("config")) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		} else {

		}

		if(result) {

			config = msg.substring(msg.indexOf(" ") + 1).substring(0, msg.indexOf(" ")); //Supposed to find the key?

			for(String k: configKeys) {
				if(k.equalsIgnoreCase(config)) {

					//Write code...

					return true;
				}
			}

		}

		return false;

	}

}
