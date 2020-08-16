package commands.moderation;

import org.json.JSONObject;

import commands.Command;
import data.Data;
import handlers.MessageHandler;
import handlers.ModerationHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Warnings extends Command {

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User mod = ((MessageReceivedEvent) misc).getAuthor(), punished = ModerationHandler.grabPunished(gld, msg, 8);
		
		if(punished == null) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		}
		
		boolean result = verifyExecution(mod, punished, gld, chn);
		
		if(result) {
			
			String generatedWarnings = generateWarningsList(gld, punished);
			
			if(generatedWarnings == null) {
				MessageHandler.sendMessage(chn, punished.getName() + " has no logged warnings.");
			} else {
				MessageHandler.sendMessage(chn, "> **" + punished.getName() + "'s List of Warnings**\n\n" + generatedWarnings);
			}
			
		}
		
		return true;
		
	}
	
	public String generateWarningsList(Guild gld, User usr) {
		
		System.out.println("DEBUG [Warnings.java] " + Data.srvr_cache.get(gld));
		
		JSONObject data = Data.srvr_cache.get(gld).getJSONObject("logs");
		String construct = null;
		
		// Checks the list of warning IDs
		for(String key: data.getJSONObject("warn").keySet()) {
			
			// Check if the key corresponds to the user ID
			if(data.getJSONObject("warn").getJSONObject(key).getString("1").equals(usr.getId())) {
				construct = "match";
				break;
			}
			
		}
		
		return construct;
		
	}

}
