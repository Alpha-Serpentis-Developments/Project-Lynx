package commands;

import java.util.HashMap;

import init.InitData;

public class About extends Command {

	public About() {
		super("about", "**DEVELOPER**: AlphaSerpentis#3203\n**VERSION**:" + InitData.version + "\n**GITHUB**: (Private for now)");
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
