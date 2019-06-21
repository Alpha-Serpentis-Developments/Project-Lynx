package commands.utilities;

import commands.Command;
import net.dv8tion.jda.core.entities.TextChannel;

public class Shutdown extends Command {

	public Shutdown() {
		super("shutdown");
		setRequirePerms(true);
	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) {
		// TODO Auto-generated method stub
		return false;
	}

}
