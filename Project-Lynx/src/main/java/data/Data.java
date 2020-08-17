package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import commands.Command;
import handlers.CommandHandler;
import init.InitData;
import init.Launcher;
import net.dv8tion.jda.api.entities.Guild;

public class Data {

	/**
	 * This is initialized at startup, but can be reinitialized
	 */
	public static volatile Map<Guild, List<Command>> command_cache;
	public static volatile Map<Guild, JSONObject> srvr_cache;
	public static volatile ArrayList<Command> cmds;
	public static volatile JSONObject rawJSON;

	//TODO: Clean this up
	/**
	 * Reads the JSON file from the file parameter
	 * @param file to a JSON file
	 * @return the result, otherwise empty ("")
	 */
	public static String readData(String file) {

		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while(line != null) {
				//System.out.println(line);
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Writes to the designated filepath in a JSON format
	 * @param file the file-path
	 * @param jLine the JSON line
	 * @return true if writeData(...) is able to write the data, otherwise false
	 */
	public static boolean writeData(String file, String jLine, boolean softInit, String id) {
		try {
			FileWriter w = new FileWriter(new File(file));

			System.out.println("[Data.java] Writing to " + file);

			w.write(jLine);
			w.close();

			if(file.equals(InitData.locationJSON) && softInit) //Updates the cache
				softInitCache(id);
			
			rawJSON = new JSONObject(jLine);

			return true;

		} catch (IOException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Misc Methods
	/**
	 * A function designed to create a backup with a timestmap at the instant it was created at
	 * 
	 * @param temp short for "temporary," defines whether or not the backup file is temporary. If it is temporary, it will be deleted at shutdown by another function
	 * @return a {@link java.io.File#member File} where the backup is located.
	 */
	public static File createBackup(boolean temp) {
		long inst = Instant.now().getEpochSecond();
		if(temp) {
			writeData(InitData.locationBackup + "BACKUP-TMP-" + inst + ".json", readData(InitData.locationJSON), false, null);
			return new File(InitData.locationBackup + "BACKUP-TMP-" + inst + ".json");
		} else {
			writeData(InitData.locationBackup + "BACKUP-" + inst + ".json", readData(InitData.locationJSON), false, null);
			return new File(InitData.locationBackup + "BACKUP-" + inst + ".json");
		}
	}

	public static File obtainBackup(boolean isTemp, long time) {
		return null; //TODO: Finish this...
	}

	public static boolean deleteGuild(Guild gld) {

		String jsonData = readData(InitData.locationJSON);
		JSONObject obj = new JSONObject(jsonData);

		for(String id: obj.keySet()) {
			if(id.equals(gld.getId())) {
				obj.remove(id);

				return writeData(InitData.locationJSON, obj.toString(), false, null);
			}
		}

		return false;
	}
	public static boolean addGuild(Guild gld) {

		String jsonData = readData(InitData.locationJSON);
		JSONObject obj = new JSONObject(jsonData);

		for(String id: obj.keySet()) {
			if(id.equals(gld.getId()))
				return false;
		}

		obj.put(gld.getId(), obj.get("DEFAULT"));

		return writeData(InitData.locationJSON, obj.toString(), true, gld.getId());

	}
	/**
	 * Determines if the guild is in BOTH caches (server and command)
	 * @param gld is the Guild object that is used as a "key" to the Maps.
	 * @return true if the key exists for the guilds on both the server and command caches.
	 */
	public static boolean hasGuild(Guild gld) {
		return srvr_cache.containsKey(gld) && command_cache.containsKey(gld);
	}
	
	/**
	 * Edits the guild with new parameters as provided
	 * @param gld represents the guild to be modified
	 * @param obj represents the JSONObject that will replace the current JSONObject
	 * @return true if the Guild has successfully been modified.
	 */
	/* TODO: Fix this!
	public static boolean replaceGuild(Guild gld, JSONObject obj) {
		
		JSONObject temp_backup = new JSONObject(srvr_cache.get(gld)); // Ensures that this backup can be obtained if needed.
		JSONObject file = new JSONObject(readData(InitData.locationJSON));
		
		boolean endResult = false;
		
		// Update the file
		for(String id: file.keySet()) {
			if(id.equals(gld.getId())) {
				
				file.getJSONObject(gld.getId()).put("cmds_config", obj); //TODO: This is what breaks it!
				
				endResult = writeData(InitData.locationJSON, file.toString(), true, gld.getId());
				
				if(!endResult)
					writeData(InitData.locationJSON, temp_backup.toString(), true, gld.getId());
				
				break;
			}
		}
		
		return endResult;
	}
	*/

	/** Checks the JSONObject against the "DEFAULT" key to see if it needs to create/modify the server's JSON values to the "DEFAULT" settings.
	 *
	 * @param obj represents JSONObject for the guild in which it will compare against the "DEFAULT" JSON key
	 * @param cfg represents the key to determine if it is a command configuration or server configuration
	 * @param id represents the guild id
	 * @return true if the method was able to add/modify the Guild's JSON values against the "DEFAULT" value
	 */
	public static boolean checkDefaults(JSONObject obj, String cfg, String id) {

		JSONObject dflt = rawJSON.getJSONObject("DEFAULT");
		//ArrayList<String> s_keys = new ArrayList<String>(((JSONObject) obj.get(srvr)).keySet()), d_keys = new ArrayList<String>(dflt.keySet()); //s for server and d for defaults
		/*
		ArrayList<String> s_inner_keys = new ArrayList<String>() {
				{
					addAll(((JSONObject) obj.get(srvr)).getJSONObject("srvr_config").keySet());
					addAll(((JSONObject) obj.get(srvr)).getJSONObject("cmds_config").keySet());
				}
			},
				d_inner_keys = new ArrayList<String>() {
				{
					addAll(dflt.getJSONObject("srvr_config").keySet());
					addAll(dflt.getJSONObject("cmds_config").keySet());
				}
			};
		*/
		
		/*
		// Special cases for when the "DEFAULT" key has an empty value
		if(dflt.getJSONObject(cfg).keySet().size() == 0) {
			
			JSONObject addMissingVal = rawJSON;
			addMissingVal.getJSONObject(id).put(cfg, dflt.getJSONObject(cfg));
			
			// Attempt to write the updated line to the JSON
			if(writeData(InitData.locationJSON, addMissingVal.toString(), true, id)) {
				System.out.println("[Data.java] checkDefaults() missing value successfully written.");
			} else {
				return false;
			}
			
		}
		*/
		
		//This for loop will iterate through the "DEFAULT" JSON key with either "cmds_config" or "srvr_config" as the "cfg" (configure) parameter 
		for(String val: dflt.getJSONObject(cfg).keySet()) {
			
			System.out.println("DEBUG [Data.java] Comparing value " + val + " in " + cfg);
			
			// Checks if it exists in server, otherwise it'll copy the DEFAULT values and return true if successfully written.
			try {
				obj.getJSONObject(cfg);
			} catch (JSONException e) {
				JSONObject addMissingVal = rawJSON;
				addMissingVal.getJSONObject(id).put("logs", dflt.getJSONObject(cfg));
				
				if(writeData(InitData.locationJSON, addMissingVal.toString(), true, id)) {
					System.out.println("[Data.java] checkDefaults() missing value successfully written.");
					return true;
				} else {
					return false;
				}
				
			}
			
			// Check if the value is in the JSON
			if(!obj.getJSONObject(cfg).keySet().contains(val)) {
				System.out.println("[Data.java] MISSING value " + val + ". Writing to server data.");
				
				//System.out.println("DEBUG [Data.java] " + rawJSON.keySet());
				
				JSONObject addMissingVal = rawJSON;
				addMissingVal.getJSONObject(id).getJSONObject(cfg).put(val, dflt.getJSONObject(cfg).get(val));
				
				// Attempt to write the updated line to the JSON
				if(writeData(InitData.locationJSON, addMissingVal.toString(), true, id)) {
					System.out.println("[Data.java] checkDefaults() missing value successfully written.");
				} else {
					return false;
				}
				
			}
			
			// Check if the value inside of the value is in the JSON
			
			//TODO: Figure a way to optimize this section???
			if(dflt.getJSONObject(cfg).get(val) instanceof JSONObject) { // Checks it is a JSONObject to ensure it can use keySet(), otherwise an Exception is thrown
				if(dflt.getJSONObject(cfg).getJSONObject(val).keySet().size() > 0) {
					
					// Iterates through the keys of the JSONObjects with keys. It'll only iterate if said value contains more than 1 key.
					for(String inner_val: dflt.getJSONObject(cfg).getJSONObject(val).keySet()) {
						
						if(!obj.getJSONObject(cfg).getJSONObject(val).keySet().contains(inner_val)) {
							System.out.println("[Data.java] MISSING (inner) value " + inner_val + ". Writing to server data.");
							
							//System.out.println("DEBUG [Data.java] " + rawJSON.keySet());
							
							JSONObject addMissingVal = rawJSON;
							addMissingVal.getJSONObject(id).getJSONObject(cfg).getJSONObject(val).put(inner_val, dflt.getJSONObject(cfg).getJSONObject(val).get(inner_val));
							
							// Attempt to write the updated line to the JSON
							if(writeData(InitData.locationJSON, addMissingVal.toString(), true, id)) {
								System.out.println("[Data.java] checkDefaults() missing value successfully written.");
							} else {
								return false;
							}
						}
						
					}
					
				}
			}
			
		}
		
		/*
		for(String key: dflt.getJSONObject(cfg).keySet()) {
			
			System.out.println("DEBUG [Data.java] Searching for " + key + " within " + dflt.getJSONObject(cfg));
			// This checks if the server configuration is even written onto the guildData.json file
			if(!((JSONObject) obj.get(cfg)).keySet().contains(key)) {
				System.out.println("[Data.java] MISSING " + key + "! Adding it to the server's data!");

				JSONObject newObj = new JSONObject(obj.toString());
				((JSONObject) newObj.get(cfg)).put(key, dflt.get(key));

				if(writeData(InitData.locationJSON, newObj.toString())) {
					System.out.println("[Data.java] checkDefaults() successfully modified data!");
				} else {
					return false;
				}
			}
			
			for(String key_inner: dflt.getJSONObject(cfg).keySet()) {
				System.out.println("DEBUG [Data.java] Searching for inner key " + key_inner);
				
				if(!((JSONObject) obj.get(cfg)).keySet().contains(key_inner)) {
					System.out.println("[Data.java] MISSING " + key + "! Adding it to the server's data!");

					JSONObject newObj = new JSONObject(obj.toString());
					((JSONObject) newObj.get(cfg)).put(key_inner, dflt.getJSONObject(key).get(key_inner));

					if(writeData(InitData.locationJSON, newObj.toString())) {
						System.out.println("[Data.java] checkDefaults() successfully modified data!");
					} else {
						return false;
					}
				}
				
			}
		}
		*/
		
		// If it got this far, assume it actually wrote the data correctly?
		return true;
	}
	
	/**
	 * Initializes a specific guild rather than checking through the entire resources/guildData.json file.
	 * @param id is a String containing the guild ID.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static void softInitCache(String id) throws InstantiationException, IllegalAccessException {
		
		System.out.println("[Data.java] NOTICE: softInitCache(...) was called!");
		
		if(rawJSON == null) {
			rawJSON = new JSONObject(readData(InitData.locationJSON));
			
			if(rawJSON.isEmpty()) {
				System.out.println("[Data.java] Shutting down! Cache cannot be initialized... Make sure resources/guildData.json isn't empty, at least having the \"DEFAULT\" object");
				System.exit(1);
			}
			
		}
		
		// If guild isn't on the 
		if(!rawJSON.keySet().contains(id)) {
			rawJSON.put(id, rawJSON.getJSONObject("DEFAULT"));
		}
		
		loadCache(id, rawJSON);
		
	}

	/**
	 * Initializes the cache of saved servers from the resources/guildData.json file.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void initCache() throws InstantiationException, IllegalAccessException {
		
		System.out.println("[Data.java] NOTICE: initCache() was called!");

		if(!InitData.acceptMultipleServers) return;
		
		// First-time initialization, highly doubt it would need to be reinitialized
		if(cmds == null) {
			cmds = new ArrayList<Command>();

			for(Command c: CommandHandler.ALL_COMMANDS) {
				try {
					cmds.add((Command) c.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}

		// Checks if the rawJSON variable is null.
		if(rawJSON == null) {
			rawJSON = new JSONObject(readData(InitData.locationJSON)); // Loads the rawJSON variable.
			
			if(rawJSON.isEmpty()) { // Ensure it isn't completely empty.
				System.out.println("[Data.java] Shutting down! Cache cannot be initialized... Make sure resources/guildData.json isn't empty, at least having the \"DEFAULT\" object");
				System.exit(1);
			}
			
		} //TODO: Rewrite to call obtainBackup() to search for backups
		command_cache = new HashMap<Guild, List<Command>>();
		srvr_cache = new HashMap<Guild, JSONObject>();

		for(String key: rawJSON.keySet()) { // "key" represents the list of keys, particularly guild IDs

			if(key.equals("DEFAULT")) continue;

			System.out.println("[Data.java]: (initCache()) " + key);
			System.out.println("[Data.java]: (initCache()) " + rawJSON.get(key));

			System.out.println("[Data.java]: Checking if server contains all the needed keys...");
			try {
				checkDefaults(rawJSON.getJSONObject(key), "cmds_config", key);
				checkDefaults(rawJSON.getJSONObject(key), "srvr_config", key);
			} catch(JSONException e) {
				System.out.println("[Data.java] ERROR on checkDefaults(...), refer to the stack trace for more information.");
				e.printStackTrace();
				System.exit(-1);
			}
			
			loadCache(key, rawJSON);

		}

	}
	
	/**
	 * Reads the guild's data and transcribes it into HashMaps
	 * @param key
	 * @param obj
	 */
	public static void loadCache(String key, JSONObject obj) {
		
		JSONObject cmds_config = obj.getJSONObject(key).getJSONObject("cmds_config"), srvr_config = obj.getJSONObject(key).getJSONObject("srvr_config");

		//COMMANDS CONFIG
		for(String con_key: cmds_config.keySet()) {
			System.out.println("COMMANDS CONFIG: Setting up " + cmds_config.getJSONObject(con_key));

			JSONObject in_config = cmds_config.getJSONObject(con_key);
			Command cmd = null;

			for(Command c: cmds) {
				if(c.getName().equalsIgnoreCase(con_key)) {
					cmd = c;
				}
			}

			if(cmd == null) continue;

			for(String in_key: in_config.keySet()) {

				System.out.println("DEBUG [Data.java] in_key: " + in_key);

				//START SWITCH STATEMENT
				switch(in_key) {

				case "roleIDs":

					if(cmd.getRequirePerms()) {

						if(cmd.getPerms().isEmpty()) {
							cmd.setPerms(new HashMap<String, ArrayList<Long>>());
							System.out.println("[Data.java] Command \"" + cmd.getName() + "\" is empty!");
						}

						if(in_config.get("roleIDs") != null) {
							try {
								for(Object id: in_config.getJSONArray("roleIDs")) {
									cmd.addPerm("ROLE", (long) id);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					break;

				case "userIDs":

					if(cmd.getRequirePerms()) {

						if(cmd.getPerms().isEmpty()) {
							cmd.setPerms(new HashMap<String, ArrayList<Long>>());
							System.out.println("Command \"" + cmd.getName() + "\" is empty!");
						}

						if(in_config.get("userIDs") != null) {
							try {
								for(Object id: in_config.getJSONArray("userIDs")) {
									cmd.addPerm("USER", (long) id);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					break;
				case "active":

					cmd.setActive(in_config.getBoolean("active"));

					break;
				case "logging":

					cmd.setLogging(in_config.getBoolean("logging"));

					break;
				case "tier_level":
					
					cmd.setCmdTier(in_config.getInt("tier_level"));
					
					if(in_config.getInt("tier_level") == 0) {
						
					} else if(in_config.getInt("tier_level") == 1) {
						
					} else if(in_config.getInt("tier_level") == 2) {
						
					}
					
					break;
				}
				//END SWITCH STATEMENT
				
				//SERVER CONFIG
				//System.out.println("[Data.java] PUTTING " + Launcher.api.getGuildById(key).getName() + " INTO THE CACHE!");
				
				srvr_cache.put(Launcher.api.getGuildById(key), srvr_config);
				command_cache.put(Launcher.api.getGuildById(key), cmds);

			}
			
		}
		
		System.out.println("DEBUG srvr_cache - [Data.java] " + srvr_cache);
		System.out.println("DEBUG command_cache - [Data.java] " + command_cache + "\n\n\n");
		
	}

}
