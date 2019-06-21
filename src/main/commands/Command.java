package main.commands;

import java.util.ArrayList;

import main.init.Launcher;
import net.dv8tion.jda.core.entities.TextChannel;

public abstract class Command {

	/*
	 * cmdName is REQUIRED
	 * cmdType is REQUIRED, otherwise it DEFAULTS to "GENERAL"
	 * cmdPerms can be left empty, however if requirePerms = true, you'll need to configure.
	 */
	private String cmdName, cmdDesc, cmdType;
	private ArrayList<Long> cmdPerms = new ArrayList<Long>();
	private boolean requirePerms = false;
	
	/**NOT RECOMMENDED, PLEASE DEFINE YOUR COMMANDS EARLY AT INIT OR IN THE CODE ELSEWHERE
	 * 
	 * @param n is the name
	 */
	public Command(String n) {
		setName(n);
		setDesc("Use the [setDesc] command to set this command's description!");
		setType("GENERAL");
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
	public Command(String n, String d, ArrayList<Long> cp) {
		setName(n);
		setDesc(d);
		setPerms(cp);
		setType("GENERAL");
	}
	
	//Setter Methods
	public void setName(String n) {
		cmdName = n;
	}
	public void setDesc(String d) {
		cmdDesc = d;
	}
	public void setPerms(ArrayList<Long> p) {
		cmdPerms = p;
	}
	public void setType(String t) {
		cmdType = t.toUpperCase();
	}

	public void setRequirePerms(boolean o) {
		requirePerms = o;
	}
	
	//Getter Methods
	public String getName() {
		return cmdName;
	}
	public String getDesc() {
		
		if(cmdPerms.size() != 0) {
			
			String returnThis = cmdDesc + "\n\n**Roles Needed**: ";
			for(long id: cmdPerms) {
				returnThis += Launcher.api.getRoleById(id).getName()+", ";
			}
			
			return returnThis.substring(0, returnThis.length() - 2);
		}
		
		return cmdDesc;
	}
	public ArrayList<Long> getRoleIDs() {
		return cmdPerms;
	}
	public String getType() {
		return cmdType;
	}
	public boolean getRequirePerms() {
		return requirePerms;
	}
	
	//Misc Methods

	public void addPerm(long id) {
		cmdPerms.add(id);
	}
	public void removePerm(long id) {
		cmdPerms.remove(id);
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
