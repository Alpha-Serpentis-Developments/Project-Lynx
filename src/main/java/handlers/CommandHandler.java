package handlers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

import net.dv8tion.jda.core.entities.Guild;

import commands.Command;
import commands.CommandType;
import commands.general.*;
import commands.moderation.*;
import commands.utilities.*;
import data.Data;
import init.InitData;


public class CommandHandler {
	
	@SuppressWarnings("serial")
	public static final List<Command> ALL_COMMANDS = new ArrayList<Command>() {
		{
				add(new About());
				add(new Help());
				add(new Ban());
				add(new Kick());
				add(new Warn());
				add(new Warnings());
				add(new Configure());
				add(new Shutdown());
				add(new Welcome());
		}};
	
	/**
	 * Initializes the ALL_COMMANDS instance variable. 
	 * <br><b>CALLING THIS MORE THAN ONCE IS UNNECESSARY!</b></br>
	 */
	public static boolean initCommands() {
		
		boolean result = false;
		
		try {
			
			Scanner sc = new Scanner(new FileReader(InitData.locationCommands));
			String str;
			
			while((str = sc.nextLine()) != null) {
				String raw = str, name = raw.substring(0, raw.indexOf(':')), desc = raw.substring(name.length() + 1);
				desc = desc.replace("[[vers]]", InitData.version).replaceAll("\\\\n", "\n").replace("[[prefix]]", String.valueOf(InitData.prefix));
				
				System.out.println("[CommandHandler.java]: " + name + " \"desc\" contains: " + desc);
				
				Command cmd = getCommand(name);
				if(desc.contains(">>REQ_PERM")) {
					desc = desc.replace(">>REQ_PERM", "");
					cmd.setRequirePerms(true);
				}
				
				//Checks what type of command it is
				if(desc.contains(">>")) {
					
					switch(desc.substring(desc.indexOf(">>") + 2).toLowerCase()) {
					
					case "mod": cmd.setCmdType(CommandType.MOD); break;
					case "guild_owner": cmd.setCmdType(CommandType.GUILD_OWNER); break;
					case "bot_owner": cmd.setCmdType(CommandType.BOT_OWNER); break;
					default: cmd.setCmdType(CommandType.GENERAL); break;
					
					}
					
					desc = desc.substring(0, desc.indexOf(">>"));
					
				}
				
				//System.out.println("DEBUG [CommandHandler.java]: (getCmdType()) " + cmd.getCmdType());
				
				cmd.setName(name);
				cmd.setDesc(desc);
				
				if(!sc.hasNextLine()) //To prevent an Exception from being thrown
					break;
				
			}
			
			
			sc.close();
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			System.out.println("[CommandHandler.java]: File cannot be accessed/found!");
		} 
		
		return result;
	}
	
	public static Command getCommand(String srch) {
		
		switch(srch.toLowerCase()) {
		
		//General
		case "about":
			return ALL_COMMANDS.get(0);
		case "help":
			return ALL_COMMANDS.get(1);
			
		//Moderation
		case "ban":
			return ALL_COMMANDS.get(2);
		case "kick":
			return ALL_COMMANDS.get(3);
		case "warn":
			return ALL_COMMANDS.get(4);
		case "warnings":
			return ALL_COMMANDS.get(5);
		
		//Utilities
		case "configure":
			return ALL_COMMANDS.get(6);
		case "shutdown": //Bot Owners Only
			return ALL_COMMANDS.get(7);
		case "welcome":
			return ALL_COMMANDS.get(8);
		}
		
		return null;
	}
	
}
