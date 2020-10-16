package lynx.manager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import lynx.commands.Command;
import lynx.commands.CommandType;
import lynx.commands.general.*;
import lynx.commands.moderation.*;
import lynx.commands.utilities.*;
import lynx.data.Data;
import lynx.init.InitData;
import net.dv8tion.jda.api.entities.Guild;


public class CommandManager {

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
				add(new Test());
				add(new Log());
				add(new LogChannel());
				add(new URP());
				add(new Prefix());
		}};

	/**
	 * Initializes the ALL_COMMANDS static list.
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

				System.out.println("[CommandManager.java]: " + name + " \"desc\" contains: " + desc);

				Command cmd = getCommand(name, null);
				if(desc.contains(">>REQ_PERM")) {
					desc = desc.replace(">>REQ_PERM", "");
					cmd.setRequirePerms(true);
				}

				//Checks what type of command it is
				if(desc.contains(">>")) {

					switch(desc.substring(desc.indexOf(">>") + 2).toLowerCase()) {

					case "mod": cmd.setCmdType(CommandType.MOD); break;
					case "admin": cmd.setCmdType(CommandType.ADMIN); break;
					case "guild_owner": cmd.setCmdType(CommandType.GUILD_OWNER); break;
					case "bot_owner": cmd.setCmdType(CommandType.BOT_OWNER); break;
					default: cmd.setCmdType(CommandType.GENERAL); break;

					}

					desc = desc.substring(0, desc.indexOf(">>"));

				}

				//System.out.println("DEBUG [CommandManager.java]: (getCmdType()) " + cmd.getCmdType());

				cmd.setName(name);
				cmd.setDesc(desc);

				if(!sc.hasNextLine()) //To prevent an Exception from being thrown
					break;

			}


			sc.close();
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();

			System.out.println("[CommandManager.java]: File cannot be accessed/found!");
		}

		return result;
	}

	public static Command getCommand(String srch, Guild g) {
		
		if(g == null || srch.equalsIgnoreCase("shutdown"))  {
			
			//System.out.println("WARNING [CommandManager.java] guild is empty");
			
			String[] cmdsArr = new String[ALL_COMMANDS.size()];
			
			for(int i = 0; i < ALL_COMMANDS.size(); i++) {
				cmdsArr[i] = ALL_COMMANDS.get(i).getName().toLowerCase();
			}
			
			return ALL_COMMANDS.get(Arrays.asList(cmdsArr).indexOf(srch.toLowerCase()));
		} else {
			
			// Checks if the server is in the cache.
			if(Data.srvr_cache.get(g) == null) {
				try {
					Data.softInitCache(g.getId());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			for(Command c: Data.command_cache.get(g)) {
				
				System.out.println("DEBUG [CommandManager.java] " + Data.command_cache.get(g));
				
				if(c.getName().equalsIgnoreCase((srch))) {
					return c;
				}
			}
		}

		return null;
	}

}
