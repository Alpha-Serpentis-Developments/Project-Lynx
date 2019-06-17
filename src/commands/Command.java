package commands;

import java.util.HashMap;

public abstract class Command {

	/*
	 * cmdName is REQUIRED
	 * cmdType is REQUIRED, otherwise it DEFAULTS to "GENERAL"
	 */
	private String cmdName, cmdDesc, cmdType;
	private HashMap<String, Integer> cmdPerms; 
	
	public Command(String n) {
		setName(n);
		setDesc("Use the [setDesc] command to set this command's description!");
		setType("GENERAL");
	}
	public Command(String n, String d) {
		setName(n);
		setDesc(d);
		setType("GENERAL");
	}
	public Command(String n, String d, String t) {
		setName(n);
		setDesc(d);
		setType(t);
	}
	public Command(String n, String d, HashMap<String, Integer> cp) {
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
	public void setPerms(HashMap<String, Integer> p) {
		cmdPerms = p;
	}
	public void setType(String t) {
		cmdType = t.toUpperCase();
	}
	
	//Getter Methods
	public String getName() {
		return cmdName;
	}
	public String getDesc() {
		return cmdDesc;
	}
	public HashMap<String, Integer> getRoleIDs() {
		return cmdPerms;
	}
	public String getType() {
		return cmdType;
	}
	
	//Misc Methods
	public void addPerm(String id, int val) {
		
	}
	public void addPerm(HashMap<String, Integer> p) {
		
	}
	
	public void removePerm(String id, int val) {
		
	}
	public void removePerm(HashMap<String, Integer> p) {
		
	}
	
	public abstract boolean action(HashMap<String, Boolean> p);
	public abstract String toString();
	
}
