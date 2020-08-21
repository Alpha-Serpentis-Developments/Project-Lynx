package init;

public class InitData {

	/*
	 * = = = = = = = = = = =
	 * NOTICE:
	 * 
	 * Modifying data here from the source code is not recommended unless you're certain changing anything in here.
	 * = = = = = = = = = = =
	 * YOU CAN OVERRIDE ALL OF THE BELOW BY MODIFYING initOverrides.txt
	 * Leave initOverrides.txt EMPTY if you do not wish to override anything!
	 * Use the following initOverrides keys:
	 * 
	 * locKey(KEY_LOCATION) -- directory
	 * locBackup(KEY_LOCATION) -- directory
	 * locJSON(KEY_LOCATION) -- directory
	 * guildID(ID) -- value (Unnecessary?)
	 * logID(ID) -- value
	 * botOwnerIDs(ID,ID...) -- value, use commas to separate IDs
	 * modIDs(ID, ID...)
	 * adminIDs(ID, ID...)
	 * permLvl(?) -- undecided... Removed... TODO: Rewrite this?
	 * prefix(String) -- string
	 * accptPrv(bool) -- boolean
	 * accptMultSrvrs(bool) -- boolean
	 * vers(VERSION) -- String
	 * 
	 * EXAMPLE:
	 * locKey(Desktop/File/key.txt)
	 * logID(123456789)
	 * prefix(.)
	 * 
	 */
	public static final String overrideKeys[] = {
			"locKey", "guildID", "logID", "botOwnerIDs", "modIDs", "adminIDs", "permLvl",
			"prefix", "accptPrv", "vers"
			};
	
	/**
	 * YOU MUST SET THE FILE DIRECTORY TO A "key.txt" or "key.dat" FILE.
	 * It doesn't have to be in the "resources" folder.
	 */
	public static String locationKey = "../resources/key.txt", locationBackup = "../resources/backup/", locationJSON = "../resources/guildData.json", locationCommands = "../resources/commands.txt";
	
	/*
	 * This is an ignored variable. Useful for hard-coding stuff if you deem necessary.
	 * 
	 * "guildID" is used for the guild's ID and "logID" is used if a log channel is available
	 * 
	 * "guildID" is optional for one server
	 */
	public static String guildID = "590215639785013298", logID = "591391617655636100";
	
	/*
	 * This is an ignored variable. Useful for hard-coding stuff if you deem necessary.
	 * 
	 * "botOwnerIDs" contains the USER IDs
	 * "modIDs" and "adminIDs" contains their respective ROLE IDs (single-server use)
	 */
	public static Long[] botOwnerIDs = {216037365829992448L, 226843345643634688L};
	
	/*
	 * You can override the default prefix at the server-level
	 * 
	 * Use this to modify the prefix, '!' is the default and can be overridden/changed if needed
	 * Prefix CANNOT be empty.
	 */
	public static String prefix = "!";
	
	/*
	 * Use this if you want for the bot to react to private messages (eg., reacts to commands)
	 * This does not apply to SENDING messages in a user's DMs
	 * "acceptMultipleServers" will determine if "guildID" will be needed
	 */
	public static boolean acceptPriv = false, acceptMultipleServers = true;
	
	/*
	 * Use this to define the version
	 */
	public static String version = "Pre-Release v0.2.0a Build 3 - The Rewrite Update";
	
}
