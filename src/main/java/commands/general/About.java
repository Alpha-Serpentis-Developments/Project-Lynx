package commands.general;

import commands.Command;
import handlers.MessageHandler;
import init.InitData;
import net.dv8tion.jda.core.entities.TextChannel;

public class About extends Command {

	public About() {
		super("about", "Lucky Lynx is a public multi-purpose Discord Bot built on JDA!\n\n**Developer**: AlphaSerpentis#3203\n**Version**: " + InitData.version + "\n**GitHub**: https://github.com/AlphaSerpentis/Discord-Lynx\n**Patreon**: https://www.patreon.com/project_lynx");
	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) {
		
		MessageHandler.sendMessage(chn, getDesc());
		
		return true;
	}
	
	
}
