package lynx.commands.moderation;

import org.json.JSONObject;

import lynx.commands.Command;
import lynx.data.Data;
import lynx.init.InitData;
import lynx.manager.MessageManager;
import lynx.manager.ModerationManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Kick extends Command {
	
	public Kick() {
		setName("kick");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User mod = ((MessageReceivedEvent) misc).getAuthor(), punished = ModerationManager.grabPunished(gld, msg, 4);

		String reason = "";

		if(punished == null) {
			MessageManager.sendMessage(chn, getDesc());
			return true;
		}

		boolean result = verifyExecution(mod, punished, gld, chn);
		
		if(result) {
			
			reason = msg.substring(5);
			
			check: try {
				
				if(reason.indexOf(" ") == -1) {
					reason = "A reason was not provided by " + mod.getAsTag() + " (" + mod.getIdLong() + ")";
					break check;
				}
				
				reason = reason.substring(reason.indexOf(" ") + 1);
			} catch(StringIndexOutOfBoundsException e) {
				
			} finally {
				if(reason.isBlank())
					reason = "A reason was not provided by " + mod.getAsTag() + " (" + mod.getIdLong() + ")";
			}
			
			//TODO: Make an option for servers to choose whether these messages are sent.
			MessageManager.sendMessage(chn, "**" + punished.getName() + "** has been kicked by **" + mod.getName() + "** for: \n\n> **" + reason + "**");
			MessageManager.sendMessage(punished.openPrivateChannel().complete(), "You have been kicked by **" + mod.getName() + "** for: **" + reason + "**\n\n");
			
			// Writes the data to the JSON
			JSONObject data = Data.rawJSON;
			Integer ban_id = data.getJSONObject(gld.getId()).getJSONObject("srvr_config").getJSONObject("logs").getJSONObject("kick").length() + 1;
						
			data.getJSONObject(gld.getId()).getJSONObject("srvr_config").getJSONObject("logs").getJSONObject("kick").put(ban_id.toString(), new String[] {mod.getId(), punished.getId(), reason});
			
			Data.writeData(InitData.locationJSON, data.toString(), true, gld.getId());
			
			// Initiate the kick
			gld.kick(punished.getId(), reason).queue();
			
			//CHECK FOR LOGGING
			System.out.println("CHECK FOR LOGGING: " + Data.srvr_cache.get(gld).get("logging_channel") + getLogging());

			if(getLogging() && !(Data.srvr_cache.get(gld).get("logging_channel") == null)) {

				MessageManager.sendMessage(gld.getTextChannelById(Data.srvr_cache.get(gld).getLong("logging_channel")), "**Moderator " + mod.getAsTag() + "** has kicked user " + punished.getAsTag() + " for the following reason: \n\n> " + reason);

			}
			
			return true;
			
		}
		
		return false;
		
	}

}
