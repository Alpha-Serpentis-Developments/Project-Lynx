package lynx.manager;

import lynx.data.Data;
import lynx.init.Launcher;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class ServerManager implements EventListener {

	/**Compares TWO roles in regards to the Guild's hierarchy
	 *
	 * @param o is the array of users. o cannot have a length greater than 2. Author is usually the first element
	 * @return 0 if they're equal, >0 if the first element is greater than the second element, otherwise <0
	 * @throws Exception if o contains more than two users, is null, or the bot is trying to be kicked
	 */
	public static int compareRoles(User[] o, Guild g) throws Exception {

		if(o.length > 2) {
			throw new Exception("Cannot compare more than two users!");
		} else if(o.length == 0 || o.length == 1) {
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

	/**Returns the highest role from the user within the guild
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

	/**
	 * Obtains the User of the server's owner
	 * @param id MUST be the server id, otherwise a null value will be returned!
	 * @return The guild's owner
	 */
	public static User getServerOwner(long id) {
		return Launcher.api.getGuildById(id).getOwner().getUser();
	}
	/**
	 * Obtains the User of the server's owner
	 * @param gld represents the Guild
	 * @return The guild's owner
	 */
	public static User getServerOwner(Guild gld) {
		return gld.getOwner().getUser();
	}

	@Override
	public void onEvent(GenericEvent event) {

		//System.out.println(event);

		//When Lucky Lynx joins a new server
		if(event instanceof GuildJoinEvent) {

			boolean cached = false;
			for(Guild gld: Data.srvr_cache.keySet()) {

				if(gld == null) continue;

				System.out.println("DEBUG (CACHED GUILDS): " + gld.getName());

				if(gld.getIdLong() == ((GuildJoinEvent) event).getGuild().getIdLong()) {

					cached = true;
					return;

				}

			}
			//Sends a welcoming message to the owner of the server
			if(!cached) {
				((GuildJoinEvent) event).getGuild().getOwner().getUser().openPrivateChannel().queue((channel) ->
				{

					channel.sendMessage("Thank you for inviting Lucky Lynx to __" + ((GuildJoinEvent) event).getGuild().getName() + "__! Use `!help` to check out the commands. Please keep in mind that Lucky Lynx is in constant development and managed by a college student so expect some bugs here and there. If you ever do come across an issue or would like to discuss with other users, come join us at https://discord.gg/M8Hs5Dg\n\n**GitHub**: https://github.com/Alpha-Serpentis-Developments/Project-Lynx\n**Patreon**: https://www.patreon.com/project_lynx").queue();

				});
				Data.addGuild(((GuildJoinEvent) event).getGuild());
			}

		} else if(event instanceof GuildLeaveEvent) { //When Lucky Lynx leaves a server :(

			if(Data.srvr_cache.containsKey(((GuildLeaveEvent) event).getGuild())) {

				Data.deleteGuild(((GuildLeaveEvent) event).getGuild());

			}

		} /*else if(event instanceof GuildMemberJoinEvent) { //WELCOMES USERS WITH A PICTURE

			Guild gld = ((GuildMemberJoinEvent) event).getGuild();
			TextChannel chn = gld.getSystemChannel();

			if(gld.getSystemChannel().canTalk()) {
				MessageManager.sendMessage(chn, "Welcome to " + gld.getName() + "! Please read the server's rules/guidelines and have a great time!", new File("resources/lynx/lynx.jpg"));
			} else {
				((GuildMemberJoinEvent) event).getUser().openPrivateChannel().queue((channel) ->
				{

					channel.sendFile(new File("resources/lynx/lynx.jpg", "Welcome to " + gld.getName() + "! Please read the server's rules/guidelines and have a great time!")).queue();

				});
			}

		}*/

	}

}
