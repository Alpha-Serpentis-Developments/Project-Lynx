package commands.moderation;

import org.json.JSONObject;

import commands.Command;
import data.Data;
import handlers.MessageHandler;
import handlers.ModerationHandler;
import init.InitData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Warn extends Command {

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {

		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User mod = ((MessageReceivedEvent) misc).getAuthor(), punished = ModerationHandler.grabPunished(gld, msg, 4);

		String reason = "";

		if(punished == null) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		}

		boolean result = verifyExecution(mod, punished, gld, chn);
		
		if(result) {
			
			try {
				reason = msg.substring(msg.indexOf("\"") + 1, msg.lastIndexOf("\""));
			} catch(StringIndexOutOfBoundsException e) {
				reason = "`A reason was not provided.`";
			}

			//TODO: Make an option for servers to choose whether these messages are sent.
			MessageHandler.sendMessage(chn, "**" + punished.getName() + "** (" + punished.getId() + ") has been warned by **" + mod.getName() + "** for: \n\n> **" + reason + "**");
			MessageHandler.sendMessage(punished.openPrivateChannel().complete(), "You have been warned by **" + mod.getName() + "** for: **" + reason + "**\n\nPlease make sure you abide by the server's rules!");

			// Writes the data to the JSON
			JSONObject data = Data.rawJSON;
			Integer warn_id = data.getJSONObject(gld.getId()).getJSONObject("srvr_config").getJSONObject("logs").getJSONObject("warn").length() + 1;

			data.getJSONObject(gld.getId()).getJSONObject("srvr_config").getJSONObject("logs").getJSONObject("warn").put(warn_id.toString(), new String[] {mod.getId(), punished.getId(), reason});
			
			Data.writeData(InitData.locationJSON, data.toString(), true, gld.getId());
			
			//CHECK FOR LOGGING
			System.out.println("CHECK FOR LOGGING: " + Data.srvr_cache.get(gld).get("logging_channel") + getLogging());

			if(getLogging() && !(Data.srvr_cache.get(gld).get("logging_channel") == null)) {
				
				MessageHandler.sendMessage(gld.getTextChannelById(Data.srvr_cache.get(gld).getLong("logging_channel")), "**Moderator " + mod.getAsTag() + "** has warned user " + punished.getAsTag() + " for the following reason: \n\n> " + reason);

			}

			return true;
		}

		return false;

	}

}
