package commands.utilities;

import commands.Command;
import handlers.MessageHandler;
import init.InitData;
import init.Launcher;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Shutdown extends Command {

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		//verifyExecution() method is NOT necessary due to this being a bot owner-only command.
		
		for(long id: InitData.botOwnerIDs)
			if(((MessageReceivedEvent) misc).getAuthor().getIdLong() == id) {
				MessageHandler.sendMessage(chn, "Shutting down " + Launcher.api.getSelfUser().getName() + "!");
				
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
