package handlers;

import java.util.ArrayList;
import init.Launcher;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class ServerHandler implements EventListener {

	public static ArrayList<Guild> cachedGuilds;

	/**Compares TWO roles in regards to the Guild's hierarchy
	 *
	 * @param o is the array of users. o cannot have a length greater than 2. Author is usually the first element
	 * @return 0 if they're equal, >0 if the first element is greater than the second element, otherwise <0
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

			return rolePos[0] - rolePos[1];

		}

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

			System.out.println(r.getName());

			if(r.getPosition() > hierarchyVal) {
				hierarchyVal = r.getPosition();
				role = r;
			}

		}

		return role;

	}

	public static Role getHighestRole(User u, Guild g, ArrayList<Role> r) {



		return null;

	}

	/**
	 * Obtains the User of the server's owner
	 * @param id MUST be the server address, otherwise an error will be thrown!
	 * @return The guild's owner
	 */
	public static User getServerOwner(long id) {
		return Launcher.api.getGuildById(id).getOwner().getUser();
	}

	@Override
	public void onEvent(Event event) {

		//System.out.println(event);

		//When Lucky Lynx joins a new server
		if(event instanceof GuildJoinEvent) {

			boolean cached = false;

			for(Guild gld: cachedGuilds) {

				if(gld.getIdLong() == ((GuildJoinEvent) event).getGuild().getIdLong()) {

					System.out.println("DEBUG (CACHED GUILDS): " + gld.getName());

					cached = true;
					return;

				}

			}

			//Sends a welcoming message to the owner of the server
			if(!cached) {
				((GuildJoinEvent) event).getGuild().getOwner().getUser().openPrivateChannel().queue((channel) ->
				{

					channel.sendMessage("Thank you for inviting Lucky Lynx to " + ((GuildJoinEvent) event).getGuild().getName() + "! Use `!help` to check out the commands. Please keep in mind that Lucky Lynx is in constant development and managed by a soon-to-be high school senior so expect some bugs here and there. If you ever do come across an issue or would like to discuss with other users, come join us at https://discord.gg/M8Hs5Dg\n\n**GitHub**: https://github.com/AlphaSerpentis/Discord-Lynx\n**Patreon**: https://www.patreon.com/project_lynx").queue();

				});
			} else {

			}

		} /*else if(event instanceof GuildMemberJoinEvent) { //WELCOMES USERS WITH A PICTURE

			Guild gld = ((GuildMemberJoinEvent) event).getGuild();
			TextChannel chn = gld.getSystemChannel();

			if(gld.getSystemChannel().canTalk()) {
				MessageHandler.sendMessage(chn, "Welcome to " + gld.getName() + "! Please read the server's rules/guidelines and have a great time!", new File("resources/lynx/lynx.jpg"));
			} else {
				((GuildMemberJoinEvent) event).getUser().openPrivateChannel().queue((channel) ->
				{

					channel.sendFile(new File("resources/lynx/lynx.jpg", "Welcome to " + gld.getName() + "! Please read the server's rules/guidelines and have a great time!")).queue();

				});
			}

		}*/

	}

	/**Determines if the user can execute an action in the guild. This method does NOT check Bot Owners.
	 *
	 * @param gld is the guild the user is in
	 * @param usr is the user that is being determined
	 * @param r is an ArrayList of type T (must be a Role or Long)
	 * @return true if the user is able to execute an action, otherwise false
	 */
	public static <T> boolean allowedToDoAction(Guild gld, User usr, ArrayList<T> r) {

		//Checks if the user is the server owner
		if(usr.equals(gld.getOwner().getUser()))
			return true;
		else if(r == null) { // To prevent a NullPointerException error after server owner check
			return false;
		} else if(!r.isEmpty() && (r.get(0) instanceof Role || r.get(0) instanceof Long)) { //Checks that type T is either a Role or Long

			boolean isRole = r.get(0) instanceof Role;

			for(T item: r) { //Goes through the given ArrayList

				if(isRole) {
					if(gld.getMember(usr).getRoles().contains(item)) //For type Role
						return true;
				} else {
					for(Role role: gld.getMember(usr).getRoles()) { //For type Long
						if(role.getIdLong() == (Long) item) {
							return true;
						}
					}
				}
			}

		}

		return false;

	}

}
