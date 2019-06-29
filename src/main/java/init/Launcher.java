package init;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import data.Data;
import handlers.MessageHandler;
import handlers.ServerHandler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

public class Launcher {

	public static JDA api;
	public static long botID;

	public Launcher() {

		//TESTING, REMOVE THIS

		Data.initCache();

		//TESTING END

		Scanner sc;

		/*
		 * Initialization will be halted IF the key's location is empty.
		 */
		if(InitData.locationKey.isEmpty()) {

			System.out.println("Key's location is empty! Checking if there is an override for key's location?");
			//OVERRIDE INITIALIZATION
			overrideInit();

		} else {
			//OVERRIDE INITIAZLIATION
			overrideInit();

			//JDA INITIALIZATION
			System.out.println("Starting up JDA initialization...");

			try {
				sc = new Scanner(new File(InitData.locationKey));

				JDAInit(sc.next());

				sc.close();
			} catch (LoginException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * Used in the event that during start-up and overrides cannot be applied, it loads the defaults and any successfully applied overrides.
	 * @throws InterruptedException
	 * @throws FileNotFoundException
	 * @throws LoginException
	 */
	public void ignoreOverride() throws InterruptedException, FileNotFoundException, LoginException {

		System.out.println("Starting up JDA initialization...");

		Scanner sc = new Scanner(new File(InitData.locationKey));

		JDAInit(sc.next());

		sc.close();

	}

	/**
	 * Initializes JDA
	 * @param key The key/token for the Discord Bot
	 * @throws LoginException
	 * @throws InterruptedException
	 */
	public void JDAInit(String key) throws LoginException, InterruptedException {

		api = new JDABuilder(key).build();
		api.addEventListener(new MessageHandler());
		api.addEventListener(new ServerHandler());

		api.getPresence().setGame(Game.playing("Loading..."));

		api.awaitReady(); // Waits for JDA to complete loading to prevent issues
		botID = api.getSelfUser().getIdLong();
		getServers();

		System.out.println("Initializing complete!");
		api.getPresence().setGame(Game.playing("Looking for food..."));

	}

	public void overrideInit() {

		try {
			System.out.println("Initializing...");

			//OVERRIDE SCANNER
			Scanner sc;

			sc = new Scanner(new File("resources/initOverrides.txt")); // Checks for overrides to be applied

			if(sc.hasNext())
				System.out.println("Override(s) detected, scanning...");
			else
				System.out.println("No overrides found, using default settings.");

			while(sc.hasNext()) {

				String itm = sc.nextLine(), ident = itm.substring(0, itm.indexOf("(")), stuff = itm.substring(itm.indexOf("(") + 1, itm.indexOf(")"));

				//System.out.println(itm + " " + ident + " " + stuff);

				for(String oKey: InitData.overrideKeys) {

					if(oKey.equals(ident)) {

						System.out.println("Applying " + oKey + " override");

						switch(ident) {

						case "locKey":
							InitData.locationKey = stuff;
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
						case "modIDs":

							scb = new Scanner(stuff);
							ids = new ArrayList<Long>();
							scb.useDelimiter(",");
							while(scb.hasNext()) {
								ids.add(scb.nextLong());
							}

							InitData.modIDs = new Long[ids.size()];
							for(int i = 0; i < ids.size(); i++) {
								InitData.modIDs[i] = ids.get(i);
							}

							break;
						case "adminIDs":

							scb = new Scanner(stuff);
							ids = new ArrayList<Long>();
							scb.useDelimiter(",");
							while(scb.hasNext()) {
								ids.add(scb.nextLong());
							}

							InitData.adminIDs = new Long[ids.size()];
							for(int i = 0; i < ids.size(); i++) {
								InitData.adminIDs[i] = ids.get(i);
							}

							break;
						case "permLvl":
							System.out.println("permLvl override is unused! Nothing changed");
							break;
						case "prefix":
							if(stuff.isEmpty() || stuff.length() > 1) {
								System.out.println("prefix override is malformed! Nothing changed");
							} else {
								InitData.prefix = stuff.charAt(0);
							}
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

	public void getServers() {
		ServerHandler.cachedGuilds = new ArrayList<Guild>(api.getGuilds());
	}

	public void shutdown() throws InterruptedException {

		api.getPresence().setStatus(OnlineStatus.OFFLINE);

		api.shutdown();

	}

	public static void main(String[] args) {

		new Launcher();

	}

}
