package lynx.commands.moderation;

import org.json.JSONObject;

import lynx.data.Data;
import lynx.init.InitData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class RemoveLog extends ModerationCommand {

	public RemoveLog() {
		super("removelog", "removed log", null);
	}

	@Override
	protected void punish(Member member, Member mod, String id) { // In actuality is not a "punish"
		Guild g = member.getGuild();
		// Writes the data to the JSON
		JSONObject data = Data.rawJSON;
		JSONObject logs = data.getJSONObject(g.getId()).getJSONObject("srvr_config").getJSONObject("logs");
		
		String type = id.substring(0, id.indexOf(":"));
		String key = id.substring(1+id.indexOf(":"));
		
		logs.getJSONObject(type).remove(key);
		
		Data.writeData(InitData.locationJSON, data.toString(), true, g.getId());
	}

}
