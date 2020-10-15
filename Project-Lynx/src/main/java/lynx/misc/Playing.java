package lynx.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lynx.init.InitData;
import lynx.init.Launcher;
import net.dv8tion.jda.api.entities.Activity;

public class Playing implements Runnable {

	public static final String[] commands = {"help", "about"};
	
	@SuppressWarnings("serial")
	public static final List<String> messages = Collections.unmodifiableList(new ArrayList<String>() {{
		add("Licking my paws...");
		add("Purring at you...");
		add("*stares at you*");
		add("*purring noises*");
		add("*sleeping*");
	}});
	
	public static boolean setPlaying(String s) {
		Launcher.api.getPresence().setActivity(Activity.playing(s));
		return true;
	}
	
	public static String getPlaying() {
		
		String returnThis = "";
		Random rand = new Random();
		
		returnThis = InitData.prefix + commands[rand.nextInt(2)] + " | " + messages.get(rand.nextInt(messages.size()));
		
		return returnThis;
	}

	@Override
	public void run() {
		try {
			Thread.currentThread().setName("Playing Thread");
			System.out.println("[Playing.java] " + Thread.currentThread().getName() + " started!");
			while(true) {
				setPlaying(getPlaying());
				Thread.sleep(120000);
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
