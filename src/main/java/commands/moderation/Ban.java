package commands.moderation;

import commands.Command;
import net.dv8tion.jda.core.entities.TextChannel;

public class Ban extends Command {

	public Ban() {
		super("ban");
		setRequirePerms(true);
	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) {
		
		
		
		return false;
	}

}
