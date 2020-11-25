package lynx.commands.moderation;

import net.dv8tion.jda.api.entities.Member;

public class RemoveLog extends ModerationCommand {

	public RemoveLog() {
		super("removelog", "removed log", null);
	}

	@Override
	protected void punish(Member member, Member mod, String reason) {
		// TODO Auto-generated method stub

	}

}
