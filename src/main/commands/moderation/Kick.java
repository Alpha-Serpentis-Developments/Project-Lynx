package main.commands.moderation;

import java.util.ArrayList;

import main.commands.Command;
import main.handlers.MessageHandler;
import main.init.InitData;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.entities.Role;

public class Kick extends Command {

	public Kick() {
		super("kick", "Moderators can use this to kick users from the discord.\n\n`" + InitData.prefix + "kick @AlphaSerpentis#3203 \"Because he bad (please don't use that as an actual reason)\"`");
		
		ArrayList<Long> ids = new ArrayList<Long>();
		ids.add(590216647458029578L);
		setPerms(ids);
	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) { 
		
		if(getRoleIDs().isEmpty()) {
			MessageHandler.sendMessage(chn, "The roles have not been defined for this! Please configure at startup or add permissions");
			
			return false;
		}
		
		for(long id: getRoleIDs()) {
			
			for(Role r: ((MessageReceivedEvent) misc).getAuthor().getJDA().getRoles()) {
				
				if(r.getIdLong() == id) {
					
					String reason = msg.substring(msg.indexOf(">") + 1);
					GuildController gld = ((MessageReceivedEvent) misc).getGuild().getController();
					User author = ((MessageReceivedEvent) misc).getAuthor(), punished = chn.getJDA().getUserById(msg.substring(msg.indexOf("@") + 1, msg.indexOf(">")));
					
					gld.kick(punished.getId(), "MOD: " + author.getAsTag() + " || " + reason).queue();
					
					//Check if the server has a log channel and log
					if(!InitData.logID.isEmpty()) {
						MessageHandler.sendMessage(gld.getGuild().getTextChannelById(InitData.logID), author.getAsTag() + " has kicked " + punished.getAsTag() + " for: " + reason);
					}
					
					MessageHandler.sendMessage(chn, "Kicked " + punished.getName() + " by " + author.getName());
					
					return true;
					
				}
				
			}
			
		}
		
		return false;
	}

}
