package lynx.commands;

import lynx.init.Launcher;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class Command implements Cloneable {
	protected final static String CONFIGURATION_PLS = "This command **requires to be configured** by the server owner or administrator! Use `!configure [command]` to use this and other commands.";
	protected final static String BOT_PERMISSION_PLS = "Looks like I wasn't given permission to do this. Ask an admin to give me the required permission (or just the Administrator permission).";


	protected static final String NO_ONE = "I couldn't find anyone with that name! Try using a mention instead.";
	protected static final String TOO_MANY = "Multiple people share that name! Try using a mention instead.";

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
	protected boolean requirePerms = false, allowPrivate = true, active = false, logging = false;

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

		//System.out.println("DEBUG getDesc() [Command.java] " + cmdPerms);
		
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
	/**
	 * Used to remove a permission rule for the command
	 * @param key must either be "USER" or "ROLE"
	 * @param id is the long ID associated with the key
	 * @throws Exception
	 */
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
	 * Verifies if the command can be executed by the user
	 *
	 * @param executor The member to verify can use this command.
	 * @return True only if either they are an administrator, or have a role configured to use this command.
	 */
	public boolean hasRolesToUse(Member executor) {
		if(executor == null)
			return false;

		// Check if user is an administrator
		if(executor.hasPermission(Permission.ADMINISTRATOR))
			return true;

		// Check if user has the roles to execute the command
		if(!cmdPerms.isEmpty()) {
			for(Role r : executor.getRoles()) {
				if(cmdPerms.get("ROLE").contains(r.getIdLong())) {
					return true;
				}
			}
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
	 * @return true if successful, otherwise false
	 */
	public abstract boolean action(MessageChannel chn, String msg, Object misc);
	//TODO: Implement this abstract method
	//public abstract boolean configure(Guild gld);

}
