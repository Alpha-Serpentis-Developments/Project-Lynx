package lynx.misc;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class UserResolver {
	private static final String[] FILTERED_STRINGS = {"!", "@", "\n", ">", "<"};

	/**
	 * Attempts to get a Member from a String. Will accept either a mention, user snowflake/ID, nickname, or username.
	 *
	 * @param fromText User-input String to be resolved to a Member.
	 * @param guild Guild to resolve from.
	 * @return A list containing none, one, or multiple members, depending on how many match.
	 */
	public static @NotNull List<Member> resolve(@NotNull String fromText, @NotNull Guild guild) {
		fromText = filter(fromText).trim();

		Member member;
		List<Member> members;

		//attempt resolving as snowflake first
		try {
			if((member = guild.getMemberById(fromText)) != null)
				return List.of(member);
		} catch(Exception ignored) {
			//wasn't an ID
		}

		//attempt name#discrim
		try {
			if((member = guild.getMemberByTag(fromText)) != null) {
				return List.of(member);
			}
		} catch(IllegalArgumentException ignored) {
			//wasn't name#discrim
		}

		members = guild.getMembersByNickname(fromText, true);
		members.addAll(guild.getMembersByName(fromText, true));
		//remove dupes
		return members.stream().distinct().collect(Collectors.toList());
	}

	private static String filter(String s) {
		for(String filter : FILTERED_STRINGS)
			s = s.replace(filter, "");
		return s;
	}
}
