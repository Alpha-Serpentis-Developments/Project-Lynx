package main.commands.utilities;

import main.commands.Command;
import net.dv8tion.jda.core.entities.TextChannel;

public class Shutdown extends Command {

	public Shutdown() {
		super("shutdown");
	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) {
		// TODO Auto-generated method stub
		return false;
	}

}
