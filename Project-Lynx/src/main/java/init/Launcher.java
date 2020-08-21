package init;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import data.Data;
import features.urp.UserRaidProtection;
import handlers.CommandHandler;
import handlers.MessageHandler;
import handlers.ServerHandler;
import misc.Playing;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Launcher {

	public static volatile boolean initialized = false;

	public static volatile JDA api;
	public static long botID;
	public static File tmpFile;

	public Launcher() {

		tmpFile = Data.createBackup(true); //Creates a temporary backup (which will delete itself upon exit)

		Scanner sc;

		/*
		 * Initialization MIGHT be halted if the InitData.locationKey is empty, but it'll check the overrides before exiting
		 */
		if(InitData.locationKey.isEmpty()) {

			System.out.println("[Launcher.java] Key's location is empty! Checking if there is an override for key's location?");
			//OVERRIDE INITIALIZATION
			overrideInit();

		} else {
			//OVERRIDE INITIAZLIATION
			overrideInit();

			//JDA INITIALIZATION
			try {
				sc = new Scanner(new File(InitData.locationKey));

				JDAInit(sc.next());

				sc.close();
			} catch (LoginException | InterruptedException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				initialized = true;
			}

		}

	}

	/**
	 * Used in the event that during start-up and overrides cannot be applied, it loads the defaults and any successfully applied overrides.
	 * @throws Exception
	 */
	public void ignoreOverride() throws Exception {

		System.out.println("[Luancher.java] Starting up JDA initialization...");

		Scanner sc = new Scanner(new File(InitData.locationKey));

		JDAInit(sc.next());

		sc.close();

	}

	/**
	 * Initializes JDA
	 * @param key The key/token for the Discord Bot
	 * @throws Exception
	 */
	public void JDAInit(String key) throws Exception {

		System.out.println("[Launcher.java] Starting up JDA initialization...");
		
		try {
			api = JDABuilder.create(key, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS).build();
			api.addEventListener(new MessageHandler());
			api.addEventListener(new ServerHandler());
			api.addEventListener(new UserRaidProtection());

			api.awaitReady(); // Waits for JDA to complete loading to prevent issues

			botID = api.getSelfUser().getIdLong();
		} catch (LoginException e) {
			System.out.println("[Launcher.java] LoginException was thrown! Ensure the key is correct! Refer to the following for more information: ");
			e.printStackTrace();
			
			System.exit(1);
		} catch (Error e) {
			System.out.println("[Launcher.java] A possibly fatal error was thrown. Refer to the stack trace for information.");
			e.printStackTrace();
			
			System.exit(1);
		}

		if(CommandHandler.initCommands())
			System.out.println("[Launcher.java] Initializing commands complete! [1/2]");
		else {
			System.out.println("[Launcher.java] Commands cannot be initialized! Shutting down!");
			System.exit(1);
		}

		System.out.println("[Launcher.java] Initializing cache...");
		Data.initCache();

		System.out.println("[Launcher.java] Initializing cache complete! [2/2]");
		new Thread(new Playing()).start();

	}

	public void overrideInit() {

		try {
			System.out.println("[Launcher.java] Initializing...");

			//OVERRIDE SCANNER
			Scanner sc;

			sc = new Scanner(new File("../resources/initOverrides.txt")); // Checks for overrides to be applied

			if(sc.hasNext())
				System.out.println("[Launcher.java] Override(s) detected, scanning...");
			else
				System.out.println("[Launcher.java] No overrides found, using default settings.");

			while(sc.hasNext()) {

				String itm = sc.nextLine(), ident = itm.substring(0, itm.indexOf("(")), stuff = itm.substring(itm.indexOf("(") + 1, itm.indexOf(")"));

				//System.out.println(itm + " " + ident + " " + stuff);

				for(String oKey: InitData.overrideKeys) {

					if(oKey.equals(ident)) {

						System.out.println("[Launcher.java] Applying " + oKey + " override");

						switch(ident) {

						case "locKey":
							InitData.locationKey = stuff;
							break;
						case "locBackup":
							InitData.locationBackup = stuff;
							break;
						case "guildID":
							InitData.guildID = stuff;
							break;
						case "logID":
							InitData.logID = stuff;
							break;
						case "botOwnerIDs":

							Scanner scb = new Scanner(stuff);
							List<Long> ids = new ArrayList<Long>();
							scb.useDelimiter(",");
							while(scb.hasNext()) {
								ids.add(scb.nextLong());
							}

							InitData.botOwnerIDs = new Long[ids.size()];
							for(int i = 0; i < ids.size(); i++) {
								InitData.botOwnerIDs[i] = ids.get(i);
							}

							break;
						case "permLvl":
							System.out.println("[Launcher.java] permLvl override is unused! Nothing changed");
							break;
						case "prefix":
							InitData.prefix = stuff;
							break;
						case "accptPrv":
							InitData.acceptPriv = Boolean.parseBoolean(stuff);
							break;
						case "vers":
							InitData.version = stuff;
							break;

						}

					}

				}

			}

			sc.close();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void shutdown() throws InterruptedException {

		api.getPresence().setStatus(null);

		api.shutdown();

		try {
			tmpFile.deleteOnExit();
			Data.createBackup(false);
			System.out.println("[Launcher.java] Deleting " + tmpFile.getName());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

	public static void main(String[] args) {

		new Launcher();

	}

}
