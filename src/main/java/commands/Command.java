package commands;

import java.util.ArrayList;
import java.util.HashMap;

import init.Launcher;
import net.dv8tion.jda.core.entities.MessageChannel;

public abstract class Command {

	/*
	 * cmdName is REQUIRED
	 * cmdPerms can be left empty, however if requirePerms = true, you'll need to configure.
	 * allowPrivate determines if the command can be used in the DMs
	 */
	private String cmdName, cmdDesc;
	private HashMap<String, ArrayList<Long>> cmdPerms = new HashMap<String, ArrayList<Long>>();
	private boolean requirePerms = false, allowPrivate = true;

	//Setter Methods
	public void setName(String n) {
		cmdName = n;
	}
	public void setDesc(String d) {
		cmdDesc = d;
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
	public ArrayList<Long> getRoleIDs() {
		return cmdPerms.get("ROLE");
	}
	public ArrayList<Long> getUserIDs() {
		return cmdPerms.get("USER");
	}

	public boolean getRequirePerms() {
		return requirePerms;
	}

	public boolean getAllowPrivate() {
		return allowPrivate;
	}

	//Misc Methods

	public void addPerm(String key, long id) throws Exception {
		if(!key.equalsIgnoreCase("USER") || !key.equalsIgnoreCase("ROLE")) {
			throw new Exception("Malformed command permission key!");
		}

		cmdPerms.get(key).add(id);
	}
	public void removePerm(String key, long id) throws Exception {
		if(!key.equalsIgnoreCase("USER") || !key.equalsIgnoreCase("ROLE")) {
			throw new Exception("Malformed command permission key!");
		}

		cmdPerms.get(key).remove(id);
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
	 * @return true if execution can be done, otherwise false.
	 */
	public boolean verifyExecution() {

		return false;
	}

	/**
	 * Define action() as you wish in classes that extend this.
	 * @param chn is used to allow sending messages to a certain text channel
	 * @param msg is used to carry around messages (you do not have to use msg if you don't wish)
	 * @param misc is optional to use, otherwise you can pass a null value
	 *
	 * @return true if successful, otherwise false
	 */
	public abstract boolean action(MessageChannel chn, String msg, Object misc);

}
