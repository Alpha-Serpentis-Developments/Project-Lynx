package commands.utilities;

import commands.Command;
import init.InitData;
import init.Launcher;
import manager.MessageManager;
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
