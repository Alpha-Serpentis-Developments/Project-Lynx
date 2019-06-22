package init;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import handlers.MessageHandler;
import handlers.ServerHandler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class Launcher {
	
	public static JDA api;
	public static long botID;
	
	public Launcher() {
		
		Scanner sc;
		
		/*
		 * Initialization will be halted IF the key's location is empty.
		 */
		if(InitData.locationKey.isEmpty()) {
			
			System.out.println("Key's location is empty! Checking if there is an override for key's location?");
			
			System.exit(-1);
			
		} else {
			
			try {
				System.out.println("Initializing...");
				
				//OVERRIDE SCANNER
				sc = new Scanner(new File("src/main/java/init/initOverrides")); // Checks for overrides to be applied
				
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
				
				//JDA INITIALIZATION
				System.out.println("Starting up JDA initialization...");
				
				sc = new Scanner(new File(InitData.locationKey));
				
				api = new JDABuilder(sc.next()).build();
				api.addEventListener(new MessageHandler());
				
				api.getPresence().setGame(Game.playing("Loading..."));
				
				api.awaitReady(); // Waits for JDA to complete loading to prevent issues
				botID = api.getSelfUser().getIdLong();
				System.out.println("Initializing complete!");
				api.getPresence().setGame(Game.playing("Looking for food..."));
				
			} catch (FileNotFoundException e) {
				System.out.println("File error! Error: ");
				
				e.printStackTrace();
				
				System.exit(-1);
			} catch (LoginException e) {
				System.out.println("Login Failure! Error: ");
				
				api.shutdownNow();
				
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("Error: ");
				
				api.shutdownNow();
				
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Possibly malformed initOverrides! Error: ");
				
				e.printStackTrace();
				
				System.out.println("Overrides ignored, using default settings.");
				try {
					ignoreOverride();
				} catch (InterruptedException | FileNotFoundException | LoginException e1) {
					
					e1.printStackTrace();
				}
				
			}
		}
		
	}
	
	public void ignoreOverride() throws InterruptedException, FileNotFoundException, LoginException {
		
		System.out.println("Starting up JDA initialization...");
		
		Scanner sc = new Scanner(new File(InitData.locationKey));
		
		api = new JDABuilder(sc.next()).build();
		api.addEventListener(new MessageHandler());
		api.addEventListener(new ServerHandler());
		
		api.getPresence().setGame(Game.playing("Loading..."));
		
		api.awaitReady(); // Waits for JDA to complete loading to prevent issues
		botID = api.getSelfUser().getIdLong();
		System.out.println("Initializing complete!");
		api.getPresence().setGame(Game.playing("Looking for food..."));
		
		sc.close();
		
	}
	
	public void shutdown() throws InterruptedException {
		
		api.getPresence().setStatus(OnlineStatus.OFFLINE);
		
		api.shutdown();
		
	}
	
	public static void main(String[] args) {
		
		new Launcher();
		
	}
	
}