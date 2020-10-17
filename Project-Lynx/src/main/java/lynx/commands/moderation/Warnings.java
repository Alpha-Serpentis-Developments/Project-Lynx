package lynx.commands.moderation;

import lynx.commands.Command;
import lynx.data.Data;
import lynx.manager.MessageManager;
import lynx.misc.Truncate;
import lynx.misc.UserResolver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Warnings extends Command {
	public Warnings() {
		setName("warnings");
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		Guild gld = ((MessageReceivedEvent) misc).getGuild();
		Member mod = ((MessageReceivedEvent) misc).getMember();

		if(!hasRolesToUse(mod)) {
			MessageManager.sendMessage(chn, "You do not have permissions to use this command.");
			return false;
		}

		String name = msg.substring(getName().length());

		if(name.isEmpty()) {
			MessageManager.sendMessage(chn, getDesc());
		} else {
			List<Member> list = UserResolver.resolve(name, gld);

			if(list.isEmpty()) {
				MessageManager.sendMessage(chn, NO_ONE);
			} else if(list.size() > 1) {
				MessageManager.sendMessage(chn, TOO_MANY);
			} else {
				User punished = list.get(0).getUser();

				String generatedWarnings = generateWarningsList(gld, punished);

				if(generatedWarnings.equals("")) {
					MessageManager.sendMessage(chn, punished.getName() + " has no logged warnings.");
				} else {
					MessageManager.sendMessage(chn, "> **List of Warnings for:** `" + punished.getAsTag() + "`\n\n" + generatedWarnings);
				}

				return true;
			}
		}
		return false;
	}

	public String generateWarningsList(Guild gld, User usr) {
		System.out.println("DEBUG [Warnings.java] " + Data.srvr_cache.get(gld));

		JSONObject data = Data.srvr_cache.get(gld).getJSONObject("logs");
		StringBuilder construct = new StringBuilder();

		// Checks the list of warning IDs
		for(String key : data.getJSONObject("warn").keySet()) {
			System.out.println("DEBUG [Warnings.java] " + data);

			JSONArray arr = data.getJSONObject("warn").getJSONArray(key);

			// Check if the key corresponds to the user ID
			if(arr.getString(1).equals(usr.getId())) {
				long modId = arr.getLong(0);

				Member mod = gld.getMemberById(arr.getLong(0));
				String modName = mod == null ? "unknown#0000": mod.getUser().getAsTag();

				String reason = Truncate.truncate(arr.getString(2), 100);

				construct.append("**Warning ID**: ")
						.append(key).append("\n**Moderator**: ")
						.append(modName)
						.append(" (")
						.append(modId)
						.append(")\n");
				if(reason.isEmpty()) {
					construct.append("No reason was provided.");
				} else {
					construct.append("**Reason**: ")
							.append(reason);
				}
				construct.append("\n\n");
			}
		}

		return construct.toString();
	}
}
