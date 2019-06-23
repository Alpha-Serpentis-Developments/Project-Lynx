package init;

public class InitData {

	/*
	 * YOU CAN OVERRIDE ALL OF THE BELOW BY MODIFYING initOverrides.txt
	 * Leave initOverrides EMPTY if you do not wish to override anything!
	 * Use the following initOverrides keys:
	 *
	 * locKey(KEY_LOCATION) -- directory
	 * guildID(ID) -- value (Unnecessary?)
	 * logID(ID) -- value
	 * botOwnerIDs(ID,ID...) -- value, use commas to separate IDs
	 * modIDs(ID, ID...)
	 * adminIDs(ID, ID...)
	 * permLvl(?) -- undecided... Removed... TODO: Rewrite this?
	 * prefix(char) -- character
	 * accptPrv(bool) -- boolean
	 * vers(VERSION) -- String
	 *
	 * EXAMPLE:
	 * locKey(Desktop/File/key.txt)
	 * logID(123456789)
	 * prefix(.)
	 *
	 */
	public static final String overrideKeys[] = {
			"locKey", "guildID", "logID", "botOwnerIDs", "permLvl",
			"prefix", "accptPrv", "vers"
			};

	/**
	 * YOU MUST SET THE FILE DIRECTORY TO A "key.txt" or "key.dat" FILE.
	 * It doesn't have to be in the depend folder.
	 */
	public static String locationKey = "depend/key.txt";

	/*
	 * "guildID" is used for the guild's ID and "logID" is used if a log channel is available
	 *
	 * TODO: Make it so that it supports multiple servers for LUCKY LYNX ONLY
	 * "guildID" can be filled in manually if needed
	 */
	public static String guildID = "", logID = "591391617655636100";

	/*
	 * "botOwnerIDs" contains the USER IDs
	 * "modIDs" and "adminIDs" contains their respective ROLE IDs
	 */
	public static Long[] botOwnerIDs = {216037365829992448L}, modIDs = {}, adminIDs = {};

	/*
	 * Use this to modify the prefix, '!' is the default and can be overridden/changed if needed
	 * Prefix only supports one character and CANNOT be empty.
	 */
	public static char prefix = '!';

	/*
	 * Use this if you want for the bot to react to private messages (eg., reacts to commands)
	 * This does not apply to SENDING messages in a user's DMs
	 */
	public static boolean acceptPriv = true, acceptMultipleServers = true;

	/*
	 * Use this to add a version
	 */
	public static String version = "0.01a";

}
