package events;

import javax.annotation.Nonnull;

import commands.Command;

public interface CommandEvent {

	/**
	 * 
	 * Returns the associated Command issuing the event.
	 * 
	 * @return The associated Command.
	 */
	@Nonnull
	Command getCommand();
	
}
