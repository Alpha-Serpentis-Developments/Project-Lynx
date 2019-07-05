package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.JSONTokener;
import org.json.JSONWriter;

import commands.Command;
import init.InitData;
import net.dv8tion.jda.core.entities.Guild;

public class Data {

	/**
	 * This is initialized at startup
	 */
	public static HashMap<Guild, ArrayList<Command>> cache;

	public static ArrayList<Command> CONFIG_DEFAULT = new ArrayList<Command>();

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
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	//TODO: Write data to the designated file
	/**
	 *
	 * @param file the file-path
	 * @param jLine the JSON line
	 * @return
	 */
	public static boolean writeData(String file, String jLine) {
		try {
			FileWriter w = new FileWriter(new File(file));

			System.out.println("Writing to " + file);

			w.write(jLine);
			w.close();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Misc Methods
	public static void createBackup(boolean temp) {
		if(temp)
			writeData(InitData.locationBackup + "BACKUP-TMP-" + Instant.now().getEpochSecond() + ".json", readData(InitData.locationJSON));
		else
			writeData(InitData.locationBackup + "BACKUP-" + Instant.now().getEpochSecond() + ".json", readData(InitData.locationJSON));
	}

	public static void obtainBackup() {

	}

	public static boolean deleteGuild() {
		return false;
	}

	public static boolean addGuild() {
		return false;
	}

	/**
	 * Initializes the cache of saved servers from the resources/guildData.json file.
	 */
	public static void initCache() {

		if(!InitData.acceptMultipleServers) return;

		String jsonData = readData("resources/guildData.json");
		if(jsonData.isEmpty()) return; //TODO: Rewrite to call obtainBackup() to search for backups
		JSONObject obj = new JSONObject(jsonData);

		cache = new HashMap<Guild, ArrayList<Command>>();

		for(String key: obj.keySet()) {
			System.out.println("DEBUG (Data.java) :" + key);
		}

	}

}
