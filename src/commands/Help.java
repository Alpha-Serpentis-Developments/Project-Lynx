package commands;

import java.util.HashMap;

public class Help extends Command {

	public Help() {
		super("help", "Use this command to obtain information on both the bot and commands available!");
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public boolean action(HashMap<String, Boolean> p) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
