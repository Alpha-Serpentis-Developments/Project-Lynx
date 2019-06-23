package handlers;

import init.Launcher;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;

public class ServerHandler implements EventListener {

	/**
	 *
	 * @param o is the array of users. o cannot have a length greater than 2. Author is usually the first element
	 * @return 0 if they're equal, 1 if the first element is greater than the second element, otherwise -1
	 * @throws Exception if o contains more than two users
	 */
	public static int compareRoles(User[] o, Guild g) throws Exception {

		if(o.length > 2) {
			throw new Exception("Cannot compare more than two users!");
		} else if(o.length == 0) {
			throw new Exception("There is nothing to compare!");
		} else {

			int rolePos[] = new int[2]; // 1st Element is usually the author

			for(int i = 0; i < 2; i++) {

				for(Role r: g.getMember(o[i]).getRoles()) {

					if(r.getPosition() > rolePos[i])
						rolePos[i] = r.getPosition();

				}

			}

		}

		return 0;
	}

	/**Returns the highest role from the user
	*
	* @param u is the user
	* @param g is the guild the user is in
	* @return the "highest" Role in the server's hierarchy, returns null if there are no roles on the user
	* @throws Exception if EITHER u or g are null
	*/
	public static Role getHighestRole(User u, Guild g) throws Exception {

		if(u == null) {
			throw new Exception("User cannot be null!");
		}
		if(g == null) {
			throw new Exception("Guild cannot be null!");
		}

		Role role = null;
		int hierarchyVal = -1;

		for(Role r: g.getMember(u).getRoles()) {

			if(r.getPosition() > hierarchyVal) {
				hierarchyVal = r.getPosition();
				role = r;
			}

		}

		return role;

	}

	@Override
	public void onEvent(Event event) {

		//When Lucky Lynx joins a new server
		if(event instanceof GuildMemberJoinEvent) {

			if(((GuildMemberJoinEvent) event).getUser().getIdLong() == Launcher.botID) {
				System.out.println("Lucky Lynx joined a new server!");
			}

		}

	}

}
