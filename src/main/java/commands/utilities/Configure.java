package commands.utilities;

import java.util.ArrayList;
import java.util.List;

import commands.Command;
import commands.Commands;
import data.Data;
import handlers.MessageHandler;
import handlers.ServerHandler;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

enum Commands {
	
	// This will suffice for now lol
	
	ABOUT("name", 0),
	HELP("help", 1);
	
	private static List<Commands> commands = new ArrayList<Commands>();
	
	private String assignmentName = "DEFAULT";
	private int assignmentNum = -1;
	
	Commands(String n, int i) {
		
	}
	
	public int getCommandAssignmentNum(String n) {

		
		
		return -1;
	}
}

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
