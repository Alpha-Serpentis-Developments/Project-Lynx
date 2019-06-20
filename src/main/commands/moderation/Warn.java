package main.commands.moderation;

import main.commands.Command;
import main.init.InitData;
import net.dv8tion.jda.core.entities.TextChannel;

public class Warn extends Command {

	public Warn() {
		super("warn", "Give a warning to a user that can have a reason attached to it.\n\n`" + InitData.prefix + "warn @AlphaSerpentis#3203 Didn't give the lynx enough back rubs`");
		setRequirePerms(true);
	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) {
		
		
		
		return false;
	}
	
}
