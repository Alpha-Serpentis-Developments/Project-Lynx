package commands.utilities;

import commands.Command;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Prefix extends Command {

	public Prefix() {
		setName("prefix");
	}
	
	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		// TODO Auto-generated method stub
		return false;
	}

}
