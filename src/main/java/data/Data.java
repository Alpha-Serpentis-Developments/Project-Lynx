package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
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
	
	public static void writeData(Object obj) {
		JSONWriter w;
	}
	
	//Misc Methods
	//TODO: Write these two methods?
	public static void createBackup() {
		
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
		if(jsonData.isEmpty()) return;
		JSONObject obj = new JSONObject(jsonData);
		
		cache = new HashMap<Guild, ArrayList<Command>>();
		
		for(String key: obj.keySet()) {
			System.out.println(key);
		}
		
	}
	
}
