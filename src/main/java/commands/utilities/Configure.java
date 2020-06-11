package commands.utilities;

import java.util.ArrayList;

import org.json.JSONObject;

import commands.Command;
import data.Data;
import handlers.MessageHandler;
import handlers.ServerHandler;
import init.InitData;
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
	//CONFIGURE("configure", 6), // You cannot configure the configure command TODO: Make it so you can actually configure the configure command.
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
	
	Command modifyCommand = null;
	
	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User usr = ((MessageReceivedEvent) misc).getAuthor();
		
		JSONObject gld_obj = new JSONObject(Data.readData(InitData.locationJSON)).getJSONObject(gld.getId());
		
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
						
						// Grab the command to be modified for this guild
						for(Command md_c: Data.command_cache.get(gld)) {
							if(md_c.getName().equalsIgnoreCase(c.getAssignmentName())) {
								modifyCommand = md_c;
							}
						}
						
						if(modifyCommand == null) {
							MessageHandler.sendMessage(chn, "Something went wrong while obtaining the command!");
							return false;
						}
						
						System.out.println("DEBUG [Configure.java] " + msg);
						//System.out.println("DEBUG [Configure.java] " + msg.substring(msg.indexOf(c.getAssignmentName())));
						
						// Determine the tier level
						
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
						
						if(tier_level == -1) {
							MessageHandler.sendMessage(chn, "Configuration syntax is incorrect! Refer to the description.\n\n" + this.getDesc());
							return false;
						}
						
						// Determine the action to be done
						if(tier_level == 0) {
							
						} else if(tier_level == 1) {
						
						} else if(tier_level == 2) {
						
							// Determine the role(s) allowed
							ArrayList<Long> roles = new ArrayList<Long>(); //List of roles allowed
							String breakApart = msg.substring(msg.indexOf("[") + 1, msg.indexOf("]")); 
							
							while(!breakApart.equals("")) {
								Long decipheredRole = null; // The deciphered Role if it can even be deciphered by the provided parameters.
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
								
								String[] illegal_chars = new String[] {"<", ">", "@", "&", "!"};
								
								for(String ill_char: illegal_chars) {
									brokenString = brokenString.replaceAll(ill_char, "");
								}
								
								try {
									decipheredRole = new Long(brokenString);
								} catch (NumberFormatException e) {
									decipheredRole = null;
								}
								
								if(decipheredRole == null) {
									MessageHandler.sendMessage(chn, "MALFORMED ROLE ID! Cannot configure!");
									break;
								} else {
									roles.add(decipheredRole);
								}
								
								if(breakApart != null)
									breakApart = breakApart.replaceFirst(brokenString, "");
								
							}
							
							if(roles.isEmpty()) {
								MessageHandler.sendMessage(chn, "Missing Role IDs! Please refer to the description.\n\n" + this.getDesc());
								break;
							}
							
							// Put the role IDs into the command cache
							
							for(String gld_obj_key: gld_obj.keySet()) { // Searches through the guild's keys
								//System.out.println("DEBUG - GLD_OBJ_KEY [Configure.java] " + gld_obj_key);
								if(gld_obj_key.equalsIgnoreCase("cmds_config")) {
									for(String cmds_keys: gld_obj.getJSONObject("cmds_config").keySet()) { // Searches inside the guild's "cmds_config" key
										//System.out.println("DEBUG - CMDS_KEYS [Configure.java] " + cmds_keys);
										
										if(cmds_keys.equalsIgnoreCase(modifyCommand.getName())) {
											
											JSONObject cmds_inner_obj = gld_obj.getJSONObject("cmds_config").getJSONObject(modifyCommand.getName());
											
											for(String cmds_in_keys: cmds_inner_obj.keySet()) {
												
												//System.out.println("DEBUG - CMDS_IN_KEYS [Configure.java] " + cmds_in_keys);
												
												switch(cmds_in_keys) {
												
												case "roleIDs":
													cmds_inner_obj.put("roleIDs", roles);
													break;
												case "active":
													cmds_inner_obj.put("active", true);
													break;
												case "logging":
													cmds_inner_obj.put("logging", true);
													break;
												}
												
											}
											
											gld_obj.getJSONObject("cmds_config").put(modifyCommand.getName(), cmds_inner_obj);
											
											break;
										}
										
									}
									
									break;
								}
							}
							
							
							if(Data.replaceGuild(gld, gld_obj)) {
								MessageHandler.sendMessage(chn, "You've configured " + c.getAssignmentName());
							} else {
								MessageHandler.sendMessage(chn, "Something went wrong configuring! Data writing has been REVERTED to original state!");
							}
							
							break;
						}
						
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
