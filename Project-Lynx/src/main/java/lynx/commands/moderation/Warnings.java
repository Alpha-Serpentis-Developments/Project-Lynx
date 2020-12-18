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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Warnings extends Command {
	private static final int PAGE_SIZE = 5;
	private static final int MAX_WARNING_LENGTH_IN_LIST = 100;

	private static final String PAGE_TOO_HIGH = "User has only %s page(s) of warnings.";
	private static final String NO_WARNINGS = "User has no logged warnings.";

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

		String params = msg.substring(getName().length()).trim();
		if(!params.isEmpty()) {
            String name = params.substring(0, params.contains(" ") ? params.lastIndexOf(' ') : params.length());

            int page = 1;
            if (name.length() != params.length()) {
                page = Integer.parseInt(params.substring(name.length()).trim());
            }


            List<Member> list = UserResolver.resolve(name, gld);

            if (list.isEmpty()) {
                MessageManager.sendMessage(chn, NO_ONE);
            } else if (list.size() > 1) {
                MessageManager.sendMessage(chn, TOO_MANY);
            } else {
                User punished = list.get(0).getUser();

                String generatedWarnings = generateWarningsList(gld, punished, page);
                MessageManager.sendMessage(chn, generatedWarnings);

                return true;
            }
        }
		MessageManager.sendMessage(chn, "Usage: `warnings <user> <optional page>`");
		return false;
	}

	//Pages start at 1
	public String generateWarningsList(Guild gld, User usr, int page) {
		if(page < 1) {
			return "Page must be >= 1";
		}
		System.out.println("DEBUG [Warnings.java] " + Data.srvr_cache.get(gld));

		JSONObject data = Data.srvr_cache.get(gld).getJSONObject("logs");
		JSONObject warn = data.getJSONObject("warn");
		String userId = usr.getId();

		//this isn't the most efficient way of doing things it just looks clean
		List<Integer> warningKeysForUser = warn.keySet().stream()
				//filters to warnings of a specific user
				.filter(key -> warn.getJSONArray(key).getString(1).equals(userId))
				//sort
				.map(Integer::parseInt)
				.sorted()
				//make it a list
				.collect(Collectors.toList());
        Collections.reverse(warningKeysForUser);

		int pageCount = (int) Math.ceil(warningKeysForUser.size() * 1.0 / PAGE_SIZE);
		if(pageCount == 0)
			return NO_WARNINGS;
		if(page > pageCount)
			return String.format(PAGE_TOO_HIGH, pageCount);

		int skip = (page - 1) * PAGE_SIZE;
		List<Integer> requestedWarnings = warningKeysForUser.stream()
				.skip(skip)
				.limit(PAGE_SIZE)
				.collect(Collectors.toList());
		Collections.reverse(requestedWarnings);

		StringBuilder output = new StringBuilder();
		output.append("> **List of Warnings for:** `").append(usr.getAsTag()).append("`\n\n");

		for (int warningId : requestedWarnings) {
			JSONArray array = warn.getJSONArray(String.valueOf(warningId));
			long modId = array.getLong(0);
			Member mod = gld.getMemberById(modId);
			String modName = mod == null ? "unknown#0000": mod.getUser().getAsTag();

			String reason = Truncate.truncate(array.getString(2), MAX_WARNING_LENGTH_IN_LIST);

			output.append("**Warning ID**: ")
					.append(warningId)
					.append("\n**Moderator**: ")
					.append(modName)
					.append(" (")
					.append(modId)
					.append(")\n");
			if(reason.isEmpty()) {
				output.append("No reason was provided.");
			} else {
				output.append("**Reason**: ").append(reason);
			}
			output.append("\n\n");
		}

		output.append("Page ").append(page).append(" of ").append(pageCount);
		return output.toString();
	}
}
