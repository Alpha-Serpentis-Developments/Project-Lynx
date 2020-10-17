package lynx.commands.moderation;

import lynx.commands.Command;
import lynx.data.Data;
import lynx.init.InitData;
import lynx.manager.MessageManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

import java.util.Arrays;

public class Log extends Command {
	
	public Log() {
		setName("log");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		if(msg.equalsIgnoreCase("log")) {
			MessageManager.sendMessage(chn, getDesc());
			return true;
		} else {
			if(!hasRolesToUse(((MessageReceivedEvent) misc).getMember())) {
				return false;
			}
		}
		
		final String[] approveKeywords = new String[] {"yes", "enable", "on"};
		final String[] rejectKeywords = new String[] {"no", "disable", "off"};
		final String[] logKeywords = new String[] {"delete", "edit", "kick", "ban", "warn", "userraiddetect", "userraid", "urp", "raid"};
		
		// Obtains the result
		String check = msg.replaceAll("log ", "");
		
		// Copy the JSON
		JSONObject modifyJSON = Data.rawJSON;
		
		// Approve check
		if(Arrays.asList(approveKeywords).contains(check)) {
			MessageManager.sendMessage(chn, "Server logging enabled.");
			
			modifyJSON.getJSONObject(((MessageReceivedEvent) misc).getGuild().getId()).getJSONObject("srvr_config").put("logging", true);
			
			Data.writeData(InitData.locationJSON, modifyJSON.toString(), true, ((MessageReceivedEvent) misc).getGuild().getId());
			return true;
		}
		
		// Reject check
		if(Arrays.asList(rejectKeywords).contains(check)) {
			MessageManager.sendMessage(chn, "Server logging disabled.");
			modifyJSON.getJSONObject(((MessageReceivedEvent) misc).getGuild().getId()).getJSONObject("srvr_config").put("logging", false);
			
			Data.writeData(InitData.locationJSON, modifyJSON.toString(), true, ((MessageReceivedEvent) misc).getGuild().getId());
			return true;
		}
		
		// Check for logging type keywords
		if(Arrays.asList(logKeywords).contains(check)) {
			
			String breakApart = check;
			
			while(!check.isEmpty()) {
				
				String crntKeyword = "";
				
				if(breakApart.contains(" ")) {
					crntKeyword = breakApart.substring(0, breakApart.indexOf(" "));
					breakApart = breakApart.replace(crntKeyword + " ", "");
				} else
					crntKeyword = breakApart;
				
			}
			
		}
		
		return false;
	}

}
