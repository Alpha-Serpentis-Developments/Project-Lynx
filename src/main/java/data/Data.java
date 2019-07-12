package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import commands.Command;
import handlers.MessageHandler;
import init.InitData;
import net.dv8tion.jda.core.entities.Guild;

public class Data {

	/**
	 * This is initialized at startup
	 */
	public static Map<Guild, List<Command>> cache;

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
	 * @return
	 */
	public static boolean writeData(String file, String jLine) {
		try {
			FileWriter w = new FileWriter(new File(file));

			System.out.println("[Data.java] Writing to " + file);

			w.write(jLine);
			w.close();

			if(file.equals(InitData.locationJSON)) //Updates the cache
				initCache();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Misc Methods
	public static File createBackup(boolean temp) {
		long inst = Instant.now().getEpochSecond();
		if(temp) {
			writeData(InitData.locationBackup + "BACKUP-TMP-" + inst + ".json", readData(InitData.locationJSON));
			return new File(InitData.locationBackup + "BACKUP-TMP-" + inst + ".json");
		} else {
			writeData(InitData.locationBackup + "BACKUP-" + inst + ".json", readData(InitData.locationJSON));
			return new File(InitData.locationBackup + "BACKUP-" + inst + ".json");
		}
	}

	public static void obtainBackup() {

	}

	public static boolean deleteGuild(Guild gld) {

		String jsonData = readData(InitData.locationJSON);
		JSONObject obj = new JSONObject(jsonData);

		for(String id: obj.keySet()) {
			if(id.equals(gld.getId())) {
				obj.remove(id);
				return true;
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

		return writeData(InitData.locationJSON, obj.toString());
	}

	public static boolean editGuild(Guild gld, Object obj) {
		return false;
	}

	/**
	 * Initializes the cache of saved servers from the resources/guildData.json file.
	 */
	public static void initCache() {

		if(!InitData.acceptMultipleServers) return;

		String jsonData = readData(InitData.locationJSON);
		if(jsonData.isEmpty()) return; //TODO: Rewrite to call obtainBackup() to search for backups
		JSONObject obj = new JSONObject(jsonData);

		cache = new HashMap<Guild, List<Command>>();

		for(String key: obj.keySet()) {
			System.out.println("[Data.java]: " + key);

		}

	}

	/**
	 *
	 * @param gld is the key to the HashMap
	 * @param obj
	 * @return True if the cache was successfully modified, otherwise false.
	 */
	public static <T extends Command & List<Command>> boolean modifyCache(Guild gld, T obj) {

		Map<Guild, List<Command>> tmpBackup = cache;

		if(tmpBackup.containsKey(gld)) {

		} else {
			if(addGuild(gld)) {

			} else {
				System.out.println("[Data.java]: WARNING: modifyCache() was unable to add Guild " + gld.getId() + "to the cache and file!");
				MessageHandler.sendMessage(gld.getOwner().getUser().openPrivateChannel().complete(), "The guild was unable to be put into the cache/data. Please do !configure to reattempt");

				return false;
			}
		}

		return false;
	}

}
