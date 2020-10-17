package lynx.commands.moderation;

import lynx.misc.Truncate;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class Ban extends ModerationCommand {
	public Ban() {
		super("ban", "banned", List.of(Permission.BAN_MEMBERS));
	}

	@Override
	protected void punish(Member member, Member mod, String reason) {
		member.getGuild().ban(member, 1, Truncate.truncate(reason, 500)).queue();
	}
}
