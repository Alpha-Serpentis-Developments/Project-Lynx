package init;

public class InitData {

	/*
	 * YOU CAN OVERRIDE ALL OF THE BELOW BY MODIFYING initOverrides.txt
	 * Leave initOverrides EMPTY if you do not wish to override anything!
	 * Use the following initOverrides keys:
	 *
	 * locKey(KEY_LOCATION) -- directory
	 * locBackup(BACKUP_LOCATION) -- directory
	 * locJSON(JSON_LOCATION) -- directory
	 * guildID(ID) -- value (Unnecessary?)
	 * logID(ID) -- value
	 * botOwnerIDs(ID,ID...) -- value, use commas to separate IDs
	 * modIDs(ID, ID...)
	 * adminIDs(ID, ID...)
	 * permLvl(?) -- undecided... Removed... TODO: Rewrite this?
	 * prefix(char) -- character
	 * accptPrv(bool) -- boolean
	 * accptMultSrvrs(bool) -- boolean
	 * vers(VERSION) -- String
	 *
	 * EXAMPLE:
	 * locKey(Desktop/File/key.txt)
	 * logID(123456789)
	 * modIDs(1,2,3,4,5)
	 * prefix(.)
	 *
	 */
	public static final String overrideKeys[] = {
			"locKey", "locBackup", "locJSON", "guildID", "logID", "botOwnerIDs", "modIDs", "adminIDs", "permLvl",
			"prefix", "accptPrv", "accptMultSrvrs", "vers"
			};

	/**
	 * YOU MUST SET THE FILE DIRECTORY TO A "key.txt" or "key.dat" FILE.
	 * It doesn't have to be in the depend folder.
	 */
	public static String locationKey = "resources/key.txt", locationBackup = "resources/backup/", locationJSON = "resources/guildData.json";

	/*
	 * "guildID" is used for the guild's ID and "logID" is used if a log channel is available
	 *
	 * TODO: Make it so that it supports multiple servers for LUCKY LYNX ONLY
	 * "guildID" is optional for one server
	 */
	public static String guildID = "590215639785013298", logID = "591391617655636100";

	/*
	 * "botOwnerIDs" contains the USER IDs
	 * "modIDs" and "adminIDs" contains their respective ROLE IDs
	 */
	public static Long[] botOwnerIDs = {216037365829992448L, 226843345643634688L}, modIDs = {594550521252020226L}, adminIDs = {594550478118060054L};

	/*
	 * Use this to modify the prefix, '!' is the default and can be overridden/changed if needed
	 * Prefix only supports one character and CANNOT be empty.
	 */
	public static char prefix = '!';

	/*
	 * Use this if you want for the bot to react to private messages (eg., reacts to commands)
	 * This does not apply to SENDING messages in a user's DMs
	 * "acceptMultipleServers" will determine if "guildID" will be needed
	 */
	public static boolean acceptPriv = false, acceptMultipleServers = true;

	/*
	 * Use this to add a version
	 */
	public static String version = "0.01a";

}
