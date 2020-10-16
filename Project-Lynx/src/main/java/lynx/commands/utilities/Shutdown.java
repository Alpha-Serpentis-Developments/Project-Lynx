package lynx.commands.utilities;

import lynx.commands.Command;
import lynx.init.InitData;
import lynx.init.Launcher;
import lynx.manager.MessageManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Shutdown extends Command {
	
	public Shutdown() {
		setName("shutdown");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		//verifyExecution() method is NOT necessary due to this being a bot owner-only command.
		
		for(long id: InitData.botOwnerIDs)
			if(((MessageReceivedEvent) misc).getAuthor().getIdLong() == id) {
				MessageManager.sendMessage(chn, "Shutting down " + Launcher.api.getSelfUser().getName() + "!");
				
				try {
					Launcher.shutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				return true;
			}
				
		return false;
		
	}

}
