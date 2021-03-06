package lynx.commands.general;

import lynx.commands.Command;
import lynx.manager.MessageManager;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Help extends Command {
	
	public Help() {
		setName("help");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
	
		MessageManager.sendMessage(chn, getDesc());
		
		return true;
		
	}

}
