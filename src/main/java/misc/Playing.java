package misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import init.InitData;

public class Playing implements Runnable {

	public static final String[] commands = {"help", "about"};

	@SuppressWarnings("serial")
	public static final List<String> messages = Collections.unmodifiableList(new ArrayList<String>() {{
		add("Licking my paws...");
		add("Purring at you...");
		add("*stares at you*");
		add("*purring noises*");
	}});

	public static boolean setPlaying(String s) {



		return false;
	}

	public static String getPlaying() {


		return "";
	}

	@Override
	public void run() {

	}

}
