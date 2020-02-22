package commands.moderation;

import commands.Command;
import handlers.MessageHandler;
import init.InitData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Kick extends Command {

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User mod = ((MessageReceivedEvent) misc).getAuthor(), punished = (msg.length() == getName().length()) ? null : gld.getMemberById(msg.substring(msg.indexOf("!") + 1, msg.indexOf(">"))).getUser();
		
		String reason = msg.substring(msg.indexOf(">") + 1);
		
		if(punished == null) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		}
		
		boolean result = verifyExecution(mod, punished, gld, chn);
		
		if(result) {
			gld.kick(gld.getMember(punished), reason).queue();
			MessageHandler.sendMessage(gld.getTextChannelById(InitData.logID), "Moderator **" + mod.getAsTag() + "** has kicked user **" + punished.getAsTag() + "** for: \n\n> " + reason);
		}
		
		return false;
		
	}

}
