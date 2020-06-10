package commands.utilities;

import java.util.ArrayList;

import commands.Command;
import handlers.MessageHandler;
import handlers.ServerHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

enum Commands {
	
	// This will suffice for now lol
	
	ABOUT("about", 0),
	HELP("help", 1),
	BAN("ban", 2),
	KICK("kick", 3),
	WARN("warn", 4),
	WARNINGS("warnings", 5),
	//CONFIGURE("configure", 6), // You cannot configure the configure command
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
		int tier_level = -1;
		
		if(msg.equalsIgnoreCase(getName())) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		} else {
			if(hasAdmin) {
				// Iterate through the values
				//System.out.println("DEBUG [Configure.java] " + msg.substring(msg.indexOf(getName()) + getName().length() + 1));
				for(Commands c: Commands.values()) {
					//System.out.println("DEBUG FOR LOOP [Configure.java] " + c.getAssignmentName());
					if(msg.substring(msg.indexOf(getName()) + getName().length() + 1).contains(c.getAssignmentName())) {
						
						modifyCommand = c;
						System.out.println("DEBUG [Configure.java] " + msg);
						//System.out.println("DEBUG [Configure.java] " + msg.substring(msg.indexOf(c.getAssignmentName())));
						
						
						try {
							//System.out.println("DEBUG TRY [Configure.java] " + msg.substring(msg.indexOf(c.getAssignmentName()) + c.getAssignmentName().length() + 1, msg.indexOf(c.getAssignmentName()) + c.getAssignmentName().length() + 3));
							tier_level = new Integer(msg.substring(msg.indexOf(c.getAssignmentName()) + c.getAssignmentName().length() + 1, msg.indexOf(c.getAssignmentName()) + c.getAssignmentName().length() + 2));
						} catch(StringIndexOutOfBoundsException e) {
							//e.printStackTrace();
						} catch(Exception e) {
							System.out.println("Caught " + e.getClass().getName());
							e.printStackTrace();
						}
						
						//System.out.println("DEBUG [Configure.java] tier_level:" + tier_level);
						
						// Determine the tier level 
						
						if(tier_level != -1) {
							MessageHandler.sendMessage(chn, "You've configured " + c.getAssignmentName());
						} else {
							MessageHandler.sendMessage(chn, "Configuration syntax is incorrect! Refer to the description.\n\n" + this.getDesc());
						}
						
						// Determine the role(s) allowed
						ArrayList<Role> roles = new ArrayList<Role>(); //List of roles allowed
						String breakApart = msg.substring(msg.indexOf("[") + 1, msg.indexOf("]")); 
						
						while(!breakApart.equals("")) {
							Role decipheredRole = null; // The deciphered Role if it can even be deciphered by the provided parameters.
							String brokenString = null; // Piece of breakApart variable
							
							// Determines if it is the last item provided
							if(breakApart.contains(",")) {
								brokenString = breakApart.substring(0, breakApart.lastIndexOf(","));
							} else {
								brokenString = breakApart;
								breakApart = "";
							}
							
							// Check if they're tagged Roles or straight up long IDs
							System.out.println("DEBUG - BREAK APART [Configure.java] " + brokenString);
							
							try {
								decipheredRole = gld.getRoleById(brokenString);
							} catch (Exception e) {
								// If not the case, then...
								String[] illegal_chars = new String[] {"<", ">", "@", "&", "!"};
								
								for(String ill_char: illegal_chars) {
									brokenString = brokenString.replaceAll(ill_char, "");
								}
								
								decipheredRole = gld.getRoleById(brokenString);
								
							} finally {
								if(decipheredRole == null) {
									MessageHandler.sendMessage(chn, "MALFORMED ROLE ID! Cannot configure!");
									break;
								} else {
									roles.add(decipheredRole);
								}
							}
							
							if(breakApart != null)
								breakApart = breakApart.replaceFirst(brokenString, "");
							
						}
						
						if(roles.isEmpty()) {
							MessageHandler.sendMessage(chn, "Missing Role IDs! Please refer to the description.\n\n" + this.getDesc());
							break;
						}
						
						// Put the Role IDs into both the cache and WRITE the data.
						System.out.println("DEBUG - CAPTURE ROLES [Configure.java] " + roles);
						
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
