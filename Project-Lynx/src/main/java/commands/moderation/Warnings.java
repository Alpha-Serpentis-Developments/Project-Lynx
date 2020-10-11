package commands.moderation;

import org.json.JSONArray;
import org.json.JSONObject;

import commands.Command;
import data.Data;
import manager.MessageManager;
import manager.ModerationManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Warnings extends Command {
	
	public Warnings() {
		setName("warnings");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		User mod = ((MessageReceivedEvent) misc).getAuthor(), punished = ModerationManager.grabPunished(gld, msg, 8);
		
		if(punished == null) {
			MessageManager.sendMessage(chn, getDesc());
			return true;
		}
		
		boolean result = verifyExecution(mod, punished, gld, chn);
		
		if(result) {
			
			String generatedWarnings = generateWarningsList(gld, punished);
			
			if(generatedWarnings.equals("")) {
				MessageManager.sendMessage(chn, punished.getName() + " has no logged warnings.");
			} else {
				MessageManager.sendMessage(chn, "> **List of Warnings for:** `" + punished.getAsTag() + "`\n\n" + generatedWarnings);
			}
			
		}
		
		return true;
		
	}
	
	public String generateWarningsList(Guild gld, User usr) {
		
		System.out.println("DEBUG [Warnings.java] " + Data.srvr_cache.get(gld));
		
		JSONObject data = Data.srvr_cache.get(gld).getJSONObject("logs");
		String construct = "";
		
		// Checks the list of warning IDs
		for(String key: data.getJSONObject("warn").keySet()) {
			
			System.out.println("DEBUG [Warnings.java] " + data);
			
			JSONArray arr = data.getJSONObject("warn").getJSONArray(key);
			
			// Check if the key corresponds to the user ID
			if(arr.getString(1).equals(usr.getId())) {
				construct += 
						"**Warning ID**: " + key +
						"\n**Moderator**: " + gld.getMemberById(arr.getLong(0)).getUser().getAsTag() + " (" + gld.getMemberById(arr.getLong(0)).getUser().getId() + ")" +
						"\n**Reason**: " + arr.getString(2) + "\n\n";
			}
			
		}
		
		return construct;
		
	}

}
