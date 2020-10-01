package commands.moderation;

import java.util.Arrays;

import org.json.JSONObject;

import commands.Command;
import data.Data;
import handlers.MessageHandler;
import init.InitData;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Log extends Command {
	
	public Log() {
		setName("log");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		
		if(msg.equalsIgnoreCase("log")) {
			MessageHandler.sendMessage(chn, getDesc());
			return true;
		} else {
			if(!verifyUse(((MessageReceivedEvent) misc).getAuthor(), ((MessageReceivedEvent) misc).getGuild(), chn)) {
				return false;
			}
		}
		
		final String[] approveKeywords = new String[] {"yes", "enable", "on"};
		final String[] rejectKeywords = new String[] {"no", "disable", "off"};
		
		// Obtains the result
		String check = msg.replaceAll("log ", "");
		
		// Copy the JSON
		JSONObject modifyJSON = Data.rawJSON;
		
		// Approve check
		if(Arrays.asList(approveKeywords).contains(check)) {
			MessageHandler.sendMessage(chn, "Server logging enabled.");
			
			modifyJSON.getJSONObject(((MessageReceivedEvent) misc).getGuild().getId()).getJSONObject("srvr_config").put("logging", true);
			
			Data.writeData(InitData.locationJSON, modifyJSON.toString(), true, ((MessageReceivedEvent) misc).getGuild().getId());
			return true;
		}
		
		// Reject check
		if(Arrays.asList(rejectKeywords).contains(check)) {
			MessageHandler.sendMessage(chn, "Server logging disabled.");
			modifyJSON.getJSONObject(((MessageReceivedEvent) misc).getGuild().getId()).getJSONObject("srvr_config").put("logging", false);
			
			Data.writeData(InitData.locationJSON, modifyJSON.toString(), true, ((MessageReceivedEvent) misc).getGuild().getId());
			return true;
		}
		
		return false;
	}

}
