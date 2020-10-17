package lynx.commands.moderation;

import lynx.data.Data;
import lynx.init.InitData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.json.JSONObject;

public class Warn extends ModerationCommand {
	public Warn() {
		super("warn", "warned", null);
	}

	@Override
	protected void punish(Member punished, Member mod, String reason) {
		Guild g = punished.getGuild();
		// Writes the data to the JSON
		JSONObject data = Data.rawJSON;
		JSONObject warns = data.getJSONObject(g.getId()).getJSONObject("srvr_config").getJSONObject("logs").getJSONObject("warn");
		int warnId = warns.length() + 1;

		warns.put(Integer.toString(warnId), new String[]{mod.getId(), punished.getId(), reason});

		Data.writeData(InitData.locationJSON, data.toString(), true, g.getId());
	}
}
