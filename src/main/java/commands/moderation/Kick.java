package commands.moderation;

import commands.Command;
import handlers.MessageHandler;
import handlers.ModerationHandler;
import handlers.ServerHandler;
import init.InitData;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class Kick extends Command {

	public Kick() {
		super("kick", "Moderators can use this to kick users from the discord.\n\n`" + InitData.prefix + "kick @AlphaSerpentis#3203 \"Because he bad (please don't use that as an actual reason)\"`");
		setRequirePerms(true);
		setAllowPrivate(false);

		setPerms(ModerationHandler.obtainHashmap(new Long[][] {InitData.modIDs, InitData.adminIDs}, true));

	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) {

		boolean isMod = false, isOwner = false;

		GuildController gld = ((MessageReceivedEvent) misc).getGuild().getController();
		User author = ((MessageReceivedEvent) misc).getAuthor();

		//The server owner CAN execute moderator commands WITHOUT having to define the roles (but it would be in the best interest to define them)
		if(ServerHandler.allowedToDoAction(gld.getGuild(), author, null)) {
			isOwner = true;
			System.out.println("Is server owner!");
		} else if(!isRoleIDsDefined()) {
			MessageHandler.sendMessage(chn, "The roles have not been defined for this! Please configure at startup or add permissions");

			return false;
		} else {
			isMod = ServerHandler.allowedToDoAction(gld.getGuild(), author, getRoleIDs());
		}

		if(isMod || isOwner) {

			if(msg.equalsIgnoreCase(InitData.prefix + "kick")) {

				MessageHandler.sendMessage(chn, "**Usage**: `" + InitData.prefix + "kick @[user] [reason]`");

				return true;

			}

			String reason;
			User punished = chn.getJDA().getUserById(msg.substring(msg.indexOf("@") + 1, msg.indexOf(">")));

			//Check if the author is higher than punished, otherwise command fails to execute
			try {
				if(!(ServerHandler.compareRoles(new User[] {author, punished}, gld.getGuild()) > 0) && !isOwner) { //The server owner overrides this permission check

					MessageHandler.sendMessage(chn, "You are not allowed to kick " + punished.getName());

					return false;


				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//Checks if the moderator even decided to put a reason
			if(msg.length() == msg.substring(0, msg.indexOf(">") + 1).length()) {
				reason = "*No reason was provided by the moderator*";
			} else {
				reason = msg.substring(msg.indexOf(">") + 2);

			}

			gld.kick(punished.getId(), "MOD: " + author.getAsTag() + " || " + reason).queue();

			//Check if the server has a log channel and log
			if(!InitData.logID.isEmpty()) {
				MessageHandler.sendMessage(gld.getGuild().getTextChannelById(InitData.logID), "**Kicked User**: " + punished.getAsTag() + "\n**Staff Member**: " + author.getAsTag() + "\n**Reason**: " + reason);
			}

			MessageHandler.sendMessage(chn, "**" + punished.getName() + "** has been kicked by **" + author.getName() + "**");

			return true;

		}

		return false;

	}

}
