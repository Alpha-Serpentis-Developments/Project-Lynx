package lynx.commands.moderation;

import lynx.commands.utilities.Truncate;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class Kick extends ModerationCommand {
	public Kick() {
		super("kick", "kicked", List.of(Permission.KICK_MEMBERS));
	}

	@Override
	protected void punishEm(Member member, Member mod, String reason) {
		member.getGuild().kick(member, Truncate.truncate(reason, 500)).queue();
	}
}
