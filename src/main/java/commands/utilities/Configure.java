package commands.utilities;

import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import commands.Command;
import data.Data;
import handlers.MessageHandler;
import handlers.ServerHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

enum Commands {
	
	// This will suffice for now lol
	
	ABOUT("about", 0),
	HELP("help", 1),
	BAN("ban", 2),
	KICK("kick", 3),
	WARN("warn", 4),
	WARNINGS("warnings", 5),
	CONFIGURE("configure", 6),
	SHUTDOWN("shutdown", 7),
	WELCOME("welcome", 8);
	
	private String assignmentName = "DEFAULT";
	private int assignmentNum = -1;
	
	Commands(String n, int i) {
		
		setAssignmentName(n);
		setAssignmentNum(i);
		
	}

	public String getAssignmentName() {
		return assignmentName;
	}
	public int getAssignmentNum() {
		return assignmentNum;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
	public void setAssignmentNum(int assignmentNum) {
		this.assignmentNum = assignmentNum;
	}
}

public class Configure extends Command {
	
	Commands modifyCommand = null;
	
	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User usr = ((MessageReceivedEvent) misc).getAuthor();
		
		//MessageHandler.sendMessage(chn, "[DEBUG] " + usr.getAsMention() + " is attempting to configure in Guild " + gld.getName() + " and has " + gld.getMember(usr).getPermissions());
		
		boolean hasAdmin = ServerHandler.getServerOwner(gld.getIdLong()).equals(usr) || gld.getMember(usr).hasPermission(Permission.ADMINISTRATOR);
		
		if(msg.equalsIgnoreCase(getName())) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		} else {
			if(hasAdmin) {
				// Iterate through the values
				for(Commands c: Commands.values()) {
					if(c.getAssignmentName().contains(msg.substring(msg.indexOf(getName()) + getName().length() + 1))) {
						
						modifyCommand = c;
						
						MessageHandler.sendMessage(chn, "You've configured " + c.getAssignmentName());
						
						break;
					}
				}
				
				return true;
			} else {
				MessageHandler.sendMessage(chn, "Insufficient permissions! (Must be an Administrator or Server Owner)");
				return false;
			}
		}
		
	}
	
}
