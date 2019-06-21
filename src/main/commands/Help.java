package main.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.handlers.MessageHandler;
import main.init.InitData;
import net.dv8tion.jda.core.entities.TextChannel;

public class Help extends Command {

	public Help() {
		super("help", "Use this command to obtain information on both the bot and commands available!\n\n`" + InitData.prefix + "help [command name]`");
	}

	@Override
	public boolean action(TextChannel chn, String msg, Object misc) { // misc can be used as a command type ident
		
		if(msg.toLowerCase().matches(InitData.prefix + "help")) {
			MessageHandler.sendMessage(chn, getDesc());
			
			return true;
		} else {
			
			for(String r: new File("src/commands/").list()) {
				System.out.println(r);
			}
			
			return true;
			
		}
	}

}
