package commands;

import java.util.ArrayList;
import java.util.HashMap;

import handlers.MessageHandler;
import init.Launcher;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;


public abstract class Command implements Cloneable {

	/*
	 * cmdName is REQUIRED
	 * cmdType will determine how verifyExecution() is used and for info
	 * cmdPerms can be left empty, however if requirePerms = true, you'll need to configure.
	 * allowPrivate determines if the command can be used in the DMs
	 */
	
	/**
	 * The Command's name.
	 */
	private String cmdName;
	/**
	 * The Command's description.
	 */
	private String cmdDesc;
	/**
	 * The Command's type as defined from enum CommandType.
	 */
	private CommandType cmdType;
	/**
	 * The Command's "tier level" (0-2)
	 */
	private int cmdTier;
	/**
	 * The Command's permissions with the String representing as a "USER" or "ROLE" and the ArrayList<Long> represents the ID(s) to the corresponding key
	 */
	private HashMap<String, ArrayList<Long>> cmdPerms = new HashMap<String, ArrayList<Long>>();
	private boolean requirePerms = false, allowPrivate = true, active = false, logging = false;

	//Setter Methods
	public void setName(String n) {
		cmdName = n;
	}
	public void setDesc(String d) {
		cmdDesc = d;
	}
	public void setCmdType(CommandType ct) {
		cmdType = ct;
	}
	public void setCmdTier(int t) {
		cmdTier = t;
	}
	public void setPerms(HashMap<String, ArrayList<Long>> p) {
		cmdPerms = p;
	}
	public void setRequirePerms(boolean o) {
		requirePerms = o;
	}
	public void setAllowPrivate(boolean b) {
		allowPrivate = b;
	}
	public void setActive(boolean b) {
		active = b;
	}
	public void setLogging(boolean b) {
		logging = b;
	}

	//Getter Methods
	public String getName() {
		return cmdName;
	}

	public String getDesc() {

		if(cmdPerms.size() != 0) {

			String returnThis = cmdDesc + "\n\n**Roles Needed**: ";
			if(cmdPerms.get("ROLE") != null) {
				for(long id: cmdPerms.get("ROLE")) {
					returnThis += Launcher.api.getRoleById(id).getName() + ", ";
				}

				returnThis = returnThis.substring(0,  returnThis.length() - 2);
			}

			if(cmdPerms.get("USER") != null) {
				returnThis += "\n**Users Exempted**: ";
				for(long id: cmdPerms.get("USER")) {
					returnThis += Launcher.api.getUserById(id).getAsTag() + ", ";
				}

				returnThis = returnThis.substring(0,  returnThis.length() - 2);
			}
			return returnThis;
		}

		return cmdDesc;

	}
	public CommandType getCmdType() {
		return cmdType;
	}
	public int getCmdTier() {
		return cmdTier;
	}
	public HashMap<String, ArrayList<Long>> getPerms() {
		return cmdPerms;
	}
	public ArrayList<Long> getRoleIDs() {
		return cmdPerms.get("ROLE");
	}
	public ArrayList<Long> getUserIDs() {
		return cmdPerms.get("USER");
	}
	public ArrayList<Long> getAllIDs() {
		ArrayList<Long> returnThis = new ArrayList<Long>(getRoleIDs());
		returnThis.addAll(getUserIDs());
		
		return returnThis;
	}
	public boolean getRequirePerms() {
		return requirePerms;
	}

	public boolean getAllowPrivate() {
		return allowPrivate;
	}
	public boolean getActive() {
		return active;
	}
	public boolean getLogging() {
		return logging;
	}

	//Misc Methods
	/**
	 * Used to add new permission rules for the command
	 * @param key must either be "USER" or "ROLE"
	 * @param id is the long ID associated with the key
	 * @throws Exception
	 */
	public void addPerm(String key, long id) throws Exception {
		if(!key.equalsIgnoreCase("USER") || !key.equalsIgnoreCase("ROLE")) {
			if(hasPerm(key)) {
				cmdPerms.get(key).add(id);
			} else {
				cmdPerms.put(key, new ArrayList<Long>());
				cmdPerms.get(key).add(id);
			}
		} else {
			throw new Exception("Malformed command permission key!");
		}
		
	}
	public void removePerm(String key, long id) throws Exception {
		if(!key.equalsIgnoreCase("USER") || !key.equalsIgnoreCase("ROLE")) {
			if(hasPerm(key)) {
				cmdPerms.get(key).remove(id);
			} else {
				cmdPerms.put(key, new ArrayList<Long>());
				cmdPerms.get(key).remove(id);
			}
		} else {
			throw new Exception("Malformed command permission key!");
		}
	}
	public boolean hasPerm(String key) {
		
		if(isRoleIDsDefined()) {
			return cmdPerms.containsKey(key);
		}
		
		System.out.println("[Command.java] Server has not defined the permissions!");
		return false;
	}
	/**
	 * Used in conjunction if requirePerms is true, otherwise this is redundant!
	 * @return true if cmdPerms is defined, otherwise false
	 */
	public boolean isRoleIDsDefined() {
		return !cmdPerms.isEmpty();
	}

	/**
	 * Verifies if the command can be executed by the bot and user
	 * <br></br>
	 * Checks for the permissions, role hierarchy, etc.
	 * @return true if execution can be done, otherwise false.
	 */
	public boolean verifyExecution(User executor, User interacted, Guild g, MessageChannel chn) {

		boolean isMod = false, isAdmin = false, isOwner = false, canBot = false;
		
		// Check if user is an administrator
		for(Role r: g.getMember(executor).getRoles()) {
			if(r.hasPermission(Permission.ADMINISTRATOR)) {
				isAdmin = true;
				break;
			}
		}
		
		//The server owner CAN execute moderator commands WITHOUT having to define the roles (but it would be in the best interest to define them)
		if(g.getOwner().getUser().equals(executor)) {
			isOwner = true;
			System.out.println("[Command.java] Is server owner!");
		} else if(isAdmin) { // It is assumed that the administrator role is given as if they're similar to the guild owner. Be careful.
			System.out.println("[Command.java] Is admin!");
		} else if(!isRoleIDsDefined() && requirePerms) {
			MessageHandler.sendMessage(chn, "This command **requires to be configured** by the server owner! Use `!configure [command]` to use this and other commands.");
			return false;
		} else if(isRoleIDsDefined() && requirePerms) {
			
			for(Role ex_rs: g.getMember(executor).getRoles()) {
				if(cmdPerms.get("ROLE").contains(ex_rs.getIdLong())) {
					
				}
			}
			
		}
		
		if(isMod || isAdmin || isOwner) {
			canBot = g.getMember(Launcher.api.getSelfUser()).canInteract(g.getMember(interacted));
			
			return canBot;
		}
		
		return false;
		
	}
	
	public String toString() {
		return getName() + ": " + cmdPerms;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Define action() as you wish in classes that extend this.
	 * @param chn is used to allow sending messages to a certain text channel
	 * @param msg is used to carry around messages
	 * @param gld is the gld and IS required
	 *
	 * @return true if successful, otherwise false
	 */
	public abstract boolean action(MessageChannel chn, String msg, Object misc);

}
