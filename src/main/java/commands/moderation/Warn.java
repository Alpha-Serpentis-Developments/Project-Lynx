package commands.moderation;

import org.json.JSONArray;
import org.json.JSONObject;

import commands.Command;
import data.Data;
import handlers.MessageHandler;
import init.InitData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Warn extends Command {

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {

		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User mod = ((MessageReceivedEvent) misc).getAuthor(), punished = (msg.length() == getName().length()) ? null : gld.getMemberById(msg.substring(msg.indexOf("@") + 2, msg.indexOf(">"))).getUser();

		String reason = "";

		if(punished == null) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		}

		boolean result = verifyExecution(mod, punished, gld, chn);

		if(result) {

			if(msg.length() == 7 + punished.getAsTag().length()) { //7 is chosen for this because "!warn @" has 7 characters

				System.out.println("No reason?");
				reason = "A reason was not provided!";
			} else {
				reason = msg.substring(8 + punished.getAsTag().length());
			}

			//TODO: Make an option for servers to choose whether these messages are sent.
			MessageHandler.sendMessage(chn, "**" + punished.getName() + "** has been warned by **" + mod.getName() + "** for: \n\n**" + reason + "**");
			MessageHandler.sendMessage(punished.openPrivateChannel().complete(), "You have been warned by **" + mod.getName() + "** for: " + reason + "**\n\nPlease make sure you abide by the server's rules!");

			//CHECK FOR LOGGING
			System.out.println("CHECK FOR LOGGING: " + Data.srvr_cache.get(gld).get("logging_channel") + getLogging());

			if(getLogging() && Data.srvr_cache.get(gld).has("logging_channel")) {

				MessageHandler.sendMessage(gld.getTextChannelById(Data.srvr_cache.get(gld).getLong("logging_channel")), "**Moderator " + mod.getAsTag() + "** has warned user " + punished.getAsTag() + " for the following reason: \n\n" + reason);

				JSONObject data = new JSONObject(Data.readData(InitData.locationJSON));
				((JSONArray) ((JSONObject) data.getJSONObject(gld.getId()).get("logs")).get("warn")).put(new String[] {mod.getId(), punished.getId(), reason}); //The hell is this line?

				Data.writeData(InitData.locationJSON, data.toString());

			}

			return true;
		}

		return false;

	}

}
