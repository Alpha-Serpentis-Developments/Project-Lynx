package commands;

import java.util.ArrayList;
import java.util.HashMap;

import init.InitData;
import init.Launcher;
import net.dv8tion.jda.core.entities.TextChannel;

public abstract class Command {

	/*
	 * cmdName is REQUIRED
	 * cmdType is REQUIRED, otherwise it DEFAULTS to "GENERAL"
	 * cmdPerms can be left empty, however if requirePerms = true, you'll need to configure.
	 * allowPrivate determines if the command can be used in the DMs
	 */
	private String cmdName, cmdDesc, cmdType, cmdUsage;
	private HashMap<String, ArrayList<Long>> cmdPerms = new HashMap<String, ArrayList<Long>>();
	private boolean requirePerms = false, allowPrivate = true;

	//TODO: Fix these constructors...

	/**NOT RECOMMENDED, PLEASE DEFINE YOUR COMMANDS EARLY AT INIT OR IN THE CODE ELSEWHERE
	 *
	 * @param n is the name
	 */
	public Command(String n) {
		setName(n);
		setDesc("Use the `" + InitData.prefix + "setDesc [description]` command to set this command's description!");
		setType("GENERAL");
	}
	public Command(String n, boolean rqrPrm) {
		setName(n);
		setDesc("Use the `" + InitData.prefix + "setDesc [description]` command to set this command's description!");
		setType("GENERAL");
		setRequirePerms(rqrPrm);
	}
	/**NOT RECOMMENDED, PLEASE DEFINE YOUR COMMANDS EARLY AT INIT OR IN THE CODE ELSEWHERE
	 *
	 * @param n is the name
	 * @param d is the description
	 */
	public Command(String n, String d) {
		setName(n);
		setDesc(d);
		setType("GENERAL");
	}
	/**NOT RECOMMENDED, PLEASE DEFINE YOUR COMMANDS EARLY AT INIT OR IN THE CODE ELSEWHERE
	 *
	 * @param n is the name
	 * @param d is the description
	 * @param t is the type
	 */
	public Command(String n, String d, String t) {
		setName(n);
		setDesc(d);
		setType(t);
	}
	/**
	 *
	 * @param n
	 * @param d
	 * @param cp
	 */
	public Command(String n, String d, HashMap<String, ArrayList<Long>> cp) {
		setName(n);
		setDesc(d);
		setPerms(cp);
		setType("GENERAL");
	}
	public Command(String n, String d, HashMap<String, ArrayList<Long>> cp, String type) {
		setName(n);
		setDesc(d);
		setPerms(cp);
		setType(type);
	}
	public Command(String n, String d, HashMap<String, ArrayList<Long>> cp, String type, boolean rqrPrm) {
		setName(n);
		setDesc(d);
		setPerms(cp);
		setType(type);
		setRequirePerms(rqrPrm);
	}
	public Command(String n, String d, HashMap<String, ArrayList<Long>> cp, String type, boolean rqrPrm, boolean allwPrv) {
		setName(n);
		setDesc(d);
		setPerms(cp);
		setType(type);
		setRequirePerms(rqrPrm);
		setAllowPrivate(allwPrv);
	}

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
	public void setType(String t) {
		cmdType = t.toUpperCase();
	}
	public void setRequirePerms(boolean o) {
		requirePerms = o;
	}
	public void setUsage(String s) {
		cmdUsage = s;
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
	public HashMap<String, ArrayList<Long>> getRoleIDs() {
		return cmdPerms;
	}
	public String getType() {
		return cmdType;
	}
	public boolean getRequirePerms() {
		return requirePerms;
	}
	public String getUsage() {
		return cmdUsage;
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
	 * Define action() as you wish in classes that extend this.
	 * @param chn is used to allow sending messages to a certain text channel
	 * @param msg is used to carry around messages (you do not have to use msg if you don't wish)
	 * @param misc is optional to use, otherwise you can pass a null value
	 *
	 * @return true if successful, otherwise false
	 */
	public abstract boolean action(TextChannel chn, String msg, Object misc);

}
