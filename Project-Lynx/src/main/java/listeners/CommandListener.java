package listeners;

import javax.annotation.Nonnull;

import events.CommandEvent;

public interface CommandListener {

	void onEvent(@Nonnull CommandEvent event);
	
}
