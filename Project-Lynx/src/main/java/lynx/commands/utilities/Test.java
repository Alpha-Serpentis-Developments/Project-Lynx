package lynx.commands.utilities;

import lynx.commands.Command;
import lynx.manager.MessageManager;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Test extends Command {
	
	public Test() {
		setName("test");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		MessageManager.embedMessage(chn);
		
		return true;
	}

}
