package lynx.listeners;

import javax.annotation.Nonnull;

import lynx.events.CommandEvent;

public interface CommandListener {

	void onEvent(@Nonnull CommandEvent event);
	
}
