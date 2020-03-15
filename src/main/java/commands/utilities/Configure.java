package commands.utilities;

import java.util.function.Consumer;

import commands.Command;
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
	
	ABOUT("name", 0),
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

public class Configure extends Command implements EventListener {
	
	private User monitorUser = null;
	private Guild monitorGuild = null;
	private boolean readyToMonitor = false;
	
	Commands modifyCommand = null;
	
	@SuppressWarnings("unchecked")
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
						Consumer<Message> callback;
						
						modifyCommand = c;
						setMonitorUser(usr);
						setMonitorGuild(gld);
						
						// Check the current Guild's configuration
						// If the Guild isn't configured, then proceed with the default configuration, otherwise proceed with 
						
						callback = MessageHandler.sendMessage(chn, "You are configuring command " + c.getAssignmentName() + "\n\n");
						
						callback = (v) -> { // TODO: Maybe fix this up if I can remember what the heck I thought of...
							setReadyToMonitor(true);
						};
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

	@Override
	public void onEvent(GenericEvent event) {
		
		if(event instanceof MessageReceivedEvent && getReadyToMonitor() && (monitorUser != null && monitorGuild != null) && ((MessageReceivedEvent) event).getGuild().equals(getMonitorGuild()) && ((MessageReceivedEvent) event).getAuthor().equals(getMonitorUser())) {
			MessageReceivedEvent evt = (MessageReceivedEvent) event;
			String message = evt.getMessage().getContentDisplay();
			
		}
		
	}
	
	// -- Simple Setter & Getter Methods
	public void setMonitorUser(User u) {
		monitorUser = u;
	}
	public void setMonitorGuild(Guild g) {
		monitorGuild = g;
	}
	public void setReadyToMonitor(boolean v) {
		readyToMonitor = v;
	}
	
	public User getMonitorUser() {
		return monitorUser;
	}
	public Guild getMonitorGuild() {
		return monitorGuild;
	}
	public boolean getReadyToMonitor() {
		return readyToMonitor;
	}
	// -- End Simple Setter & Getter Methods
	
	public boolean isGuildConfigured() {
		
		
		
		return false;
	}
	
}
