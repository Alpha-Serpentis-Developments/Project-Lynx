package lynx.commands.moderation;

import lynx.commands.Command;
import lynx.data.Data;
import lynx.manager.MessageManager;
import lynx.misc.UserResolver;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public abstract class ModerationCommand extends Command {
	protected enum VerificationResult {
		/**
		 * User can preform this action.
		 */
		SUCCESS,
		/**
		 * User doesn't have required permissions.
		 */
		USER_REQUIRES_PERMISSIONS,
		/**
		 * User has required permissions, but the user they're trying to act on is too powerful (either above them,
		 * or an admin).
		 */
		USER_TOO_POWERFUL,
		/**
		 * The user should be able to perform this action, but the bot is missing permissions to do it.
		 */
		BOT_REQUIRES_PERMISSIONS,
		/**
		 * The command requires configuration by a staff member.
		 */
		REQUIRES_CONFIGURATION,
		/**
		 * Something bad has happened.
		 */
		SOMETHING_BAD_HAPPENED
	}
	
	protected enum ModerationFlags {
		/**
		 * Anonymous flag does not show which moderator executed the command to the punished user
		 */
		ANONYMOUS,
		/**
		 * "DO NOT SEND" flag does not send the moderative action reasoning to the punished user
		 */
		DO_NOT_SEND
	}

	/**
	 * String format:
	 * Punished username,
	 * Past tense action,
	 * Moderator name
	 */
	private static final String RESPONSE_MSG = "**%s** has been %s by **%s**.";

	/**
	 * String format:
	 * Moderator name,
	 * Server name and ID,
	 * Past tense action
	 */
	private static final String DM_PUBLIC_MSG = "**%s** in server %s has %s you.";
	
	/**
	 * String format:
	 * Server name and ID,
	 * Paste tense action
	 */
	private static final String DM_ANON_MSG = "A staff member in server %s has %s you.";

	/**
	 * String format:
	 * Moderator username,
	 * Past tense action,
	 * Punished username
	 */
	private static final String LOG_MSG = "**Moderator %s** has %s user %s.";

	private static final String NO_REASON = "No reason has been provided.";
	private static final String REASON = "A reason has been provided:";

	//hehe funny meme
	private static final String SELF_PUNISH = "You seem a little *lost*. Maybe you meant to punish someone else?";

	/**
	 * String format
	 * Punished username,
	 * Past tense action
	 */
	private static final String PUNISHING_US = "**%s** has been %s b- Hey... wait a minute... that's not cool :(";

	/**
	 * String format:
	 * Command name
	 */
	private static final String NO_PERMS = "You do not have permission to %s this user.";

	private final String pastTenseAction;
	private final List<Permission> discordPermissions;

	ModerationCommand(String cmd, String pastTense, List<Permission> discordPermissions) {
		setName(cmd);
		this.pastTenseAction = pastTense;
		this.discordPermissions = discordPermissions;
	}

	@Override
	public boolean action(MessageChannel chn, String msg, Object misc) {
		MessageReceivedEvent event = (MessageReceivedEvent) misc;

		Guild guild = event.getGuild();

		//find where the name ends
		int endIndex = msg.indexOf("\"") - 1;
		if(endIndex < 0) {
			endIndex = msg.indexOf(">");
			if(endIndex != -1) {
				endIndex++;
			}
		}

		if(endIndex < 0) {
			endIndex = msg.length();
		}

		String name = msg.substring(
				this.getName().length() + 1, //remove command (and space)
				endIndex //remove up to the quote (or remove nothing if there are none)
		);

		//gotta do weird crap cus lambda
		String reason;
		{
			String working = msg.substring(this.getName().length() + 1 + name.length()).trim();
			if(working.startsWith("\""))
				working = working.substring(1);
			if(working.endsWith("\""))
				working = working.substring(0, working.length() - 1);
			reason = working;
		}

		name = name.trim();

		String message;
		boolean success = false;
		if(!name.isEmpty()) {
			//Find the user to punish.
			List<Member> members = UserResolver.resolve(name, guild);

			if(members.isEmpty()) { //Found no one
				message = NO_ONE;
			} else if(members.size() > 1) { //Banning multiple people at once would be a bad idea... unless?
				message = TOO_MANY;
			} else { //Great! We have a user to punish >:3
				Member receiver = members.get(0);
				Member giver = event.getMember();

				//Check silly things
				//noinspection ConstantConditions (Only possible for giver to be null if message was sent by a webhook)
				if(receiver.getId().equals(giver.getId())) { //if they're trying to punish themselves...
					message = SELF_PUNISH;
				} else if(receiver.getId().equals(event.getJDA().getSelfUser().getId())) { //if the person being punished is us :<
					message = String.format(PUNISHING_US, event.getJDA().getSelfUser().getName(), pastTenseAction);
				} else { //Getting closer...
					//Now, let's check permissions to make sure we can actually punish them.

					//Check if user above the other one, and that they have the required permissions (or admin)
					VerificationResult result = canUseOnMember(giver, receiver);
					if(result == VerificationResult.SUCCESS) {
						//Punish them! >:3

						//Make sure the punishment is run after they receive the DM.
						receiver.getUser().openPrivateChannel().queue(privateChannel -> {
							MessageManager.sendMessage(privateChannel, addReason(String.format(DM_PUBLIC_MSG, giver.getUser().getName(), guild.getName() + " (" + guild.getId() + ")", pastTenseAction), reason));
							punish(receiver, giver, reason);
						}, error -> {
							//I'm not sure why it would fail here, but, just in case.
							punish(receiver, giver, reason);
						});

						//Send message to log channel
						if(getLogging()) {
							Object o = Data.srvr_cache.get(guild).get("logging_channel");
							if(o instanceof Long) {
								TextChannel channel = guild.getTextChannelById((long) o);
								if(channel != null) {
									MessageManager.sendMessage(channel, addReason(String.format(LOG_MSG, giver.getUser().getAsTag(), pastTenseAction, receiver.getUser().getAsTag()), reason));
								}
							}
						}

						success = true;
						message = addReason(String.format(RESPONSE_MSG, receiver.getEffectiveName(), pastTenseAction, giver.getEffectiveName()), reason);
					} else if(result == VerificationResult.REQUIRES_CONFIGURATION) {
						message = CONFIGURATION_PLS;
					} else if(result == VerificationResult.BOT_REQUIRES_PERMISSIONS) {
						message = BOT_PERMISSION_PLS;
					} else {
						message = String.format(NO_PERMS, getName());
					}
				}
			}
		} else {
			//They didn't even bother giving a username....
			message = getDesc();
		}

		MessageManager.sendMessage(event.getChannel(), message);
		return success;
	}

	/**
	 * Adds reason to base message.
	 *
	 * @param base Base message.
	 * @param reason The reason to add to the message.
	 * @return Returns base message with {@link ModerationCommand#REASON} and the provided reason if not empty, otherwise
	 * returns base message with {@link ModerationCommand#NO_REASON}
	 */
	private static String addReason(String base, String reason) {
		if(!(base.endsWith(" ") || base.endsWith("\n")))
			base += " ";
		if(reason.isEmpty()) {
			base += NO_REASON;
		} else {
			base += REASON + "\n" + reason;
		}
		return base;
	}

	/**
	 * Verifies if the command can be executed by the bot and user with the determining factor of the interacted user
	 * <br></br>
	 * Checks for the permissions, role hierarchy, etc.
	 *
	 * @return one of {@link VerificationResult}
	 * @throws IllegalArgumentException if members do not belong to the same Guild.
	 */
	public VerificationResult canUseOnMember(Member executor, Member interacted) {
		Guild g = executor.getGuild();
		
		//Check whether the bot can actually do the action...
		if(!g.getSelfMember().canInteract(interacted))
			return VerificationResult.BOT_REQUIRES_PERMISSIONS;
		
		//Checks if executor is owner, otherwise rest are redundant
		if(g.getOwner().equals(executor))
			return VerificationResult.SUCCESS;
		
		if(!g.getId().equals(interacted.getGuild().getId()))
			throw new IllegalArgumentException("The members provided do not belong to the same Guild!");

		if(!requirePerms) {
			System.err.println("Moderation command doesn't require any permissions to use! " + getName());
			new Exception().printStackTrace();
			return VerificationResult.SOMETHING_BAD_HAPPENED;
		}

		//Checks if the configuration is set.
		if(!isRoleIDsDefined())
			return VerificationResult.REQUIRES_CONFIGURATION;

		//Checks that they are configured to be able to use this command.
		if(!hasRolesToUse(executor) || (discordPermissions != null && executor.hasPermission(discordPermissions)))
			return VerificationResult.USER_REQUIRES_PERMISSIONS;

		//Checks role hierarchy and whether the person being punished is an admin
		if(!memberAboveOther(executor, interacted) || interacted.hasPermission(Permission.ADMINISTRATOR))
			return VerificationResult.USER_TOO_POWERFUL;

		return VerificationResult.SUCCESS;
	}

	/**
	 * Compares member roles to see if one member is higher than the other.
	 *
	 * @return true when member1 is above the other member. False if they're level or below.
	 */
	private static boolean memberAboveOther(Member member1, Member member2) {
		return getPowerLevel(member1) > getPowerLevel(member2);
	}

	private static int getPowerLevel(Member member) {
		List<Role> roles = member.getRoles();
		if(roles.isEmpty()) {
			return 0;
		} else {
			Guild g = member.getGuild();
			Role highestRole = roles.get(0);

			//index 0 is the highest on the list, so gotta do math
			return g.getRoles().size() - g.getRoles().indexOf(highestRole);
		}
	}

	protected abstract void punish(Member member, Member mod, String reason);
}
