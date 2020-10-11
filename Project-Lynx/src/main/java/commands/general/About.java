package commands.general;

import commands.Command;
import manager.MessageManager;
import net.dv8tion.jda.api.entities.MessageChannel;

public class About extends Command {
	
	public About() {
		setName("about");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		MessageManager.sendMessage(chn, getDesc());
		
		return true;
	}
	
}
